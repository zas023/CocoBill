package com.copasso.cocobill.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * AccountFragment
 */
public class MonthAccountAdapter extends PagerAdapter {
	private List<View> imageViews ;
	private String[] mTitles ;
    public MonthAccountAdapter(List<View> imageViews){
    	this.imageViews = imageViews;
    }
    public MonthAccountAdapter(List<View> imageViews, String[] mTitles){
    	this.imageViews = imageViews;
    	this.mTitles= mTitles;
    }
	@Override
	public int getCount() {
		return imageViews.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(imageViews.get(position));
		return imageViews.get(position);
	}
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	container.removeView(imageViews.get(position));
    }
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

}
