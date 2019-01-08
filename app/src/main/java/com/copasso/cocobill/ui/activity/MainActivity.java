package com.copasso.cocobill.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.copasso.cocobill.R;
import com.copasso.cocobill.common.Constants;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.local.NoteBean;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.model.repository.LocalRepository;
import com.copasso.cocobill.ui.adapter.MainFragmentPagerAdapter;
import com.copasso.cocobill.base.BaseActivity;
import com.copasso.cocobill.model.repository.BmobRepository;
import com.copasso.cocobill.ui.fragment.MonthListFragment;
import com.copasso.cocobill.utils.SharedPUtils;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import cn.bmob.v3.BmobUser;

/**
 * Created by Zhouas666 on AndroidStudio
 * Date: 2019-01-08
 * Github: https://github.com/zas023
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawer;
    NavigationView navigationView;

    private View drawerHeader;
    private ImageView drawerIv;
    private TextView drawerTvAccount, drawerTvMail;

    protected static final int USERINFOACTIVITY_CODE = 0;
    protected static final int LOGINACTIVITY_CODE = 1;

    // Tab
    private FragmentManager mFragmentManager;
    private MainFragmentPagerAdapter mFragmentPagerAdapter;

    private MyUser currentUser;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //第一次进入将默认账单分类添加到数据库
        if(SharedPUtils.isFirstStart(mContext)){
            Log.i(TAG,"第一次进入将默认账单分类添加到数据库");
            NoteBean note= new Gson().fromJson(Constants.BILL_NOTE, NoteBean.class);
            List<BSort> sorts=note.getOutSortlis();
            sorts.addAll(note.getInSortlis());
            LocalRepository.getInstance().saveBsorts(sorts);
            LocalRepository.getInstance().saveBPays(note.getPayinfo());
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        toolbar=findViewById(R.id.toolbar);
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.main_viewpager);
        drawer=findViewById(R.id.main_drawer);
        navigationView=findViewById(R.id.main_nav_view);

        //初始化Toolbar
        toolbar.setTitle("CocoBill");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawerHeader = navigationView.inflateHeaderView(R.layout.drawer_header);
        drawerIv = drawerHeader.findViewById(R.id.drawer_iv);
        drawerTvAccount = drawerHeader.findViewById(R.id.drawer_tv_name);
        drawerTvMail = drawerHeader.findViewById(R.id.drawer_tv_mail);

        //设置头部账户
        setDrawerHeaderAccount();

        //初始化ViewPager
        mFragmentManager = getSupportFragmentManager();
        mFragmentPagerAdapter = new MainFragmentPagerAdapter(mFragmentManager);
        mFragmentPagerAdapter.addFragment(new MonthListFragment(), "明细");

        viewPager.setAdapter(mFragmentPagerAdapter);

        //初始化TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("明细"));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //监听侧滑菜单项
        navigationView.setNavigationItemSelectedListener(this);
        //监听侧滑菜单头部点击事件
        drawerHeader.setOnClickListener(v-> {
            startActivityForResult(new Intent(mContext,LandActivity.class),LOGINACTIVITY_CODE);
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_account) {      //账户
            // Handle the camera action
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_month) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_total) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_sync) {   //同步账单
            if(currentUser==null)
                SnackbarUtils.show(mContext,"请先登陆");
            else
                BmobRepository.getInstance().syncBill(currentUser.getObjectId());
        }  else if (id == R.id.nav_setting) {   //设置
//            startActivity(new Intent(this,SettingActivity.class));
        } else if (id == R.id.nav_about) {     //关于
//            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (id == R.id.nav_theme) {     //主题
//            showUpdateThemeDialog();
        } else if (id == R.id.nav_exit) {      //退出登陆

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * 监听Activity返回值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case USERINFOACTIVITY_CODE:
                    setDrawerHeaderAccount();
                    break;
                case LOGINACTIVITY_CODE:
                    setDrawerHeaderAccount();
                    break;
            }
        }
    }

    /**
     * 设置DrawerHeader的用户信息
     */
    public void setDrawerHeaderAccount() {
        currentUser= BmobUser.getCurrentUser(MyUser.class);
        //获取当前用户
        if (currentUser != null) {
            drawerTvAccount.setText(currentUser.getUsername());
            drawerTvMail.setText(currentUser.getEmail());
            Glide.with(mContext).load(currentUser.getImage()).into(drawerIv);
        }else{
            drawerTvAccount.setText("账号");
            drawerTvMail.setText("点我登陆");
            drawerIv.setImageResource(R.mipmap.ic_def_icon);
        }
    }
}
