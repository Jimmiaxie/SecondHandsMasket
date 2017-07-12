package com.wmj.startPager;

import com.wmj.secondhandmasket.MainActivity;
import com.wmj.secondhandmasket.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
/*
 * 欢迎页activty类
 */
public class WeclomePage extends Activity {
	private static final int TIME = 2000;
	private static final int WELCOME = 1000;
	private static final int VIEWPAGE = 1001;
	private boolean play = false;
	//通过Handler处理打开程序启动的页面
	private Handler hander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WELCOME:
				goWelcome();
				break;

			case VIEWPAGE:
				goViewpage();
				break;
			}
		}

	};
    @Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   /**全屏设置，隐藏窗口所有装饰**/
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);
       /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
       requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		init();
	}
    private void init() {
    	//判断是否是第一次进入到该主程序
		SharedPreferences shared = getSharedPreferences("welcome", MODE_PRIVATE);
		play = shared.getBoolean("play", true);
		//如果有记录的话则直接进入欢迎页
		if (!play) {
			hander.sendEmptyMessageDelayed(WELCOME, TIME);
		} else {
			//如果是第一次的话则进入到引导页并保存记录
			hander.sendEmptyMessageDelayed(VIEWPAGE, TIME);
			Editor editor = shared.edit();
			editor.putBoolean("play", false);
			editor.commit();
		}

	}
    //程序启动直接通过欢迎页进入主程序
    private void goWelcome() {
		Intent i = new Intent(WeclomePage.this, MainActivity.class);
		startActivity(i);
		finish();

	}
   //第一次安装此程序进入引导页后再进入到主程序
	private void goViewpage() {
		Intent i = new Intent(WeclomePage.this, GuidePage.class);
		startActivity(i);
		finish();

	}
}
