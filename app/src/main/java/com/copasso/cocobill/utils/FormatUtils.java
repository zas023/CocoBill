package com.copasso.cocobill.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhouas666 on 2017/2/22.
 */

public class FormatUtils {

    /**
     * @param money
     * @return
     * @Method : moneyFormat
     * @Description: 对金额的格式调整到分
     */
    public static String moneyFormat(String money) {// 23->23.00
        StringBuffer sb = new StringBuffer();
        if (money == null) {
            return "0.00";
        }
        while (money.startsWith("0")) {
            money = money.substring(1);
        }

        int index = money.indexOf(".");
        if (index == -1) {
            return money + ".00";
        } else {
            String s0 = money.substring(0, index);// 整数部分
            String s1 = money.substring(index + 1);// 小数部分
            if (s1.length() == 1) {// 小数点后一位
                s1 = s1 + "0";
            } else if (s1.length() > 2) {// 如果超过3位小数，截取2位就可以了
                s1 = s1.substring(0, 2);
            }
            sb.append(s0);
            sb.append(".");
            sb.append(s1);
        }
        return sb.toString();
    }

    public static String MyDecimalFormat(String pattern, double value) {
        DecimalFormat myFormat = new DecimalFormat();
        myFormat.applyPattern(pattern);
        String str = myFormat.format(value);
        return str;
    }

    public static String getMoneyStr(double money) {
        return MyDecimalFormat("##,###,###.##", money);
    }


    /**
     * 时间格式转换
     * @param timestamp
     * @return
     */
    public static String format1(long timestamp) {
        SimpleDateFormat formate = new SimpleDateFormat("y-M-d", Locale.CHINA);
        return formate.format(new Date(timestamp* 1000));
    }

    public static String format2(long timestamp) {
        SimpleDateFormat formate = new SimpleDateFormat("y-M-d HH:mm", Locale.CHINA);
        return formate.format(new Date(timestamp* 1000));
    }


}
