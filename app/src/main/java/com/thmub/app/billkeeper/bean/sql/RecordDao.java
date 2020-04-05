package com.thmub.app.billkeeper.bean.sql;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.thmub.app.billkeeper.bean.entity.GroupDaily;
import com.thmub.app.billkeeper.bean.entity.Record;
import com.thmub.app.billkeeper.bean.entity.GroupCategory;
import com.thmub.app.billkeeper.bean.entity.GroupType;

import java.util.List;

/**
 * Created by Enosh on 2020-03-27
 * Github: https://github.com/zas023
 */
@Dao
public interface RecordDao {

    /**
     * 插入数据（冲突时更新）
     *
     * @param record
     * @return 主键
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertRecord(Record record);

    /**
     * 插入数据（冲突时更新）
     *
     * @param records
     * @return 主键
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertRecords(Record... records);

    /**
     * 删除数据
     *
     * @param records
     * @return 数量
     */
    @Delete
    int deleteRecords(Record... records);


    /**
     * 通过ID查找账单记录
     *
     * @param id
     * @return
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select *  from record where record_id = :id")
    Record queryById(long id);


    /**
     * 查询所有类型记录
     *
     * @param startTime
     * @param endTime
     * @param limit
     * @param offset
     * @return
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select * from record where record_date >= :startTime and record_date <= :endTime order by record_date desc limit :limit offset :offset")
    List<Record> queryAll(long startTime, long endTime, long limit, long offset);

    /**
     * 查询指定时间区间内的日支出数据
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param type  账单类型
     * @return 查询到的日支出数据
     */
    @Query("select count(*) as record_count, sum(cast(record_amount as float(8,2))) as record_amount, record_date " +
            "from record " +
            "where record_date >= :start and record_date<= :end and record_type = :type " +
            "group by strftime('%Y-%m-%d', datetime(record_date/1000, 'unixepoch', 'localtime')) " +
            "order by record_date asc")
    List<GroupDaily> queryGroupByDaily(long start, long end, int type);

    /**
     * 查询指定时间区间内的分类支出数据
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param type  账单类型
     * @return 查询到的分类支出数据
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select record_type, count(*) as group_count, sum(record_amount) as group_amount, record_category_name, record_category_res " +
            "from record " +
            "where record_date >= :start and record_date<= :end and record_type = :type " +
            "group by record_category_name " +
            "order by group_amount asc")
    List<GroupCategory> queryGroupByCategory(long start, long end, int type);

    /**
     * 查询指定时间区间内收支总量
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 查询到的分类支出数据
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select count(*) as group_count, sum(record_amount) as group_amount, record_type, record_date " +
            "from record " +
            "where record_date >= :start and record_date<= :end " +
            "group by record_type " +
            "order by record_type asc")
    List<GroupType> queryGroupByType(long start, long end);

    /**
     * 关键词查询记录
     *
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select * from record " +
            "where (record_category_name like '%' || :keyword || '%' or record_comment like '%' || :keyword || '%') " +
            "order by record_date desc limit :limit offset :offset")
    List<Record> queryByKeyword(String keyword, long limit, long offset);

}

