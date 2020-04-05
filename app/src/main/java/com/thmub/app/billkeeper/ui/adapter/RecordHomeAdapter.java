package com.thmub.app.billkeeper.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.bean.entity.Record;
import com.thmub.app.billkeeper.constant.CategoryRes;
import com.thmub.app.billkeeper.constant.Constant;
import com.thmub.app.billkeeper.util.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Enosh on 2020-03-22
 * Github: https://github.com/zas023
 * <p>
 * 首页账单记录
 */
public class RecordHomeAdapter extends BaseMultiItemQuickAdapter<Record, BaseViewHolder> {

    private SimpleDateFormat sdf;
    private List<Record> items;

    public RecordHomeAdapter(List<Record> data) {
        super(data);
        setItemType(data);
        items = data;
        sdf = new SimpleDateFormat("MM-dd HH:mm");
        addItemType(Constant.RECORD_ITEM_NORMAL, R.layout.item_record_home);
        addItemType(Constant.RECORD_ITEM_DATE, R.layout.item_record_home_date);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Record record) {
        // 账单分类名称
        baseViewHolder.setText(R.id.tvCategoryName, record.getCategoryName());
        // 账单分类图标
        baseViewHolder.setBackgroundResource(R.id.lvCategoryIcon
                , record.getType() == Constant.TYPE_OUTCOME ? R.drawable.bg_category_circle_outcome : R.drawable.bg_category_circle_income);
        baseViewHolder.setImageResource(R.id.ivCategoryRes, CategoryRes.resId(record.getCategoryRes()));
        // 账单备注
        if (record.getComment().isEmpty()) {
            baseViewHolder.setGone(R.id.tvComment, true);
        } else {
            baseViewHolder.setGone(R.id.tvComment, false);
            baseViewHolder.setText(R.id.tvComment, record.getComment());
        }
        // 账单金额
        baseViewHolder.setText(R.id.tvAmount, (record.getType() == Constant.TYPE_OUTCOME ? "-" : "+") + record.getAmount());
        // 账单时间
        baseViewHolder.setText(R.id.tvDate, sdf.format(record.getRecordDate()));

        // 当天日期
        if (record.getItemType() == Constant.RECORD_ITEM_DATE) {
            // 当天日期
            baseViewHolder.setText(R.id.tv_list_date, DateUtils.getWeekDate(record.getRecordDate(), DateUtils.FORMAT_MONTH_DAY));
            // 当天金额
            baseViewHolder.setText(R.id.tv_list_money, getDayMoney(items, record.getRecordDate()));
        }
    }

    @Override
    public void setNewData(List<Record> data) {
        setItemType(data);
        items = data;
        super.setNewData(data);
    }


    /**
     * 获取当天的收入、支出金额
     */
    private String getDayMoney(List<Record> list, long date) {
        BigDecimal totalCostMoney = new BigDecimal(0);
        BigDecimal totalIncomeMoney = new BigDecimal(0);
        int originDay = DateUtils.getDayOfDate(date);
        for (Record record : list) {
            // 判断如果是指定天
            if (originDay == DateUtils.getDayOfDate(record.getRecordDate())) {
                if (Constant.TYPE_OUTCOME == record.getType()) {
                    totalCostMoney = totalCostMoney.add(new BigDecimal(record.getAmount()));
                } else {
                    totalIncomeMoney = totalIncomeMoney.add(new BigDecimal(record.getAmount()));
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("支出: ");
        sb.append(totalCostMoney);
        sb.append("     收入: ");
        sb.append(totalIncomeMoney);
        return sb.toString();
    }

    /**
     * 设置条目类型，根据是否是同一天来区别展示是否带时间条目
     */
    public void setItemType(List<Record> list) {
        if (list == null) return;
        String preDay = "";
        for (Record record : list) {
            // 获取天
            String day = DateUtils.getDateText(record.getRecordDate(), DateUtils.FORMAT_DAY);
            // 根据是否是同一天设置条目类型
            record.setItemType(day.equals(preDay) ? Constant.RECORD_ITEM_NORMAL : Constant.RECORD_ITEM_DATE);
            // 赋值给上一天
            preDay = day;
        }
    }
}
