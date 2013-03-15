package tn.insat.androcope;

import java.io.IOException;
import java.util.UUID;
import tn.insat.androcope.thread.TransmissionThread;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothCommandService {
	// Debugging
    private static final String TAG = "BluetoothCommandService";
    private static final boolean D = true;

    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
    
    
    // Member fields
    private final BluetoothAdapter adapter;
    private final Handler handler;
    private ConnectThread connectThread;
    private TransmissionThread transmissionThread;
    private int state;
    
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    
    // Constants that indicate command to computer
    public static final int EXIT_CMD = -1;
    public static final int VOL_UP = 1;
    public static final int VOL_DOWN = 2;
    public static final int MOUSE_MOVE = 3;
    
    /**
     * Constructor. Prepares a new BluetoothChat session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     */
    public BluetoothCommandService(Context context, Handler handler) {
    	adapter = BluetoothAdapter.getDefaultAdapter();
    	state = STATE_NONE;
    	this.handler = handler;
    }
    
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    public synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + state + " -> " + state);
        this.state = state;

        // Give the new state to the Handler so the UI Activity can update
        handler.obtainMessage(MessageHandler.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the current connection state. */
    public synchronized int getState() {
        return state;
    }
    
    public Handler getHandler() {
        return handler;
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
        connectThread = new ConnectThread(device);
        connectThread.start();
        setState(STATE_CONNECTING);
    }
    
    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (connectThread != null) {connectThread.cancel(); connectThread = null;}

        // Cancel any thread currently running a connection
        if (transmissionThread != null) {transmissionThread.cancel(); transmissionThread = null;}

        // Start the thread to manage the connection and perform transmissions
        transmissionThread = new TransmissionThread(socket, this);
        transmissionThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = handler.obtainMessage(MessageHandler.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(MessageHandler.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        handler.sendMessage(msg);
        
        setState(STATE_CONNECTED);
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
    
    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(STATE_LISTEN);

        // Send a failure message back to the Activity
        Message msg = handler.obtainMessage(MessageHandler.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MessageHandler.TOAST, "Unable to connect device");
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        	setState(STATE_LISTEN);
	        // Send a failure message back to the Activity
	        Message msg = handler.obtainMessage(MessageHandler.MESSAGE_TOAST);
	        Bundle bundle = new Bundle();
	        bundle.putString(MessageHandler.TOAST, "Device connection was lost");
	        msg.setData(bundle);
	        handler.sendMessage(msg);
//        }
    }
    
    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            adapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                // Start the service over to restart listening mode
                BluetoothCommandService.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothCommandService.this) {
                connectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}