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
 * 每个已发帖子的详细信息界面，用户可以进行查看每一个已发帖子的详细信息，并且可以进行一定数据的修改，
 * 包括商品名称、商品描述、商品价钱、发布帖子的用户名、用户联系方式、发布帖子的时间。 修改后可以像数据库提交已进行修改的信息。
 */
public class DetailInfoActivity extends BaseActivity implements OnClickListener {

	private PostInfomation postInfo = null; // 定义一个PostInfomation实例
	private EditText shopName, shopDescripe, shopPrice, UserName, userPhone,publishTime;
	private Button changeInfo;
	private Spinner shopCategoty;
	private String newShopType, newShopName, newShopPrice, newShopDescripe,
			newUserPhone; // 定义用来表示修改后的信息的参数
	private String[] types = new String[] { "电子产品", "电器", "体育用品", "家具", "图书",
			"其他" }; // Spinner的数据源
	private List<EditText> viewlist = new ArrayList<EditText>(); // 用来存放各种可以修改的EditText控件
	private ProgressDialog dialog;
	private ChangeInfoAsyncTask changInfoAsyncTask; // 使用AsyncTask进行异步消息处理

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

	// 初始化界面视图,包括Spinner的item选择事件及配置适配器
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

		// 配置Spinner的适配器
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, types);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shopCategoty.setAdapter(typeAdapter);

		// 将可以变成编辑的EditText添加到list中(除了用户名和发布时间)
		viewlist.add(shopDescripe);
		viewlist.add(shopName);
		viewlist.add(shopPrice);
		viewlist.add(userPhone);
	}

	/*
	 * 设置显示帖子的详细信息
	 */
	public void noEditView() {
		shopName.setText(postInfo.getName());
		shopPrice.setText(postInfo.getPrice());
		shopDescripe.setText(postInfo.getDescripe());
		userPhone.setText(postInfo.getPhone());
		UserName.setText(postInfo.getUserName());
		publishTime.setText(postInfo.getTime());
		String category = postInfo.getCategory();
		if (category.equals("电子产品")) { // 判断原来的商品类别，并确定Spinner当前显示的信息
			shopCategoty.setSelection(0);
		} else if (category.equals("电器")) {
			shopCategoty.setSelection(1);
		} else if (category.equals("体育用品")) {
			shopCategoty.setSelection(2);
		} else if (category.equals("家具")) {
			shopCategoty.setSelection(3);
		} else if (category.equals("图书")) {
			shopCategoty.setSelection(4);
		} else {
			shopCategoty.setSelection(5);
		}
	}

	/*
	 * （1）当按钮显示为“修改信息”时，使EditText可以进行编辑，同时可以更改商品类别，按钮文字变成“确定修改”
	 * （2）当按钮显示为“确定修改”时，向服务器提交信息进行修改， 其中发布者用户名和发布时间不能修改，并且将EditText设置成失去焦点
	 */
	@Override
	public void onClick(View view) {
		if (changeInfo.getText().equals("修改信息")) { // 按钮显示为“修改信息”
			for (EditText edittext : viewlist) {
				edittext.setFocusable(true);
				edittext.setFocusableInTouchMode(true);
			}
			shopCategoty.setClickable(true);
			changeInfo.setText("确定修改");
		} else { // 按钮显示为“确定修改”
			newShopName = shopName.getText().toString().trim();
			newShopPrice = shopPrice.getText().toString().trim();
			newShopDescripe = shopDescripe.getText().toString().trim();
			newUserPhone = userPhone.getText().toString().trim();
			changeInfo.setText("修改信息");
			if (newShopName.equals(postInfo.getName())
					&& newShopDescripe.equals(postInfo.getDescripe())
					&& newShopPrice.equals(postInfo.getPrice())
					&& newUserPhone.equals(postInfo.getPhone())
					&& newShopType.equals(postInfo.getCategory())) {
				Toast.makeText(this, "您当前没有进行任何修改", Toast.LENGTH_SHORT).show();
			} else {
				new ChangeInfoAsyncTask().execute();
			}
		}
	}

	/*
	 * 给予异步消息处理机制的AsyncTask，能更方便在子线程处理耗时的操作并且能对UI进行操作
	 * （1）给出一个对话框，在界面上弹出显示"修改中,请稍后..."的信息 （2）在子线程中将请求和数据拼接传给服务器并进行交互
	 * （3）根据服务器返回的数据判断是否修改信息成功并以Toast形式告知用户。
	 */
	private class ChangeInfoAsyncTask extends
			AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(DetailInfoActivity.this);
			dialog.setTitle("提示");
			dialog.setMessage("修改中,请稍后...");
			dialog.setCancelable(true);
			dialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (changInfoAsyncTask != null) {
						changInfoAsyncTask.cancel(true);
						changInfoAsyncTask = null;
						Toast.makeText(getApplicationContext(), "修改被取消",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			dialog.show();
		}

		// 在子线程中实现拼接消息并发送给服务器，请求得到该用户已发帖子的信息
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

		// 根据服务器返回的数据判断是否修改信息成功并以Toast形式告知用户。
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			changInfoAsyncTask = null;
			dialog.dismiss();
			if (result.trim().length() == 0) {
				Toast.makeText(getApplicationContext(), "修改失败",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				Toast.makeText(getApplicationContext(), "修改成功",
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
