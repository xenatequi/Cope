package tn.insat.androcope.listener;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.ObjectOutputStream;

import tn.insat.androcope.bean.Mouse;

public class ClipboardListener implements FlavorListener{
	
	Transferable contents ;
	ObjectOutputStream output;
	String result;
	
	public ClipboardListener(ObjectOutputStream output) {
		this.output = output;
	}

	@Override
	public void flavorsChanged(FlavorEvent arg0) {
		System.out.println("Clipboard Changed");
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		contents = clipboard.getContents(null);
		try {
			result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			System.out.println("Result : "+result);
			Mouse mouse = new Mouse(Mouse.ACTION_COPY,result);
			output.writeObject(mouse);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}