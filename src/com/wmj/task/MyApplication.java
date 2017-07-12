package com.wmj.task;
import java.util.concurrent.ConcurrentHashMap;
import android.app.Application;
/*
 * ȫ�ֱ����������洢�Ѿ���¼���û�
 */
public class MyApplication extends Application{
	public ConcurrentHashMap<String, Object> userMap;
	private static MyApplication instance;
	public MyApplication() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		userMap = new ConcurrentHashMap<String, Object>();
	}

	public synchronized static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}


}
