package com.wmj.fragment;

import java.io.File;
import java.io.IOException;
import java.util.*;
import com.wmj.activity.ActivityCollector;
import com.wmj.activity.ChangePasswordActivity;
import com.wmj.activity.HavePostActivity;
import com.wmj.activity.LoginActivity;
import com.wmj.activity.PublishPostActivity;

import com.wmj.bean.Person;
import com.wmj.secondhandmasket.MainActivity;
import com.wmj.secondhandmasket.R;
import com.wmj.task.MyApplication;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/*
 * 关于我的Fragment，其中包括
 * 1.未登录状态
 * （1）在此界面进行操作都会跳转到登录界面，要求进行登录
 * （2）不显示退出当前账号的按钮选项
 * 2.已登录状态，可以进行以下操作
 * （1）显示当前用户的用户名
 * （2）发布帖子.图片可以包括本地相册中获取或直接现拍
 * （3）查看该用户已发帖子
 * （4）修改密码
 * （5）退出当前账号，点击按钮后退出当前账号
 */
public class AboutmeFragment extends Fragment implements OnItemClickListener,
		OnClickListener {
	private ListView listView;
	private Button exitButton, loginButton;
	private ImageView imagePicture;
	private TextView tvName;
	private String string;
	private List<Map<String, Object>> data; // 数据源
	private Map<String, Object> item;
	private boolean isLogin = false; // 用来判断是否已经登录
	private int images[] = { R.drawable.myself0, R.drawable.myself1,
			R.drawable.myself4 };
	private String description[] = { "发布帖子", "已发帖子", "修改密码" };
	Person person = null;
	private MyApplication myApplication;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_aboutme, container, false);
		myApplication = (MyApplication) getActivity().getApplicationContext();
	    person = (Person) myApplication.userMap.get("person");
		if (person != null) {
			string = person.getName();
		}
		listView = (ListView) view.findViewById(R.id.myself_listview);
		exitButton = (Button) view.findViewById(R.id.myself_exit);
		loginButton = (Button) view.findViewById(R.id.myself_login);
		imagePicture = (ImageView) view.findViewById(R.id.myself_portrait);
		tvName = (TextView) view.findViewById(R.id.myself_name);
	
		if (!(string == null || TextUtils.isEmpty(string))) { // 说明已经登录了
			loginButton.setVisibility(View.INVISIBLE);
			exitButton.setVisibility(View.VISIBLE);
			tvName.setVisibility(View.VISIBLE);
			tvName.setText(string);
			isLogin = true;
		}
		initData();
		listView.setOnItemClickListener(this);
		exitButton.setOnClickListener(this);
		imagePicture.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		return view;
	}

	// 初始化ListView的数据并显示在界面上
	public void initData() {
		// 生成数据源data
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			item = new HashMap<String, Object>();
			item.put("photo", images[i]);
			item.put("description", description[i]);
			item.put("other", R.drawable.toright);
			data.add(item);
		}
		// 生成简单适配器
		SimpleAdapter listAdapter = new SimpleAdapter(getActivity()
				.getApplicationContext(), data, R.layout.aboutme_item,
				new String[] { "photo", "description" }, new int[] {
						R.id.myself_imageview1, R.id.myself_tv });

		// 设置适配器
		listView.setAdapter(listAdapter);
	}

	// 先判断是否已经登录，如果已经登录，根据选择进行页面跳转，如果没有登录，那就跳转到登录界面提醒用户登录
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (isLogin) { // 已经登录状态
			switch (position) {
			case 0: // 发布帖子
				Intent intent0 = new Intent(getActivity(),
						PublishPostActivity.class);
				intent0.putExtra("person", person);
				startActivity(intent0);
				break;
			case 1: // 查看已发帖子
				Intent intent1 = new Intent(getActivity(),
						HavePostActivity.class);
				intent1.putExtra("person", person);
				startActivity(intent1);
				break;

			case 2: // 修改密码
				Intent intent2 = new Intent(getActivity(),
						ChangePasswordActivity.class);
				intent2.putExtra("person", person);
				startActivity(intent2);
				break;
			default:
				break;
			}
		} else { // 未登录状态
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		}
	}

	// 先判断是否已经登录，若已经登录，那就进行响应的操作，否则跳到登录界面
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myself_exit: // 点击退出按钮
			ActivityCollector.finishAll();
			Intent intent = new Intent(getActivity(), MainActivity.class);
			myApplication.userMap.remove("person");
			startActivity(intent);
			break;
		case R.id.myself_portrait: // 点击头像 //************
			if (isLogin) { // 登录状态
				File outputImage = new File(
						Environment.getExternalStorageDirectory(),
						"SecondHand.jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else { // 未登录状态
				Intent intent1 = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent1);
			}
			break;
		case R.id.myself_login:
			Intent intent2 = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
}
