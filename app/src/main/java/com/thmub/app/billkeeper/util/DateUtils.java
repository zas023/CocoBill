package com.thmub.app.billkeeper.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.media.MediaExtractor.MetricsConstants.FORMAT;

/**
 * Created by Enosh on 2020-03-22
 * Github: https://github.com/zas023
 */
public class DateUtils {

    /**
     * 年月日格式
     */
    public static final String FORMAT_ALL = "YYYY年MM月dd日 HH:mm";
    public static final String FORMAT_YEAR_MONTH_DAY = "YYYY年MM月dd日";

    public static final String FORMAT_YEAR_MONTH = "YYYY年MM月";

    /**
     * 月和日格式
     */
    public static final String FORMAT_MONTH_DAY = "MM月dd日";
    public static final String FORMAT_MONTH_DAY_SIMPLE = "M-d";

    public static final String FORMAT_MONTH_DAY_HOUR_MINUTE = "MM月dd日 HH:mm";

    /**
     * 单获取天格式
     */
    public static final String FORMAT_DAY = "dd";

    private static String[] weeks = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 将日期对象，格式化为指定日期格式的日期字符串。
     *
     * @param date   日期对象
     * @param format 日期格式
     * @return 根据指定日期格式格式话后的字符串，如：2017-04-07
     */
    public static String getDateText(Date date, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format != null ? format : FORMAT, Locale.getDefault());
        return f.format(date);
    }

    /**
     * 将日期对象，格式化为指定日期格式的日期字符串。
     *
     * @param c      日期对象
     * @param format 日期格式
     * @return 根据指定日期格式格式话后的字符串，如：2017-04-07
     */
    public static String getDateText(Calendar c, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format != null ? format : FORMAT, Locale.getDefault());
        return f.format(c.getTime());
    }

    /**
     * 将时间戳，格式化为指定日期格式的日期字符串。
     *
     * @param time   时间戳
     * @param format 日期格式
     * @return 根据指定日期格式格式话后的字符串，如：2017-04-07
     */
    public static String getDateText(long time, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format != null ? format : FORMAT, Locale.getDefault());
        return f.format(time);
    }

    /**
     * 获取带星期的日期。
     *
     * @param date   日期对象
     * @param format 日期格式
     * @return 如：2017-04-07 星期一
     */
    public static String getWeekDate(Date date, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format != null ? format : FORMAT, Locale.getDefault());
        return f.format(date).concat(" ").concat(getWeekOfDate(date));
    }

    public static String getWeekDate(long date, String format) {
        return getWeekDate(new Date(date), format);
    }

    /**
     * 通过 Date 对象获取对应的星期。
     *
     * @param date 日期对象。
     * @return 如：星期一
     */
    public static String getWeekOfDate(long date) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.setTimeInMillis(date);
        return weeks[c.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String getWeekOfDate(Date date) {
        return getWeekOfDate(date.getTime());
    }


    /**
     * 通过 Date 获取对应几号
     *
     * @param date
     * @return
     */
    public static int getDayOfDate(long date) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.setTimeInMillis(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfDate(Date date) {
        return getDayOfDate(date.getTime());
    }

    /**
     * 获取当月开始时间
     */
    public static long getMonthStartTime(long date) {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        // 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当月的结束时间
     */
    public static long getMonthEndTime(long date) {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        // 获取当前月最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当月的天数
     */
    public static int getDaysOfMonth(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        int maxDate = calendar.get(Calendar.DATE);
        return maxDate;
    }
}
