package tn.insat.androcope.bean;

import java.io.Serializable;

public class Mouse implements Serializable {

	private static final long serialVersionUID = -1247827936977116858L;
	public static String CLIPBOARD = "";
	
	public static final int SCROLL = 0;
	public static final int RIGHT_CLICK = 1;
	public static final int LEFT_CLICK = 2;
	public static final int ACTION_COPY = 3;
	public static final int ACTION_PASTE = 4;
	public static final int EXIT_CMD = -1;
	
	int action;
	int x;
	int y;
	String clipboard;
	
	public Mouse(int action, int x, int y){
		this.action=action;
		this.x=x;
		this.y=y;
	}
	
	public Mouse(int action, String clipboard){
		this.action=action;
		this.clipboard = clipboard;
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

	public String getClipboard() {
		return clipboard;
	}

	public void setClipboard(String clipboard) {
		this.clipboard = clipboard;
	}
	
	
	
}
