package tn.insat.androcope;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
		
	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null)
		   Toast.makeText(MainActivity.this, "Pas de Bluetooth", 
		                  Toast.LENGTH_SHORT).show();
		else
		   Toast.makeText(MainActivity.this, "Avec Bluetooth", 
		                  Toast.LENGTH_SHORT).show();

		if (!bluetoothAdapter.isEnabled()) {
		   Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		   startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
		}
		else{
			Toast.makeText(MainActivity.this, "Bluetooth already activated", 
	                  Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	       super.onActivityResult(requestCode, resultCode, data);
	       if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH)
	           return;
	       if (resultCode == RESULT_OK) {
	    	   Toast.makeText(MainActivity.this, "Activated", 
		                  Toast.LENGTH_SHORT).show();
	       } else {
	    	   Toast.makeText(MainActivity.this, "Not Activated", 
		                  Toast.LENGTH_SHORT).show();
	       }    
	}

}
