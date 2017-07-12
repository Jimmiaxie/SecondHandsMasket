package com.wmj.activity;

import com.wmj.bean.Person;
import com.wmj.secondhandmasket.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 管理员登录系统的主界面
 * 
 * @author Administrator
 * 
 */
public class AdminActivity extends BaseActivity {
	private Button managerUserBtn;
	private Button managerRegisterBtn;
	private Person person = null;
	private Button exitBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin);
		managerUserBtn = (Button) findViewById(R.id.manager_btn);
		managerRegisterBtn = (Button) findViewById(R.id.manager_register_btn);
		Intent intent = this.getIntent();
		person = (Person) intent.getSerializableExtra("person");
		exitBtn = (Button) findViewById(R.id.exit_btn);
		exitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AdminActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
		});
		managerUserBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AdminActivity.this,
						AdminUserActiivty.class);
				intent.putExtra("person", person);
				startActivity(intent);
			}
		});
		managerRegisterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AdminActivity.this,
						AdminRegisterActivity.class);
				intent.putExtra("person", person);
				startActivity(intent);
			}
		});

	}
	// 退出程序对话框
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

}
