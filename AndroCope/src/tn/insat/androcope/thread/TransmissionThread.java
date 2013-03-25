package tn.insat.androcope.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import tn.insat.androcope.BluetoothCommandService;
import tn.insat.androcope.MessageHandler;
import tn.insat.androcope.Mouse;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class TransmissionThread extends Thread {
	private static final String TAG = "ConnectedThread";
    private final BluetoothSocket socket;
    BluetoothCommandService commandService;
    private final ObjectInputStream inStream;
    private final ObjectOutputStream outStream;

    public TransmissionThread(BluetoothSocket socket, BluetoothCommandService commandService) {
        Log.d(TAG, "create ConnectedThread");
        this.socket = socket;
        this.commandService = commandService;
        ObjectInputStream tmpIn = null;
        ObjectOutputStream tmpOut = null;

        try {
        	InputStream is = socket.getInputStream();
        	OutputStream os = socket.getOutputStream();
        	Log.e(TAG, "simple stream");//////////////////////////////////////////////////
            tmpOut = new ObjectOutputStream(os);
            tmpOut.flush();
            Log.e(TAG, "object stream");//////////////////////////////////////////////////
            tmpIn = new ObjectInputStream(is);
            
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }
        Log.e(TAG, "el streamet t3amlou");//////////////////////////////////////////////////
        inStream = tmpIn;
        outStream = tmpOut;
    }

    public void run() {
        Log.i(TAG, "BEGIN mConnectedThread");
        byte[] buffer = new byte[1024];
        
        // Keep listening to the InputStream while connected
        while (true) {
            try {
            	// Read from the InputStream
                int bytes = inStream.read();

                // Send the obtained bytes to the UI Activity
                commandService.getHandler().obtainMessage(MessageHandler.MESSAGE_READ, bytes, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "disconnected", e);
                connectionLost();
                break;
            }
        }
    }

    private void connectionLost() {
    	commandService.setState(commandService.STATE_LISTEN);
        // Send a failure message back to the Activity
        Message msg = commandService.getHandler().obtainMessage(MessageHandler.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MessageHandler.TOAST, "Device connection was lost");
        msg.setData(bundle);
        commandService.getHandler().sendMessage(msg);
    }
    
    /**
     * Write to the connected OutStream.
     * @param buffer  The bytes to write
     */
    
    public void write(Mouse mouse) {
        try {
            outStream.writeObject(mouse);
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }

    public void cancel() {
        try {
        	outStream.writeObject(new Mouse(Mouse.EXIT_CMD,0,0));
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }
}
