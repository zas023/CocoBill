package com.thmub.app.billkeeper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseFragment;
import com.thmub.app.billkeeper.bean.entity.Category;
import com.thmub.app.billkeeper.bean.sql.LocalDatabase;
import com.thmub.app.billkeeper.constant.CategoryRes;
import com.thmub.app.billkeeper.constant.Constant;
import com.thmub.app.billkeeper.ui.activity.CategoryManagerActivity;
import com.thmub.app.billkeeper.ui.adapter.CategoryGridAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

/**
 * Created by Enosh on 2020-03-11
 * Github: https://github.com/zas023
 * <p>
 * 账单编辑页分类展示
 */
public class CategoryPageFragment extends BaseFragment {

    public static final String EXTRA_RECORD_TYPE_KEY = "extra_record_type";
    public static final String EXTRA_CATEGORY_NAME_KEY = "extra_category_name";

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private CategoryGridAdapter categoryGridAdapter;
    private List<Category> categories;
    private int categoryType;
    private int selected;

    public static CategoryPageFragment newInstance(int type, String categoryName) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_RECORD_TYPE_KEY, type);
        bundle.putString(EXTRA_CATEGORY_NAME_KEY, categoryName == null ? "" : categoryName);
        CategoryPageFragment fragment = new CategoryPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 获取选中的分类
     *
     * @return
     */
    public Category getSelectedCategory() {
        return categories.get(selected);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        categoryType = getArguments().getInt(EXTRA_RECORD_TYPE_KEY);
        categories = new ArrayList<>();
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        categoryGridAdapter = new CategoryGridAdapter(categories);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        recyclerView.setAdapter(categoryGridAdapter);
        // categoryGridAdapter.setNewData(categories);
        // categoryGridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initClick() {
        // 设置点击事件
        categoryGridAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == categories.size() - 1) {
                Intent intent = new Intent(mContext, CategoryManagerActivity.class);
                intent.putExtra(CategoryManagerActivity.EXTRA_CATEGORY_TYPE, categoryType);
                startActivity(intent);
            } else {
                categoryGridAdapter.onItemSelected(position);
                selected = position;
            }
        });
    }

    @Override
    protected void processLogic() {
        categories.clear();
        categories.addAll(LocalDatabase.getInstance().categoryDao().queryByType(categoryType));
        categories.add(new Category(null, "编辑", CategoryRes.IC_NAME_SETTING, Constant.TYPE_INCOME, 0));
        categoryGridAdapter.onItemSelected(indexOfSelected());
        categoryGridAdapter.notifyDataSetChanged();
    }

    private int indexOfSelected() {
        String selectedName = getArguments().getString(EXTRA_CATEGORY_NAME_KEY);
        for (int i = 0, n = categories.size(); i < n; i++) {
            if (categories.get(i).getName().equals(selectedName)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        processLogic();
    }
}
