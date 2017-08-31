/**
* Copyright © yue.w.wu@oracle.com. All rights reserved.
* @Title: WindowMenu.java
* @Package yue.wu.banksystem
* @version V1.0
* @author ywu
* @date Aug 31, 2017
*/
package yue.wu.banksystem;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.*;

/**
 * menu window
 */
public class WindowMenu {

	private JFrame frmBankSystem;

	private JMenu mnStart;
	private static JMenu mnUser;
	private static JMenu mnAdmin;

	private String[] columnTitle1 = { "CARD NUMBER", "PASSWORD", "BALANCE" };
	private DefaultTableModel tableModel1;
	private JTable table1;
	private JScrollPane sp1;

	private String[] columnTitle2 = { "CARD NUMBER", "DATE AND TIME", "OPERATION", "AMOUNT" };
	private DefaultTableModel tableModel2;
	private JTable table2;
	private JScrollPane sp2;

	private static JLabel lblHisRecord;

	private JPopupMenu m_popupMenu;
	// left click to choose user, and set flag to true, then right click to operate
	private boolean userChosenFlag = false;
	// index of user data linked list, not index of table
	private int[] selectedUserIndex;

	/**
	 * entry function
	 *
	 * @param args
	 *            null
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowMenu window = new WindowMenu();

					if (false == BankSystem.getAdminFlag()) {
						mnAdmin.setEnabled(false);
					} else if (true == BankSystem.getAdminFlag()) {
						mnUser.setEnabled(false);
					}

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
	public WindowMenu() {
		initialize();

		JLabel lblWelcome = new JLabel("<html><body><br>Welcome to Simple Bank System<br><br></body></html>",
				JLabel.CENTER);
		lblWelcome.setForeground(Color.RED);
		lblWelcome.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
		lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
		frmBankSystem.getContentPane().add(lblWelcome);

		JLabel lblAccInfo = new JLabel("<html><body>Account Information</body></html>", JLabel.CENTER);
		lblAccInfo.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
		lblAccInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		frmBankSystem.getContentPane().add(lblAccInfo);

		showTable();

		lblHisRecord = new JLabel("<html><body><br>Historical Records</body></html>", JLabel.CENTER);
		lblHisRecord.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
		lblHisRecord.setAlignmentX(Component.CENTER_ALIGNMENT);
		frmBankSystem.getContentPane().add(lblHisRecord);

		showTable2();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBankSystem = new JFrame();
		frmBankSystem.setTitle("Bank System");
		frmBankSystem.setBounds(700, 200, 450, 500);
		frmBankSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBankSystem.getContentPane().setLayout(new BoxLayout(frmBankSystem.getContentPane(), BoxLayout.Y_AXIS));

		JMenuBar menuBar = new JMenuBar();
		frmBankSystem.setJMenuBar(menuBar);

		mnStart = new JMenu("Start");
		mnStart.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		menuBar.add(mnStart);

		JMenuItem mntmWelcome = new JMenuItem("Welcome");
		mntmWelcome.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		mntmWelcome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowWelcome.main(null);
			}
		});
		mnStart.add(mntmWelcome);

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		mnStart.add(mntmLogout);

		mnUser = new JMenu("User");
		mnUser.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		menuBar.add(mnUser);

		JMenuItem mntmQuery = new JMenuItem("Query Balance");
		mntmQuery.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		mntmQuery.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
		mntmQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryBalance();
			}
		});
		mnUser.add(mntmQuery);

		JMenuItem mntmDeposit = new JMenuItem("Deposit Money");
		mntmDeposit.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		mntmDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				depositMoney();
			}
		});
		mnUser.add(mntmDeposit);

		JMenuItem mntmDraw = new JMenuItem("Withraw Money");
		mntmDraw.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		mntmDraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				withdrawMoney();
			}
		});
		mnUser.add(mntmDraw);

		JMenuItem mntmChange = new JMenuItem("Change Password");
		mntmChange.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		mntmChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePassword();
			}
		});
		mnUser.add(mntmChange);

		JMenuItem mntmQueryRecord = new JMenuItem("Query Record");
		mntmQueryRecord.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		mntmQueryRecord.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmQueryRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryRecord();
			}
		});
		mnUser.add(mntmQueryRecord);

		mnAdmin = new JMenu("Admin");
		mnAdmin.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		menuBar.add(mnAdmin);

		JMenuItem mntmAdd = new JMenuItem("Open Account");
		mntmAdd.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		mntmAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAccount();
			}
		});
		mnAdmin.add(mntmAdd);

		JMenuItem mntmShow = new JMenuItem("Show All Accounts");
		mntmShow.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		mntmShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAllAccounts();
			}
		});
		mnAdmin.add(mntmShow);
	}

	/**
	 * if one row is selected, set index of chosen user<br>
	 * if several rows are selected, save indexes of chosen users into an array
	 */
	private void setIndex() {
		int[] selectedRowIndexes = table1.getSelectedRows();
		int count = selectedRowIndexes.length;
		selectedUserIndex = new int[count];

		if (count == 1) {
			String number = String.valueOf(table1.getValueAt(table1.getSelectedRow(), 0));
			for (int index = 0; index < BankSystem.getSize(); index++) {
				if (BankSystem.getAccount(index).number.equals(number)) {
					BankSystem.setIndex(index);
					selectedUserIndex[0] = index;
					break;
				}
			}
		} else if (count > 1) {
			int size = BankSystem.getSize();
			for (int i = 0; i < count; i++) {
				String number = String.valueOf(table1.getValueAt(selectedRowIndexes[i], 0));
				for (int index = 0; index < size; index++) {
					if (BankSystem.getAccount(index).number.equals(number)) {
						selectedUserIndex[i] = index;
						break;
					}
				}
			}
		}
	}

