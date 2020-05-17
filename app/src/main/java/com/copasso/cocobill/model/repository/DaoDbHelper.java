package com.copasso.cocobill.model.repository;

import android.database.sqlite.SQLiteDatabase;

import com.copasso.cocobill.MyApplication;
import com.copasso.cocobill.model.gen.DaoMaster;
import com.copasso.cocobill.model.gen.DaoSession;

public class DaoDbHelper {
    private static final String DB_NAME = "CocoBill_DB";

    private static volatile DaoDbHelper sInstance;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mSession;

    private DaoDbHelper(){
        //封装数据库的创建、更新、删除
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(MyApplication.getContext(),DB_NAME,null);
        //获取数据库
        mDb = openHelper.getWritableDatabase();
        //封装数据库中表的创建、更新、删除
        mDaoMaster = new DaoMaster(mDb);  //合起来就是对数据库的操作
        //对表操作的对象。
        mSession = mDaoMaster.newSession(); //可以认为是对数据的操作
    }


    public static DaoDbHelper getInstance(){
        if (sInstance == null){
            synchronized (DaoDbHelper.class){
                if (sInstance == null){
                    sInstance = new DaoDbHelper();
                }
            }
        }
        return sInstance;
    }

    public DaoSession getSession(){
        return mSession;
    }

    public SQLiteDatabase getDatabase(){
        return mDb;
    }

    public DaoSession getNewSession(){
        return mDaoMaster.newSession();
    }
}
