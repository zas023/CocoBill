package com.thmub.app.billkeeper.ui.dailog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.thmub.app.billkeeper.R;

import androidx.constraintlayout.widget.Guideline;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Enosh on 2020-03-15
 * Github: https://github.com/zas023
 * <p>
 * 文字编辑对话框
 */
public class TextEditDialog extends Dialog {

    private TextView tvTitle;
    private EditText etContent;
    private TextView tvCancel;
    private TextView tvConfirm;
    private Guideline guideCenter;

    private Listener mListener;

    public TextEditDialog(Activity activity) {
        super(activity, R.style.Widget_Dialog_BottomSheet);
        initView(activity);
    }

    private void initView(Activity activity) {
        View root = View.inflate(activity, R.layout.dialog_text_deit, null);

        tvTitle = root.findViewById(R.id.tv_title);
        etContent = root.findViewById(R.id.et_content);
        tvCancel = root.findViewById(R.id.tv_cancel);
        tvConfirm = root.findViewById(R.id.tvConfirm);
        guideCenter = root.findViewById(R.id.guideCenter);

        root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        setContentView(root);
        initWindow(root.getMeasuredHeight());

        // 输入框获取焦点时，弹出软键盘
        etContent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && getWindow() != null) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        // 取消
        tvCancel.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onNegativeClick(etContent, this);
            }
        });
        tvConfirm.setOnClickListener(v -> {
            String content = etContent.getText().toString();
            // 回调
            if (mListener != null) {
                mListener.onPositiveClick(etContent, this, content);
            }
        });

    }

    private void initWindow(int height) {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = window.getWindowManager().getDefaultDisplay().getWidth();
        attributes.height = height;
        window.setAttributes(attributes);
    }

    @Override
    public void show() {
        super.show();
        etContent.setFocusable(true);
        etContent.requestFocus();
    }

    public TextEditDialog setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    public TextEditDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public TextEditDialog setContent(String content) {
        etContent.setText(content);
        // etContent.executePendingBindings();
        etContent.setSelection(etContent.getText().length());
        return this;
    }

    public TextEditDialog setHint(String hint) {
        etContent.setHint(hint);
        return this;
    }

    public interface Listener {
        /**
         * 确定按钮回调
         *
         * @param editText EditText
         * @param dialog   dialog
         * @param text     输入内容
         */
        void onPositiveClick(EditText editText, DialogInterface dialog, String text);

        /**
         * 取消按钮回调
         *
         * @param editText EditText
         * @param dialog   dialog
         */
        void onNegativeClick(EditText editText, DialogInterface dialog);
    }
}
