package com.hugboga.custom.utils;

import android.text.TextUtils;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Zongfi
 * @datetime 2014-1-6 下午3:15:01
 * @email zzf_soft@163.com
 */
public class DateUtils {

    public static SimpleDateFormat dateWeekFormat = new SimpleDateFormat("yyyy-MM-dd 周E HH:mm");
    public static SimpleDateFormat dateWeekFormat2 = new SimpleDateFormat("yyyy-MM-dd 周E");
    public static SimpleDateFormat dateWeekFormatOnly = new SimpleDateFormat("yyyy年MM月dd日 EE");
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateTimeFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat dateTimeFormat3 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    public static SimpleDateFormat dateDayFormat = new SimpleDateFormat("MM-dd HH:mm");
    public static SimpleDateFormat dateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateSecondDateFormat = new SimpleDateFormat("yyMMddHHmmss");
    public static SimpleDateFormat datePonintDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    public static SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    public static SimpleDateFormat datePonintDateTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public static DecimalFormat decimalFormat = new DecimalFormat("00");

    public static String getNowDatetime() {
        return dateTimeFormat.format(Calendar.getInstance().getTime());
    }

    public static Date getDateTimeFromStr(String datetime) throws ParseException {
        return dateTimeFormat.parse(datetime);
    }

    public static String formatSecond(Long time) {
        long minute = time / (60 * 1000);
        long second = (time - minute * 60 * 1000) / 1000;
        return decimalFormat.format(minute) + ":" + decimalFormat.format(second);
    }

    /**
     * 将秒数转换成时分秒
     * Created by ZHZEPHI at 2015年4月6日 下午8:39:04
     *
     * @param t
     * @return
     */
    public static String secToTime(long t) {
        int h = (int) t / 3600;
        int m = (int) (t % 3600) / 60;
//		int s=(int)t%60;
        StringBuilder sb = new StringBuilder();
        if (h > 0) {
            sb.append(h + "时");
        }
        if (m > 0) {
            sb.append(m + "分");
        }
//		if(s>0){
//			sb.append(s+"秒");
//		}
        if (sb.length() == 0) {
            sb.append("1分");
        }
        return sb.toString();
    }

