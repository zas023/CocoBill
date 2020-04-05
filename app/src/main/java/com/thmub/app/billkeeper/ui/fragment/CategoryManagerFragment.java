package com.thmub.app.billkeeper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseFragment;
import com.thmub.app.billkeeper.bean.entity.Category;
import com.thmub.app.billkeeper.bean.sql.LocalDatabase;
import com.thmub.app.billkeeper.ui.activity.CategoryEditActivity;
import com.thmub.app.billkeeper.ui.adapter.CategoryManagerAdapter;
import com.thmub.app.billkeeper.util.ValidationUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Enosh on 2020-03-24
 * Github: https://github.com/zas023
 * <p>
 * 账单分类管理
 */
public class CategoryManagerFragment extends BaseFragment {

    public static final String EXTRA_RECORD_TYPE_KEY = "extra_record_type";

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private List<Category> categories;
    private CategoryManagerAdapter adapter;

    public static CategoryManagerFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_RECORD_TYPE_KEY, type);
        CategoryManagerFragment fragment = new CategoryManagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 保存分类排序
     */
    public void saveCategories() {
        // 设置序号
        for (int i = 0; i < categories.size(); i++) {
            categories.get(i).setOrder(i);
        }
        if (ValidationUtils.isEmpty(LocalDatabase.getInstance().categoryDao().insertCategories(categories))) {
            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        categories = LocalDatabase.getInstance()
                .categoryDao().queryByType(getArguments().getInt(EXTRA_RECORD_TYPE_KEY));
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        adapter = new CategoryManagerAdapter(categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initClick() {
        adapter.setOnItemClickListener((adapter, view, position) -> {
            goToEditAct(position);
        });

        // 监听子布局点击
        adapter.addChildClickViewIds(R.id.tv_modify, R.id.tv_delete);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_modify: //修改
                    goToEditAct(position);
                    break;
                case R.id.tv_delete: //删除
                    showDeleteDialog(position);
                    break;
            }
        });

        // 拖拽
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //首先回调的方法 返回int表示是否监听该方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//拖拽
                // int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//侧滑删除
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //滑动事件
                Collections.swap(categories, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                return;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    /**
     * 跳转分类编辑页
     *
     * @param pos
     */
    private void goToEditAct(int pos) {
        Intent intent = new Intent(mContext, CategoryEditActivity.class);
        intent.putExtra(CategoryEditActivity.EXTRA_CATEGORY_SERIAL, categories.get(pos));
        startActivity(intent);
    }

    /**
     * 删除确认对话框
     *
     * @param pos
     */
    private void showDeleteDialog(int pos) {
        MaterialDialog dialog = new MaterialDialog(mContext, MaterialDialog.getDEFAULT_BEHAVIOR());
        dialog.setTitle(categories.get(pos).getName());
        dialog.message(R.string.message_delete, null, null);
        dialog.positiveButton(R.string.confirm, null, materialDialog -> {
            LocalDatabase.getInstance().categoryDao().deleteCategories(categories.get(pos));
            categories.remove(pos);
            adapter.notifyDataSetChanged();
            return null;
        });
        dialog.negativeButton(R.string.cancel, null, null);
        dialog.show();
    }
}
