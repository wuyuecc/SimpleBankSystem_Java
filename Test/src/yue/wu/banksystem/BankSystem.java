/**
* Copyright © yue.w.wu@oracle.com. All rights reserved.
* @Title: BankSystem.java
* @Package yue.wu.banksystem
* @version V1.0
* @author ywu
* @date Aug 31, 2017
*/
package yue.wu.banksystem;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * store account data and provide method to access data
 */
public class BankSystem {
	/**
	 * @Fields mAccount : account information stored in vector
	 */
	private static Vector<AccountInfo> mAccount;
	/**
	 * @Fields mHistoricalRecord : historical record stored in vector
	 */
	private static Vector<HistoricalRecord> mHistoricalRecord;
	/**
	 * @Fields mIndex : index that points to the chosen user
	 */
	private static int mIndex;
	/**
	 * @Fields adminFlag : flag to show identity of login person: user or
	 *         administrator
	 */
	private static boolean adminFlag;

	/**
	 * create a new instance BankSystem.
	 *
	 */
	public BankSystem() {
	}

	/**
	 * read account file or history file
	 * 
	 * @param filename
	 *            read out filename
	 */
	public static void readFile(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			if (filename.equals("account.txt")) {
				mAccount = new Vector<AccountInfo>();
				while ((line = reader.readLine()) != null) {
					String strs[] = line.split(" ");
					AccountInfo account = new AccountInfo();
					account.number = strs[0];
					account.password = strs[1];
					account.balance = Double.parseDouble(strs[2]);
					mAccount.add(account);
				}
			} else if (filename.equals("record.txt")) {
				mHistoricalRecord = new Vector<HistoricalRecord>();
				while ((line = reader.readLine()) != null) {
					String strs[] = line.split(" ");
					HistoricalRecord record = new HistoricalRecord();
					record.number = strs[0];
					record.time = strs[1] + " " + strs[2];
					record.operation = strs[3];
					record.amount = strs[4];
					mHistoricalRecord.add(record);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * write account file or history file
	 * 
	 * @param filename
	 *            write in filename
	 */
	public static void writeFile(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			StringBuffer buffer = new StringBuffer();

			if (filename.equals("account.txt")) {
				for (AccountInfo account : mAccount) {
					buffer.append(account.number + " " + account.password + " " + account.balance);
					buffer.append(System.getProperty("line.separator"));
				}
			} else if (filename.equals("record.txt")) {
				for (HistoricalRecord record : mHistoricalRecord) {
					buffer.append(record.number + " " + record.time + " " + record.operation + " " + record.amount);
					buffer.append(System.getProperty("line.separator"));
				}
			}

			writer.write(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * deposit money
	 * 
	 * @param moneyAdded
	 *            money to be deposited
	 */
	public static void depositMoney(double moneyAdded) {
		mAccount.get(mIndex).balance += moneyAdded;
		writeFile("account.txt");
		logRecord("Deposit", moneyAdded);
	}

	/**
	 * withdraw money
	 * 
	 * @param moneyReduced
	 *            money to be withdrawn
	 */
	public static void withdrawMoney(double moneyReduced) {
		mAccount.get(mIndex).balance -= moneyReduced;
		writeFile("account.txt");
		logRecord("Withdraw", moneyReduced);
	}

	/**
	 * change password
	 * 
	 * @param newPasswd
	 *            new password
	 */
	public static void changePassword(String newPasswd) {
		mAccount.get(mIndex).password = ShaEncrypt.encryptSha(newPasswd);
		writeFile("account.txt");
	}

	/**
	 * open account
	 * 
	 * @param account
	 *            information of new opened account
	 */
	public static void addAccount(AccountInfo account) {
		account.password = ShaEncrypt.encryptSha(account.password);
		mAccount.add(account);
		writeFile("account.txt");
	}

	/**
	 * log record
	 * 
	 * @param operation
	 *            operation type, deposit or withdraw
	 * @param amount
	 *            money operated
	 */
	public static void logRecord(String operation, double amount) {
		HistoricalRecord record = new HistoricalRecord();

		record.number = mAccount.get(mIndex).number;
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		record.time = sdf.format(d);
		record.operation = operation;
		record.amount = String.valueOf(amount);

		mHistoricalRecord.add(record);
		writeFile("record.txt");
	}

	/**
	 * log record
	 * 
	 * @param operation
	 *            operation type, deposit or withdraw
	 * @param amount
	 *            money operated
	 * @param number
	 *            card number, login name
	 */
	public static void logRecord(String operation, double amount, String number) {
		HistoricalRecord record = new HistoricalRecord();

		record.number = number;
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		record.time = sdf.format(d);
		record.operation = operation;
		record.amount = String.valueOf(amount);

		mHistoricalRecord.add(record);
		writeFile("record.txt");
	}

	/**
	 * query record
	 * 
	 * @return historical record
	 */
	public static Vector<HistoricalRecord> queryRecord() {
		Vector<HistoricalRecord> records = new Vector<HistoricalRecord>();
		String number = mAccount.get(mIndex).number;
		for (HistoricalRecord record : mHistoricalRecord) {
			if (record.number.equals(number)) {
				records.add(record);
			}
		}

		return records;
	}

	/**
	 * get quantity of accounts
	 * 
	 * @return quantity of accounts
	 */
	public static int getSize() {
		return mAccount.size();
	}

	/**
	 * get account info of chosen user
	 * 
	 * @return account info of chosen user
	 */
	public static AccountInfo getAccount() {
		AccountInfo account = mAccount.get(mIndex);
		return account;
	}

	/**
	 * get account info of user by input index
	 * 
	 * @param index
	 *            index of user that is desired
	 * @return account info of user with input index
	 */
	public static AccountInfo getAccount(int index) {
		AccountInfo account = mAccount.get(index);
		return account;
	}

	/**
	 * set index to the chosen user
	 * 
	 * @param index
	 *            index of chosen user
	 */
	public static void setIndex(int index) {
		mIndex = index;
	}

	/**
	 * get the flag that indicates the identity of login person
	 * 
	 * @return flag that indicates the identity of login person
	 */
	public static boolean getAdminFlag() {
		return adminFlag;
	}

	/**
	 * set the flag that indicates the identity of login person
	 * 
	 * @param flag
	 *            flag that indicates the identity of login person
	 */
	public static void setAdminFlag(boolean flag) {
		adminFlag = flag;
	}

}
