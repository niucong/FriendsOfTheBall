package com.fob.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharUtil {

	/**
	 * 手机号是否合法
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isValidPhone(String paramString) {
		// L.i(TAG, "isValidPhone phone=" + paramString);
		if (paramString == null || paramString.equals(""))
			return false;
		Pattern p = Pattern.compile("^((1))\\d{10}$");
		Matcher m = p.matcher(paramString);
		return m.matches();
	}

	/**
	 * 验证邮箱合法性
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isValidEmail(String paramString) {
		// L.i(TAG, "isValidEmail Email=" + paramString);
		if (paramString == null || paramString.equals(""))
			return false;
		return paramString.matches("\\w+[\\w]*@[\\w]+\\.[\\w]+$")
				|| paramString.matches("\\w+[\\w]*@[\\w]+\\.[\\w]+\\.[\\w]+$");
		// return paramString
		// .matches("[a-zA-Z0-9._-]*@([a-zA-Z0-9-_]+\\.)+(com|gov|net|org|com\\.cn|edu\\.cn)$");
	}

	/**
	 * 验证密码是否合法
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isValidPassword(String paramString) {
		if (paramString == null || paramString.equals(""))
			return false;
		return paramString.matches("[0-9A-Za-z]{4,16}+$");
	}

	/**
	 * 验证昵称是否合法
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isValidName(String paramString) {
		if (paramString == null || paramString.equals(""))
			return false;
		String regEx = "[`~!@#$%^&*()-_+=|{}':;',\"\\[\\].<>/?·！@#￥%……&*（）——【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(paramString);
		return !m.find();
	}

	/**
	 * 返回字符长度
	 * 
	 * @param val
	 * @return
	 */
	public static int getByteLen(String val) {
		int len = 0;
		String[] valsArr = val.split("");
		String info = "[\u4E00-\u9FA5]";
		for (int i = 0; i < valsArr.length; i++) {
			if (valsArr[i].matches(info)) // 全角
				len += 2;
			else
				len += 1;
		}
		return len;
	}

}
