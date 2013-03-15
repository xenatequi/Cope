package tn.insat.androcope;

import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import tn.insat.androcope.R;

public class GestureListener implements OnGestureListener{

	BluetoothCommandService mCommandService;
	
	public GestureListener(BluetoothCommandService mCommandService) {
		super();
		this.mCommandService = mCommandService;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			Mouse command = new Mouse(Mouse.SCROLL, (int)(distanceX*1.5), (int)(distanceY*1.5));
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
		Mouse command = new Mouse(Mouse.LEFT_CLICK, (int)e.getX(), (int)e.getY());
		mCommandService.write(command);
		Log.d("SingleTap", "single tap");
		return true;
	}
}