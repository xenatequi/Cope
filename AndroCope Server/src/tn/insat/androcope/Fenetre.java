package tn.insat.androcope;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tn.insat.androcope.thread.WaitThread;

public class Fenetre extends JFrame implements ActionListener {

	public int i;

	private JPanel panel_bouton = null;
	private JPanel panel_label = null;
	ImageIcon icon_start = null;
	ImageIcon icon_stop = null;
	private JButton start = null;
	private JButton stop = null;
	private JLabel label = null;
	WaitThread waitThread = null;
	MyWindowListener x = new MyWindowListener();

	public Fenetre()  {

		this.init_Var();
		this.initBoutons();
		this.initWindowProperties();


	}

	public void init_Var() {

		panel_label = new JPanel();
		label = new JLabel();
		// ajouter les composants aux conteneurs
		panel_label.add(label);

		// Black Background
		panel_label.setBackground(Color.BLACK);

		this.add(panel_label, BorderLayout.NORTH);

		waitThread = new WaitThread();

	}

	public void initBoutons() {

		panel_bouton = new JPanel();

		icon_start = new ImageIcon(("./image/start.png"));
		icon_stop = new ImageIcon(("./image/stop.png"));
		start = new JButton(icon_start);
		stop = new JButton(icon_stop);

		// Making a transparent button having only the icon
		start.setBorderPainted(false);
		start.setFocusPainted(false);
		start.setContentAreaFilled(false);

		stop.setBorderPainted(false);
		stop.setFocusPainted(false);
		stop.setContentAreaFilled(false);

		// Ajout des actions boutons
		start.addActionListener(this);
		stop.addActionListener(this);

		// ajouter les composants aux conteneurs
		panel_bouton.add(start);
		panel_bouton.add(stop);

		// Black Background
		panel_bouton.setBackground(Color.BLACK);
		this.add(panel_bouton, BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == start) {
			i=i+1;
			if (!(waitThread.isAlive())) {
				label.setText("Server is waiting for connection");
				label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,14));
				label.setForeground(Color.BLUE);
				
				waitThread.start();
			} else {
				label.setText("Server already started");
				label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,14));
				label.setForeground(Color.RED);
			}
		}
		if (e.getSource() == stop) {
			waitThread.setStop(true);
			System.out.println("stopppppppppp");
		}
	}

	// ============================= WINDOW ========================

	private void initWindowProperties() {
		Container contenu = this.getContentPane();
		contenu.setBackground(Color.BLACK);
		setTitle("Cope Project");
		this.addWindowListener(x);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocation(380, 190);
		setMinimumSize(new Dimension(300, 400));
		setVisible(true);
	}

}
