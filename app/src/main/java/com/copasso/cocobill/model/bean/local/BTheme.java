package com.copasso.cocobill.model.bean.local;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.copasso.cocobill.model.gen.DaoSession;
import com.copasso.cocobill.model.gen.BBillDao;
import com.copasso.cocobill.model.gen.BThemeDao;

/**
 * Created by Zhouas666 on 2019-04-20
 * Github: https://github.com/zas023
 * <p>
 * 账单主题
 */

@Entity
public class BTheme {

    @Id
    private Long id;
    //主题名称
    private String name;

    @ToMany(referencedJoinProperty = "id")
    private List<BBill> bBills;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 606247279)
    private transient BThemeDao myDao;

    @Generated(hash = 215087736)
    public BTheme(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1583527038)
    public BTheme() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1782816240)
    public List<BBill> getBBills() {
        if (bBills == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BBillDao targetDao = daoSession.getBBillDao();
            List<BBill> bBillsNew = targetDao._queryBTheme_BBills(id);
            synchronized (this) {
                if (bBills == null) {
                    bBills = bBillsNew;
                }
            }
        }
        return bBills;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 776152820)
    public synchronized void resetBBills() {
        bBills = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1002810658)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBThemeDao() : null;
    }

}
