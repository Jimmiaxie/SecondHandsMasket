package com.wmj.activity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.wmj.bean.Person;
import com.wmj.secondhandmasket.MainActivity;
import com.wmj.secondhandmasket.R;
import com.wmj.task.MyApplication;
import com.wmj.util.HttpUtil;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
/*
 * 登陆界面，通过是否选择记住密码操作来记录用户名和密码，并通过与服务器信息进行匹配是否属于合法登陆
 */
import android.widget.Toast;

/**
 * 用户登录界面,通过SharedPreferences中的数据先判断之前是否已经标记登录过，如果有则显示在界面上，
 * 登录时先判断是否选择记住密码，登陆成功时就将信息保存并跳转到主界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private Button butLogin, butRegiste;
	private EditText edtName, edtPassword;
	private SharedPreferences share; // 用来存储用户名和密码和是否记住密码
	private CheckBox isRemember;
	private MyApplication myApplication;
	private ProgressDialog dialog;
	private LoginAsyncTask loginAsyncTask; // 使用AsyncTask进行异步消息处理
	@SuppressWarnings("unused")
	private List<Person> personList; // 存放商品信息的list列表
	int id,isAdmin,isApprove;
	String name, password, telephone;
	private Person person;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		myApplication = (MyApplication) this.getApplicationContext();
		share = PreferenceManager.getDefaultSharedPreferences(this); // 得到实例
		initView();
	}

	// 初始化工作
	public void initView() {
		butLogin = (Button) findViewById(R.id.login_login);
		butRegiste = (Button) findViewById(R.id.login_registered);
		edtName = (EditText) findViewById(R.id.login_name);
		edtPassword = (EditText) findViewById(R.id.login_password);
		isRemember = (CheckBox) findViewById(R.id.login_isremember);
		butLogin.setOnClickListener(this);
		butRegiste.setOnClickListener(this);
		boolean isremember = share.getBoolean("remember_infomation", false);
		if (isremember) { // 之前有保存用户名和密码
			edtName.setText(share.getString("name", ""));
			edtPassword.setText(share.getString("password", ""));
			isRemember.setChecked(true);
		}
	}

	/*
	 * （1）选择登陆按钮时，与服务器信息进行匹配，成功就判断当前用户信息是否存储并跳转页面，否则提示信息 （2）选择注册按钮时，跳转到注册按钮
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_login:

			String loginname = edtName.getText().toString().trim();
			String loginpassword = edtPassword.getText().toString().trim();

			if (TextUtils.isEmpty(loginname)) {
				Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			} else if (!TextUtils.isEmpty(loginname)
					&& TextUtils.isEmpty(loginpassword)) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			} else {
				// 与服务器数据库信息作比较
				new LoginAsyncTask().execute();
			}
			break;
		case R.id.login_registered:
			Intent intent = new Intent(this, RegisteredActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/*
	 * 给予异步消息处理机制的AsyncTask，能更方便在子线程处理耗时的操作并且能对UI进行操作
	 * （1）给出一个对话框，在界面上弹出显示"登录中,请稍后.."的信息 （2）在子线程中将请求和数据拼接传给服务器并进行交互
	 * （3）根据服务器返回的数据判断是否修改信息成功，登录失败以Toast形式告知用户，成功则判断用户是否选择记住密码，并跳转到主界面。
	 */
	private class LoginAsyncTask extends AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setTitle("提示");
			dialog.setMessage("登录中,请稍后..");
			dialog.setCancelable(true);
			dialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (loginAsyncTask != null) {
						loginAsyncTask.cancel(true);
						loginAsyncTask = null;
						Toast.makeText(getApplicationContext(), "登录被取消", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
			dialog.show();
		}

		// 在子线程中实现拼接消息并发送给服务器，请求登录
		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String datastr;
			try {
				datastr = "Action=login"
						+ "&username="
						+ URLEncoder.encode(edtName.getText().toString(),
								"utf-8")
						+ "&psd="
						+ URLEncoder.encode(edtPassword.getText().toString(),
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

		// 根据服务器返回的数据判断是否登录成功，登录失败以Toast形式告知用户，成功就跳到主页面。
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			loginAsyncTask = null;
			dialog.dismiss();
			if (result != null && result.trim().equals("2")) {
				Toast.makeText(LoginActivity.this, "密码错误",Toast.LENGTH_SHORT).show();
			} else if (result != null && result.trim().equals("3")) {
				Toast.makeText(LoginActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
			} else if (result == null) {
				Toast.makeText(LoginActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
			} 
			else {
				personList = new ArrayList<Person>();
				JSONArray jsonArray;

				try {
					jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						person = new Person();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						int id = jsonObject.getInt("id");
						name = jsonObject.getString("name");
						password = jsonObject.getString("password");
						telephone = jsonObject.getString("telephone");
						isAdmin=jsonObject.getInt("isadmin");
						isApprove=jsonObject.getInt("isapprove");
						person.setId(id);
						person.setName(name);
						person.setPassword(password);
						person.setPhone(telephone);
						person.setIsAdmin(isAdmin);
						person.setIsApprove(isApprove);
						myApplication.userMap.put("person", person);
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

				SharedPreferences.Editor editor = share.edit();
				if (isRemember.isChecked()) // 选择记住密码
				{
					editor.putBoolean("remember_infomation", true);
					editor.putInt("id", id);
					editor.putString("name", name);
					editor.putString("password", password);
					editor.putString("telephone", telephone);
					editor.putInt("isAdmin", isAdmin);
					editor.commit();
				} else {
					editor.clear();
				}

				// 登录成功进行跳转
				if(isAdmin==0&&isApprove==1){
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("person", person);
				startActivity(intent);
				}else if(isAdmin==1){
				    Intent intent = new Intent(getApplicationContext(),
							AdminActivity.class);
					intent.putExtra("person", person);
					startActivity(intent);
				}else{
					Toast.makeText(LoginActivity.this, "请等待管理员批准....", 5000).show();
				}
			}
		}
	}
}
