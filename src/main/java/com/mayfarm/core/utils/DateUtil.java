package com.mayfarm.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	//날짜 계산기
    public static String calendarCalculator(Calendar calendar, int year, int month, int day, String format) {    	
    	String date = "";
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    	if(year != 0) {calendar.add(Calendar.YEAR, year);}
    	if(month != 0) {calendar.add(Calendar.MONTH, month);}
    	if(day != 0) {calendar.add(Calendar.DAY_OF_MONTH, day);}
    	date = simpleDateFormat.format(calendar.getTime());
    	return date;
    }
	
    
    //현재 시간
    public static String getNow(String format) {
    	String today = "";
    	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat(format);    	
    	today = sdf.format(date);    	
    	return today;
    }
}
