package com.copasso.cocobill.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * 日期时间工具类
 * Created by plh on 2016/6/24.
 */
public class DateUtils {

    /**
     * 中文简写如：2010年
     */
    public static String FORMAT_Y_CN = "yyyy年";

    /**
     * 英文简写如：2010
     */
    public static String FORMAT_Y = "yyyy";

    /**
     * 英文简写如：09
     */
    public static String FORMAT_M = "MM";

    /**
     * 英文简写如：09
     */
    public static String FORMAT_D = "dd";

    /**
     * 英文简写如：12:01
     */
    public static String FORMAT_HM = "HH:mm";

    /**
     * 英文简写如：1-12 12:01
     */
    public static String FORMAT_MDHM = "MM-dd HH:mm";

    /**
     * 中文简写如：1月12日
     */
    public static String FORMAT_MD_CN = "MM月dd日";
    /**
     * 英文简写如：1-12
     */
    public static String FORMAT_MD = "MM-dd";

    /**
     * 英文简写如：2017-02
     */
    public static String FORMAT_YM = "yyyy-MM";

    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_YMD = "yyyy-MM-dd";

    /**
     * 英文全称  如：2010-12-01 23:15
     */
    public static String FORMAT_YMDHM = "yyyy-MM-dd HH:mm";

    /**
     * 英文全称  如：2010-12-01 23:15:06
     */
    public static String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL_SN = "yyyyMMddHHmmssS";

    /**
     * 中文简写  如：2010年12月01日
     */
    public static String FORMAT_YMD_CN = "yyyy年MM月dd日";

    /**
     * 中文简写  如：2010年12月
     */
    public static String FORMAT_YM_CN = "yyyy年MM月";

    /**
     * 中文简写  如：2010年12月01日  12时
     */
    public static String FORMAT_YMDH_CN = "yyyy年MM月dd日 HH时";

    /**
     * 中文简写  如：2010年12月01日  12时12分
     */
    public static String FORMAT_YMDHM_CN = "yyyy年MM月dd日 HH时mm分";

    /**
     * 中文全称  如：2010年12月01日  23时15分06秒
     */
    public static String FORMAT_YMDHMS_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    /**
     * 中文全称  如： 23时15分06秒
     */
    public static String FORMAT_HMS_CN = "HH时mm分ss秒";

    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";


    /**
     * 中英混合  如：2010年12月01日  23:15
     */
    public static String FORMAT_YMDHM_CN_EN = "yyyy年MM月dd日  HH:mm";
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Calendar calendar = null;

