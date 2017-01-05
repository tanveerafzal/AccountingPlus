package com.accounting.action;

import javax.mail.internet.ParseException;

public class TEST {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		String date = "2008-12-12";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		java.sql.Date indate = new java.sql.Date(formatter.parse(date).getTime());
		System.out.println(indate);
	}

}
