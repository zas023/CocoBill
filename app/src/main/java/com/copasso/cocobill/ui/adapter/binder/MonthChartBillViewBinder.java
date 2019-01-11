package com.copasso.cocobill.ui.adapter.binder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.copasso.cocobill.R;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.utils.DateUtils;
import com.copasso.cocobill.utils.ImageUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import me.drakeet.multitype.ItemViewBinder;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_HMS_CN;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_YMD_CN;

/**
 * Created by Zhouas666 on 2019-01-10
 * Github: https://github.com/zas023
 * ChartFragment中分类目录下账单排序列表
 */
public class MonthChartBillViewBinder extends ItemViewBinder<BBill, MonthChartBillViewBinder.ViewHolder> {

    private Context mContext;

    public MonthChartBillViewBinder(Context context){
        this.mContext=context;
    }

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
        holder.root.setOnClickListener(v -> {
            new MaterialDialog.Builder(mContext)
                    .title(item.getSortName())
                    .content("\t\t" + Math.abs(item.getCost()) + "元\n\t\t" + item.getContent()
                            +"\n\n\t\t"+DateUtils.long2Str(item.getCrdate(), FORMAT_YMD_CN)
                            +"\n\t\t"+DateUtils.long2Str(item.getCrdate(), FORMAT_HMS_CN))
                    .positiveText("朕知道了")
                    .icon(ImageUtils.getDrawable(item.getSortImg()))
                    .limitIconToDefaultSize()
                    .show();
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private TextView title;
        private TextView money;
        private TextView rank;

        public ViewHolder(View view) {
            super(view);
            root = view;
            title = view.findViewById(R.id.title);
            money = view.findViewById(R.id.money);
            rank = view.findViewById(R.id.rank);
        }
    }
}
