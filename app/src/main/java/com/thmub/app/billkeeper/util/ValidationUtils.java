package com.thmub.app.billkeeper.util;

import java.util.Collection;

/**
 * Created by Enosh on 2020-04-04
 * Github: https://github.com/zas023
 * <p>
 * 校验工具类
 */
public class ValidationUtils {

    public static boolean isEmpty(String object) {
        return object == null || object.isEmpty();
    }

    public static boolean isEmpty(Collection<? extends Object> collection) {
        return collection == null || collection.isEmpty();
    }
}
