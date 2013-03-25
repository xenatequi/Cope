package tn.insat.androcope;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable{

	private StreamConnection mConnection;
	
	public ProcessConnectionThread(StreamConnection connection){
		mConnection = connection;
	}
	
	@Override
	public void run() {
		try {
			InputStream inputStream = mConnection.openInputStream();
	        ObjectInputStream ois = new ObjectInputStream(inputStream);
	        OutputStream outputStream = mConnection.openOutputStream();
	        ObjectOutputStream ouis = new ObjectOutputStream(outputStream);
	        ouis.flush();

			System.out.println("waiting for input");
    	
			while (true) {
	        	Mouse command = (Mouse)ois.readObject();
	        	
	        	if (command.getAction() == Mouse.EXIT_CMD){	
	        		System.out.println("finish process");
	        		break;
	        	}
	        	
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
			MouseRobot robot = new MouseRobot();
			
			switch (command.getAction()) {
				case Mouse.SCROLL:
					robot.scroll(command);
					break;
				case Mouse.LEFT_CLICK:
					robot.clickLeft();
					break;
				case Mouse.RIGHT_CLICK:
					robot.clickRight();
					break;
				default:
					break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
