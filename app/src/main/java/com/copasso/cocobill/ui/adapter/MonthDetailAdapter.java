package com.copasso.cocobill.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.copasso.cocobill.R;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.MonthDetailBean;
import com.copasso.cocobill.widget.stickyheader.StickyHeaderGridAdapter;
import com.copasso.cocobill.utils.*;
import com.copasso.cocobill.widget.SwipeMenuView;

import java.util.List;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_HMS_CN;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_YMD_CN;

/**
 * 悬浮头部项
 * 可侧滑编辑、删除
 */
public class MonthDetailAdapter extends StickyHeaderGridAdapter {

    private Context mContext;

    private OnStickyHeaderClickListener onStickyHeaderClickListener;

    private List<MonthDetailBean.DaylistBean> mDatas;

    public MonthDetailAdapter(Context context, List<MonthDetailBean.DaylistBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public void setmDatas(List<MonthDetailBean.DaylistBean> mDatas) {
        this.mDatas = mDatas;
    }

    public void setOnStickyHeaderClickListener(OnStickyHeaderClickListener listener) {
        if (onStickyHeaderClickListener == null)
            this.onStickyHeaderClickListener = listener;
    }

    public void remove(int section, int offset) {
        mDatas.get(section).getList().remove(offset);
        notifySectionItemRemoved(section, offset);
    }

    public void clear() {
        this.mDatas = null;
        notifyAllSectionsDataSetChanged();
    }

    @Override
    public int getSectionCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public int getSectionItemCount(int section) {
        return (mDatas == null || mDatas.get(section).getList() == null) ? 0 : mDatas.get(section).getList().size();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_header, parent, false);
        return new MyHeaderViewHolder(view);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_item, parent, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int section) {
        final MyHeaderViewHolder holder = (MyHeaderViewHolder) viewHolder;
        holder.header_date.setText(mDatas.get(section).getTime());
        holder.header_money.setText(mDatas.get(section).getMoney());
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, final int section, final int position) {
        final MyItemViewHolder holder = (MyItemViewHolder) viewHolder;

        BBill bBill=mDatas.get(section).getList().get(position);
        holder.item_title.setText(bBill.getSortName());
//        Glide.with(mContext).load(Constants.BASE_URL+Constants.IMAGE_SORT
//                + bBill.getSortImg())
//                .into(holder.item_img);
        holder.item_img.setImageDrawable(ImageUtils.getDrawable(bBill.getSortImg()));
        if (bBill.isIncome()) {
            holder.item_money.setText("+" + bBill.getCost());
        } else {
            holder.item_money.setText("-" + bBill.getCost());
        }

        //监听侧滑删除事件
        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int section = getAdapterPositionSection(holder.getAdapterPosition());
                final int offset = getItemSectionOffset(section, holder.getAdapterPosition());

//                确认删除
                new AlertDialog.Builder(mContext).setTitle("是否删除此条记录")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onStickyHeaderClickListener
                                        .OnDeleteClick(mDatas.get(section).getList().get(offset), section, offset);
                            }
                        })
                        .show();
            }
        });
        //监听侧滑编辑事件
        holder.item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int section = getAdapterPositionSection(holder.getAdapterPosition());
                final int offset = getItemSectionOffset(section, holder.getAdapterPosition());
                onStickyHeaderClickListener.OnEditClick(
                        mDatas.get(section).getList().get(offset), section, offset);
            }
        });
        //监听单击显示详情事件
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int section = getAdapterPositionSection(holder.getAdapterPosition());
                final int offset = getItemSectionOffset(section, holder.getAdapterPosition());
                final AlertDialog alertDialog = new AlertDialog.Builder(mContext).setTitle("备注")
                        .setPositiveButton("朕知道了", null)
                        .show();
                final Window window = alertDialog.getWindow();
                window.setContentView(R.layout.dialog_a_bill);
                TextView tv_title = (TextView) window.findViewById(R.id.dialog_bill_tv_title);
                TextView tv_content = (TextView) window.findViewById(R.id.dialog_bill_tv_content);
                TextView tv_date = (TextView) window.findViewById(R.id.dialog_bill_tv_date);
                ImageView iv_bill = (ImageView) window.findViewById(R.id.dialog_bill_iv);
                TextView tv_btn = (TextView) window.findViewById(R.id.dialog_bill_btn);

                BBill bBill=mDatas.get(section).getList().get(offset);

                iv_bill.setImageDrawable(ImageUtils.getDrawable(bBill.getSortImg()));
                String content = bBill.getContent();
                if (content!=null) tv_content.setText("备注信息：" + content);
                tv_title.setText("因" + bBill.getSortName()+ "消费" + Math.abs(bBill.getCost()) + "元");
                if (bBill.isIncome())
                    tv_title.setText("因" + bBill.getSortName()+ "收入" + bBill.getCost() + "元");
                tv_date.setText(DateUtils.long2Str(bBill.getCrdate(), FORMAT_YMD_CN)
                        + "\n\n" + DateUtils.long2Str(bBill.getCrdate(), FORMAT_HMS_CN));
                tv_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            }
        });
    }

    /**
     * 自定义编辑、删除接口
     */
    public interface OnStickyHeaderClickListener {
        void OnDeleteClick(BBill item, int section, int offset);
        void OnEditClick(BBill item, int section, int offset);
    }

    public static class MyHeaderViewHolder extends HeaderViewHolder {
        TextView header_date;
        TextView header_money;

        MyHeaderViewHolder(View itemView) {
            super(itemView);
            header_date = (TextView) itemView.findViewById(R.id.header_date);
            header_money = (TextView) itemView.findViewById(R.id.header_money);
        }
    }

    public static class MyItemViewHolder extends ItemViewHolder {
        TextView item_title;
        TextView item_money;
        Button item_delete;
        Button item_edit;
        ImageView item_img;
        RelativeLayout item_layout;
        SwipeMenuView mSwipeMenuView;

        MyItemViewHolder(View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_money = (TextView) itemView.findViewById(R.id.item_money);
            item_delete = (Button) itemView.findViewById(R.id.item_delete);
            item_edit = (Button) itemView.findViewById(R.id.item_edit);
            item_img = (ImageView) itemView.findViewById(R.id.item_img);
            item_layout = (RelativeLayout) itemView.findViewById(R.id.item_layout);
            mSwipeMenuView = (SwipeMenuView) itemView.findViewById(R.id.swipe_menu);
        }
    }
}
