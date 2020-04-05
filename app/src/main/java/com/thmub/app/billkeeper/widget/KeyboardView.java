package com.thmub.app.billkeeper.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thmub.app.billkeeper.R;

import java.math.BigDecimal;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;

/**
 * Created by Enosh on 2020-03-08
 * Github: https://github.com/zas023
 * <p>
 * 自定义键盘
 */
public class KeyboardView extends LinearLayout {

    private static final int MAX_INTEGER_NUMBER = 6;

    @BindView(R.id.edit_input)
    EditText editInput;
    @BindView(R.id.keyboard_num_1)
    TextView keyboardNum1;
    @BindView(R.id.keyboard_num_2)
    TextView keyboardNum2;
    @BindView(R.id.keyboard_num_3)
    TextView keyboardNum3;
    @BindView(R.id.keyboard_num_4)
    TextView keyboardNum4;
    @BindView(R.id.keyboard_num_5)
    TextView keyboardNum5;
    @BindView(R.id.keyboard_num_6)
    TextView keyboardNum6;
    @BindView(R.id.keyboard_num_7)
    TextView keyboardNum7;
    @BindView(R.id.keyboard_num_8)
    TextView keyboardNum8;
    @BindView(R.id.keyboard_num_9)
    TextView keyboardNum9;
    @BindView(R.id.keyboard_num_point)
    TextView keyboardNumPoint;
    @BindView(R.id.keyboard_num_0)
    TextView keyboardNum0;
    @BindView(R.id.keyboard_delete)
    LinearLayout keyboardDelete;
    @BindView(R.id.keyboard_confirm)
    TextView keyboardConfirm;
    @BindView(R.id.keyboard_num_sub)
    TextView keyboardNumSub;
    @BindView(R.id.keyboard_num_add)
    TextView keyboardNumAdd;

    protected Unbinder unbinder;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setText(String text) {
        if (editInput != null) {
            editInput.setText(text);
        }
    }

