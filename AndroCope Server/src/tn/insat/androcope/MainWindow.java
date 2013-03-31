package tn.insat.androcope;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tn.insat.androcope.thread.WaitThread;

public class MainWindow extends JFrame implements ActionListener {

	public static int MESSAGE_INFO  = 1;
	public static int MESSAGE_ERROR  = 2;
	
	private static String ICON_START = "./image/start.png";
	private static String ICON_STOP  = "./image/stop.png";
	private static String TITLE  = "Cope Server";
	
	private JPanel buttonPanel = new JPanel();
	private JPanel messagePanel = new JPanel();
	private ImageIcon iconStart = null;
	private ImageIcon iconStop = null;
	private JButton start = null;
	private JButton stop = null;
	private JLabel message = new JLabel();
	private WaitThread waitThread;

	public MainWindow()  {
		this.initMessagePanel();
		this.initButtonPanel();
		this.initWindowProperties();
	}

	private void initMessagePanel() {
		message.setFont(new Font(Font.SANS_SERIF,Font.BOLD,14));
		message.setForeground(Color.BLUE);
		
		messagePanel.add(message);
		messagePanel.setOpaque(false);
		this.add(messagePanel, BorderLayout.NORTH);
	}

	private void initButtonPanel() {
		iconStart = new ImageIcon((ICON_START));
		iconStop = new ImageIcon((ICON_STOP));
		start = new JButton(iconStart);
		stop = new JButton(iconStop);
		start.addActionListener(this);
		stop.addActionListener(this);

		setTransparentButton(start);
		setTransparentButton(stop);

		buttonPanel.add(start);
		buttonPanel.add(stop);

		buttonPanel.setOpaque(false);
		this.add(buttonPanel, BorderLayout.SOUTH);

	}

	private void setTransparentButton(JButton button) {
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
	}

	private void initWindowProperties() {
		this.getContentPane().setBackground(Color.BLACK);
		this.setLocation(380, 190);
		this.setMinimumSize(new Dimension(400,200));
		this.setTitle(TITLE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("./image/logo.png")); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == start) {
			if (waitThread == null) {
				waitThread = new WaitThread(this);
				waitThread.start();
			}
			else if (!(waitThread.isAlive())){	
				waitThread.start();
				setMessage("Server is waiting for connection", this.MESSAGE_INFO);
			}
		}
		else if (e.getSource() == stop) {
			setMessage("Server stopped", this.MESSAGE_INFO);
			System.exit(0);
		}
	}
	
	public void setMessage(String message, int type) {
		this.message.setText(message);
	}


}
