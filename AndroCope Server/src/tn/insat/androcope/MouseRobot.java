package tn.insat.androcope;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;

public class MouseRobot {

	Robot robot;
	Transferable contents ;
	
	public MouseRobot () throws AWTException{
		robot = new Robot();
	}
	
	
	public void scroll(Mouse mouse){
		Point point = MouseInfo.getPointerInfo().getLocation();
		int x = (int) (point.getX() - mouse.getX());
		int y = (int) (point.getY() - mouse.getY());
		robot.mouseMove(x, y);
		
		System.out.println(x + " "+ y); ////////////////////////////////////////////////////
	}
	
	public void clickLeft(){
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	public void clickRight(){
		robot.mousePress(InputEvent.BUTTON3_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	
	public void paste(Mouse mouse){
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(mouse.getClipboard()),null);
		System.out.println("Clipboard : "+ mouse.getClipboard()); //////////////////////////////////////
		
	}
}
