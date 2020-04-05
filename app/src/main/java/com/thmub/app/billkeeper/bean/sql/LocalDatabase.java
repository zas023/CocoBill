package com.thmub.app.billkeeper.bean.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.thmub.app.billkeeper.App;
import com.thmub.app.billkeeper.bean.entity.Category;
import com.thmub.app.billkeeper.bean.entity.Record;
import com.thmub.app.billkeeper.constant.CategoryRes;
import com.thmub.app.billkeeper.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enosh on 2020-03-27
 * Github: https://github.com/zas023
 */
@Database(entities = {Record.class, Category.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class LocalDatabase extends RoomDatabase {

    /**
     * sqlite db name
     */
    private static final String DATABASE_NAME = "money_keeper";

    private static LocalDatabase sInstance = null;

    public abstract RecordDao recordDao();

    public abstract CategoryDao categoryDao();

    public static LocalDatabase getInstance() {
        if (sInstance == null) {
            synchronized (LocalDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(App.getContext(), LocalDatabase.class, DATABASE_NAME)
                            .addCallback(databaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }

    private static Callback databaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Context context = App.getContext();

            List<Category> categoryList = new ArrayList<>();
            // 支出
            categoryList.add(new Category(null, "一般", CategoryRes.IC_NAME_OTHER, Constant.TYPE_OUTCOME, 0));
            categoryList.add(new Category(null, "餐饮", CategoryRes.IC_NAME_CAN_YIN, Constant.TYPE_OUTCOME, 2));
            categoryList.add(new Category(null, "交通", CategoryRes.IC_NAME_JIAO_TONG, Constant.TYPE_OUTCOME, 3));
            categoryList.add(new Category(null, "购物", CategoryRes.IC_NAME_GOU_WU, Constant.TYPE_OUTCOME, 4));
            categoryList.add(new Category(null, "服饰", CategoryRes.IC_NAME_FU_SHI, Constant.TYPE_OUTCOME, 5));
            categoryList.add(new Category(null, "日用品", CategoryRes.IC_NAME_RI_YONG_PIN, Constant.TYPE_OUTCOME, 6));
            categoryList.add(new Category(null, "娱乐", CategoryRes.IC_NAME_YU_LE, Constant.TYPE_OUTCOME, 7));
            categoryList.add(new Category(null, "食材", CategoryRes.IC_NAME_SHI_CAI, Constant.TYPE_OUTCOME, 8));
            categoryList.add(new Category(null, "零食", CategoryRes.IC_NAME_LING_SHI, Constant.TYPE_OUTCOME, 9));
            categoryList.add(new Category(null, "烟酒茶", CategoryRes.IC_NAME_YAN_JIU_CHA, Constant.TYPE_OUTCOME, 10));
            categoryList.add(new Category(null, "学习", CategoryRes.IC_NAME_XUE_XI, Constant.TYPE_OUTCOME, 11));
            categoryList.add(new Category(null, "医疗", CategoryRes.IC_NAME_YI_LIAO, Constant.TYPE_OUTCOME, 12));
            categoryList.add(new Category(null, "住房", CategoryRes.IC_NAME_ZHU_FANG, Constant.TYPE_OUTCOME, 13));
            categoryList.add(new Category(null, "水电煤", CategoryRes.IC_NAME_SHUI_DIAN_MEI, Constant.TYPE_OUTCOME, 14));
            categoryList.add(new Category(null, "通讯", CategoryRes.IC_NAME_TONG_XUN, Constant.TYPE_OUTCOME, 15));
            categoryList.add(new Category(null, "人情来往", CategoryRes.IC_NAME_REN_QING, Constant.TYPE_OUTCOME, 16));
            // 收入
            categoryList.add(new Category(null, "一般", CategoryRes.IC_NAME_OTHER, Constant.TYPE_INCOME, 0));
            categoryList.add(new Category(null, "薪资", CategoryRes.IC_NAME_XIN_ZI, Constant.TYPE_INCOME, 1));
            categoryList.add(new Category(null, "奖金", CategoryRes.IC_NAME_JIANG_JIN, Constant.TYPE_INCOME, 2));
            categoryList.add(new Category(null, "借入", CategoryRes.IC_NAME_JIE_RU, Constant.TYPE_INCOME, 3));
            categoryList.add(new Category(null, "收债", CategoryRes.IC_NAME_SHOU_ZHAI, Constant.TYPE_INCOME, 4));
            categoryList.add(new Category(null, "利息收入", CategoryRes.IC_NAME_LI_XIN_SHOU_RU, Constant.TYPE_INCOME, 5));
            categoryList.add(new Category(null, "投资回收", CategoryRes.IC_NAME_TOU_ZI_HUI_SHOU, Constant.TYPE_INCOME, 6));
            categoryList.add(new Category(null, "投资收益", CategoryRes.IC_NAME_TOU_ZI_SHOU_YI, Constant.TYPE_INCOME, 7));
            categoryList.add(new Category(null, "意外所得", CategoryRes.IC_NAME_YI_WAI_SUO_DE, Constant.TYPE_INCOME, 8));
            // 插入记录事务
            db.beginTransaction();

            // 插入分类
            for (Category category : categoryList) {
                ContentValues values = new ContentValues(4);
                values.put("category_name", category.getName());
                values.put("category_icon_res", category.getIconRes());
                values.put("category_type", category.getType());
                values.put("category_order", 0);
                db.insert("category", SQLiteDatabase.CONFLICT_NONE, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    };
}
