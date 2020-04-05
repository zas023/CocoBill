package com.thmub.app.billkeeper.util;

import java.math.BigDecimal;

/**
 * Created by Enosh on 2020-03-22
 * Github: https://github.com/zas023
 * <p>
 * BigDecimal
 */
public class MathUtils {

    /**
     * 大数运算解决浮点数精度问题
     *
     * @param s1
     * @param s2
     * @return
     */
    public static String add(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b1.add(b2).toString();
    }
}
