package com.wmj.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.wmj.bean.PostInfomation;
import com.wmj.secondhandmasket.R;
import com.wmj.util.HttpUtil;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * ÿ���ѷ����ӵ���ϸ��Ϣ���棬�û����Խ��в鿴ÿһ���ѷ����ӵ���ϸ��Ϣ�����ҿ��Խ���һ�����ݵ��޸ģ�
 * ������Ʒ���ơ���Ʒ��������Ʒ��Ǯ���������ӵ��û������û���ϵ��ʽ���������ӵ�ʱ�䡣 �޸ĺ���������ݿ��ύ�ѽ����޸ĵ���Ϣ��
 */
public class DetailInfoActivity extends BaseActivity implements OnClickListener {

	private PostInfomation postInfo = null; // ����һ��PostInfomationʵ��
	private EditText shopName, shopDescripe, shopPrice, UserName, userPhone,publishTime;
	private Button changeInfo;
	private Spinner shopCategoty;
	private String newShopType, newShopName, newShopPrice, newShopDescripe,
			newUserPhone; // ����������ʾ�޸ĺ����Ϣ�Ĳ���
	private String[] types = new String[] { "���Ӳ�Ʒ", "����", "������Ʒ", "�Ҿ�", "ͼ��",
			"����" }; // Spinner������Դ
	private List<EditText> viewlist = new ArrayList<EditText>(); // ������Ÿ��ֿ����޸ĵ�EditText�ؼ�
	private ProgressDialog dialog;
	private ChangeInfoAsyncTask changInfoAsyncTask; // ʹ��AsyncTask�����첽��Ϣ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_detailedinfo);
		Intent intent = getIntent();
		postInfo = (PostInfomation) intent
				.getSerializableExtra("postinfomation");
		initView();
		noEditView();
	}

	// ��ʼ��������ͼ,����Spinner��itemѡ���¼�������������
	public void initView() {
		shopName = (EditText) findViewById(R.id.detail_shopname);
		shopDescripe = (EditText) findViewById(R.id.detail_descripe);
		shopPrice = (EditText) findViewById(R.id.detail_price);
		UserName = (EditText) findViewById(R.id.detail_username);
		userPhone = (EditText) findViewById(R.id.detail_phone);
		publishTime = (EditText) findViewById(R.id.detail_time);
		shopCategoty = (Spinner) findViewById(R.id.detail_category);
		shopCategoty.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				newShopType = types[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		changeInfo = (Button) findViewById(R.id.detail_button);
		changeInfo.setOnClickListener(this);

		// ����Spinner��������
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, types);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shopCategoty.setAdapter(typeAdapter);

		// �����Ա�ɱ༭��EditText��ӵ�list��(�����û����ͷ���ʱ��)
		viewlist.add(shopDescripe);
		viewlist.add(shopName);
		viewlist.add(shopPrice);
		viewlist.add(userPhone);
	}

	/*
	 * ������ʾ���ӵ���ϸ��Ϣ
	 */
	public void noEditView() {
		shopName.setText(postInfo.getName());
		shopPrice.setText(postInfo.getPrice());
		shopDescripe.setText(postInfo.getDescripe());
		userPhone.setText(postInfo.getPhone());
		UserName.setText(postInfo.getUserName());
		publishTime.setText(postInfo.getTime());
		String category = postInfo.getCategory();
		if (category.equals("���Ӳ�Ʒ")) { // �ж�ԭ������Ʒ��𣬲�ȷ��Spinner��ǰ��ʾ����Ϣ
			shopCategoty.setSelection(0);
		} else if (category.equals("����")) {
			shopCategoty.setSelection(1);
		} else if (category.equals("������Ʒ")) {
			shopCategoty.setSelection(2);
		} else if (category.equals("�Ҿ�")) {
			shopCategoty.setSelection(3);
		} else if (category.equals("ͼ��")) {
			shopCategoty.setSelection(4);
		} else {
			shopCategoty.setSelection(5);
		}
	}

	/*
	 * ��1������ť��ʾΪ���޸���Ϣ��ʱ��ʹEditText���Խ��б༭��ͬʱ���Ը�����Ʒ��𣬰�ť���ֱ�ɡ�ȷ���޸ġ�
	 * ��2������ť��ʾΪ��ȷ���޸ġ�ʱ����������ύ��Ϣ�����޸ģ� ���з������û����ͷ���ʱ�䲻���޸ģ����ҽ�EditText���ó�ʧȥ����
	 */
	@Override
	public void onClick(View view) {
		if (changeInfo.getText().equals("�޸���Ϣ")) { // ��ť��ʾΪ���޸���Ϣ��
			for (EditText edittext : viewlist) {
				edittext.setFocusable(true);
				edittext.setFocusableInTouchMode(true);
			}
			shopCategoty.setClickable(true);
			changeInfo.setText("ȷ���޸�");
		} else { // ��ť��ʾΪ��ȷ���޸ġ�
			newShopName = shopName.getText().toString().trim();
			newShopPrice = shopPrice.getText().toString().trim();
			newShopDescripe = shopDescripe.getText().toString().trim();
			newUserPhone = userPhone.getText().toString().trim();
			changeInfo.setText("�޸���Ϣ");
			if (newShopName.equals(postInfo.getName())
					&& newShopDescripe.equals(postInfo.getDescripe())
					&& newShopPrice.equals(postInfo.getPrice())
					&& newUserPhone.equals(postInfo.getPhone())
					&& newShopType.equals(postInfo.getCategory())) {
				Toast.makeText(this, "����ǰû�н����κ��޸�", Toast.LENGTH_SHORT).show();
			} else {
				new ChangeInfoAsyncTask().execute();
			}
		}
	}

	/*
	 * �����첽��Ϣ������Ƶ�AsyncTask���ܸ����������̴߳����ʱ�Ĳ��������ܶ�UI���в���
	 * ��1������һ���Ի����ڽ����ϵ�����ʾ"�޸���,���Ժ�..."����Ϣ ��2�������߳��н����������ƴ�Ӵ��������������н���
	 * ��3�����ݷ��������ص������ж��Ƿ��޸���Ϣ�ɹ�����Toast��ʽ��֪�û���
	 */
	private class ChangeInfoAsyncTask extends
			AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(DetailInfoActivity.this);
			dialog.setTitle("��ʾ");
			dialog.setMessage("�޸���,���Ժ�...");
			dialog.setCancelable(true);
			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (changInfoAsyncTask != null) {
						changInfoAsyncTask.cancel(true);
						changInfoAsyncTask = null;
						Toast.makeText(getApplicationContext(), "�޸ı�ȡ��",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			dialog.show();
		}

		// �����߳���ʵ��ƴ����Ϣ�����͸�������������õ����û��ѷ����ӵ���Ϣ
		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String datastr;
			try {
				datastr = "Action=changeinfo" + "&shopid=" + postInfo.getId()
						+ "&newshopname="
						+ URLEncoder.encode(newShopName, "utf-8")
						+ "&newshoptype="
						+ URLEncoder.encode(newShopType, "utf-8")
						+ "&newshopprice="
						+ URLEncoder.encode(newShopPrice, "utf-8")
						+ "&newshopdescripe="
						+ URLEncoder.encode(newShopDescripe, "utf-8")
						+ "&newuserphone="
						+ URLEncoder.encode(newUserPhone, "utf-8");

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

		// ���ݷ��������ص������ж��Ƿ��޸���Ϣ�ɹ�����Toast��ʽ��֪�û���
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			changInfoAsyncTask = null;
			dialog.dismiss();
			if (result.trim().length() == 0) {
				Toast.makeText(getApplicationContext(), "�޸�ʧ��",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				Toast.makeText(getApplicationContext(), "�޸ĳɹ�",
						Toast.LENGTH_SHORT).show();
				for (EditText edittext : viewlist) {
					edittext.setFocusable(false);
					edittext.setFocusableInTouchMode(false);
				}
				shopCategoty.setClickable(false);
			}
		}
	}
}
