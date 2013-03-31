package tn.insat.androcope.thread;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import tn.insat.androcope.view.MainWindow;

public class WaitThread extends Thread{
	
	private static String MY_UUID = "04c6093b00001000800000805f9b34fb";
	private MainWindow mainWindow;
	private ProcessConnectionThread processThread;
	
	public WaitThread(MainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void run() {
			waitForConnection(); 
	} 
			
	private void waitForConnection() {

		LocalDevice localDevice = null;
		
		StreamConnectionNotifier notifier;
		StreamConnection connection = null;
		
		try {
			localDevice = LocalDevice.getLocalDevice();
			localDevice.setDiscoverable(DiscoveryAgent.GIAC);
			
			UUID uuid = new UUID( MY_UUID, false);
			System.out.println("UUID : " + uuid.toString());
			
            String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
            notifier = (StreamConnectionNotifier)Connector.open(url);
        } catch (BluetoothStateException e) {
        	mainWindow.setMessage("Bluetooth is not turned on", mainWindow.MESSAGE_ERROR);
			e.printStackTrace();
			return;
		} catch (IOException e) {
			mainWindow.setMessage("Cannot read from Bluetooth.", mainWindow.MESSAGE_ERROR);
			e.printStackTrace();
			return;
		}
		
		while(true) {
			try {
				mainWindow.setMessage("Waiting for connection...", mainWindow.MESSAGE_INFO);
	            connection = notifier.acceptAndOpen();
	           
	            processThread = new ProcessConnectionThread(connection, mainWindow);
	            processThread.start();
	            
			} catch (IOException e) {
				mainWindow.setMessage("Problem occured while accepting an external connection...", mainWindow.MESSAGE_ERROR);
				e.printStackTrace();
				return;
			}
		}
	}
}
