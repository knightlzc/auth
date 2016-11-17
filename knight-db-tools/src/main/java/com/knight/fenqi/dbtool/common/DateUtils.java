/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司   http://www.renren-inc.com</p> 
 * <p>Author:zicui.liu/刘子萃</p>
 * @Title: DateUtils.java
 * @Package com.renren.fenqi.carloan.util
 * @date 2015-2-2 下午8:41:52 
 */
package com.renren.fenqi.dbtool.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;

/**
 * <p>
 * Copyright: All Rights Reserved
 * </p>
 * <p>
 * Company: 人人公司 http://www.renren-inc.com
 * </p>
 * <p>
 * Description: 时间工具类
 * </p>
 * <p>
 * Author:zicui.liu/刘子萃
 * </p>
 */

public class DateUtils {

	/**
	 * <br/>
	 * Description:把时间置为一天的开头
	 * <p>
	 * Author:zicui.liu/刘子萃
	 * </p>
	 * 
	 * @param date
	 * @return
	 */
	public static Date getTheStartOfOneDay(String date) {
		String time = new DateTime(date).toString("yyyy-MM-dd");
		return DateTime.parse(time + " 00:00:00",
				DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
	}

	/**
	 * <br/>
	 * Description:把时间置为一天的结尾
	 * <p>
	 * Author:zicui.liu/刘子萃
	 * </p>
	 * 
	 * @param date
	 * @return
	 */
	public static Date getTheEndOfOneDay(String date) {
		String time = new DateTime(date).toString("yyyy-MM-dd");
		return DateTime.parse(time + " 23:59:59",
				DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
	}

	/**
	 * <br/>
	 * Description:获取当前年份
	 * <p>
	 * Author:zicui.liu/刘子萃
	 * </p>
	 * 
	 * @return
	 */
	public static int getCurYear() {
		return DateTime.now().getYear();
	}

	/**
	 * 获得一个月最大日期
	 * 
	 * @param date
	 * @return
	 * @author xiaoyang.gao
	 */
	public static Date getMaxDateOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE,
				calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND,
				calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND,
				calendar.getActualMaximum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	/**
	 * 取得一月中的最早一天。
	 * 
	 * @param date
	 * @return
	 * @author xiaoyang.gao
	 */
	public static Date getMinDateOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE,
				calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND,
				calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND,
				calendar.getActualMinimum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	/**
	 * 日期转换字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		String str = "";
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		str = formater.format(date);
		return str;
	}
	
	/**
	 * <br/>Description:
	 * <p>Author:zcliu/刘子萃</p>
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDateToString(Date date, String pattern) {
		if(null == date){
			return null;
		}
		try {
			DateTime dateTime = new DateTime(date);
			return dateTime.toString(pattern);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String dateToString(Date date) {
		try {
			
			DateTime dateTime = new DateTime(date);
			return dateTime.toString("yyyy年MM月dd日");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获得上一个月的日期
	 * 
	 * @return
	 */
	public static Date getLastMonthDate() {
		Date date = new Date();// 当前日期
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.MONTH, -1);// 月份减一
		return calendar.getTime();
	}
	/**
	 * 获得指定日期上一个月的日期
	 * 
	 * @return
	 */
	public static Date getLastMonthDate(Date date) {
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.MONTH, -1);// 月份减一
		return calendar.getTime();
	}
	/**
	 * 获得下一个月的日期
	 * 
	 * @return
	 */
	public static Date getNextMonthDate() {
		Date date = new Date();// 当前日期
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.MONTH, 1);// 月份加1
		return calendar.getTime();
	}
	
	/**
	 * 获得下一个月的日期
	 * 
	 * @return
	 */
	public static Date getTheNextMonthDate(Date time) {
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(time);// 设置当前日期
		calendar.add(Calendar.MONTH, 1);// 月份加1
		return calendar.getTime();
	}

	/**
	 * 判断date是否在两个日期之间
	 * 
	 * @param date
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean dateIsBetween(Date date, Date startDate, Date endDate) {
		if (date == null && startDate == null && endDate == null) {
			return false;
		}
		if (date.after(startDate) && date.before(endDate)) {
			return true;
		}
		return false;
	}

	/**
	 * 获得当前日期加几天
	 * 
	 * @param days
	 * @param currentDate
	 * @return
	 */
	public static Date getCurrentDatePlus(int days, Date currentDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}
	
	/**
	 * 获得当前日期加几年
	 * 
	 * @param days
	 * @param currentDate
	 * @return
	 */
	public static Date getCurrentDatePlusYear(int years, Date currentDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.YEAR, years);
		return c.getTime();
	}
	
	/**
	 * <br/>
	 * Description:计算两个日期的时间差
	 * <p>
	 * Author:zcliu/刘子萃
	 * </p>
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static int getDifOfTwoDate(Date before, Date after) {
		if (before.getTime() == after.getTime()) {
			return 0;
		}
		Period p = new Period(before.getTime(), after.getTime(),
				PeriodType.days());
		int days = p.getDays();
		return days + 1;
	}
	/**
	 * <br/>
	 * Description:计算两个日期的时间差
	 * <p>
	 * Author:zcliu/刘子萃
	 * </p>
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static int getDifMonthOfTwoDate(Date before, Date after) {
		if (before.getTime() == after.getTime()) {
			return 0;
		}
		Period p = new Period(before.getTime(), after.getTime(),
				PeriodType.months());
		int month = p.getMonths();
		return month ;
	}
	
	/**
	 * <br/>Description:计算两个日期的时间差（单位：年，忽略小数）
	 * <p>Author:zcliu/刘子萃</p>
	 * @param before
	 * @param after
	 * @return
	 */
	public static int getDifYearOfTwoDate(Date before, Date after) {
		if (before.getTime() == after.getTime()) {
			return 0;
		}
		Period p = new Period(before.getTime(), after.getTime(),
				PeriodType.years());
		int year = p.getYears();
		return year;
	}

	/**
	 * 获得当前日期减几天
	 * 
	 * @param days
	 * @param currentDate
	 * @return
	 */
	public static Date getCurrentDateSub(int days, Date currentDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DAY_OF_MONTH, -days);
		return c.getTime();
	}

	/**
	 * 获取2个时间相隔几天
	 */
	public static int getBetweenDayNumberIgnoreTime(Date startDate, Date endDate) {
		if (startDate == null || endDate == null)
			return -1;

		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			startDate = dateFormat.parse(dateFormat.format(startDate));
			endDate = dateFormat.parse(dateFormat.format(endDate));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		long dayNumber = -1L;
		long DAY = 24L * 60L * 60L * 1000L;

		try {
			// "2010-08-01 00:00:00 --- 2010-08-03 23:59:59"算两天
			dayNumber = (endDate.getTime() + 1000 - startDate.getTime()) / DAY;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) dayNumber;
	}
	
	/**
	 * <br/>Description:字符串转换为时间，异常则返回null
	 * <p>Author:zcliu/刘子萃</p>
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date getStrToDate(String dateStr,String format) {
		try {
			return DateTime.parse(dateStr,DateTimeFormat.forPattern(format)).toDate();
		} catch (Exception e) {
			return null;
		}
	}
	
    /**
     * 取得一个date对象对应的日期的0分0秒时刻的Date对象。
     */
    public static Date getMinDateOfHour(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }
	
    /**
     * 取得一个date对象对应的日期的0点0分0秒时刻的Date对象。
     */
    public static Date getMinDateOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getMinDateOfHour(date));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        return calendar.getTime();
    }
    
    /**
     * 取得一个date对象对应的日期的23点59分59秒时刻的Date对象。
     */
    public static Date getMaxDateOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));

        return calendar.getTime();
    }

    /**
     * <br/>Description:验证日期格式是否为“yyyy-MM-dd”
     * <p>Author:zcliu/刘子萃</p>
     * @param date
     * @return
     */
    public static boolean verifyYYYYMMDDDate(String date) {
        if (null==date || date.length() != "yyyy-MM-dd".length()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^((?:19|20)\\d\\d)-(0[0-9]|1[0-12])-(0[1-9]|[12][0-9]|3[01])$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

	public static void main(String[] args) throws ParseException {
		 System.out.println(getDateToString(new DateTime(2014, 12, 30, 18, 23,55).toDate(), "yyyy-M"));
		// System.out.println(getMinDateOfMonth(new Date()));
		// System.out.println(getLastMonthDate());
//		System.out.println(getDifOfTwoDate(new DateTime(2014, 12, 30, 18, 23,55).toDate(), new DateTime(2014, 12, 31, 18, 23,56).toDate()));
		// System.out
		// .println(getCurrentDatePlus(10, getMinDateOfMonth(new Date())));
//		System.out.println(getCurrentDatePlusYear(10, new Date()));
		System.out.println(verifyYYYYMMDDDate("2016-02-29"));
		System.out.println(getDifYearOfTwoDate(new DateTime(2011, 7, 27, 0, 0,0).toDate(), new Date()));
		
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date before = sdf.parse("2016-04-04 12:12:12");
		Date after = sdf.parse("2016-04-05 14:14:14");
		System.out.println(getDifDayOfTwoDate(before,after));*/
	}
	
	/**
	 * 
	 * @param before
	 * @param after
	 * @return xx天xx小时xx分     
	 */
	public static String getDifDayOfTwoDate(Date before, Date after){
		long between = after.getTime() - before.getTime();
		
		long day = between/(24 * 60 * 60 * 1000);
		long hour = between / (60 * 60 * 1000) - day * 24;
		long min = between / (60 * 1000) - day * 24 * 60 - hour * 60;
		//long sec = between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
		//long mesc = between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - sec * 1000;
		
		String result = day + "天" + hour + "小时" + min + "分";
		return result;
	}
}
