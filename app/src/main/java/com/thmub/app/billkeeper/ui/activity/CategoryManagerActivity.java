package com.thmub.app.billkeeper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseActivity;
import com.thmub.app.billkeeper.bean.entity.Category;
import com.thmub.app.billkeeper.constant.Constant;
import com.thmub.app.billkeeper.ui.adapter.CategoryFragmentAdapter;
import com.thmub.app.billkeeper.ui.fragment.CategoryManagerFragment;
import com.thmub.app.billkeeper.widget.tablayout.MTabLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Enosh on 2020-03-24
 * Github: https://github.com/zas023
 * <p>
 * 账单分类管理
 */
public class CategoryManagerActivity extends BaseActivity {

    public static final String EXTRA_CATEGORY_TYPE = "extra_category_type";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    MTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private CategoryFragmentAdapter fragmentAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_category_manager;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        fragmentAdapter = new CategoryFragmentAdapter(getSupportFragmentManager());
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 账单分类
        fragmentAdapter.addFragment(CategoryManagerFragment.newInstance(Constant.TYPE_OUTCOME), "支出");
        fragmentAdapter.addFragment(CategoryManagerFragment.newInstance(Constant.TYPE_INCOME), "收入");

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // 设置展示分类类型
        int type = getIntent().getIntExtra(EXTRA_CATEGORY_TYPE, Constant.TYPE_OUTCOME);
        viewPager.setCurrentItem(type);
    }

    @Override
    protected void initClick() {
        // 标题栏返回
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.tv_add, R.id.action_save})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:  // 添加分类
                Intent intent = new Intent(mContext, CategoryEditActivity.class);
                Category category = new Category();
                category.setType(viewPager.getCurrentItem());
                intent.putExtra(CategoryEditActivity.EXTRA_CATEGORY_SERIAL, category);
                startActivity(intent);
                break;
            case R.id.action_save:  // 保存分类排序
                ((CategoryManagerFragment) fragmentAdapter.getItem(viewPager.getCurrentItem())).saveCategories();
                break;
        }
    }
}
