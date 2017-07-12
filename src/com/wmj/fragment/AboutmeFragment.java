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
 * �����ҵ�Fragment�����а���
 * 1.δ��¼״̬
 * ��1���ڴ˽�����в���������ת����¼���棬Ҫ����е�¼
 * ��2������ʾ�˳���ǰ�˺ŵİ�ťѡ��
 * 2.�ѵ�¼״̬�����Խ������²���
 * ��1����ʾ��ǰ�û����û���
 * ��2����������.ͼƬ���԰�����������л�ȡ��ֱ������
 * ��3���鿴���û��ѷ�����
 * ��4���޸�����
 * ��5���˳���ǰ�˺ţ������ť���˳���ǰ�˺�
 */
public class AboutmeFragment extends Fragment implements OnItemClickListener,
		OnClickListener {
	private ListView listView;
	private Button exitButton, loginButton;
	private ImageView imagePicture;
	private TextView tvName;
	private String string;
	private List<Map<String, Object>> data; // ����Դ
	private Map<String, Object> item;
	private boolean isLogin = false; // �����ж��Ƿ��Ѿ���¼
	private int images[] = { R.drawable.myself0, R.drawable.myself1,
			R.drawable.myself4 };
	private String description[] = { "��������", "�ѷ�����", "�޸�����" };
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
	
		if (!(string == null || TextUtils.isEmpty(string))) { // ˵���Ѿ���¼��
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

	// ��ʼ��ListView�����ݲ���ʾ�ڽ�����
	public void initData() {
		// ��������Դdata
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			item = new HashMap<String, Object>();
			item.put("photo", images[i]);
			item.put("description", description[i]);
			item.put("other", R.drawable.toright);
			data.add(item);
		}
		// ���ɼ�������
		SimpleAdapter listAdapter = new SimpleAdapter(getActivity()
				.getApplicationContext(), data, R.layout.aboutme_item,
				new String[] { "photo", "description" }, new int[] {
						R.id.myself_imageview1, R.id.myself_tv });

		// ����������
		listView.setAdapter(listAdapter);
	}

	// ���ж��Ƿ��Ѿ���¼������Ѿ���¼������ѡ�����ҳ����ת�����û�е�¼���Ǿ���ת����¼���������û���¼
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (isLogin) { // �Ѿ���¼״̬
			switch (position) {
			case 0: // ��������
				Intent intent0 = new Intent(getActivity(),
						PublishPostActivity.class);
				intent0.putExtra("person", person);
				startActivity(intent0);
				break;
			case 1: // �鿴�ѷ�����
				Intent intent1 = new Intent(getActivity(),
						HavePostActivity.class);
				intent1.putExtra("person", person);
				startActivity(intent1);
				break;

			case 2: // �޸�����
				Intent intent2 = new Intent(getActivity(),
						ChangePasswordActivity.class);
				intent2.putExtra("person", person);
				startActivity(intent2);
				break;
			default:
				break;
			}
		} else { // δ��¼״̬
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		}
	}

	// ���ж��Ƿ��Ѿ���¼�����Ѿ���¼���Ǿͽ�����Ӧ�Ĳ���������������¼����
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myself_exit: // ����˳���ť
			ActivityCollector.finishAll();
			Intent intent = new Intent(getActivity(), MainActivity.class);
			myApplication.userMap.remove("person");
			startActivity(intent);
			break;
		case R.id.myself_portrait: // ���ͷ�� //************
			if (isLogin) { // ��¼״̬
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
			} else { // δ��¼״̬
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
