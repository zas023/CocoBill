package com.thmub.app.billkeeper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseActivity;
import com.thmub.app.billkeeper.bean.entity.Record;
import com.thmub.app.billkeeper.bean.sql.LocalDatabase;
import com.thmub.app.billkeeper.ui.adapter.RecordHomeAdapter;
import com.thmub.app.billkeeper.util.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Enosh on 2020-03-07
 * Github: https://github.com/zas023
 * <p>
 * 搜索
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.recyclerResult)
    RecyclerView recyclerResult;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private RecordHomeAdapter adapter;
    private List<Record> records;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        records = new ArrayList<>();
        adapter = new RecordHomeAdapter(records);
    }

    @Override
    protected void initWidget() {
        overridePendingTransition(0, 0);
        // SearchView
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
        showSoftKeyBoard();

        // recyclerView
        recyclerResult.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerResult.setAdapter(adapter);
    }

    @Override
    protected void initClick() {
        // toolbar
        toolbar.setNavigationOnClickListener((view) -> finish());

        // search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.addData(LocalDatabase.getInstance().recordDao().queryByKeyword(query, 1000, 0));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (ValidationUtils.isEmpty(newText)) {
                    records.clear();
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        // 下拉刷新
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                String keyword = searchView.getQuery().toString();
                if (!ValidationUtils.isEmpty(keyword)) {
                    records.clear();
                    adapter.addData(LocalDatabase.getInstance().recordDao().queryByKeyword(keyword, 1000, 0));
                }
                refreshLayout.finishRefresh();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }
        });

        // recycler
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, RecordDetailActivity.class);
            Record record = (Record) adapter.getItem(position);
            intent.putExtra(RecordDetailActivity.EXTRA_RECORD_ID_KEY, record.getId());
            startActivity(intent);
        });
    }

    private void showSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.showSoftInputFromInputMethod(searchView.getWindowToken(), 0);
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

}
