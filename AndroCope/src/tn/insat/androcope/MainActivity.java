package tn.insat.androcope;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothManager bluetoothManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		bluetoothManager = new BluetoothManager(this, bluetoothAdapter);
		if (bluetoothManager.supportBluetooth()) {
			bluetoothManager.enableBluetoothWithRequest();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//appelée par BluetoothManager.enbaleBluetoothWithRequest(), grâce à activity.startActivityForResult
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH)
			return;
		if (resultCode == RESULT_OK) {
			Toast.makeText(MainActivity.this, "Enabled", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(MainActivity.this, "Not Enabled",
					Toast.LENGTH_SHORT).show();
		}
	}

}
