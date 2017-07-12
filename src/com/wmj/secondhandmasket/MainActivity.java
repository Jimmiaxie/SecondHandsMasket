package com.wmj.secondhandmasket;

import com.wmj.activity.ActivityCollector;
import com.wmj.activity.BaseActivity;
import com.wmj.fragment.AboutmeFragment;
import com.wmj.fragment.Tab1Fragment;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;

import android.os.Bundle;


import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 进入主程序的界面处理，处理两个fragment的切换
 * @author Administrator
 *
 */
public class MainActivity extends BaseActivity implements OnClickListener{

     private ImageView mHomeImg;
     private ImageView mMeImg;
	private LinearLayout home_page;
	private LinearLayout about_me;
	private Tab1Fragment tab1Fragment;             //定义首页Fragment
	private AboutmeFragment mFragment;                 //定义我Fragment
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		home_page=(LinearLayout)findViewById(R.id.home_page);
		about_me=(LinearLayout)findViewById(R.id.me);
		mHomeImg=(ImageView)findViewById(R.id.home_page_img);
		mMeImg=(ImageView)findViewById(R.id.me_img);
		home_page.setOnClickListener(this);
		about_me.setOnClickListener(this);
		initFragment(0);
	}
	// 退出程序对话框
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("退出程序");
				builder.setIcon(R.drawable.rate_star_big_on_holo_dark);
				builder.setMessage("要退出程序吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										ActivityCollector.finishAll();
									}
								}).setNegativeButton("取消", null).show();
			}
			return true;
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//初始化进入程序后要显示的fragment,默认先显示主页的fragment
	private void initFragment(int i) {
		FragmentManager manager=getFragmentManager();
		FragmentTransaction tarc=manager.beginTransaction();
		hideFragment(tarc);
		switch (i) {
		case 0:
			if (tab1Fragment == null) {
				tab1Fragment = new Tab1Fragment();
				tarc.add(R.id.content, tab1Fragment);
			}else{
				tarc.show(tab1Fragment);
			}
			break;
		case 1:
			if (mFragment == null) {
				mFragment = new AboutmeFragment();
			
				tarc.add(R.id.content, mFragment);
			}else{
				tarc.show(mFragment);
			}
			break;

		default:
			break;
		}
       tarc.commit();
		
	}
	//当点击了切换按钮后隐藏另一个fragment
	private void hideFragment(FragmentTransaction tarc) {
		// TODO Auto-generated method stub
		if (tab1Fragment != null) {
			tarc.hide(tab1Fragment);
		}
		if (mFragment != null) {
			tarc.hide(mFragment);
		}
	}
	@Override
	public void onClick(View v) {
		restartImage();
		switch (v.getId()) {
		case R.id.home_page:
			 mHomeImg.setImageResource(R.drawable.s3_plaza2);
			 initFragment(0);

			break;
		case R.id.me:
			mMeImg.setImageResource(R.drawable.s3_mine2);
			initFragment(1);
			break;
		
		default:
			break;
		}
		
	}
	private void restartImage() {
		// TODO Auto-generated method stub
		mHomeImg.setImageResource(R.drawable.s3_plaza);
		mMeImg.setImageResource(R.drawable.s3_mine);
	}
	
}
