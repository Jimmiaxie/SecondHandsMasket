package com.wmj.activity;
import android.app.Activity;
import android.os.Bundle;

/**
 * 定义一个BaseActivity继承于Activity，让每一个活动都继承BaseActivity，为的是方便管理,让每个
 * 继承BaseActivity的Activity都在创建的时候添加到ActivityCollector的activities（一个List）中，
 * 在摧毁时都能从activities中移除掉
 */
public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}

