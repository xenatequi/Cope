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
            tmpOut = new ObjectOutputStream(os);
            tmpOut.flush();
            tmpIn = new ObjectInputStream(is);
            
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }

        inStream = tmpIn;
        outStream = tmpOut;
    }

    public void run() {
        Log.i(TAG, "BEGIN mConnectedThread");
        
        commandService.write(new Mouse(Mouse.ACTION_PASTE, Mouse.CLIPBOARD));
        
        while (true) {
            try {
                Mouse mouse = (Mouse)inStream.readObject();
                Mouse.CLIPBOARD = mouse.getClipboard();
                
                Message msg = commandService.getHandler().obtainMessage(MessageHandler.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString(MessageHandler.TOAST, "Data copied to clipboard");
                msg.setData(bundle);
                commandService.getHandler().sendMessage(msg);
                
                Log.e(TAG, Mouse.CLIPBOARD);
            } catch (IOException e) {
                Log.e(TAG, "disconnected", e);
                connectionLost();
                break;
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "class not found", e);
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
