package com.wmj.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wmj.Adapter.PostInfoAdapter;
import com.wmj.bean.Person;
import com.wmj.bean.PostInfomation;
import com.wmj.secondhandmasket.R;
import com.wmj.util.HttpUtil;


import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * ��ʾ�ѷ����ӵ��б���棬ѡ���ѷ����ӡ��ؼ�ʱ������ת���˽��棬���б����ʽ��ʾ���û��Ѿ���������������Ϣ���û�����ѡ����ĳһ���ѷ�������ת����ϸ���沢���в���,
 * ���а����鿴���޸�ÿ��������Ʒ����ϸ��Ϣ���������ڴ˽���ѡ��ɾ����ť���ѷ����ӽ���ɾ������
 */
public class HavePostActivity extends BaseActivity implements
		OnItemClickListener {

	private ListView postListView;
	private ProgressDialog dialog;

	@SuppressWarnings("unused")
	private HadPostAsyncTask postAsyncTask;
	private int id;
	private List<PostInfomation> postList; // �����Ʒ��Ϣ��list�б�
	private PostInfoAdapter postAdapter; // ListView��������
	private Person person = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hadpost);
		postListView = (ListView) findViewById(R.id.post_list);
		postListView.setOnItemClickListener(this);
		Intent intent = this.getIntent();
		person = (Person) intent.getSerializableExtra("person");
		id = person.getId();
		new HadPostAsyncTask().execute(); // ʹ��AsyncTask�����첽��Ϣ����
	}

	// ListView��item����¼�������ת��ѡ�����ӵ���ϸ��Ϣ����
	@Override
	public void onItemClick(AdapterView<?> arg0, View arviewg1, int position,
			long arg3) {
		PostInfomation postInfo = postList.get(position);
		Intent intent = new Intent(this, DetailInfoActivity.class);
		intent.putExtra("postinfomation", postInfo);
		startActivity(intent);
	}

	/*
	 * �����첽��Ϣ������Ƶ�AsyncTask���ܸ����������̴߳����ʱ�Ĳ��������ܶ�UI���в��� ��1������һ���Ի����ڽ����ϵ�����ʾ���ڼ��ص���Ϣ
	 * ��2�������߳��н����������ƴ�Ӵ��������������н��� ��3���������������ݹ��������ݣ�������UI����
	 */
	private class HadPostAsyncTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(HavePostActivity.this);
			dialog.setTitle("");
			dialog.setMessage("Loading...");
			dialog.setCancelable(false); // ���ð���Back������ȡ���Ի���
			dialog.show();
		}

		// �����߳���ʵ��ƴ����Ϣ�����͸�������������õ����û��ѷ����ӵ���Ϣ
		@Override
		protected String doInBackground(String... params) {
			// String urlString =
			// "http://192.168.1.104:8080/SecondHandServer/servlet/ServletService";
			String result = "";
			String datastr;
			try {

				datastr = "Action=getpost" + "&id=" + id;
				result = HttpUtil.sendHttpRequests(datastr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// �����ɷ������˴�������Json��ʽ������,����PostInfomation������ӵ�List�б���,��ͨ��������ʹ������ListView����ʾ������
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			postAsyncTask = null;
			dialog.dismiss();
			try {
				if (TextUtils.isEmpty(result)) {
					Toast.makeText(HavePostActivity.this, "����û�з���������Ŷ",
							Toast.LENGTH_SHORT).show();
				} else {
					postList = new ArrayList<PostInfomation>();
					JSONArray jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						PostInfomation postInfo = new PostInfomation();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						int id = jsonObject.getInt("id");
						String shopname = jsonObject.getString("shopname");
						String descripe = jsonObject.getString("descripe");
						String category = jsonObject.getString("category");
						String price = jsonObject.getString("price");
						String username = jsonObject.getString("username");
						String phone = jsonObject.getString("phone");
						String time = jsonObject.getString("time");

						postInfo.setId(id);
						postInfo.setName(shopname);
						postInfo.setDescripe(descripe);
						postInfo.setCategory(category);
						postInfo.setPrice(price);
						postInfo.setPhone(phone);
						postInfo.setTime(time);
						postInfo.setUserName(username);

						postList.add(postInfo);

					}
					postAdapter = new PostInfoAdapter(HavePostActivity.this,
							R.layout.hadpost_item, postList);
					postListView.setAdapter(postAdapter);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

