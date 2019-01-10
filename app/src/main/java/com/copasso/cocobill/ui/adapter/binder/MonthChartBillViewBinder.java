package com.copasso.cocobill.ui.adapter.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.copasso.cocobill.R;
import com.copasso.cocobill.model.bean.local.BBill;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Zhouas666 on 2019-01-10
 * Github: https://github.com/zas023
 * ChartFragment中分类目录下账单排序列表
 */
public class MonthChartBillViewBinder extends ItemViewBinder<BBill, MonthChartBillViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_recycler_monthchart_rank, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull BBill item) {

        holder.rank.setText(holder.getAdapterPosition() + 1 + "");
        holder.title.setText(item.getSortName());
        if (item.isIncome())
            holder.money.setText("+" + item.getCost());
        else
            holder.money.setText("-" + item.getCost());
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView money;
        private TextView rank;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            money = view.findViewById(R.id.money);
            rank = view.findViewById(R.id.rank);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            }
        }
    }
}
