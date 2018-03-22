package com.copasso.cocobill.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.copasso.cocobill.R;

/**
 * 关于App
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.about_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.about_tv_developer)
    TextView developerTv;

    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initEventAndData() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * 监听点击事件 R.id.drawer_tv_name,R.id.drawer_tv_mail
     *
     * @param view
     */
    @OnClick({R.id.about_tv_developer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_tv_developer:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/zas023/CocoBill");
                intent.setData(content_url);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
