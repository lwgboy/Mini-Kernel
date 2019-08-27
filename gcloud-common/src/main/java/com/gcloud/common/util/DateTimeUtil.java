package com.gcloud.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateTimeUtil {
	public static Date getBegin(Date original)
	{
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String today=sdf1.format(original);
	    String tDate=today.split(" ")[0];
	    try {
			Date d1=sdf1.parse(tDate+" "+"00:00:00");
			return d1;
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return original;
	}
	public static Date getEnd(Date original)
	{
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String today=sdf1.format(original);
	    String tDate=today.split(" ")[0];
	    try {
			Date d1=sdf1.parse(tDate+" "+"23:59:59");
			return d1;
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return original;
	}
	
	/** 
	* @Title daysBetween 
	* @Description 两个时间之间相隔天数
	* @param @param smdate
	* @param @param bdate
	* @param @return
	* @param @throws ParseException 
	* @return int    返回类型 
	* @throws 
	*/
	public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days = Math.abs(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }
	
	/** 
	* @Title getDateAfterDays 
	* @Description 获取N天之后的日期
	* @param @param days
	* @param @return 
	* @return Date    返回类型 
	* @throws 
	*/
	public static Date getDateAfterDays(Integer days)
	{
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, days);
		long date = cal.getTimeInMillis();
		return new Date(date);
	}
	
	public static long differ(String begin,String end,String format){
		if(StringUtils.isBlank(begin)||StringUtils.isBlank(end))
			return 0l;
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		try {
			Date date_begin= sdf.parse(begin);
			Date date_end= sdf.parse(end);
			return date_end.getTime()-date_begin.getTime();
		} catch (ParseException e) {
			
			e.printStackTrace();
			return 0l;
		}
	}
	public static long differ(String begin,String end){
		return differ(begin, end, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String httpDate(TimeZone serverTimeZone) {
        //final String DateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        final String DateFormat = "yyyy-MM-dd'T'HH:mm:00'Z'";
        SimpleDateFormat format = new SimpleDateFormat( DateFormat, Locale.US );
        format.setTimeZone(serverTimeZone);
        return format.format( new Date() );
	}
	
	public static Date addHourOfDate(Date date,int i){
		Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.HOUR_OF_DAY, i);
	    Date newDate = c.getTime();
	    return newDate;
	}
}
