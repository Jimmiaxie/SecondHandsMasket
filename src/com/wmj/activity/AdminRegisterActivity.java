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
 * ����Ա����ע���û��Ľ��棬����ѡ����׼����׼�û�
 * 
 * @author Administrator
 * 
 */
public class AdminRegisterActivity extends BaseActivity {
	private ListView userlist_view;
	private ProgressDialog dialog;
	@SuppressWarnings("unused")
	private int id;
	private List<Person> userList; // ����û���Ϣ��list�б�
	private UserAdapter userAdapter; // ListView��������
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
		url = Constant.URL + "��Action=getuser";
		new GetUserAsyncTask().execute(url);
	}

	/*
	 * �����첽��Ϣ������Ƶ�AsyncTask���ܸ����������̴߳����ʱ�Ĳ��������ܶ�UI���в��� ��1������һ���Ի����ڽ����ϵ�����ʾ���ڼ��ص���Ϣ
	 * ��2�������߳��н����������ƴ�Ӵ��������������н��� ��3���������������ݹ��������ݣ�������UI����
	 */
	private class GetUserAsyncTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AdminRegisterActivity.this);
			dialog.setTitle("");
			dialog.setMessage("Loading...");
			dialog.setCancelable(false); // ���ð���Back������ȡ���Ի���
			dialog.show();
		}

		// �����߳��з��������������������õ�ע����û�
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

		// �����ɷ������˴�������Json��ʽ������,����Person������ӵ�List�б���,��ͨ��������ʹ������ListView����ʾ������
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			try {
				if (TextUtils.isEmpty(result)) {
					Toast.makeText(AdminRegisterActivity.this, "û��δ��׼���û�",
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
