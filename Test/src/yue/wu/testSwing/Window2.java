package yue.wu.testSwing;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.*;

public class Window2 {

	private JFrame frmBankSystem;

	private static JMenu mnStart;
	private static JMenu mnUser;
	private static JMenu mnAdmin;

	private String[] columnTitle1 = { "CARD NUMBER", "PASSWORD", "BALANCE" };
	private static DefaultTableModel tableModel1;
	private JTable table1;
	private JScrollPane sp1;

	private String[] columnTitle2 = { "CARD NUMBER", "DATE AND TIME", "OPERATION", "AMOUNT" };
	private static DefaultTableModel tableModel2;
	private JTable table2;
	private static JScrollPane sp2;

	private static JLabel lblHisRecord;

	private JPopupMenu m_popupMenu;

	/**
	 * Launch the application.
	 */
	public static void notmain(String args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window2 window = new Window2();

					if (args.equals("user")) {
						mnAdmin.setEnabled(false);
						// tableModel1.setRowCount(1);
						// tableModel2.setRowCount(5);
						lblHisRecord.setVisible(true);
						sp2.setVisible(true);
					} else if (args.equals("admin")) {
						// mnUser.setEnabled(false);
						// tableModel1.setRowCount(5);
						// lblHisRecord.setVisible(false);
						// sp2.setVisible(false);
						// tableModel2.setRowCount(5);
						lblHisRecord.setVisible(true);
						sp2.setVisible(true);
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
	public Window2() {
		initialize();

		JLabel lblWelcome = new JLabel("<html><body>Welcome to Simple Bank System<br><br></body></html>",
				JLabel.CENTER);
		lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
		frmBankSystem.getContentPane().add(lblWelcome);

		JLabel lblAccInfo = new JLabel("<html><body>Account Information</body></html>", JLabel.CENTER);
		lblAccInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		frmBankSystem.getContentPane().add(lblAccInfo);

		showTable();

		lblHisRecord = new JLabel("<html><body>Historical Records</body></html>", JLabel.CENTER);
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
		menuBar.add(mnStart);

		JMenuItem mntmWelcome = new JMenuItem("Welcome");
		mntmWelcome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowWelcome.notmain(null);
			}
		});
		mnStart.add(mntmWelcome);

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		mnStart.add(mntmLogout);

		mnUser = new JMenu("User");
		menuBar.add(mnUser);

