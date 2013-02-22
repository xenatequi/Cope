package com.luugiathuy.apps.remotebluetooth;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	// event when double tap occurs
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();

		Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

		return true;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.d("Double Tap", "Tapped at: (" + distanceX + "," + distanceY + ")");
		return true;
	}
}