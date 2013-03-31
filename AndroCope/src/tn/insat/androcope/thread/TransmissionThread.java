package tn.insat.androcope.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import tn.insat.androcope.bean.Mouse;
import tn.insat.androcope.service.BluetoothCommandService;
import tn.insat.androcope.service.MessageHandler;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class TransmissionThread extends Thread {
	private static final String TAG = "TransmissionThread";
    private final BluetoothSocket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    
    private BluetoothCommandService commandService;

    public TransmissionThread(BluetoothSocket socket, BluetoothCommandService commandService) {
        Log.d(TAG, "create TransmissionThread");
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
            Log.e(TAG, "Temp sockets not created", e);
        }

        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    public void run() {
        Log.i(TAG, "BEGIN transmissionThread");
        
        if(Mouse.CLIPBOARD != "")
        	commandService.write(new Mouse(Mouse.ACTION_PASTE, Mouse.CLIPBOARD));
        
        while (true) {
            try {
                Mouse mouse = (Mouse)inputStream.readObject();
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
    	
        Message msg = commandService.getHandler().obtainMessage(MessageHandler.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MessageHandler.TOAST, "Device connection was lost");
        msg.setData(bundle);
        commandService.getHandler().sendMessage(msg);
    }
        
    public void write(Mouse mouse) {
        try {
            outputStream.writeObject(mouse);
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }

    public void cancel() {
        try {
        	outputStream.writeObject(new Mouse(Mouse.EXIT_CMD,0,0));
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }
}