		JMenuItem mntmQuery = new JMenuItem("Query Balance");
		mntmQuery.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
		mntmQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryBalance();
			}
		});
		mnUser.add(mntmQuery);

		JMenuItem mntmDeposit = new JMenuItem("Deposit Money");
		mntmDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				depositMoney();
			}
		});
		mnUser.add(mntmDeposit);

		JMenuItem mntmDraw = new JMenuItem("Withraw Money");
		mntmDraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				withdrawMoney();
			}
		});
		mnUser.add(mntmDraw);

		JMenuItem mntmChange = new JMenuItem("Change Password");
		mntmChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePassword();
			}
		});
		mnUser.add(mntmChange);

		JMenuItem mntmQueryRecord = new JMenuItem("Query Record");
		mntmQueryRecord.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmQueryRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryRecord();
			}
		});
		mnUser.add(mntmQueryRecord);

		mnAdmin = new JMenu("Admin");
		menuBar.add(mnAdmin);

		JMenuItem mntmAdd = new JMenuItem("Add Account");
		mntmAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAccount();
			}
		});
		mnAdmin.add(mntmAdd);

		JMenuItem mntmShow = new JMenuItem("Show All Accounts");
		mntmShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAllAccounts();
			}
		});
		mnAdmin.add(mntmShow);
	}

	private void setIndex() {
		String number = String.valueOf(table1.getValueAt(table1.getSelectedRow(), 0));
		for (int index = 0; index < BankSystem2.getSize(); index++) {
			if (BankSystem2.getAccount(index).number.equals(number)) {
				BankSystem2.setIndex(index);
				break;
			}
		}
	}

	private void createPopupMenu() {
		m_popupMenu = new JPopupMenu();

		JMenuItem mntmAdminDeposit = new JMenuItem();
		mntmAdminDeposit.setText("Deposit");
		mntmAdminDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				depositMoney();
			}
		});
		m_popupMenu.add(mntmAdminDeposit);

		JMenuItem mntmAdminWithdraw = new JMenuItem();
		mntmAdminWithdraw.setText("Withdraw");
		mntmAdminWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				withdrawMoney();
			}
		});
		m_popupMenu.add(mntmAdminWithdraw);

		JMenuItem mntmAdminChangePassword = new JMenuItem();
		mntmAdminChangePassword.setText("ChangePassword");
		mntmAdminChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				changePassword();
			}
		});
		m_popupMenu.add(mntmAdminChangePassword);
	}

	private void mouseButtonClick(MouseEvent evt) {
		if (evt.getButton() == MouseEvent.BUTTON1) {
			if (-1 == table1.rowAtPoint(evt.getPoint())) {
				return;
			}
			setIndex();
		} else if (evt.getButton() == MouseEvent.BUTTON3) {
			if (-1 == table1.rowAtPoint(evt.getPoint())) {
				return;
			}
			// setIndex();
			m_popupMenu.show(table1, evt.getX(), evt.getY());
		}
	}

	public void showTable() {
		tableModel1 = new DefaultTableModel(columnTitle1, 0);
		table1 = new JTable(tableModel1);
		sp1 = new JScrollPane(table1);
		frmBankSystem.getContentPane().add(sp1);

		createPopupMenu();

		table1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				mouseButtonClick(evt);
			}
		});
	}

	public void showTable2() {
		tableModel2 = new DefaultTableModel(columnTitle2, 0);
		table2 = new JTable(tableModel2);
		// set to non-editable
		table2.setEnabled(false);
		sp2 = new JScrollPane(table2);
		frmBankSystem.getContentPane().add(sp2);
		// frmBankSystem.pack();
	}

	public void logout() {
		frmBankSystem.dispose();
		Window1.main(null);
	}

	public void queryBalance() {
		tableModel1 = new DefaultTableModel(columnTitle1, 1);
		table1.setModel(tableModel1);

		AccountInfo account = BankSystem2.getAccount();

		table1.setValueAt(account.number, 0, 0);
		table1.setValueAt(account.password, 0, 1);
		table1.setValueAt(account.balance, 0, 2);
	}

	public void depositMoney() {
		String amount = JOptionPane.showInputDialog(null, "Please input amount to be deposited:", "Deposit",
				JOptionPane.PLAIN_MESSAGE);
		if (null == amount || amount.equals("")) {
			return;
		}

		double moneyAdded = Double.parseDouble(amount);
		BankSystem2.depositMoney(moneyAdded);

		// refresh displayed data
		if (false == BankSystem2.getAdminFlag()) {
			queryBalance();
		} else if (true == BankSystem2.getAdminFlag()) {
			showAllAccounts();
		}
		queryRecord();
	}

	public void withdrawMoney() {
		String amount = JOptionPane.showInputDialog(null, "Please input amount to be withdrawed:", "Withdraw",
				JOptionPane.PLAIN_MESSAGE);
		if (null == amount || amount.equals("")) {
			return;
		}

		double moneyReduced = Double.parseDouble(amount);

		if (moneyReduced > BankSystem2.getAccount().balance) {
			JOptionPane.showMessageDialog(null, "Not enough money!", "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}

		BankSystem2.withdrawMoney(moneyReduced);

		// refresh displayed data
		if (false == BankSystem2.getAdminFlag()) {
			queryBalance();
		} else if (true == BankSystem2.getAdminFlag()) {
			showAllAccounts();
		}
		queryRecord();
	}

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

		BankSystem2.changePassword(newPasswdOnce);

		// refresh displayed data
		if (false == BankSystem2.getAdminFlag()) {
			queryBalance();
		} else if (true == BankSystem2.getAdminFlag()) {
			showAllAccounts();
		}
	}

	public void queryRecord() {
		Vector<HistoricalRecord> records = BankSystem2.queryRecord();
		int index = 0;
		int size = records.size();

		// empty table before display
		tableModel2.setRowCount(0);
		tableModel2.setRowCount(size);

		while (index < size) {
			// new records ahead of old ones
			HistoricalRecord record = records.get(size - 1 - index);

			table2.setValueAt(record.number, index, 0);
			table2.setValueAt(record.time, index, 1);
			table2.setValueAt(record.operation, index, 2);
			table2.setValueAt(record.amount, index, 3);
			index++;
		}
	}

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

		BankSystem2.addAccount(account);
		BankSystem2.logRecord("OpenAccount", account.balance, account.number);

		// refresh displayed data
		showAllAccounts();
	}

	public void showAllAccounts() {

		int index = 0;
		int size = BankSystem2.getSize();

		tableModel1 = new DefaultTableModel(columnTitle1, size);
		table1.setModel(tableModel1);

		while (index < size) {
			AccountInfo account = BankSystem2.getAccount(index);
			table1.setValueAt(account.number, index, 0);
			table1.setValueAt(account.password, index, 1);
			table1.setValueAt(account.balance, index, 2);
			index++;
		}
	}

}
