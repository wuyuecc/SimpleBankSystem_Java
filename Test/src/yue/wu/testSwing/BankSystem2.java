package yue.wu.testSwing;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

class BankSystem2 {

	private static Vector<AccountInfo> mAccount;
	private static Vector<HistoricalRecord> mHistoricalRecord;
	private static int mIndex;
	private static boolean adminFlag;
	
	public BankSystem2() {
	}

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

	public static void depositMoney(double moneyAdded) {
		mAccount.get(mIndex).balance += moneyAdded;
		writeFile("account.txt");
		logRecord("Deposit", moneyAdded);
	}

	public static void withdrawMoney(double moneyReduced) {
		mAccount.get(mIndex).balance -= moneyReduced;
		writeFile("account.txt");
		logRecord("Withdraw", moneyReduced);
	}

	public static void changePassword(String newPasswd) {
		mAccount.get(mIndex).password = SHAencrypt.encryptSHA(newPasswd);
		writeFile("account.txt");
	}

	public static void addAccount(AccountInfo account) {
		account.password = SHAencrypt.encryptSHA(account.password);
		mAccount.add(account);
		writeFile("account.txt");
	}

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

	// overload
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

	public static int getSize() {
		return mAccount.size();
	}

	public static AccountInfo getAccount() {
		AccountInfo account = mAccount.get(mIndex);
		return account;
	}

	public static AccountInfo getAccount(int index) {
		AccountInfo account = mAccount.get(index);
		return account;
	}

	public static void setIndex(int index) {
		mIndex = index;
	}

	public static boolean getAdminFlag() {
		return adminFlag;
	}
	
	public static void setAdminFlag(boolean flag) {
		adminFlag = flag;
	}
	
}
