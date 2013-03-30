package tn.insat.androcope;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tn.insat.androcope.thread.WaitThread;

public class MainWindow extends JFrame implements ActionListener {

	private static String ICON_START = "./image/start.png";
	private static String ICON_STOP  = "./image/stop.png";
	public int i;

	private JPanel panelButton = new JPanel();
	private JPanel panelLabel = new JPanel();
	private ImageIcon iconStart = null;
	private ImageIcon iconStop = null;
	private JButton start = null;
	private JButton stop = null;
	private JLabel label = new JLabel();
	WaitThread waitThread = new WaitThread();
	MyWindowListener windowsListener = new MyWindowListener();

	public MainWindow()  {
		this.initVariables();
		this.initButtons();
		this.initWindowProperties();


	}

	public void initVariables() {

		panelLabel.add(label);
		panelLabel.setBackground(Color.BLACK);
		this.add(panelLabel, BorderLayout.NORTH);
	}

	public void initButtons() {
		iconStart = new ImageIcon((ICON_START));
		iconStop = new ImageIcon((ICON_STOP));
		start = new JButton(iconStart);
		stop = new JButton(iconStop);

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
		panelButton.add(start);
		panelButton.add(stop);

		// Black Background
		panelButton.setBackground(Color.BLACK);
		this.add(panelButton, BorderLayout.SOUTH);

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
		this.addWindowListener(windowsListener);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocation(380, 190);
		setMinimumSize(new Dimension(300, 400));
		setVisible(true);
	}

}
