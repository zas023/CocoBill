package com.copasso.cocobill.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityManagerUtils {
	
	/** 记录处于前台的Activity */
	public static Activity mForegroundActivity = null;
	/** 记录所有活动的Activity */
	public static final List<Activity> mActivities = new LinkedList<Activity>();

	
	/** 获取当前处于栈顶的activity，无论其是否处于前台 */
	public static Activity getCurrentActivity() {
		List<Activity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<Activity>(mActivities);
		}
		if (copy.size() > 0) {
			return copy.get(copy.size() - 1);
		}
		return null;
	}

	/** 是否有启动的Activity */
	public static boolean hasActivity() {
		return mActivities.size() > 0;
	}

	/** 获取当前处于前台的activity */
	public static Activity getForegroundActivity() {
		return mForegroundActivity;
	}

	/** 关闭所有Activity，除了参数传递的Activity */
	public static void finishAll(Class except) {
		List<Activity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<Activity>(mActivities);
		}
		for (Activity activity : copy) {
			if (activity.getClass() != except)
				activity.finish();
		}
	}

	/** 关闭所有Activity */
	public static void finishAll() {
		List<Activity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<Activity>(mActivities);
		}
		for (Activity activity : copy) {
			activity.finish();
		}
	}
	
	

}
