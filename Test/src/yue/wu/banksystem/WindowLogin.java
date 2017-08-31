/**
* Copyright © yue.w.wu@oracle.com. All rights reserved.
* @Title: WindowLogin.java
* @Package yue.wu.banksystem
* @version V1.0
* @author ywu
* @date Aug 31, 2017
*/
package yue.wu.banksystem;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * login window
 */
public class WindowLogin {

	private JFrame frmBankSystem;
	private JTextField textUsername;
	private JPasswordField passwordField;

	/**
	 * entry function
	 *
	 * @param args
	 *            null
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
					WindowLogin window = new WindowLogin();
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
	public WindowLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
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

		passwordField = new JPasswordField();
		passwordField.setBounds(166, 73, 185, 27);
		frmBankSystem.getContentPane().add(passwordField);

		JLabel lblAuthority = new JLabel("Authority");
		lblAuthority.setBounds(70, 129, 81, 21);
		frmBankSystem.getContentPane().add(lblAuthority);

		JRadioButton rdbtnUser = new JRadioButton("User");
		JRadioButton rdbtnAdministrator = new JRadioButton("Administrator");
		ButtonGroup buttonGroup = new ButtonGroup();

		buttonGroup.add(rdbtnUser);
		rdbtnUser.setBounds(166, 125, 67, 29);
		frmBankSystem.getContentPane().add(rdbtnUser);
		rdbtnUser.setSelected(true);

		buttonGroup.add(rdbtnAdministrator);
		rdbtnAdministrator.setBounds(244, 125, 143, 29);
		frmBankSystem.getContentPane().add(rdbtnAdministrator);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BankSystem.readFile("account.txt");
				BankSystem.readFile("record.txt");
				if (rdbtnUser.isSelected()) {
					loginAsUser();
				} else if (rdbtnAdministrator.isSelected()) {
					loginAsAdmin();
				}
			}
		});
		btnLogin.setBounds(80, 165, 125, 31);
		frmBankSystem.getContentPane().add(btnLogin);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textUsername.setText(null);
				passwordField.setText(null);
			}
		});
		btnReset.setBounds(226, 165, 125, 31);
		frmBankSystem.getContentPane().add(btnReset);

		// press Enter to login instead of pressing login button
		frmBankSystem.getRootPane().setDefaultButton(btnLogin);
	}

	/**
	 * login as common user
	 *
	 */
	public void loginAsUser() {
		int index;
		int size = BankSystem.getSize();
		boolean loginFlag = false;
		String password = ShaEncrypt.encryptSha(String.valueOf(passwordField.getPassword()));
		for (index = 0; index < size; index++) {
			if (BankSystem.getAccount(index).number.equals(textUsername.getText())
					&& BankSystem.getAccount(index).password.equals(password)) {
				loginFlag = true;
				break;
			}
		}

		if (index == size) {
			loginFlag = false;
		}

		if (true == loginFlag) {
			frmBankSystem.dispose();
			WindowMenu.main(null);
		} else {
			JOptionPane.showMessageDialog(null, "Invalid username or password!", "Message", JOptionPane.ERROR_MESSAGE);
		}

		BankSystem.setIndex(index);
		BankSystem.setAdminFlag(false);
	}

	/**
	 * login as administrator
	 *
	 */
	public void loginAsAdmin() {
		if (textUsername.getText().equals("admin") && String.valueOf(passwordField.getPassword()).equals("admin")) {
			frmBankSystem.dispose();
			WindowMenu.main(null);
		} else {
			JOptionPane.showMessageDialog(null, "Invalid username or password!", "Message", JOptionPane.ERROR_MESSAGE);
		}

		BankSystem.setIndex(-1);
		BankSystem.setAdminFlag(true);
	}
}
