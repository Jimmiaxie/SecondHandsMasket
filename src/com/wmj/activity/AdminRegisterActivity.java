package com.wmj.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.wmj.Adapter.UserAdapter;
import com.wmj.bean.Person;
import com.wmj.secondhandmasket.R;
import com.wmj.task.Constant;
import com.wmj.util.HttpUtil;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.Toast;

/***
 * 管理员管理注册用户的界面，可以选择批准或不批准用户
 * 
 * @author Administrator
 * 
 */
public class AdminRegisterActivity extends BaseActivity {
	private ListView userlist_view;
	private ProgressDialog dialog;
	@SuppressWarnings("unused")
	private int id;
	private List<Person> userList; // 存放用户信息的list列表
	private UserAdapter userAdapter; // ListView的适配器
	private Person person = null;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_register);
		userlist_view = (ListView) findViewById(R.id.user_list);
		Intent intent = this.getIntent();
		person = (Person) intent.getSerializableExtra("person");
		id = person.getId();
		url = Constant.URL + "？Action=getuser";
		new GetUserAsyncTask().execute(url);
	}

	/*
	 * 给予异步消息处理机制的AsyncTask，能更方便在子线程处理耗时的操作并且能对UI进行操作 （1）给出一个对话框，在界面上弹出显示正在加载的信息
	 * （2）在子线程中将请求和数据拼接传给服务器并进行交互 （3）解析服务器传递过来的数据，并更新UI操作
	 */
	private class GetUserAsyncTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AdminRegisterActivity.this);
			dialog.setTitle("");
			dialog.setMessage("Loading...");
			dialog.setCancelable(false); // 设置按下Back键不能取消对话框
			dialog.show();
		}

		// 在子线程中发送请求给服务器，请求得到注册的用户
		@Override
		protected String doInBackground(String... params) {
			System.out.println("doInBackground---");
			String json = null;
			try {
				json = HttpUtil.sendHttpRequest(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception---" + e.getMessage());
			}
			return json;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// 解析由服务器端传过来的Json形式的数据,并以Person对象添加到List列表中,并通过适配器使数据在ListView中显示出来。
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			try {
				if (TextUtils.isEmpty(result)) {
					Toast.makeText(AdminRegisterActivity.this, "没有未批准的用户",
							Toast.LENGTH_SHORT).show();
				} else {
					userList = new ArrayList<Person>();
					JSONArray jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						Person person = new Person();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						int id = jsonObject.getInt("id");
						String username = jsonObject.getString("name");
						String userphone = jsonObject.getString("telephone");
						person.setId(id);
						person.setName(username);
						person.setPhone(userphone);
						userList.add(person);

					}
					userAdapter = new UserAdapter(AdminRegisterActivity.this,
							R.layout.admin_register_item, userList);
					userlist_view.setAdapter(userAdapter);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
