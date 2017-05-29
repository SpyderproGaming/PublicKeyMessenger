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
public class ActionSelect 
{
	JFrame ActionSelectWindow;
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
					ActionSelect SelectionWindow = new ActionSelect();
					SelectionWindow.ActionSelectWindow.setVisible(true);
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	public ActionSelect()
	{
		initialize();
	}
	private void initialize()
	{
		ActionSelectWindow = new JFrame();
		ActionSelectWindow.setType(Type.UTILITY);
		ActionSelectWindow.setBackground(Color.WHITE);
		ActionSelectWindow.setForeground(Color.WHITE);
		ActionSelectWindow.setFont(new Font("Arial", Font.PLAIN, 12));
		ActionSelectWindow.getContentPane().setBackground(ResourcePack.getBackgroundColor());
		ActionSelectWindow.setTitle("Secure messenger selector V"+ResourcePack.getVersionNum());
		int winWid=ResourcePack.getWidth()/6;
		int winHei=ResourcePack.getHeight()/6;
		int posRit=(ResourcePack.getWidth()/2)-(winWid/2);
		int posDwn=(ResourcePack.getHeight()/2)-(winHei/2);
		ActionSelectWindow.setBounds(posRit, posDwn, winWid, winHei);//right,down,width,height
		ActionSelectWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ActionSelectWindow.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblWelcomeToAros = new JLabel("Secure Java Messenger");
		lblWelcomeToAros.setForeground(ResourcePack.getTextColor());
		lblWelcomeToAros.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToAros.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ActionSelectWindow.getContentPane().add(lblWelcomeToAros);
		JLabel lblPleaseSelectAn = new JLabel("Please select an action:");
		lblPleaseSelectAn.setForeground(ResourcePack.getTextColor());
		lblPleaseSelectAn.setVerticalAlignment(SwingConstants.TOP);
		lblPleaseSelectAn.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseSelectAn.setFont(new Font("Tahoma", Font.PLAIN, 17));
		ActionSelectWindow.getContentPane().add(lblPleaseSelectAn);
		JButton btnSignIn = new JButton("Sign In");
		btnSignIn.setForeground(ResourcePack.getTextColor());
		btnSignIn.setBackground(Color.LIGHT_GRAY);
		ActionSelectWindow.getContentPane().add(btnSignIn);
		JButton btnCreateAnAccount = new JButton("Create An Account");
		btnCreateAnAccount.setForeground(ResourcePack.getTextColor());
		btnCreateAnAccount.setBackground(Color.LIGHT_GRAY);
		ActionSelectWindow.getContentPane().add(btnCreateAnAccount);
		btnSignIn.addActionListener(new ActionListener()//Sign in button
		{
			public void actionPerformed(ActionEvent e)
			{
				Login logWin = new Login();
				logWin.LoginWindow.setVisible(true);
				ActionSelectWindow.dispose();
			}
		});
		btnCreateAnAccount.addActionListener(new ActionListener()//Create an account button
		{
			public void actionPerformed(ActionEvent e)
			{
				UserCreation createWin = new UserCreation();
				createWin.UserCreationWin.setVisible(true);
				ActionSelectWindow.dispose();
			}
		});
	}
}