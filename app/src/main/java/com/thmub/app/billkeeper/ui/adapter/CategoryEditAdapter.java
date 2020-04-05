package com.thmub.app.billkeeper.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.constant.CategoryRes;


import java.util.List;

/**
 * Created by Enosh on 2020-03-14
 * Github: https://github.com/zas023
 * <p>
 * 编辑页面账单分类
 */
public class CategoryEditAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    // 当前选中条目位置
    private String selectedName;

    public CategoryEditAdapter(List<String> items) {
        super(R.layout.item_category_edit, items);
    }

    // private void getSelectedPos(List<Category> items, String selectName) {
    //     if (items == null) {
    //         return;
    //     }
    //     for (int i = 0, n = items.size(); i < n; i++) {
    //         if (items.get(i).getName().equals(selectName)) {
    //             items.get(i).setSelected(true);
    //             mSelectedPos = i;
    //         }else {
    //             items.get(i).setSelected(false);
    //         }
    //     }
    //     // 默认第一个
    //     if (mSelectedPos == 0) {
    //         items.get(mSelectedPos).setSelected(true);
    //     }
    // }

    // @Override
    // protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
    //     // 绑定 view
    //     DataBindingUtil.bind(viewHolder.itemView);
    // }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String string) {
        if (string == null) {
            return;
        }
        baseViewHolder.setImageResource(R.id.ivCategoryRes, CategoryRes.resId(string));
        // 是否选中 #onItemSelected
        // category.setSelected(mSelectedPos == getItemPosition(category));
    }


    public void onItemSelected(String name) {
        if (name.equals(selectedName))
            return;
        selectedName = name;
        notifyDataSetChanged();
    }

}
