/**
* Copyright © yue.w.wu@oracle.com. All rights reserved.
* @Title: HistoricalRecord.java
* @Package yue.wu.banksystem
* @version V1.0
* @author ywu
* @date Aug 31, 2017
*/
package yue.wu.banksystem;

/**
 * data structure of historical record
 */
public class HistoricalRecord {
	/**
	 * @Fields number : card number, login name
	 */
	String number;
	/**
	 * @Fields time : record operation time
	 */
	String time;
	/**
	 * @Fields operation : record operation type(deposit or withdraw)
	 */
	String operation;
	/**
	 * @Fields amount : record amount deposited or withdrawn
	 */
	String amount;
}
