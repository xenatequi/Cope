package tn.insat.androcope.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import tn.insat.androcope.R;
import tn.insat.androcope.listener.GestureListener;
import tn.insat.androcope.service.BluetoothCommandService;
import tn.insat.androcope.service.MessageHandler;

public class AndroCopeActivity extends Activity {

	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	
	private TextView statusText;
	private Handler handler;
	private GestureDetector gestureDetector;
	private BluetoothAdapter bluetoothAdapter = null;
	
	private BluetoothCommandService commandService = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		initWindow();
		initStatusTextHandler();
		initCommandService();
		
		gestureDetector = new GestureDetector(AndroCopeActivity.this, new GestureListener(commandService));
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (bluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	private void initWindow() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
	}

	private void initStatusTextHandler() {
		statusText = (TextView) findViewById(R.id.title_right_text);
		handler = new MessageHandler(statusText, getApplicationContext());
	}

	private void initCommandService() {
		commandService = new BluetoothCommandService(this, handler);
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		// If Bluetooth is not on, request that it be enabled.
		// initCommandService() will then be called during onActivityResult
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		// otherwise set up the command service
		else {
			if (commandService == null)
				initCommandService();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (commandService != null) {
			if (commandService.getState() == BluetoothCommandService.STATE_NONE) {
				commandService.start();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (commandService != null)
			commandService.stop();
	}

	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BluetoothDevice object
				BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
				// Attempt to connect to the device
				commandService.connect(device);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up the service
				initCommandService();
			} else {
				// User did not enable Bluetooth or an error occured
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
		case R.id.discoverable:
			ensureDiscoverable();
			return true;
		}
		return false;
	}

	private void ensureDiscoverable() {
		if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra( BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
	    return gestureDetector.onTouchEvent(me);
	}
}