package com.wmj.activity;

import java.util.*;
/**
 * 活动管理器，用List暂存活动，提供了方法分别添加和移除活动，可以实现随时随地地退出程序
 */
import android.app.Activity;

public class ActivityCollector {
	public static List<Activity> activities = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	public static void finishAll() {
		for (Activity activity : activities) {
			activity.finish();
		}
	}
}
