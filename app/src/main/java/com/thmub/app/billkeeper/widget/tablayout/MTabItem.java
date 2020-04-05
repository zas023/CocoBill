package com.thmub.app.billkeeper.widget.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.thmub.app.billkeeper.R;

import androidx.appcompat.content.res.AppCompatResources;

/**
 * Created by Enosh on 2020-03-11
 * Github: https://github.com/zas023
 */
public class MTabItem extends View {
    final CharSequence mText;
    final Drawable mIcon;
    final int mCustomLayout;

    public MTabItem(Context context) {
        this(context, null);
    }

    public MTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabItem);
        mText = typedArray.getText(R.styleable.MTabItem_android_text);
        mIcon = getDrawable(context, typedArray, R.styleable.MTabItem_android_icon);
        mCustomLayout = typedArray.getResourceId(R.styleable.MTabItem_android_layout, 0);

        typedArray.recycle();
    }

    public Drawable getDrawable(Context context, TypedArray typedArray, int index) {
        if (typedArray.hasValue(index)) {
            final int resourceId = typedArray.getResourceId(index, 0);
            if (resourceId != 0) {
                return AppCompatResources.getDrawable(context, resourceId);
            }
        }
        return typedArray.getDrawable(index);
    }
}
