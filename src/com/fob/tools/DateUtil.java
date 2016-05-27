package com.fob.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

;

public class DateUtil
{
	public static String getDateStr(Date date)
	{
		String str = "MM/dd";
		SimpleDateFormat sf = new SimpleDateFormat(str);
		return sf.format(date);
	}
	public static String getDateYearStr(Date date)
	{
		String str = "yyyy/MM";
		SimpleDateFormat sf = new SimpleDateFormat(str);
		return sf.format(date);
	}
	public static String getDateTimeStr(Date date)
	{
		String str = "MM/dd HH:mm:ss";
		SimpleDateFormat sf = new SimpleDateFormat(str);
		return sf.format(date);
	}
	public static String getDateShortTimeStr(Date date)
	{
		String str = "MM/dd HH:mm";
		SimpleDateFormat sf = new SimpleDateFormat(str);
		return sf.format(date);
	}

	public static String dateTimeToStr(Date date,String format)
	{
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}

	public static Date getDateTime(String sDate)
	{
		String str = "MM/dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(str);
		try
		{
			return format.parse(sDate);
		}
		catch (ParseException e)
		{
			return getDateTime("2000-01-01 00:00:00");
		}
	}

	public static Date getDate(String sDate)
	{
		String str = "MM/dd";
		SimpleDateFormat format = new SimpleDateFormat(str);
		try
		{
			return format.parse(sDate);
		}
		catch (ParseException e)
		{
			return getDate("2000-01-01");
		}
	}

	public static Date getDateYear(String sDate)
	{
		String str = "yyyy/MM";
		SimpleDateFormat format = new SimpleDateFormat(str);
		try
		{
			return format.parse(sDate);
		}
		catch (ParseException e)
		{
			return getDate("2000-01-01");
		}
	}


}
