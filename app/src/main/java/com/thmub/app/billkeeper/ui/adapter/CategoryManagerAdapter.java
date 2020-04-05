package com.thmub.app.billkeeper.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.bean.entity.Category;
import com.thmub.app.billkeeper.constant.CategoryRes;
import com.thmub.app.billkeeper.constant.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Enosh on 2020-03-24
 * Github: https://github.com/zas023
 */
public class CategoryManagerAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    public CategoryManagerAdapter(List<Category> data) {
        super(R.layout.item_category_manager, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Category category) {
        // 账单分类名称
        baseViewHolder.setText(R.id.tvCategoryName, category.getName());
        // 账单分类图标
        baseViewHolder.setBackgroundResource(R.id.flCategoryIcon
                , category.getType() == Constant.TYPE_OUTCOME ? R.drawable.bg_category_circle_outcome : R.drawable.bg_category_circle_income);
        baseViewHolder.setImageResource(R.id.ivCategoryIcon, CategoryRes.resId(category.getIconRes()));
    }
}
