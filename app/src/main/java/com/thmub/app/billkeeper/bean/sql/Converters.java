package com.thmub.app.billkeeper.bean.sql;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Created by Enosh on 2020-03-27
 * Github: https://github.com/zas023
 */
public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
