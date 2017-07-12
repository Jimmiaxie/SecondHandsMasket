package com.wmj.activity;

import java.util.*;
/**
 * �����������List�ݴ����ṩ�˷����ֱ���Ӻ��Ƴ��������ʵ����ʱ��ص��˳�����
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
