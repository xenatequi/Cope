package tn.insat.androcope.thread;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

import tn.insat.androcope.bean.Mouse;
import tn.insat.androcope.listener.ClipboardListener;
import tn.insat.androcope.service.MouseRobot;
import tn.insat.androcope.view.MainWindow;

public class ProcessConnectionThread extends Thread{

	private StreamConnection connection;
	private ClipboardListener clipboardListener;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private MainWindow mainWindow;
	
	public ProcessConnectionThread(StreamConnection connection, MainWindow mainWindow){
		this.connection = connection;
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void run() {
		try {
			
			initObjectStreams();
	        initClipboard();
	        mainWindow.setMessage("Waiting for input", mainWindow.MESSAGE_INFO);
	
			while (true) {
	        	Mouse command = (Mouse)inputStream.readObject();
	        	
	        	if (command.getAction() == Mouse.EXIT_CMD){	
	        		mainWindow.setMessage("Finish process", mainWindow.MESSAGE_INFO);
	        		break;
	        	}
	        	
	        	processCommand(command);
        	}	
        } catch (Exception e) {
    		e.printStackTrace();
    	}
		
		
		
	}

	private void initObjectStreams() {
		InputStream is;
		OutputStream os;
		try {
			is = connection.openInputStream();
			os = connection.openOutputStream();
	        inputStream = new ObjectInputStream(is);
	        outputStream = new ObjectOutputStream(os);
	        outputStream.flush();
		} catch (IOException e) {
			mainWindow.setMessage("Problem occured while establishing a connection", MainWindow.MESSAGE_ERROR);
			e.printStackTrace();
		}
	}

	private void initClipboard() {
		clipboardListener = new ClipboardListener(outputStream);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.addFlavorListener(clipboardListener);
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
