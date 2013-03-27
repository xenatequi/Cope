package tn.insat.androcope;

import tn.insat.androcope.thread.WaitThread;

public class RemoteBluetoothServer{
	
	public static void main(String[] args) {
		Thread waitThread = new Thread(new WaitThread());
		waitThread.start();
	}
}
