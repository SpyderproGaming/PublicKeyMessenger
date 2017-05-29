package JFrames;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import ResourcePack.ResourcePack;
import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.SwingConstants;
import java.awt.Color;
public class Login
{
	JFrame LoginWindow;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e)
		{
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Login logInWindow = new Login();
					logInWindow.LoginWindow.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	public Login()
	{
		initialize();
	}
	private void initialize()
	{
		LoginWindow = new JFrame();
		LoginWindow.getContentPane().setBackground(ResourcePack.getBackgroundColor());
		LoginWindow.setTitle("Secure Messenger Login V"+ResourcePack.getVersionNum());
		LoginWindow.setType(Type.UTILITY);
		int winWid=ResourcePack.getWidth()/9;
		int winHei=ResourcePack.getHeight()/6;
		int posRit=(ResourcePack.getWidth()/2)-(winWid/2);
		int posDwn=(ResourcePack.getHeight()/2)-(winHei/2);
		LoginWindow.setBounds(posRit, posDwn, winWid, winHei);//right,down,width,height
		LoginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LoginWindow.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setForeground(ResourcePack.getTextColor());
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		LoginWindow.getContentPane().add(lblNewLabel);
		textFieldUsername = new JTextField();
		textFieldUsername.setForeground(ResourcePack.getTextColor());
		textFieldUsername.setBackground(Color.LIGHT_GRAY);
		textFieldUsername.setToolTipText("Username");
		LoginWindow.getContentPane().add(textFieldUsername);
		textFieldUsername.setColumns(10);
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(ResourcePack.getTextColor());
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		LoginWindow.getContentPane().add(lblPassword);
		passwordField = new JPasswordField();
		passwordField.setForeground(ResourcePack.getTextColor());
		passwordField.setBackground(Color.LIGHT_GRAY);
		passwordField.setToolTipText("Password");
		LoginWindow.getContentPane().add(passwordField);
		JButton btnSignIn = new JButton("Sign In");
		btnSignIn.setForeground(ResourcePack.getTextColor());
		btnSignIn.setBackground(Color.LIGHT_GRAY);
		btnSignIn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		LoginWindow.getContentPane().add(btnSignIn);
		btnSignIn.addActionListener(new ActionListener()//Sign in button
		{
			public void actionPerformed(ActionEvent e)
			{
				if(textFieldUsername.getText().length()>0)
				{
					if(passwordField.getPassword().length>0)
					{
						ResourcePack.calcDirs();
						ArrayList<String> creds=new ArrayList<String>();
						try
						{
							creds=ResourcePack.readStrings();//see if the file exists
						} catch (IOException e1)
						{
							try
							{
								Files.write(Paths.get(ResourcePack.getFileName()),"".getBytes());//if it doesnt, make it
							} catch (IOException e2)
							{
								e2.printStackTrace();
							}
						}
						try
						{
							creds = ResourcePack.readStrings();
						} catch (IOException e1)
						{
							e1.printStackTrace();
						}
						ResourcePack.setPass(new String(passwordField.getPassword()));
						try
						{
							if(ResourcePack.logIn(creds,textFieldUsername.getText()))
							{
								SendRecPick sendRecPick=new SendRecPick();
								sendRecPick.SendRecPickWin.setVisible(true);
								LoginWindow.dispose();
							}
							else
								JOptionPane.showMessageDialog(null,"Login not found, try again");
						} catch (NoSuchAlgorithmException | IOException e1)
						{
							e1.printStackTrace();
						} catch (HeadlessException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
					}
					else
						JOptionPane.showMessageDialog(null,"Please enter a password");
				}
				else
					JOptionPane.showMessageDialog(null,"Please enter a username");
			}
		});
	}
}
