package tn.insat.androcope;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;
import tn.insat.androcope.R;

public class MyHandler extends Handler {

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothCommandService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	
	// Layout view
	private TextView mTitle;
	// Name of the connected device
	private String mConnectedDeviceName = null;
	private Context mApplicationContext;
	
	
	public MyHandler(TextView title, Context context){
		mTitle = title;
		mApplicationContext = context;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE_STATE_CHANGE:
			switch (msg.arg1) {
			case BluetoothCommandService.STATE_CONNECTED:
				mTitle.setText(R.string.title_connected_to);
				mTitle.append(mConnectedDeviceName);
				break;
			case BluetoothCommandService.STATE_CONNECTING:
				mTitle.setText(R.string.title_connecting);
				break;
			case BluetoothCommandService.STATE_LISTEN:
			case BluetoothCommandService.STATE_NONE:
				mTitle.setText(R.string.title_not_connected);
				break;
			}
			break;
		case MESSAGE_DEVICE_NAME:
			// save the connected device's name
			mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
			Toast.makeText(mApplicationContext,
					"Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT)
					.show();
			break;
		case MESSAGE_TOAST:
			Toast.makeText(mApplicationContext,
					msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
