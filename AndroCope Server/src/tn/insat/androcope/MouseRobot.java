package tn.insat.androcope;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class MouseRobot {

	Robot robot;
	
	public MouseRobot () throws AWTException{
		robot = new Robot();
	}
	
	
	public void scroll(Mouse mouse){
		Point point = MouseInfo.getPointerInfo().getLocation();
		double x = point.getX();
		double y = point.getY();
		System.out.println(x + " "+ y); ////////////////////////////////////////////////////

		robot.mouseMove((int)x-(int)mouse.getX(), (int)y-(int)mouse.getY());
	}
	
	public void clickLeft(){
		robot.keyPress(KeyEvent.VK_LEFT);
		robot.keyRelease(KeyEvent.VK_LEFT);
	}
	
	public void clickRight(){
		robot.keyPress(KeyEvent.VK_RIGHT);
		robot.keyRelease(KeyEvent.VK_RIGHT);
	}
}
