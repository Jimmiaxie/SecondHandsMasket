package com.wmj.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

/*
 * 注册界面，要求输入用户名，密码，确认密码，邮箱，联系方式等信息，再进行判断是否合法并进行详细的操作
 */
public class RegisteredActivity extends BaseActivity implements OnClickListener {

	private EditText registName, registPassword, ConfirmPassw, registPhone;
	private Button butRegist;
	private ProgressDialog dialog;
	private RegisteAsyncTask registeAsyncTask; // 使用AsyncTask进行异步消息处理

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_registered);
		initView();
	}

	/*
	 * 初始化工作
	 */
	public void initView() {
		registName = (EditText) findViewById(R.id.regist_name);
		registPassword = (EditText) findViewById(R.id.regist_password);
		ConfirmPassw = (EditText) findViewById(R.id.regist_confirmpassw);
		registPhone = (EditText) findViewById(R.id.regist_telephone);
		butRegist = (Button) findViewById(R.id.regist_confirm);
		butRegist.setOnClickListener(this);

	}

	/*
	 * 判断输入是否合法。先得到用户输入信息，再判断注册所需信息是否为空，如果有空，就提示输入不能为空
	 */
	public boolean jugeContent() {
		boolean result = true;
		String mName = registName.getText().toString().trim();
		String mPassw = registPassword.getText().toString().trim();
		String mConfirmPassw = ConfirmPassw.getText().toString().trim();
		String mPhone = registPhone.getText().toString().trim();

		if (TextUtils.isEmpty(mName)) {
			Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!TextUtils.isEmpty(mName) && TextUtils.isEmpty(mPassw)) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!TextUtils.isEmpty(mName) && TextUtils.isEmpty(mPhone)) {
			Toast.makeText(this, "请输入联系方式", Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!mPassw.equals(mConfirmPassw)) {
			Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
			result = false;
		}
		if(mPhone.length() < 11 || mPhone.length() > 11) {
			Toast.makeText(this, "手机号码格式不对", Toast.LENGTH_SHORT).show();
			result = false;
	     }

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 先判断输入，输入合法时，再进行与服务器的交互操作
	 */
	@Override
	public void onClick(View arg0) {
		if (jugeContent()) { // 输入符合输入要求
			new RegisteAsyncTask().execute();
		}

	}

	/*
	 * 给予异步消息处理机制的AsyncTask，能更方便在子线程处理耗时的操作并且能对UI进行操作
	 * （1）给出一个对话框，在界面上弹出显示"登录中,请稍后.."的信息 （2）在子线程中将请求和数据拼接传给服务器并进行交互
	 * （3）根据服务器返回的数据判断是否修改信息成功，登录失败以Toast形式告知用户，成功则判断用户是否选择记住密码，并跳转到主界面。
	 */
	private class RegisteAsyncTask extends AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(RegisteredActivity.this);
			dialog.setTitle("提示");
			dialog.setMessage("注册中,请稍后..");
			dialog.setCancelable(true);
			dialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (registeAsyncTask != null) {
						registeAsyncTask.cancel(true);
						registeAsyncTask = null;
						Toast.makeText(getApplicationContext(), "取消注册", Toast.LENGTH_SHORT).show();
					}
				}
			});
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String datastr;
			try {
				datastr = "Action=register"
						+ "&username="
						+ URLEncoder.encode(registName.getText().toString(),
								"utf-8")
						+ "&psd="
						+ URLEncoder.encode(
								registPassword.getText().toString(), "utf-8")
						+ "&telephone="
						+ URLEncoder.encode(registPhone.getText().toString(),
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

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			registeAsyncTask = null;
			dialog.dismiss();
			if (result != null && result.trim().equals("1")) {
				// 注册成功就跳转到登录界面
				Toast.makeText(getApplicationContext(), "注册成功,请等待管理员批准",  Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(intent);

			} else if (result != null && result.trim().equals("2")) {
				Toast.makeText(getApplicationContext(), "该联系方式已注册", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "该用户名已经被占用",  Toast.LENGTH_SHORT).show();
			}
		}
	}
}