	/**
	 * pop up menu when mouse right clicked and row selected
	 *
	 */
	private void createPopupMenu() {
		m_popupMenu = new JPopupMenu();

		JMenuItem mntmAdminDeposit = new JMenuItem();
		mntmAdminDeposit.setText("Deposit");
		mntmAdminDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (selectedUserIndex.length == 1) {
					depositMoney();
				} else if (selectedUserIndex.length > 1) {
					depositMoneyBatch();
				}
			}
		});
		m_popupMenu.add(mntmAdminDeposit);

		JMenuItem mntmAdminWithdraw = new JMenuItem();
		mntmAdminWithdraw.setText("Withdraw");
		mntmAdminWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (selectedUserIndex.length == 1) {
					withdrawMoney();
				} else if (selectedUserIndex.length > 1) {
					withdrawMoneyBatch();
				}
			}
		});
		m_popupMenu.add(mntmAdminWithdraw);

		JMenuItem mntmAdminChangePassword = new JMenuItem();
		mntmAdminChangePassword.setText("Change Password");
		mntmAdminChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				changePassword();
			}
		});
		m_popupMenu.add(mntmAdminChangePassword);

		JMenuItem mntmAdminQueryRecord = new JMenuItem();
		mntmAdminQueryRecord.setText("Query Record");
		mntmAdminQueryRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				queryRecord();
			}
		});
		m_popupMenu.add(mntmAdminQueryRecord);
	}

	/**
	 * set actions when left click to choose user or right lick to pop menu
	 *
	 * @param evt
	 */
	private void mouseClick(MouseEvent evt) {
		if (evt.getButton() == MouseEvent.BUTTON1) {
			if (-1 == table1.rowAtPoint(evt.getPoint())) {
				return;
			}
			setIndex();
			userChosenFlag = true;
		} else if (evt.getButton() == MouseEvent.BUTTON3) {
			if (-1 == table1.rowAtPoint(evt.getPoint())) {
				return;
			}
			if (true == userChosenFlag) {
				m_popupMenu.show(table1, evt.getX(), evt.getY());
				userChosenFlag = false;
			}
		}
	}

	/**
	 * show table of account information
	 *
	 */
	public void showTable() {
		tableModel1 = new DefaultTableModel(columnTitle1, 0);
		table1 = new JTable(tableModel1);
		sp1 = new JScrollPane(table1);
		frmBankSystem.getContentPane().add(sp1);

		createPopupMenu();

		table1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				mouseClick(evt);
			}
		});
	}

	/**
	 * show table of historical record
	 *
	 */
	public void showTable2() {
		tableModel2 = new DefaultTableModel(columnTitle2, 0);
		table2 = new JTable(tableModel2);
		// set to non-editable
		table2.setEnabled(false);
		sp2 = new JScrollPane(table2);
		frmBankSystem.getContentPane().add(sp2);
	}

	/**
	 * log out current user
	 *
	 */
	public void logout() {
		frmBankSystem.dispose();
		WindowLogin.main(null);
	}

	/**
	 * query balance
	 *
	 */
	public void queryBalance() {
		tableModel1 = new DefaultTableModel(columnTitle1, 1) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table1.setModel(tableModel1);

		AccountInfo account = BankSystem.getAccount();

		table1.setValueAt(account.number, 0, 0);
		table1.setValueAt(account.password, 0, 1);
		table1.setValueAt(account.balance, 0, 2);
	}

	/**
	 * deposit money
	 *
	 */
	public void depositMoney() {
		String amount = JOptionPane.showInputDialog(null, "Please input amount to be deposited:", "Deposit",
				JOptionPane.PLAIN_MESSAGE);
		if (null == amount || amount.equals("")) {
			return;
		}

		double moneyAdded = Double.parseDouble(amount);
		BankSystem.depositMoney(moneyAdded);

		// refresh displayed data
		if (false == BankSystem.getAdminFlag()) {
			queryBalance();
		} else if (true == BankSystem.getAdminFlag()) {
			showAllAccounts();
		}
		queryRecord();
	}

	/**
	 * withdraw money
	 *
	 */
	public void withdrawMoney() {
		String amount = JOptionPane.showInputDialog(null, "Please input amount to be withdrawed:", "Withdraw",
				JOptionPane.PLAIN_MESSAGE);
		if (null == amount || amount.equals("")) {
			return;
		}

		double moneyReduced = Double.parseDouble(amount);

		if (moneyReduced > BankSystem.getAccount().balance) {
			JOptionPane.showMessageDialog(null, "Not enough money!", "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}

		BankSystem.withdrawMoney(moneyReduced);

		// refresh displayed data
		if (false == BankSystem.getAdminFlag()) {
			queryBalance();
		} else if (true == BankSystem.getAdminFlag()) {
			showAllAccounts();
		}
		queryRecord();
	}

	/**
	 * change password
	 *
	 */
	public void changePassword() {
		String newPasswdOnce = JOptionPane.showInputDialog(null, "Please input new password:", "Change Password",
				JOptionPane.PLAIN_MESSAGE);
		String newPasswdTwice = JOptionPane.showInputDialog(null, "Please confirm new password:", "Change Password",
				JOptionPane.PLAIN_MESSAGE);

		if (null == newPasswdOnce || newPasswdOnce.equals("") || null == newPasswdTwice || newPasswdTwice.equals("")) {
			return;
		}

		if (!newPasswdOnce.equals(newPasswdTwice)) {
			JOptionPane.showMessageDialog(null, "The two passwords are different!", "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		BankSystem.changePassword(newPasswdOnce);

		// refresh displayed data
		if (false == BankSystem.getAdminFlag()) {
			queryBalance();
		} else if (true == BankSystem.getAdminFlag()) {
			showAllAccounts();
		}
	}

	/**
	 * query record
	 *
	 */
	public void queryRecord() {
		Vector<HistoricalRecord> records = BankSystem.queryRecord();
		int index = 0;
		int size = records.size();

		// empty table before display
		tableModel2.setRowCount(0);
		tableModel2.setRowCount(size);

		while (index < size) {
			// show new records ahead of old ones
			HistoricalRecord record = records.get(size - 1 - index);

			table2.setValueAt(record.number, index, 0);
			table2.setValueAt(record.time, index, 1);
			table2.setValueAt(record.operation, index, 2);
			table2.setValueAt(record.amount, index, 3);
			index++;
		}
	}

	/**
	 * open account
	 *
	 */
	public void addAccount() {
		AccountInfo account = new AccountInfo();
		String number = JOptionPane.showInputDialog(null, "Please input card number:", "Card Number",
				JOptionPane.PLAIN_MESSAGE);
		String password = JOptionPane.showInputDialog(null, "Please input card password:", "Card Password",
				JOptionPane.PLAIN_MESSAGE);
		String balance = JOptionPane.showInputDialog(null, "Please input card balance:", "Card Balance",
				JOptionPane.PLAIN_MESSAGE);

		if (null == number || number.equals("") || null == password || password.equals("") || null == balance
				|| balance.equals("")) {
			JOptionPane.showMessageDialog(null, "Required infomation is empty!", "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}

		account.number = number;
		account.password = password;
		account.balance = Double.parseDouble(balance);

		BankSystem.addAccount(account);
		BankSystem.logRecord("OpenAccount", account.balance, account.number);

		// refresh displayed data
		showAllAccounts();
	}

	/**
	 * show information o all accounts
	 *
	 */
	public void showAllAccounts() {

		int index = 0;
		int size = BankSystem.getSize();

		tableModel1 = new DefaultTableModel(columnTitle1, size) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table1.setModel(tableModel1);

		while (index < size) {
			AccountInfo account = BankSystem.getAccount(index);
			table1.setValueAt(account.number, index, 0);
			table1.setValueAt(account.password, index, 1);
			table1.setValueAt(account.balance, index, 2);
			index++;
		}
	}

	/**
	 * deposit money for several users at the same time
	 *
	 */
	public void depositMoneyBatch() {
		String amount = JOptionPane.showInputDialog(null, "Please input amount to be deposited for all selected users:",
				"Deposit", JOptionPane.PLAIN_MESSAGE);
		if (null == amount || amount.equals("")) {
			return;
		}
		double moneyAdded = Double.parseDouble(amount);

		for (int i = 0; i < selectedUserIndex.length; i++) {
			BankSystem.setIndex(selectedUserIndex[i]);
			BankSystem.depositMoney(moneyAdded);
		}

		showAllAccounts();
	}

	/**
	 * withdraw money for several users at the same time
	 *
	 */
	public void withdrawMoneyBatch() {
		String amount = JOptionPane.showInputDialog(null, "Please input amount to be withdrawed:", "Withdraw",
				JOptionPane.PLAIN_MESSAGE);
		if (null == amount || amount.equals("")) {
			return;
		}
		double moneyReduced = Double.parseDouble(amount);

		for (int i = 0; i < selectedUserIndex.length; i++) {
			BankSystem.setIndex(selectedUserIndex[i]);
			if (moneyReduced > BankSystem.getAccount().balance) {
				JOptionPane.showMessageDialog(null, "Account " + BankSystem.getAccount().number + " Not enough money!",
						"Message", JOptionPane.ERROR_MESSAGE);
				continue;
			}
			BankSystem.withdrawMoney(moneyReduced);
		}
		showAllAccounts();
	}
}
