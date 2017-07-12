package com.wmj.activity;
import android.app.Activity;
import android.os.Bundle;

/**
 * ����һ��BaseActivity�̳���Activity����ÿһ������̳�BaseActivity��Ϊ���Ƿ������,��ÿ��
 * �̳�BaseActivity��Activity���ڴ�����ʱ����ӵ�ActivityCollector��activities��һ��List���У�
 * �ڴݻ�ʱ���ܴ�activities���Ƴ���
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

