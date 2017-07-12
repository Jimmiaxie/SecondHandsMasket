package com.wmj.task;
import java.util.concurrent.ConcurrentHashMap;
import android.app.Application;
/*
 * 全局变量，用来存储已经登录的用户
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
