package com.luugiathuy.apps.remotebluetooth;

import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyGestureListener implements OnGestureListener, OnTouchListener{

	BluetoothCommandService mCommandService;
	
	public MyGestureListener(BluetoothCommandService mCommandService) {
		super();
		this.mCommandService = mCommandService;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			Mouse command = new Mouse(Mouse.SCROLL, (int)distanceX*2, (int)distanceY*2);
			mCommandService.write(command);
			Log.d("Scroll", "Tapped at : (" + (int)distanceX + "," + (int)distanceY + ")");

		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		Mouse command = new Mouse(event.getAction(), (int)event.getX(), (int)event.getY());
		mCommandService.write(command);
		Log.d("Scroll", "Tapped at : (" +event.getAction()+ (int)event.getX() + "," + (int)event.getY() + ")");
		return true;
	}
}