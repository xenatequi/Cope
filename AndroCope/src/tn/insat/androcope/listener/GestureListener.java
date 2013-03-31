package tn.insat.androcope.listener;

import tn.insat.androcope.bean.Mouse;
import tn.insat.androcope.service.BluetoothCommandService;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class GestureListener implements OnGestureListener{

	BluetoothCommandService commandService;
	
	public GestureListener(BluetoothCommandService mCommandService) {
		super();
		this.commandService = mCommandService;
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Mouse command = new Mouse(Mouse.LEFT_CLICK, (int)e.getX(), (int)e.getY());
		commandService.write(command);
		Log.d("SingleTap", "single tap");
		return true;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		Mouse command = new Mouse(Mouse.RIGHT_CLICK, 0, 0);
		commandService.write(command);
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			Mouse command = new Mouse(Mouse.SCROLL, (int)(distanceX*1.5), (int)(distanceY*1.5));
			commandService.write(command);
			Log.d("Scroll", "Tapped at : (" + (int)distanceX + "," + (int)distanceY + ")");
		return true;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}
}