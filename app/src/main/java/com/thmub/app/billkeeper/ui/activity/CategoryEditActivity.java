package com.thmub.app.billkeeper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseActivity;
import com.thmub.app.billkeeper.bean.entity.Category;
import com.thmub.app.billkeeper.bean.sql.LocalDatabase;
import com.thmub.app.billkeeper.constant.CategoryRes;
import com.thmub.app.billkeeper.constant.Constant;
import com.thmub.app.billkeeper.ui.adapter.CategoryEditAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Enosh on 2020-03-24
 * Github: https://github.com/zas023
 * <p>
 * 账单分类添加/编辑
 */
public class CategoryEditActivity extends BaseActivity {

    public static final String EXTRA_CATEGORY_SERIAL = "extra_category_serial";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ivCategoryRes)
    AppCompatImageView ivCategoryRes;
    @BindView(R.id.flCategoryIcon)
    FrameLayout flCategoryIcon;
    @BindView(R.id.etCategoryName)
    EditText etCategoryName;

    private CategoryEditAdapter adapter;
    private List<String> resList;
    private int selectedPos;
    private Category category;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_category_edit;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        resList = new ArrayList<>(CategoryRes.ALL_ICON);
        Intent intent = getIntent();
        // todo category为null
        category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY_SERIAL);
        // 判断类型
        if (category == null) {
            category = new Category();
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        adapter = new CategoryEditAdapter(resList);
        recycler.setLayoutManager(new GridLayoutManager(mContext, 5));
        recycler.setAdapter(adapter);
        // 选中图表背景
        flCategoryIcon.setBackgroundResource(category.getType() == Constant.TYPE_OUTCOME
                ? R.drawable.bg_category_circle_outcome : R.drawable.bg_category_circle_income);
        // 设置分类信息
        etCategoryName.setText(category.getName() == null ? "" : category.getName());
        ivCategoryRes.setImageResource(CategoryRes.resId(category.getIconRes()));
    }

    @Override
    protected void initClick() {
        // 标题栏返回
        toolbar.setNavigationOnClickListener(v -> finish());

        // Recycler
        adapter.setOnItemClickListener((adapter, view, position) -> {
            ivCategoryRes.setImageResource(CategoryRes.resId(resList.get(position)));
            selectedPos = position;
        });
    }

    @OnClick({R.id.tvSubmit})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:  // 保存
                saveCategory();
                break;
        }
    }

    /**
     * 保存账单分类
     */
    private void saveCategory() {
        String categoryName = etCategoryName.getText().toString();
        if (categoryName.isEmpty()) {
            Toast.makeText(mContext, "请输入分类名称", Toast.LENGTH_SHORT).show();
            return;
        }
        category.setName(categoryName);
        category.setIconRes(resList.get(selectedPos));

        if (LocalDatabase.getInstance().categoryDao().insertCategories(category) != null) {
            finish();
        } else {
            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
