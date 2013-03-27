package tn.insat.androcope;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable{

	private StreamConnection connection;
	private ClipboardListener clipboardListener;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	public ProcessConnectionThread(StreamConnection connection){
		this.connection = connection;
	}
	
	@Override
	public void run() {
		try {
			
			initObjectStreams();
	        initClipboard();

			System.out.println("Waiting for input");
	
			while (true) {
	        	Mouse command = (Mouse)inputStream.readObject();
	        	
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
	
	private void initClipboard() {
		clipboardListener = new ClipboardListener(outputStream);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.addFlavorListener(clipboardListener);
	}

	private void initObjectStreams() {
		InputStream is = connection.openInputStream();
		OutputStream os = connection.openOutputStream();
		
        inputStream = new ObjectInputStream(is);
        outputStream = new ObjectOutputStream(os);
        outputStream.flush();	
	}

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
				case Mouse.ACTION_PASTE:
					robot.paste(command);
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
