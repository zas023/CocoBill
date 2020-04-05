package com.thmub.app.billkeeper.bean.sql;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.thmub.app.billkeeper.bean.entity.Category;

import java.util.List;

/**
 * Created by Enosh on 2020-03-27
 * Github: https://github.com/zas023
 */
@Dao
public interface CategoryDao {

    /**
     * 插入数据（冲突时更新）
     *
     * @param categories
     * @return 主键
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertCategories(Category... categories);

    /**
     * 插入数据（冲突时更新）
     *
     * @param categories
     * @return 主键
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertCategories(List<Category> categories);

    /**
     * 删除数据
     *
     * @param categories
     * @return 数量
     */
    @Delete
    int deleteCategories(Category... categories);


    /**
     * 通过ID查找账单记录
     *
     * @param id
     * @return
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select *  from category where category_id = :id")
    Category queryById(long id);

    /**
     * 通过类型查找账单记录
     *
     * @param type
     * @return
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select *  from category where category_type = :type order by category_order asc")
    List<Category> queryByType(int type);
}
