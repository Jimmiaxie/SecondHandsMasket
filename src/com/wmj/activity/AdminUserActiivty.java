package com.wmj.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import android.widget.ListView;
import android.widget.Toast;


import com.wmj.Adapter.UserAgreeAdapter;

import com.wmj.bean.Person;
import com.wmj.secondhandmasket.R;
import com.wmj.task.Constant;
import com.wmj.util.HttpUtil;
/**
 * ����Ա�����û��Ľ��棬���Եõ��Ѿ��Ǳ�ϵͳ���û������Զ��û�����ɾ��������Ҳ���Զ��û����ӽ���ɾ������
 * @author Administrator
 *
 */
public class AdminUserActiivty extends BaseActivity{
	private ListView useragreelist_view;
	private ProgressDialog dialog;
	@SuppressWarnings("unused")
	private int id;
	private List<Person> userList; // ����û���Ϣ��list�б�
	private UserAgreeAdapter userAdapter; // ListView��������
	private Person person = null;
	 String url;
	  protected void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.admin_user);
	    	useragreelist_view = (ListView) findViewById(R.id.user_agree_list);
	    	Intent intent = this.getIntent();
			person = (Person) intent.getSerializableExtra("person");
			id = person.getId();
			url=Constant.URL+"��Action=getagreeuser";
			new GetUserAsyncTask().execute(url);
	    
	    }

		/*
		 * �����첽��Ϣ������Ƶ�AsyncTask���ܸ����������̴߳����ʱ�Ĳ��������ܶ�UI���в��� ��1������һ���Ի����ڽ����ϵ�����ʾ���ڼ��ص���Ϣ
		 * ��2�������߳��н����������ƴ�Ӵ��������������н��� ��3���������������ݹ��������ݣ�������UI����
		 */
		private class GetUserAsyncTask extends AsyncTask<String, Integer, String> {

			@Override
			protected void onPreExecute() {
				dialog = new ProgressDialog(AdminUserActiivty.this);
				dialog.setTitle("");
				dialog.setMessage("Loading...");
				dialog.setCancelable(false); // ���ð���Back������ȡ���Ի���
				dialog.show();
			}

			// �����߳��з��������������������õ��û�����Ϣ
			@Override
			protected String doInBackground(String... params) {
			
				System.out.println("doInBackground---");
				String json=null;
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
					if(TextUtils.isEmpty(result)) {
						Toast.makeText(AdminUserActiivty.this, "û���û�",
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
						userAdapter = new UserAgreeAdapter(AdminUserActiivty.this,
								R.layout.admin_user_item, userList);
						useragreelist_view.setAdapter(userAdapter);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
}
