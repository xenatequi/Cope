package tn.insat.androcope.thread;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class WaitThread extends Thread{
	
	private static String MY_UUID = "04c6093b00001000800000805f9b34fb";
	private boolean running ;
	private ProcessConnectionThread processThread;
	
	@Override
	public void run() {
		while( running ) {
			waitForConnection();   
	    } 
	} 
	
	public void setRunning(boolean running) {
		this.running = running;
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
        	System.out.println("Bluetooth is not turned on.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			System.out.println("Cannot read from Bluetooth.");
			e.printStackTrace();
			return;
		}
		
		while(true) {
			try {
				System.out.println("Waiting for connection...");
	            connection = notifier.acceptAndOpen();
	           
	            processThread = new ProcessConnectionThread(connection);
	            processThread.start();
	            
			} catch (IOException e) {
				System.out.println("Problem occured while accepting an external connection...");
				e.printStackTrace();
				return;
			}
		}
	}
}
