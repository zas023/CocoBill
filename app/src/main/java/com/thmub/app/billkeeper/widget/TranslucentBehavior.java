package com.thmub.app.billkeeper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * Created by Enosh on 2020-03-10
 * Github: https://github.com/zas023
 */
public class TranslucentBehavior extends CoordinatorLayout.Behavior<View> {

    /**
     * 原始X轴
     */
    private float originalY = 0;

    public TranslucentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        float percent = (dependency.getY() + dependency.getHeight())/dependency.getHeight();
        if (percent <= 0.6f) {
            percent = 0;
        }
        child.setAlpha(percent);
        return true;
    }

}
