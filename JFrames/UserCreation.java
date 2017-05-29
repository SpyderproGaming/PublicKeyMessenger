package JFrames;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import ResourcePack.ResourcePack;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.SwingConstants;
public class UserCreation
{
	JFrame UserCreationWin;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
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
					UserCreation window = new UserCreation();
					window.UserCreationWin.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	public UserCreation()
	{
		initialize();
	}
	private void initialize()
	{
		UserCreationWin = new JFrame();
		UserCreationWin.setBackground(ResourcePack.getBackgroundColor());
		UserCreationWin.getContentPane().setBackground(ResourcePack.getBackgroundColor());
		UserCreationWin.getContentPane().setForeground(ResourcePack.getTextColor());
		UserCreationWin.setForeground(ResourcePack.getTextColor());
		UserCreationWin.setTitle("Secure User Creation V"+ResourcePack.getVersionNum());
		UserCreationWin.setType(Type.UTILITY);
		int winWid=ResourcePack.getWidth()/9;
		int winHei=ResourcePack.getHeight()/6;
		int posRit=(ResourcePack.getWidth()/2)-(winWid/2);
		int posDwn=(ResourcePack.getHeight()/2)-(winHei/2);
		UserCreationWin.setBounds(posRit, posDwn, winWid, winHei);//right,down,width,height
		UserCreationWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UserCreationWin.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblUsername = new JLabel("New Username");
		lblUsername.setBackground(ResourcePack.getBackgroundColor());
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setForeground(ResourcePack.getTextColor());
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
		UserCreationWin.getContentPane().add(lblUsername);
		textFieldUsername = new JTextField();
		textFieldUsername.setToolTipText("New Username");
		textFieldUsername.setBackground(Color.LIGHT_GRAY);
		textFieldUsername.setForeground(ResourcePack.getTextColor());
		UserCreationWin.getContentPane().add(textFieldUsername);
		textFieldUsername.setColumns(10);
		JLabel lblPassword = new JLabel("New Password");
		lblPassword.setBackground(ResourcePack.getBackgroundColor());
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setForeground(ResourcePack.getTextColor());
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		UserCreationWin.getContentPane().add(lblPassword);
		passwordField = new JPasswordField();
		passwordField.setToolTipText("New Password");
		passwordField.setBackground(Color.LIGHT_GRAY);
		passwordField.setForeground(ResourcePack.getTextColor());
		UserCreationWin.getContentPane().add(passwordField);
		JButton btnCreateUser = new JButton("Create User");
		btnCreateUser.setBackground(Color.LIGHT_GRAY);
		btnCreateUser.setForeground(ResourcePack.getTextColor());
		btnCreateUser.setFont(new Font("Tahoma", Font.PLAIN, 16));
		UserCreationWin.getContentPane().add(btnCreateUser);
		btnCreateUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String passHash="";
				boolean duplicate=false;
				if(textFieldUsername.getText().length()>0)
				{
					if(passwordField.getPassword().length>0)
					{
						String newUser=textFieldUsername.getText();
						String newPass=new String(passwordField.getPassword());
						try
						{
							passHash=ResourcePack.hashGenerator(newPass);
						}
						catch (NoSuchAlgorithmException | UnsupportedEncodingException e1)
						{
							e1.printStackTrace();
						}
						ResourcePack.calcDirs();
						ArrayList<String> creds=new ArrayList<String>();
						try//try to write to the file
						{
							creds = ResourcePack.readStrings();
							for(int i=0;i<creds.size();i++)
								if(creds.get(i).equals(newUser))
									duplicate=true;
							if(!duplicate)
							{
								creds.add(newUser);
								creds.add(passHash);
								KeyPair keys=ResourcePack.generatePair();
								PrivateKey privKey=keys.getPrivate();
								PublicKey pubKey=keys.getPublic();
								creds.add(ResourcePack.encryptBlowfish(ResourcePack.privKeyExp(privKey).toString(),newPass));
								creds.add(ResourcePack.encryptBlowfish(ResourcePack.pubKeyMod(pubKey).toString(), newPass));
								ResourcePack.writeStrings(creds);
							}
							else
								JOptionPane.showMessageDialog(null,"That username is already taken");
						}
						catch (IOException e1)//if the file doesnt exist, so make it and add our username
						{
							try
							{
								Files.write(Paths.get(ResourcePack.getFileName()),"".getBytes());
								creds.add(textFieldUsername.getText());
								creds.add(passHash);
								KeyPair keys=ResourcePack.generatePair();
								PrivateKey privKey=keys.getPrivate();
								PublicKey pubKey=keys.getPublic();
								creds.add(ResourcePack.encryptBlowfish(ResourcePack.privKeyExp(privKey).toString(),newPass));
								creds.add(ResourcePack.encryptBlowfish(ResourcePack.pubKeyMod(pubKey).toString(), newPass));
								ResourcePack.writeStrings(creds);
							}
							catch (IOException e2)
							{
								e2.printStackTrace();
							} catch (NoSuchAlgorithmException e2) {
								e2.printStackTrace();
							} catch (InvalidKeySpecException e2) {
								e2.printStackTrace();
							} catch (InvalidKeyException e2) {
								e2.printStackTrace();
							} catch (IllegalBlockSizeException e2) {
								e2.printStackTrace();
							} catch (BadPaddingException e2) {
								e2.printStackTrace();
							} catch (NoSuchPaddingException e2) {
								e2.printStackTrace();
							}
						} catch (NoSuchAlgorithmException e1) {
							e1.printStackTrace();
						} catch (InvalidKeySpecException e1) {
							e1.printStackTrace();
						} catch (InvalidKeyException e1) {
							e1.printStackTrace();
						} catch (IllegalBlockSizeException e1) {
							e1.printStackTrace();
						} catch (BadPaddingException e1) {
							e1.printStackTrace();
						} catch (NoSuchPaddingException e1) {
							e1.printStackTrace();
						}
						if(!duplicate)
						{
							ActionSelect actionSel = new ActionSelect();
							actionSel.ActionSelectWindow.setVisible(true);
							UserCreationWin.dispose();
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