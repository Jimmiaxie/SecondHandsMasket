package com.wmj.activity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.wmj.bean.Person;
import com.wmj.secondhandmasket.R;
import com.wmj.util.HttpUtil;



import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 更改用户密码界面,要求用户输入原有密码、新密码、确认密码等信息。先进行输入判断，再进行信息合法性判断，
 * 如果输入合法，在提交给服务器进行数据更改
 */
public class ChangePasswordActivity extends BaseActivity {
	private EditText currentPassw, newPassw, confirmPassw;
	private Button changePassw;
	private int id;
	private ProgressDialog dialog;
	private ChangePasswAsyncTask changePasswAsyncTask;
	private Person person = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_changepassword);
		Intent intent = this.getIntent();
		person = (Person) intent.getSerializableExtra("person");
		id = person.getId();
		initView();
	}

	/*
	 * 初始化工作
	 */
	public void initView() {
		currentPassw = (EditText) findViewById(R.id.change_passw);
		newPassw = (EditText) findViewById(R.id.change_newpassw);
		confirmPassw = (EditText) findViewById(R.id.change_confirmpassw);
		changePassw = (Button) findViewById(R.id.change_butconfirm);
		changePassw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changePassword();
			}
		});
	}

	/*
	 * 1.判断要求输入的三个值是否为空或空格 2.判断新密码与确认密码的输入是否一致 3.再判断信息是否正确，正确则修改密码成功，否则失败
	 */
	public void changePassword() {
		String curPassw = currentPassw.getText().toString().trim();
		String newPassword = newPassw.getText().toString().trim();
		String conPassw = confirmPassw.getText().toString();
		if (TextUtils.isEmpty(curPassw) || TextUtils.isEmpty(conPassw)
				|| TextUtils.isEmpty(newPassword)) {
			Toast.makeText(this, "修改密码所需信息均不能为空", Toast.LENGTH_SHORT).show();
		} else {
			if (!newPassword.equals(conPassw)) {
				Toast.makeText(this, "确认密码与新密码不一致，请重新确认", Toast.LENGTH_SHORT)
						.show();
			} else {
				new ChangePasswAsyncTask().execute();
			}
		}
	}

	/*
	 * 给予异步消息处理机制的AsyncTask，能更方便在子线程处理耗时的操作并且能对UI进行操作
	 * （1）给出一个对话框，在界面上弹出显示"更改密码中,请稍后.."的信息 （2）在子线程中将请求和数据拼接传给服务器并进行交互
	 * （3）根据服务器返回的数据判断是否修改信息成功并以Toast形式告知用户。
	 */
	private class ChangePasswAsyncTask extends
			AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ChangePasswordActivity.this);
			dialog.setTitle("提示");
			dialog.setMessage("更改密码中,请稍后..");
			dialog.setCancelable(true);
			dialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (changePasswAsyncTask != null) {
						changePasswAsyncTask.cancel(true);
						changePasswAsyncTask = null;
						Toast.makeText(getApplicationContext(), "修改密码被取消", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
			dialog.show();
		}

		// 在子线程中实现拼接消息并发送给服务器，请求修改密码
		@Override
		protected String doInBackground(String... params) {
		    String result="";
			String datastr;
			try {
				datastr = "Action=changepassw"
						+ "&id="
						+ id
						+ "&psd="
						+ URLEncoder.encode(currentPassw.getText().toString(),
								"utf-8")
						+ "&newpassword="
						+ URLEncoder.encode(newPassw.getText().toString(),
								"utf-8");
				result = HttpUtil.sendHttpRequests(datastr);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// 根据服务器返回的数据判断是否修改密码成功并以Toast形式告知用户。
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			changePasswAsyncTask = null;
			dialog.dismiss();
			if (result != null && result.trim().equals("1")) {
				// 注册成功就跳转到登录界面
				Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "原密码输入错误", Toast.LENGTH_SHORT).show();

			}
		}
	}
}
