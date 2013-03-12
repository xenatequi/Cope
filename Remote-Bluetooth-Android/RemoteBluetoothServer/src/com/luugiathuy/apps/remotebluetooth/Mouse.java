package com.luugiathuy.apps.remotebluetooth;

import java.io.Serializable;

public class Mouse implements Serializable {

	static int SCROLL;
	static int RIGHT_CLICK;
	static int LEFT_CLICK;
	static int EXIT_CMD;
	
	int action;
	int x;
	int y;
	
	public Mouse(int action, int x, int y){
		this.action=action;
		this.x=x;
		this.y=y;
	}

	public static int getSCROLL() {
		return SCROLL;
	}

	public static void setSCROLL(int sCROLL) {
		SCROLL = sCROLL;
	}

	public static int getRIGHT_CLICK() {
		return RIGHT_CLICK;
	}

	public static void setRIGHT_CLICK(int rIGHT_CLICK) {
		RIGHT_CLICK = rIGHT_CLICK;
	}

	public static int getLEFT_CLICK() {
		return LEFT_CLICK;
	}

	public static void setLEFT_CLICK(int lEFT_CLICK) {
		LEFT_CLICK = lEFT_CLICK;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
