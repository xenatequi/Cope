package tn.insat.androcope;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClipboardListener implements FlavorListener{
	
	Transferable contents ;
	ObjectOutputStream output;
	String result;
	public ClipboardListener(ObjectOutputStream output) {
		this.output = output;
	}

	

	@Override
	public void flavorsChanged(FlavorEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("A3333333333333");
		final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		contents = clipboard.getContents(null);
		try {
			result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			System.out.println("resultat "+result);
			Mouse mouse = new Mouse(Mouse.ACTION_COPY,result);
			output.writeObject(mouse);
			
			
			
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}