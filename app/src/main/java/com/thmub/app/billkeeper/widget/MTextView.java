package com.thmub.app.billkeeper.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Enosh on 2020-03-08
 * Github: https://github.com/zas023
 */
public class MTextView extends AppCompatTextView {

    public MTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写设置字体方法
    @Override
    public void setTypeface(@Nullable Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Bold.ttf");
        } else {
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Regular.ttf");
        }
        super.setTypeface(tf, style);
    }
}