    /**
     * 根据年月日格式化显示时间
     *
     * @param year
     * @param month
     * @param day
     * @return
     * @author Zongfi
     * @datetime 2014-2-18 下午3:38:29
     * @email zzf_soft@163.com
     */
    public static String formatTheDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return dateDateFormat.format(calendar.getTime());
    }

    /**
     * 根据定时时间计算休眠时间
     *
     * @param toTime
     * @return
     * @author Zongfi
     * @datetime 2014-3-10 下午2:23:05
     * @email zzf_soft@163.com
     */
    public static String getSleepDate(Integer toTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, toTime);
        return timeFormat.format(calendar.getTime());
    }

    /**
     * 根据时间字符串获取时间年月日格式
     * Created by ZHZEPHI at 2015年2月3日 下午1:49:20
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDateFromStr(String date) throws ParseException {
        Date date2 = dateDateFormat.parse(date);
        return dateDateFormat.format(date2);
    }

    /**
     * yyyy-MM-dd转yyyy年MM月dd日
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDateFromSimpleStr(String date) throws ParseException {
        Date date2 = dateDateFormat.parse(date);
        return dateSimpleDateFormat.format(date2);
    }

    /**
     * 格式化年月日，时分格式的时间
     * yyyy-MM-dd HH:mm
     * Created by ZHZEPHI at 2015年2月3日 下午8:15:41
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDate2FromStr(String date) throws ParseException {
        Date date2 = dateTimeFormat.parse(date);
        return dateTimeFormat2.format(date2);
    }

    /**
     * 显示当地时间
     * Created by ZHZEPHI at 2015年4月30日 上午10:53:09
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDateLocalFromStr(String date) throws ParseException {
        if (TextUtils.isEmpty(date)) {
            return date;
        }
        SimpleDateFormat localFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        localFromat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Long timeLong = Long.valueOf(date);
        Calendar oldCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        oldCalendar.setTimeInMillis(timeLong);
        localFromat.setTimeZone(TimeZone.getDefault());
        return localFromat.format(oldCalendar.getTime());
    }

    /**
     * 指定格式化时间方式
     *
     * @param formatStr
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDateLocalFromStr(String formatStr, String date) throws ParseException {
        if (TextUtils.isEmpty(date)) {
            return date;
        }
        SimpleDateFormat newFromat = new SimpleDateFormat(formatStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(date));
        return newFromat.format(calendar.getTime());
    }

    /**
     * 私信时间格式化
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String resetLetterTime(String date) throws ParseException {
        String localTime = getDateLocalFromStr(date); //服务器时间格式化成本地时间
        SimpleDateFormat localFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(TextUtils.isEmpty(localTime)){
            return date;
        }
        Date oldDate = localFromat.parse(localTime);
        Calendar oldCalendar = Calendar.getInstance();
        oldCalendar.setTime(oldDate); //私信时间
        //获取本地时间
        Calendar nowCalendar = Calendar.getInstance();
        if (oldCalendar.get(Calendar.YEAR) != nowCalendar.get(Calendar.YEAR)) {
            return getDateLocalFromStr("yyyy-MM-dd", date);
        } else if (oldCalendar.get(Calendar.DAY_OF_MONTH) != nowCalendar.get(Calendar.DAY_OF_MONTH)) {
            return getDateLocalFromStr("MM-dd", date);
        }
        return getDateLocalFromStr("HH:mm", date);
    }

    /**
     * 格式化年月日，时分格式的时间
     * yyyy年MM月dd日 HH:mm
     * Created by ZHZEPHI at 2015年4月16日 下午3:00:36
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDate3FromStr(String date) throws ParseException {
        Date date2 = dateTimeFormat.parse(date);
        return dateTimeFormat3.format(date2);
    }

    /**
     * 根据指定时间计算距离现在时间的天数
     * yyyy-MM-dd
     * Created by ZHZEPHI at 2015年1月21日 上午10:08:18
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getFromToInteger(String date) throws ParseException {
        Date date2 = dateDateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        long between = (date2.getTime() - calendar.getTime().getTime()) / 1000; //多少秒
        if (between > 0) {
            long day1 = between / (24 * 3600);
            long hour1 = between % (24 * 3600) / 3600;
            long minute1 = between % 3600 / 60;
//			long second1=between%60/60;
            if (day1 > 0) {
                return day1 + "天";
            }
            if (hour1 > 0) {
                return hour1 + "小时";
            }
            if (minute1 > 0) {
                return minute1 + "分钟";
            }
            return "0";
        }
        return "0";
    }

    /**
     * 根据指定时间计算距离现在时间的天数
     * yyyy-MM-dd HH:mm:ss
     * Created by ZHZEPHI at 2015年4月17日 上午10:49:57
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getFromToInteger1(String date) throws ParseException {
        Date date2 = dateTimeFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        long between = (date2.getTime() - calendar.getTime().getTime()) / 1000; //多少秒
        if (between > 0) {
            long day1 = between / (24 * 3600);
            long hour1 = between % (24 * 3600) / 3600;
            long minute1 = between % 3600 / 60;
//			long second1=between%60/60;
            if (day1 > 0) {
                return day1 + "天";
            }
            if (hour1 > 0) {
                return hour1 + "小时";
            }
            if (minute1 > 0) {
                return minute1 + "分钟";
            }
            return "0";
        }
        return "0";
    }

    /**
     * 指定时间差小时数
     * Created by ZHZEPHI at 2015年1月21日 下午4:08:36
     *
     * @param fromString
     * @param toString
     * @return
     * @throws ParseException
     */
    public static long getFromToHour(String fromString, String toString) throws ParseException {
        Date date1 = dateDateFormat.parse(fromString);
        Date date2 = dateDateFormat.parse(toString);
        long between = (date2.getTime() - date1.getTime()) / (1000 * 3600); //多少小时
        return between;
    }

    /**
     * 指定时间差秒数
     * Created by ZHZEPHI at 2015年4月28日 下午5:17:16
     *
     * @param fromString
     * @param toString
     * @return
     * @throws ParseException
     */
    public static long getFromToSecond(String fromString, String toString) throws ParseException {
        Date date1 = dateSecondDateFormat.parse(fromString);
        Date date2 = dateSecondDateFormat.parse(toString);
        long between = (date2.getTime() - date1.getTime()) / 1000; //多少秒
        return between;
    }

    /**
     * 格式化成有周的时间格式
     * Created by ZHZEPHI at 2015年1月29日 下午6:29:17
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static String getWeekStrFromDate(String dateStr) throws ParseException {
        Date date = dateTimeFormat.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateFormatSymbols symbols = dateWeekFormat.getDateFormatSymbols();
        symbols.setShortWeekdays(new String[]{"", "日", "一", "二", "三", "四", "五", "六"});
        dateWeekFormat.setDateFormatSymbols(symbols);
        return dateWeekFormat.format(calendar.getTime());
    }

    /**
     * 格式化成有周的时间格式
     * Created by ZHZEPHI at 2015年1月29日 下午6:29:17
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static String getWeekStrByDate(String dateStr, SimpleDateFormat dateFormat, SimpleDateFormat dateweekFormat) throws ParseException {
        Date date = dateFormat.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateFormatSymbols symbols = dateweekFormat.getDateFormatSymbols();
        symbols.setShortWeekdays(new String[]{"", "日", "一", "二", "三", "四", "五", "六"});
        dateweekFormat.setDateFormatSymbols(symbols);
        return dateweekFormat.format(calendar.getTime());
    }

    /**
     * 格式化有短日期的时间格式
     * Created by ZHZEPHI at 2015年1月30日 下午4:05:02
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static String getDayStrFromDate(String dateStr) throws ParseException {
        Date date = dateTimeFormat.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return dateDayFormat.format(calendar.getTime());
    }

    /**
     * 格式化成点间隔的时间格式
     * Created by ZHZEPHI at 2015年2月6日 下午7:28:41
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static String getPointStrFromDate(String dateStr) throws ParseException {
        Date date = dateTimeFormat.parse(dateStr);
        return datePonintDateFormat.format(date.getTime());
    }

    /**
     * 格式化时间
     * （yyyy-MM-dd）-> (yyyy.MM.dd)
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static String getPointStrFromDate1(String dateStr) throws ParseException {
        Date date = dateDateFormat.parse(dateStr);
        return datePonintDateFormat.format(date.getTime());
    }

    /**
     * 格式化成点间隔的年月日时间格式
     * Created by ZHZEPHI at 2015年4月2日 下午4:26:15
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static String getPointTimeFromDate(String dateStr) throws ParseException {
        Date date = dateTimeFormat.parse(dateStr);
        return datePonintDateTimeFormat.format(date.getTime());
    }

    /**
     * 换算时间段日期
     * Created by ZHZEPHI at 2015年3月9日 上午10:43:45
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static String parseSimpleDateForStr(String str) throws ParseException {
        StringBuilder result = new StringBuilder();
        String[] dateStrings = str.split(",");
        for (String date : dateStrings) {
            if (result.length() > 0) {
                result.append("\r\n");
            }
            String[] date1 = date.split(":");
            if (date1.length > 1) {
                StringBuilder sb = new StringBuilder();
                //开始日期
                Calendar cal1 = Calendar.getInstance();
                Date dateStar = dateDateFormat.parse(date1[0]);
                cal1.setTime(dateStar);
                //结束日期
                Calendar cal2 = Calendar.getInstance();
                Date dateEnd = dateDateFormat.parse(date1[1]);
                cal2.setTime(dateEnd);
                //组合时间段
                sb.append(new SimpleDateFormat("yyyy年M月d日").format(cal1.getTime()));
                sb.append("——");
                if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                    //年相等，判断月
                    if (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
                        //月相等，判断日
                        if (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) {

                        } else {
                            sb.append(new SimpleDateFormat("d日").format(cal2.getTime()));
                        }
                    } else {
                        sb.append(new SimpleDateFormat("M月d日").format(cal2.getTime()));
                    }
                } else {
                    sb.append(new SimpleDateFormat("yyyy年M月d日").format(cal2.getTime()));
                }
                result.append(sb.toString());
            } else {
                result.append(new SimpleDateFormat("yyyy年M月d日").format(dateDateFormat.parse(date1[0])));
            }
        }
        return result.toString();
    }

    /**
     * 选择日期到现在的天数
     * Created by ZHZEPHI at 2015年3月12日 下午2:20:27
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static long getLongTime(String dateStr) throws ParseException {
        Date date = dateDateFormat.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        long days = (calendar.getTime().getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
        return days;
    }

    /**
     * 选择日期到现在的天数
     * Created by ZHZEPHI at 2015年3月12日 下午2:20:27
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date getDateByStr(String dateStr) throws ParseException {
        Date date = getDateByStr(dateStr, dateDateFormat);
        return date;
    }

    /**
     * 选择日期到现在的天数
     * Created by ZHZEPHI at 2015年3月12日 下午2:20:27
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date getDateByStr(String dateStr, SimpleDateFormat dateDateFormat) throws ParseException {
        Date date = dateDateFormat.parse(dateStr);
        return date;
    }

    public static int getDiffByDate(Date start, Date end) {
        long times = end.getTime() - start.getTime();
        int day = (int) (times / 1000 / 60 / 60 / 24);
        return day;
    }

    public static Date addDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
}
