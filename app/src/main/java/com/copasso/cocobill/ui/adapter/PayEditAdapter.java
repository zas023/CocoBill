package com.copasso.cocobill.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.copasso.cocobill.R;
import com.copasso.cocobill.model.bean.local.BPay;
import com.copasso.cocobill.common.Constants;

import java.util.List;

/**
 * Created by zhouas666 on 2017/12/30.
 */
public class PayEditAdapter extends RecyclerView.Adapter<PayEditAdapter.MyItemViewHolder> {

    private Context mContext;
    private List<BPay> mData;

    public PayEditAdapter(Context mContext, List<BPay> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_note_edit, parent, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyItemViewHolder holder, int position) {
        holder.item_name.setText(mData.get(position).getPayName());
        Glide.with(mContext).load(Constants.BASE_URL + Constants.IMAGE_PAY + mData.get(position).getPayImg())
                .into(holder.item_img);
//        Log.i("adapter",Constants.BASE_URL + Constants.IMAGE_PAY + mData.get(position).getPayImg());
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public static class MyItemViewHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        ImageView item_img;

        MyItemViewHolder(View view) {
            super(view);
            item_img = (ImageView) view.findViewById(R.id.item_note_edit_iv);
            item_name = (TextView) view.findViewById(R.id.item_note_edit_tv);
        }
    }
}
