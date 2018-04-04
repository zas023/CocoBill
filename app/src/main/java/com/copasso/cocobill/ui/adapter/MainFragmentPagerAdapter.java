package com.copasso.cocobill.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouas666 on 2017/12/13.
 * 主布局中FragmentPagerAdapter
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments ;//添加的Fragment的集合
    private List<String> mFragmentsTitles ;//每个Fragment对应的title的集合

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragmentsTitles = new ArrayList<>();
    }
    /**
     * @param fragment      添加Fragment
     * @param fragmentTitle Fragment的标题，即TabLayout中对应Tab的标题
     */
    public void addFragment(Fragment fragment, String fragmentTitle) {
        mFragments.add(fragment);
        mFragmentsTitles.add(fragmentTitle);
    }

    /**
     * 更新
     * @param index
     * @param fragment
     * @param fragmentTitle
     */
    public void updateFragment(int index,Fragment fragment, String fragmentTitle) {
        mFragments.remove(index);
        mFragments.add(index,fragment);
        mFragmentsTitles.add(fragmentTitle);

        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        //得到对应position的Fragment
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        //返回Fragment的数量
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //得到对应position的Fragment的title
        return mFragmentsTitles.get(position);
    }
}
