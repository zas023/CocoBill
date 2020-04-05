package com.thmub.app.billkeeper.util;

/**
 * Created by Enosh on 2020-03-15
 * Github: https://github.com/zas023
 */
public class CommonUtils {
    public static int string2int(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int string2int(String string, int defaultVal) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultVal;
    }

    public static float string2float(String string, float defaultVal) {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultVal;
    }
}
