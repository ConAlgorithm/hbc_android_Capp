package com.hugboga.custom.widget.calendar;

import com.hugboga.custom.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtils {

    private static final String[] WEEK_DATA = new String[]{"日", "一", "二", "三", "四", "五", "六"};

    /**
     * 某月的天数
     */
    private static int daysOfMonth = 0;

    /**
     * 具体某一天是星期几
     */
    private static int dayOfWeek = 0;

    /**
     * 判断是否为闰年
     *
     * @param year 指定的年份
     * @return
     */
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取指定月份的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        return getDaysOfMonth(isLeapYear(year), month);
    }

    /**
     * 得到某月有多少天数
     *
     * @param isLeapyear 目标年份
     * @param month      目标月份
     * @return
     */
    public static int getDaysOfMonth(boolean isLeapyear, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }

        }
        return daysOfMonth;
    }

    /**
     * 指定某年中的某月的第一天是星期几
     *
     * @param year  目标年份
     * @param month 目标月份
     * @return
     */
    public static int getWeekdayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }

    /**
     * 获取当前年份与月份
     *
     * @return 返回日期数组，整形array[0]，为年份，array[1]为月份, array[2]为日期
     */
    public static int[] getCurrentYearAndMonth() {
        int[] result = new int[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String str = "";
        Date date = new Date();

        str = sdf.format(date); // 当期日期
        result[0] = Integer.parseInt(str.split("-")[0]);
        result[1] = Integer.parseInt(str.split("-")[1]);
        result[2] = Integer.parseInt(str.split("-")[2]);

        return result;
    }

    /**
     * 构建当月日历cell
     *
     * @param thisCalendar
     * @return
     */
    public static List<CalendarCell> getCalendarData(Calendar thisCalendar) {
        List<CalendarCell> monthDatas = new ArrayList<>();

        Calendar cellCalendar = Calendar.getInstance();
        cellCalendar.set(thisCalendar.get(Calendar.YEAR), thisCalendar.get(Calendar.MONTH), 1);

        //本月第一天是周几
        int firstDayOfMonth = CalendarUtils.getWeekdayOfMonth(cellCalendar.get(Calendar.YEAR), cellCalendar.get(Calendar.MONTH) + 1);
        //本月总天数
        int countDaysOfMonth = CalendarUtils.getDaysOfMonth(cellCalendar.get(Calendar.YEAR), cellCalendar.get(Calendar.MONTH) + 1);

        // 前7个格子为周一到周日文字说明
        for (int i = 0; i < WEEK_DATA.length; i++) {
            CalendarCell cell = new CalendarCell();
            cell.setType(0);
            cell.setName(WEEK_DATA[i]);
            cell.setTxtSize(R.dimen.text_size_smallx);
            monthDatas.add(cell);
        }
        //空格子
        for (int i = 0; i < firstDayOfMonth; i++) {
            CalendarCell cell = new CalendarCell();
            cell.setType(1);
            cell.setName("");
            monthDatas.add(cell);
        }
        //本月日历
        for (int i = 0; i < countDaysOfMonth; i++) {
            CalendarCell cell = new CalendarCell();
            cell.setType(2);
            cell.setName(String.valueOf(cellCalendar.get(Calendar.DAY_OF_MONTH)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cellCalendar.getTime());
            cell.setCalendar(calendar);

            if(isToday(cellCalendar)){
                cell.setName("今天");
            }
            cell.setTxtSize(R.dimen.text_size_medium);
            monthDatas.add(cell);
            cellCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return monthDatas;
    }

    /**
     * 指定日期是否是今天
     *
     * @param cal
     * @return
     */
    public static boolean isToday(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 指定日期是否在今日之前
     *
     * @param cal
     * @return
     */
    public static boolean isEnable(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        if (cal.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)) {
            return false;
        } else if (cal.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
            return true;
        }
        if (cal.get(Calendar.MONTH) < calendar.get(Calendar.MONTH)) {
            return false;
        } else if (cal.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)) {
            return true;
        }
        if (cal.get(Calendar.DAY_OF_MONTH) < calendar.get(Calendar.DAY_OF_MONTH)) {
            return false;
        } else if (cal.get(Calendar.DAY_OF_MONTH) > calendar.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    /**
     * 指定日期是否在今日之前
     *
     * @param cal
     * @return
     */
    public static boolean isAfterEndDate(Calendar cal,Calendar calendar) {

        if (cal.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)) {
            return false;
        } else if (cal.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
            return true;
        }
        if (cal.get(Calendar.MONTH) < calendar.get(Calendar.MONTH)) {
            return false;
        } else if (cal.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)) {
            return true;
        }
        if (cal.get(Calendar.DAY_OF_MONTH) < calendar.get(Calendar.DAY_OF_MONTH)) {
            return false;
        } else if (cal.get(Calendar.DAY_OF_MONTH) > calendar.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }
    /**
     * 是否是过去的月份
     *
     * @param cal
     * @return
     */
    public static Integer isLostMonth(Calendar cal,Calendar calendar) {
        if (cal.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)) {
            return -1;
        } else if (cal.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
            return 1;
        }
        if (cal.get(Calendar.MONTH) < calendar.get(Calendar.MONTH)) {
            return -1;
        } else if (cal.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)) {
            return 1;
        }
        return 0;
    }

    /**
     * 是否最大月份
     *
     * @param cal
     * @return
     */
    public static Integer isMastMonth(Calendar cal,Calendar calendar) {
        //Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.MONTH, 6);
        if (cal.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)) {
            return 1;
        } else if (cal.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
            return -1;
        }
        if (cal.get(Calendar.MONTH) < calendar.get(Calendar.MONTH)) {
            return 1;
        } else if (cal.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)) {
            return -1;
        }
        return 0;
    }

    /**
     * 在当前时间和6个月后的月份最大天之间
     * @param cal
     * @return
     */
    public static boolean isBetweenMonth(Calendar cal) {
        Calendar calendarS = Calendar.getInstance();
        Calendar calendarE = Calendar.getInstance();
        calendarE.add(Calendar.MONTH, 7);
        calendarE.set(Calendar.DAY_OF_MONTH, 1);
        calendarE.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTimeInMillis() > calendarS.getTimeInMillis() && cal.getTimeInMillis() < calendarE.getTimeInMillis();
    }
}
