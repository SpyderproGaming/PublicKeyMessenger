package JFrames;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Recieve {

	static JFrame recieveFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Recieve window = new Recieve();
					Recieve.recieveFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Recieve() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		recieveFrame = new JFrame();
		recieveFrame.setBounds(100, 100, 450, 300);
		recieveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
