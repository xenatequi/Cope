package com.luugiathuy.apps.remotebluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ConnectThread extends Thread {
	private static final String TAG = "BluetoothCommandService";
	private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
	
	private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mAdapter;
    private final BluetoothCommandService mBluetoothCommandService;
    
    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter, BluetoothCommandService bluetoothCommandService) {
        mmDevice = device;
        mAdapter = adapter;
        mBluetoothCommandService = bluetoothCommandService;
      
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
        mAdapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mmSocket.connect();
        } catch (IOException e) {
        	mBluetoothCommandService.connectionFailed();
            // Close the socket
            try {
                mmSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "unable to close() socket during connection failure", e2);
            }
            // Start the service over to restart listening mode
            start();
            return;
        }

        // Reset the ConnectThread because we're done
        synchronized (this) {
        	mBluetoothCommandService.setConnectThread(null);
        }

        // Start the connected thread
        mBluetoothCommandService.connected(mmSocket, mmDevice);
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }
}
