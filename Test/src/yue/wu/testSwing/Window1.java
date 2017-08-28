package yue.wu.testSwing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Window1 extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JFrame frmBankSystem;
	private JTextField textUsername;
	private JPasswordField passwordField;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window1 window = new Window1();
					window.frmBankSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	JRadioButton rdbtnUser = new JRadioButton("User");
	JRadioButton rdbtnAdministrator = new JRadioButton("Administrator");

	private void initialize() {
		frmBankSystem = new JFrame();
		frmBankSystem.setTitle("Bank System");
		frmBankSystem.setBounds(700, 350, 450, 270);
		frmBankSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBankSystem.getContentPane().setLayout(null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(70, 31, 81, 21);
		frmBankSystem.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(70, 76, 81, 21);
		frmBankSystem.getContentPane().add(lblPassword);

		textUsername = new JTextField();
		textUsername.setBounds(166, 28, 185, 27);
		frmBankSystem.getContentPane().add(textUsername);
		//textUsername.setColumns(10);

		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(80, 165, 125, 31);
		frmBankSystem.getContentPane().add(btnLogin);

		//set listener
		btnLogin.addActionListener(this);  

		passwordField = new JPasswordField();
		passwordField.setBounds(166, 73, 185, 27);
		frmBankSystem.getContentPane().add(passwordField);


		buttonGroup.add(rdbtnUser);
		rdbtnUser.setBounds(166, 125, 67, 29);
		frmBankSystem.getContentPane().add(rdbtnUser);


		buttonGroup.add(rdbtnAdministrator);
		rdbtnAdministrator.setBounds(244, 125, 143, 29);
		frmBankSystem.getContentPane().add(rdbtnAdministrator);

		//choose user button by default
		rdbtnUser.setSelected(true);

		JLabel lblAuthority = new JLabel("Authority");
		lblAuthority.setBounds(70, 129, 81, 21);
		frmBankSystem.getContentPane().add(lblAuthority);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textUsername.setText(null);
				passwordField.setText(null);
			}
		});
		btnReset.setBounds(226, 165, 125, 31);
		frmBankSystem.getContentPane().add(btnReset);
		
		//directly login by press enter
		frmBankSystem.getRootPane().setDefaultButton(btnLogin);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Login")) {
          
			BankSystem2.readFile("account.txt");
			BankSystem2.readFile("record.txt");

			if (rdbtnUser.isSelected()) {
				loginAsUser();
			}
			else if(rdbtnAdministrator.isSelected()) {
				loginAsAdmin();
			}

		}
	}

	public void loginAsUser() {
		int index;
		int size = BankSystem2.getSize();
		boolean flag = false;
		String password = SHAencrypt.encryptSHA(String.valueOf(passwordField.getPassword()));
		for (index = 0; index < size; index++)
		{
			if (BankSystem2.getAccount(index).number.equals(textUsername.getText()) 
					&& BankSystem2.getAccount(index).password.equals(password))
			{
				flag = true;
				break;
			}
		}

		if (index == size)
		{
			flag = false;
		}
		
		BankSystem2.setIndex(index);
		
		if (flag == true) {
			frmBankSystem.dispose();
			Window2.notmain("user");
		}
		else {
			JOptionPane.showMessageDialog(null, "Invalid username or password!", "Message",JOptionPane.ERROR_MESSAGE);
		}
	}

	public void loginAsAdmin() {
		if (textUsername.getText().equals("admin") && String.valueOf(passwordField.getPassword()).equals("admin")) {
			frmBankSystem.dispose();
			Window2.notmain("admin");
		}
		else {
			JOptionPane.showMessageDialog(null, "Invalid username or password!", "Message",JOptionPane.ERROR_MESSAGE);
		}
	}
}
