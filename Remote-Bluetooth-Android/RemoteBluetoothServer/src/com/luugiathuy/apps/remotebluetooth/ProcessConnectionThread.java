package com.luugiathuy.apps.remotebluetooth;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable{

	private StreamConnection mConnection;
	
	// Constant that indicate command from devices
	private static final int EXIT_CMD = -1;
	private static final int KEY_RIGHT = 1;
	private static final int KEY_LEFT = 2;
	
	public ProcessConnectionThread(StreamConnection connection)
	{
		mConnection = connection;
	}
	
	@Override
	public void run() {
		try {
			
			// prepare to receive data
			InputStream inputStream = mConnection.openInputStream();
	        ObjectInputStream ois = new ObjectInputStream(inputStream);

			System.out.println("waiting for input");
    	
			while (true) {
	        	Mouse command = (Mouse)ois.readObject();
/*	        	
	        	if (command == EXIT_CMD)
	        	{	
	        		System.out.println("finish process");
	        		break;
	        	}
*/	        	
	        	processCommand(command);
	        	
        	}	
        } catch (Exception e) {
    		e.printStackTrace();
    	}
	}
	
	/**
	 * Process the command from client
	 * @param command the command code
	 */
	private void processCommand(Mouse command) {
		try {
			Robot robot = new Robot();
			
			//get current mouse location
			Point point = MouseInfo.getPointerInfo().getLocation();
			double x = point.getX();
			double y = point.getY();
			System.out.println(x + " "+ y);
	
			robot.mouseMove((int)x-(int)command.getX(), (int)y-(int)command.getY());
			
			
/*			switch (command) {
	    	case KEY_RIGHT:
	    		robot.keyPress(KeyEvent.VK_RIGHT);
	    		System.out.println("Right");
	    		// release the key after it is pressed. Otherwise the event just keeps getting trigged	    		
	    		robot.keyRelease(KeyEvent.VK_RIGHT);
	    		break;
	    	case KEY_LEFT:
	    		robot.keyPress(KeyEvent.VK_LEFT);
	    		System.out.println("Left");
	    		// release the key after it is pressed. Otherwise the event just keeps getting trigged	    		
	    		robot.keyRelease(KeyEvent.VK_LEFT);
	    		break;
			}
*/		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
