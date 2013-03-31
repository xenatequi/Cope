package tn.insat.androcope.thread;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import tn.insat.androcope.MainWindow;

public class WaitThread extends Thread{
	
	private static String MY_UUID = "04c6093b00001000800000805f9b34fb";
	private StreamConnectionNotifier notifier;
	private StreamConnection connection;
	private MainWindow mainWindow; 
	
	public WaitThread(MainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void run() {
		prepareConnection();
		waitForConnection();   
	} 
	
	private void prepareConnection() {
		try{
			LocalDevice localDevice = null;
			localDevice = LocalDevice.getLocalDevice();
			if(localDevice.getDiscoverable() != DiscoveryAgent.GIAC){
				localDevice.setDiscoverable(DiscoveryAgent.GIAC);
			}
			
			UUID uuid = new UUID( MY_UUID, false);
			System.out.println("UUID : " + uuid.toString());
			
	        String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
	        notifier = (StreamConnectionNotifier)Connector.open(url);
		} catch (BluetoothStateException e) {
        	mainWindow.setMessage("Bluetooth is not turned on.", MainWindow.MESSAGE_ERROR);
			e.printStackTrace();
			return;
		} catch (IOException e) {
			mainWindow.setMessage("Cannot read from Bluetooth.", MainWindow.MESSAGE_ERROR);
			e.printStackTrace();
			return;
		}
	}
			
	private void waitForConnection() {
			try {
				mainWindow.setMessage("Waiting for connection...", MainWindow.MESSAGE_INFO);
	            connection = notifier.acceptAndOpen();
	            
			} catch (IOException e) {
				mainWindow.setMessage("Problem occured while accepting an external connection...", MainWindow.MESSAGE_ERROR);
				e.printStackTrace();
				return;
			}
			
			synchronized (this) {
				mainWindow.setWaitThread(null);
			}
			
			connect();
	}

	private void connect(){
		if (mainWindow.getWaitThread() != null) {mainWindow.getWaitThread().cancel(); mainWindow.setWaitThread(null);}
        if (mainWindow.getProcessThread() != null) {mainWindow.getProcessThread().cancel(); mainWindow.setProcessThread(null);}

        mainWindow.setProcessThread(new ProcessConnectionThread(connection, mainWindow));
        mainWindow.getProcessThread().start();
	}
	
	public void cancel() {
		try {
			notifier.close();
		} catch (Exception e) {
			mainWindow.setMessage("Error occured while stopping Cope Server", MainWindow.MESSAGE_ERROR);
			e.printStackTrace();
		}
	} 
}
