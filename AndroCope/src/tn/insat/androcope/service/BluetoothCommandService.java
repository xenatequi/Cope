package tn.insat.androcope.service;

import tn.insat.androcope.bean.Mouse;
import tn.insat.androcope.thread.ConnectThread;
import tn.insat.androcope.thread.TransmissionThread;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class BluetoothCommandService {

    private static final String TAG = "BluetoothCommandService";
    private static final boolean D = true;    
    
    private final Handler handler;
    private ConnectThread connectThread;
    private TransmissionThread transmissionThread;
    private int state;
    
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    
    /**
     * Constructor. Prepares a new Bluetooth session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     */
    public BluetoothCommandService(Context context, Handler handler) {
    	state = STATE_NONE;
    	this.handler = handler;
    }
    
    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() */
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (connectThread != null) {connectThread.cancel(); connectThread = null;}

        // Cancel any thread currently running a connection
        if (transmissionThread != null) {transmissionThread.cancel(); transmissionThread = null;}

        setState(STATE_LISTEN);
    }
    
    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
    	if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (state == STATE_CONNECTING) {
            if (connectThread != null) {connectThread.cancel(); connectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (transmissionThread != null) {transmissionThread.cancel(); transmissionThread = null;}

        // Start the thread to connect with the given device
        connectThread = new ConnectThread(device, this, handler);
        connectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");
        if (connectThread != null) {connectThread.cancel(); connectThread = null;}
        if (transmissionThread != null) {transmissionThread.cancel(); transmissionThread = null;}
        
        setState(STATE_NONE);
    }
    
    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    
    public void write(Mouse mouse) {
        // Create temporary object
        TransmissionThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (state != STATE_CONNECTED) return;
            r = transmissionThread;
        }
        // Perform the write unsynchronized
        r.write(mouse);
    }

    public TransmissionThread getTransmissionThread() {
		return transmissionThread;		
	}
    
    public void setTransmissionThread(TransmissionThread transmissionThread) {
		this.transmissionThread= transmissionThread;		
	}
    
	public void setConnectThread(ConnectThread connectThread) {
		this.connectThread= connectThread;		
	}
	
    public synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + state + " -> " + state);
        this.state = state;

        // Give the new state to the Handler so the UI Activity can update
        handler.obtainMessage(MessageHandler.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return state;
    }

    public synchronized ConnectThread getConnectThread() {
        return connectThread;
    }
    
    public Handler getHandler() {
        return handler;
    }
    
}