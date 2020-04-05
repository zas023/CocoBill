package com.thmub.app.billkeeper.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.thmub.app.billkeeper.ui.fragment.CategoryPageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enosh on 2020-03-11
 * Github: https://github.com/zas023
 */
public class CategoryFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles;

    public CategoryFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    public void addFragment(Fragment fragment, String title) {
        addFragment(fragment);
        titles.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (fragments == null)
            return null;
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
