package tn.insat.androcope;

import java.io.Serializable;

public class Mouse implements Serializable {

	public static final int SCROLL = 0;
	public static final int RIGHT_CLICK = 1;
	public static final int LEFT_CLICK = 2;
	public static final int EXIT_CMD = -1;
	
	int action;
	int x;
	int y;
	
	public Mouse(int action, int x, int y){
		this.action=action;
		this.x=x;
		this.y=y;
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
