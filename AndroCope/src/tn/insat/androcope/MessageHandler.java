package tn.insat.androcope;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;
import tn.insat.androcope.R;

public class MessageHandler extends Handler {

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
	private TextView title;
	// Name of the connected device
	private String connectedDeviceName = null;
	private Context applicationContext;
	
	
	public MessageHandler(TextView title, Context context){
		this.title = title;
		this.applicationContext = context;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE_STATE_CHANGE:
			switch (msg.arg1) {
			case BluetoothCommandService.STATE_CONNECTED:
				title.setText(R.string.title_connected_to);
				title.append(connectedDeviceName);
				break;
			case BluetoothCommandService.STATE_CONNECTING:
				title.setText(R.string.title_connecting);
				break;
			case BluetoothCommandService.STATE_LISTEN:
			case BluetoothCommandService.STATE_NONE:
				title.setText(R.string.title_not_connected);
				break;
			}
			break;
		case MESSAGE_DEVICE_NAME:
			// save the connected device's name
			connectedDeviceName = msg.getData().getString(DEVICE_NAME);
			Toast.makeText(applicationContext,
					"Connected to " + connectedDeviceName, Toast.LENGTH_SHORT)
					.show();
			break;
		case MESSAGE_TOAST:
			Toast.makeText(applicationContext,
					msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
