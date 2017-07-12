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
 * ��ӭҳactivty��
 */
public class WeclomePage extends Activity {
	private static final int TIME = 2000;
	private static final int WELCOME = 1000;
	private static final int VIEWPAGE = 1001;
	private boolean play = false;
	//ͨ��Handler����򿪳���������ҳ��
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
	   /**ȫ�����ã����ش�������װ��**/
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);
       /**����������View�ģ����Դ������е����β��ֱ����غ������Ȼ��Ч,��Ҫȥ������**/
       requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		init();
	}
    private void init() {
    	//�ж��Ƿ��ǵ�һ�ν��뵽��������
		SharedPreferences shared = getSharedPreferences("welcome", MODE_PRIVATE);
		play = shared.getBoolean("play", true);
		//����м�¼�Ļ���ֱ�ӽ��뻶ӭҳ
		if (!play) {
			hander.sendEmptyMessageDelayed(WELCOME, TIME);
		} else {
			//����ǵ�һ�εĻ�����뵽����ҳ�������¼
			hander.sendEmptyMessageDelayed(VIEWPAGE, TIME);
			Editor editor = shared.edit();
			editor.putBoolean("play", false);
			editor.commit();
		}

	}
    //��������ֱ��ͨ����ӭҳ����������
    private void goWelcome() {
		Intent i = new Intent(WeclomePage.this, MainActivity.class);
		startActivity(i);
		finish();

	}
   //��һ�ΰ�װ�˳����������ҳ���ٽ��뵽������
	private void goViewpage() {
		Intent i = new Intent(WeclomePage.this, GuidePage.class);
		startActivity(i);
		finish();

	}
}
