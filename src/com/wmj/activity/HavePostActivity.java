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
 * 显示已发帖子的列表界面，选择“已发帖子”控件时，就跳转到此界面，以列表的形式显示该用户已经发布过的帖子信息，用户可以选择点击某一个已发帖子跳转到详细界面并进行操作,
 * 其中包括查看、修改每个出售商品的详细信息，还可以在此界面选择删除按钮对已发帖子进行删除操作
 */
public class HavePostActivity extends BaseActivity implements
		OnItemClickListener {

	private ListView postListView;
	private ProgressDialog dialog;

	@SuppressWarnings("unused")
	private HadPostAsyncTask postAsyncTask;
	private int id;
	private List<PostInfomation> postList; // 存放商品信息的list列表
	private PostInfoAdapter postAdapter; // ListView的适配器
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
		new HadPostAsyncTask().execute(); // 使用AsyncTask进行异步消息处理
	}

	// ListView的item点击事件处理，跳转到选择帖子的详细信息界面
	@Override
	public void onItemClick(AdapterView<?> arg0, View arviewg1, int position,
			long arg3) {
		PostInfomation postInfo = postList.get(position);
		Intent intent = new Intent(this, DetailInfoActivity.class);
		intent.putExtra("postinfomation", postInfo);
		startActivity(intent);
	}

	/*
	 * 给予异步消息处理机制的AsyncTask，能更方便在子线程处理耗时的操作并且能对UI进行操作 （1）给出一个对话框，在界面上弹出显示正在加载的信息
	 * （2）在子线程中将请求和数据拼接传给服务器并进行交互 （3）解析服务器传递过来的数据，并更新UI操作
	 */
	private class HadPostAsyncTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(HavePostActivity.this);
			dialog.setTitle("");
			dialog.setMessage("Loading...");
			dialog.setCancelable(false); // 设置按下Back键不能取消对话框
			dialog.show();
		}

		// 在子线程中实现拼接消息并发送给服务器，请求得到该用户已发帖子的信息
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

		// 解析由服务器端传过来的Json形式的数据,并以PostInfomation对象添加到List列表中,并通过适配器使数据在ListView中显示出来。
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			postAsyncTask = null;
			dialog.dismiss();
			try {
				if (TextUtils.isEmpty(result)) {
					Toast.makeText(HavePostActivity.this, "您还没有发布过帖子哦",
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