    /*
     * 日期比较
     * */
    public static boolean compareDate(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.getTime() >= endDate.getTime()) {
//                ToastTools.showToast("开始日期不能大于或等于结束日期");
//                ToastUtil.Toast("所选日期不能不能大于或等于当前日期");
                return false;
            }
        }
        return true;
    }


    /*
     * 将时间转换为时间戳
     */
    public static String date2Stamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String date2Stamp(String format, String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stamp2Date(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static Date str2Date(String str) {
        return str2Date(str, null);
    }

    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return 提取字符串日期
     */
    public static String str2Str(String strDate) {
        Date date = str2Date(strDate);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        return formatter.format(date);
    }

    public static String long2Str(long mseconds, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(mseconds);
        return sdf.format(date);
    }

    public static Calendar str2Calendar(String str) {
        return str2Calendar(str, null);
    }

    public static Calendar str2Calendar(String str, String format) {

        Date date = str2Date(str, format);
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c;
    }

    public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
        return date2Str(c, null);
    }

    public static String date2Str(Calendar c, String format) {
        if (c == null) {
            return null;
        }
        return date2Str(c.getTime(), format);
    }

    public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }

    public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }

    public static String getCurDateStr() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" +
                c.get(Calendar.DAY_OF_MONTH) + "-" +
                c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) +
                ":" + c.get(Calendar.SECOND);
    }

    /*
     * 获取当前日期  2017年8月5日
     * */
    public static Date getCurDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date today = null;
        try {
            today = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return today;
    }

    /**
     * 获得当前日期的字符串格式
     *
     * @param format 格式化的类型
     * @return 返回格式化之后的事件
     */
    public static String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);
    }

    /**
     * @param time 当前的时间
     * @return 格式到秒
     */
    public static String getMillon(long time) {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(time));
    }

    /**
     * @param time 当前的时间
     * @return 当前的天
     */
    public static String getDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    /**
     * 根据传入的日期，获取指定的日期
     *
     * @param strDate
     * @param day
     * @return
     */
    public static String getDay(String strDate, int day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);

        Date dateNew = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM.dd");
        return format.format(dateNew);
    }

    public static String getLastYearDateOfYM() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) - 1 + "年" + (c.get(Calendar.MONTH) + 1 + "月");
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return 增加数个整月
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();

    }

    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return 增加之后的天数
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 获取距现在某一小时的时刻
     *
     * @param format 格式化时间的格式
     * @param h      距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
     * @return 获取距现在某一小时的时刻
     */
    public static String getNextHour(String format, int h) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        date.setTime(date.getTime() + h * 60 * 60 * 1000);
        return sdf.format(date);
    }

    /**
     * 获取时间戳
     *
     * @return 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 功能描述：返回月
     *
     * @param date Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 功能描述：返回日
     *
     * @param date Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 功能描述：返回小
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获得默认的 date pattern
     *
     * @return 默认的格式
     */
    public static String getDatePattern() {
        return FORMAT_YMDHMS;
    }

    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        calendar = Calendar.getInstance();

        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return 提取字符串的日期
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 功能描述：返回毫
     *
     * @param date 日期
     * @return 返回毫
     */
    public static long getMillis(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    public static long getMillis(String strDate) {
        calendar = Calendar.getInstance();
        calendar.setTime(parse(strDate));
        return calendar.getTimeInMillis();
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return 按默认格式的字符串距离今天的天数
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return 提取字符串日期
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当天日期
     */
    public static String getCurDay() {
        return getDay(0, FORMAT_YMD_CN);
    }

    /**
     * @param format 日期格式 获取当天日期
     */
    public static String getCurDay(String format) {
        return getDay(0, format);
    }

    public static String getCurMonth(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return date2Str(calendar, format);
    }

    public static String getCurYear(String format) {
        return getYear(0, format);
    }
    /**
     * 获取前一天日期
     */

    public static String getNextDay() {
        return getDay(-1, FORMAT_YMD_CN);
    }

    /**
     * @param format 日期格式 获取前一天日期
     */
    public static String getNextDay(String format) {
        return getDay(-1, format);
    }

    /**
     * 获取当前日期前后日期
     *
     * @param day    天
     * @param format 日期格式
     */
    public static String getDay(int day, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return date2Str(calendar, format);
    }

    public static String getDay(String day) {
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(FORMAT_YMD).parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return date2Str(calendar, FORMAT_YMD);
    }

    public static String getYear(int year, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, year);
        return date2Str(calendar, format);
    }

    /**
     * YMDHMS日期格式转换
     *
     * @param date   日期字符串
     * @param format 转换后的格式
     * @return 按用户格式字符串距离今天的天数
     */

    public static String convertYMDHMS(String date, String format) {
        if (TextUtils.isEmpty(date)) return "";
        return convert(date, FORMAT_YMDHMS, format);
    }

    /**
     * YMD_EN日期格式转换
     *
     * @param date   日期字符串
     * @param format 转换后的格式
     * @return 按用户格式字符串距离今天的天数
     */

    public static String convertYMD_EN(String date, String format) {
        return convert(date, FORMAT_YMD, format);
    }

    /**
     * YMD日期格式转换
     *
     * @param date   日期字符串
     * @param format 转换后的格式
     * @return 按用户格式字符串距离今天的天数
     */

    public static String convertYMD(String date, String format) {
        return convert(date, FORMAT_YMD_CN, format);
    }

    /**
     * 日期格式转换
     *
     * @param date          日期字符串
     * @param curFormat     当前日期格式
     * @param convertFormat 转换后日期格式
     * @return 按用户格式字符串距离今天的天数
     */

    public static String convert(String date, String curFormat, String convertFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(curFormat);
        try {
            Date d = sdf.parse(date);
            return date2Str(d, convertFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getCurDay();
    }

    /**
     * 计算两个日期相差天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 按用户格式字符串距离今天的天数
     */

    public static long countDays(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YMD_CN);
        try {
            Date d1 = sdf.parse(startDate);
            Date d2 = sdf.parse(endDate);
            return (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Calculate days in month int.
     *
     * @param year  the year
     * @param month the month
     * @return the int
     */
    public static int calculateDaysInMonth(int year, int month) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] bigMonths = {"1", "3", "5", "7", "8", "10", "12"};
        String[] littleMonths = {"4", "6", "9", "11"};
        List<String> bigList = Arrays.asList(bigMonths);
        List<String> littleList = Arrays.asList(littleMonths);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (bigList.contains(String.valueOf(month))) {
            return 31;
        } else if (littleList.contains(String.valueOf(month))) {
            return 30;
        } else {
            if (year <= 0) {
                return 29;
            }
            // 是否闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    /**
     * 月日时分秒，0-9前补0
     *
     * @param number the number
     * @return the string
     */
    @NonNull
    public static String fillZero(int number) {
        return number < 10 ? "0" + number : "" + number;
    }

    /**
     * 比较后面的时间是否大于前面的时间
     */
    public static boolean judgeTime2Time(String format, String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            long l1 = date1.getTime() / 1000;
            long l2 = date2.getTime() / 1000;
            if (l2 - l1 > 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 比较后面的时间是否大于前面的时间
     */
    public static boolean judgeTime2Time(String time1, String time2) {
        return judgeTime2Time(FORMAT_YMD_CN, time1, time2);
    }

    public static String parseDate(String createTime) {
        try {
            String ret = "";
            long create = new SimpleDateFormat(FORMAT_YMDHMS).parse(createTime).getTime();
            Calendar now = Calendar.getInstance();
            long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));//毫秒数
            long ms_now = now.getTimeInMillis();
            if (ms_now - create < ms) {
                ret = convert(createTime, DateUtils.FORMAT_YMDHMS, DateUtils.FORMAT_HM);
            } else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
                ret = "昨天";
            } else {
                ret = convert(createTime, DateUtils.FORMAT_YMDHMS, DateUtils.FORMAT_MD_CN);
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
