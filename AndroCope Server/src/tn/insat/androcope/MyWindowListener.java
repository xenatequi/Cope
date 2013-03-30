package tn.insat.androcope;

import java.awt.event.*;
import javax.swing.*;

public class MyWindowListener extends WindowAdapter {

	public void windowClosing(WindowEvent e) {
		JFrame fen = new JFrame();
		JLabel etiket = new JLabel("texte initial");
		int p = JOptionPane.showConfirmDialog(fen,
				"Voulez-vous vraiment quitter ?", "Projet Word Gl2/2",
				JOptionPane.YES_NO_OPTION);
		if (p == JOptionPane.YES_OPTION)
			System.exit(0);
		

	}

}