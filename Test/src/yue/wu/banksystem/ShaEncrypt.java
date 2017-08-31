/**
* @author anonym
*/
package yue.wu.banksystem;

import java.security.MessageDigest;

/**
 * encrypt function
 */
public class ShaEncrypt {

	private final static String KEY_SHA = "SHA";
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
			"E", "F" };

	public ShaEncrypt() {
	}

	public static String encryptSha(String data) {
		if (data == null || data.equals("")) {
			return "";
		}
		try {
			MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
			sha.update(data.getBytes());
			byte[] bytes = sha.digest();
			return byteArrayToHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String byteToHexString(byte b) {
		int ret = b;
		if (ret < 0) {
			ret += 256;
		}
		int m = ret / 16;
		int n = ret % 16;
		return hexDigits[m] + hexDigits[n];
	}

	private static String byteArrayToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(byteToHexString(bytes[i]));
		}
		return sb.toString();
	}

}
