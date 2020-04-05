package com.thmub.app.billkeeper.ui;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

/**
 * Created by Enosh on 2020-03-24
 * Github: https://github.com/zas023
 */
public class MBindingAdapter {

    @BindingAdapter(value = {"imageSrc"}, requireAll = true)
    public static void setImageSrc(AppCompatImageView view, int src) {
        view.setImageResource(src);
    }
}