    /*************************Constructor*************************/


    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_keyboard, this);
        unbinder = ButterKnife.bind(this);
        initView(context);
    }

    /**********************Initialization****************************/
    private void initView(Context context) {
        // 设置竖屏页面
        Activity activity = (Activity) context;
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setOrientation(VERTICAL);
        // 设置不弹键盘
        editInput.setFocusable(false);
        // 禁止长按
        editInput.setLongClickable(false);
    }

    /**
     * 监听数字键
     *
     * @param view
     */
    @OnClick({R.id.keyboard_num_0, R.id.keyboard_num_1, R.id.keyboard_num_2, R.id.keyboard_num_3
            , R.id.keyboard_num_4, R.id.keyboard_num_5, R.id.keyboard_num_6, R.id.keyboard_num_7
            , R.id.keyboard_num_8, R.id.keyboard_num_9, R.id.keyboard_num_point, R.id.keyboard_delete
            , R.id.keyboard_num_sub, R.id.keyboard_num_add, R.id.keyboard_confirm})
    protected void onClick(View view) {
        StringBuilder sb = new StringBuilder(editInput.getText().toString().trim());
        switch (view.getId()) {
            case R.id.keyboard_num_0:
            case R.id.keyboard_num_1:
            case R.id.keyboard_num_2:
            case R.id.keyboard_num_3:
            case R.id.keyboard_num_4:
            case R.id.keyboard_num_5:
            case R.id.keyboard_num_6:
            case R.id.keyboard_num_7:
            case R.id.keyboard_num_8:
            case R.id.keyboard_num_9:
            case R.id.keyboard_num_point:
                // 数字键
                inputBuilder(sb, ((TextView) view).getText().toString());
                break;
            case R.id.keyboard_num_sub:
            case R.id.keyboard_num_add:
                int pos = Math.max(sb.lastIndexOf("+"), sb.lastIndexOf("-"));
                // 是否含有运算符（过滤以运算符开头）
                if (pos > 0) {
                    calculate(sb);
                } else {
                    keyboardConfirm.setText("=");
                }
                sb.append(((TextView) view).getText());
                break;
            case R.id.keyboard_delete:
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                break;
            case R.id.keyboard_confirm:
                if ("=".equals(keyboardConfirm.getText())) {
                    keyboardConfirm.setText("确定");
                    calculate(sb);
                } else {
                    if (onClickListener != null) {
                        // 取绝对值
                        onClickListener.onAffirmClick(sb.toString().replace("-", ""));
                    }
                    return;
                }
                break;
        }
        editInput.setText(sb.toString());
    }

    /**
     * 构建新字符串
     *
     * @param sb
     * @param text
     * @return
     */
    private StringBuilder inputBuilder(StringBuilder sb, String text) {
        int pos = Math.max(sb.lastIndexOf("+"), sb.lastIndexOf("-"));
        // 不含运算符
        if (pos == -1) {
            return inputFilter(sb, text);
        }
        StringBuilder tmp = inputFilter(new StringBuilder(sb.substring(pos + 1)), text);
        // 清除运算符后的字符
        sb.delete(pos + 1, sb.length());
        return sb.append(tmp);
    }

    /**
     * 校验输入
     *
     * @param sb
     * @param text
     * @return
     */
    private StringBuilder inputFilter(StringBuilder sb, String text) {
        if (sb.length() < 1) {
            // 输入第一个字符
            if (TextUtils.equals(text, ".")) {
                sb.insert(0, "0.");
            } else {
                sb.insert(0, text);
            }
        } else if (sb.length() == 1) {
            // 输入第二个字符
            if (sb.toString().startsWith("0")) {
                if (TextUtils.equals(".", text)) {
                    sb.insert(sb.length(), ".");
                } else {
                    sb.replace(0, 1, text);
                }
            } else {
                sb.insert(sb.length(), text);
            }
        } else if (sb.toString().contains(".")) {
            // 已经输入了小数点
            int length = sb.length();
            int index = sb.indexOf(".");
            if (!TextUtils.equals(".", text)) {
                if (length - index < 3) {
                    sb.insert(sb.length(), text);
                }
            }
        } else {
            // 整数
            if (TextUtils.equals(".", text)) {
                sb.insert(sb.length(), text);
            } else {
                if (sb.length() < MAX_INTEGER_NUMBER) {
                    sb.insert(sb.length(), text);
                }
            }
        }
        return sb;
    }

    /**
     * 计算结果
     *
     * @param sb
     */
    private void calculate(StringBuilder sb) {
        // 判断运算符
        boolean isAdd = true;
        int pos = sb.lastIndexOf("+");
        if (pos == -1) {
            pos = sb.lastIndexOf("-");
            isAdd = false;
        }
        // 运算符开头
        if (pos == 0) {
            return;
        }
        // 判断是否有无decimal2
        if (pos == sb.length() - 1) {
            sb.deleteCharAt(sb.length() - 1);
            return;
        }
        BigDecimal decimal1 = new BigDecimal(sb.substring(0, pos));
        BigDecimal decimal2 = new BigDecimal(sb.substring(pos + 1, sb.length()));
        decimal1 = isAdd ? decimal1.add(decimal2) : decimal1.subtract(decimal2);
        sb.delete(0, sb.length());
        sb.append(decimal1.toString());
    }

    /**
     * 监听长按事件
     *
     * @param view
     */
    @OnLongClick(R.id.keyboard_delete)
    protected boolean onLongClick(View view) {
        // 长按删除键清空
        StringBuilder sb = new StringBuilder(editInput.getText().toString().trim());
        if (sb.length() > 0) {
            editInput.setText("");
        }
        return false;
    }

    /**********************Lifecycle*****************************/

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unbinder.unbind();
    }

    /**********************interface*****************************/
    public interface OnClickListener {
        /**
         * 确定按钮点击事件
         *
         * @param text 输入框的文字
         */
        void onAffirmClick(String text);
    }


}
