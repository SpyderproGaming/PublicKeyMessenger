package JFrames;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.UIManager;
import ResourcePack.ResourcePack;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.Window.Type;
public class SendRecPick 
{
	JFrame SendRecPickWin;
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					SendRecPick SelectionWindow = new SendRecPick();
					SelectionWindow.SendRecPickWin.setVisible(true);
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	public SendRecPick()
	{
		initialize();
	}
	private void initialize()
	{
		SendRecPickWin = new JFrame();
		SendRecPickWin.setType(Type.UTILITY);
		SendRecPickWin.setBackground(Color.WHITE);
		SendRecPickWin.setForeground(Color.WHITE);
		SendRecPickWin.setFont(new Font("Arial", Font.PLAIN, 12));
		SendRecPickWin.getContentPane().setBackground(ResourcePack.getBackgroundColor());
		SendRecPickWin.setTitle("SendRec Window V"+ResourcePack.getVersionNum());
		int winWid=ResourcePack.getWidth()/6;
		int winHei=ResourcePack.getHeight()/6;
		int posRit=(ResourcePack.getWidth()/2)-(winWid/2);
		int posDwn=(ResourcePack.getHeight()/2)-(winHei/2);
		SendRecPickWin.setBounds(posRit, posDwn, winWid, winHei);//right,down,width,height
		SendRecPickWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SendRecPickWin.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblPleaseSelectAn = new JLabel("Please select an action:");
		lblPleaseSelectAn.setForeground(ResourcePack.getTextColor());
		lblPleaseSelectAn.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseSelectAn.setFont(new Font("Tahoma", Font.PLAIN, 21));
		SendRecPickWin.getContentPane().add(lblPleaseSelectAn);
		JButton btnSignIn = new JButton("Send Message");
		btnSignIn.setForeground(ResourcePack.getTextColor());
		btnSignIn.setBackground(Color.LIGHT_GRAY);
		SendRecPickWin.getContentPane().add(btnSignIn);
		JButton btnCreateAnAccount = new JButton("Recieve Message");
		btnCreateAnAccount.setForeground(ResourcePack.getTextColor());
		btnCreateAnAccount.setBackground(Color.LIGHT_GRAY);
		SendRecPickWin.getContentPane().add(btnCreateAnAccount);
		btnSignIn.addActionListener(new ActionListener()//Send Button
		{
			public void actionPerformed(ActionEvent e)
			{
				Send sendFrame=new Send();
				Send.sendFrame.setVisible(true);
				SendRecPickWin.dispose();
			}
		});
		btnCreateAnAccount.addActionListener(new ActionListener()//Recieve button
		{
			public void actionPerformed(ActionEvent e)
			{
				Recieve recieveFrame = new Recieve();
				Recieve.recieveFrame.setVisible(true);
				SendRecPickWin.dispose();
			}
		});
	}
}