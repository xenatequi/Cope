package tn.insat.androcope;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothManager {

	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	
	private Activity activity;
	private BluetoothAdapter bluetoothAdapter;
	
	
	public BluetoothManager(Activity activity, BluetoothAdapter bluetoothAdapter) {
		this.activity = activity;
		this.bluetoothAdapter= bluetoothAdapter;
	}

	public boolean supportBluetooth() {
		
		if (bluetoothAdapter == null) {
			Toast.makeText(activity,
					"Your device doesn't support Bluetooth :(", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			Toast.makeText(activity, "Your device supports Bluetooth :)", Toast.LENGTH_SHORT)
					.show();
			return true;
		}
	}

	// Ajouter dans les préférences, soit on demande l'activation à
	// l'utilisateur, soit on l'active tout seul, bluetoothAdapter.enable();
	public void enableBluetoothWithRequest() {
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			activity.startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
		} else {
			Toast.makeText(activity, "Bluetooth already activated", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void enableBluetooth() {
		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
		}
	}
	
	
}
