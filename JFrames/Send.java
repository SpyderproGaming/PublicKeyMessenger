package JFrames;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Send {

	static JFrame sendFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Send window = new Send();
					Send.sendFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Send() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		sendFrame = new JFrame();
		sendFrame.setBounds(100, 100, 450, 300);
		sendFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
