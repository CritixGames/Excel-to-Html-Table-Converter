//-----------------------------------------------------
//Assignment 3
//Written by: Cristian Gasparesc 40209205 Amirhossein Tavakkoly 40203604
//Due Date: Due 11:59 PM – Friday, March 25, 2022
//-----------------------------------------------------
/**
* @author Cristian Gasparesc, Amirhossein Tavakkoly
* @version 16/03/22
*/
public class CSVDataMissing extends Exception{
	
	/**
	 * Constructor for Exception
	 */
	public CSVDataMissing() {
		super("Error: Input row cannot be parsed due to missing information");
	}
	/**
	 * Constructor for Exception taking a String Parameter
	 * @param s error message
	 */
	public CSVDataMissing(String s) {
		super(s);
	}
	/**
	 * get message method
	 * @return error message
	 */
	public String getmsg() {
		return super.getMessage();
	}
}
