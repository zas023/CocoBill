package com.thmub.app.billkeeper.ui.adapter;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.bean.entity.Category;
import com.thmub.app.billkeeper.databinding.ItemCategoryGridBinding;

import java.util.List;

/**
 * Created by Enosh on 2020-03-14
 * Github: https://github.com/zas023
 * <p>
 * 编辑页面账单分类
 */
public class CategoryGridAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    // 当前选中条目位置
    private int mSelectedPos;

    public CategoryGridAdapter() {
        super(R.layout.item_category_grid, null);
    }

    public CategoryGridAdapter(List<Category> items) {
        super(R.layout.item_category_grid, items);
    }

    @Override
    protected void onItemViewHolderCreated(BaseViewHolder viewHolder, int viewType) {
        // 绑定 view
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Category category) {
        if (category == null) {
            return;
        }
        ItemCategoryGridBinding binding = baseViewHolder.getBinding();

        if (binding != null) {
            // 设置数据
            binding.setCategory(category);
            binding.executePendingBindings();
        }
    }

    public void onItemSelected(int position) {
        // if (mSelectedPos == position)
        //     return;
        mSelectedPos = position;
        // 设置选中：o(n)
        List<Category> categories = getData();
        for (int i = 0, n = categories.size(); i < n; i++) {
            categories.get(i).setSelected(mSelectedPos == i);
        }
        notifyDataSetChanged();
    }

}
