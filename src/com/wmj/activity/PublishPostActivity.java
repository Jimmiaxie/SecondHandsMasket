package com.wmj.activity;
/**
 * 
 * 发布帖子的界面，用户可以输入商品信息，如果输入为空，系统会提示输入，并且价格不能输入除数字外的字符。如果输入正确则把请求发送到服务器
 * 服务器进行处理，如果发布成功，则会提示发布成功，否则发布失败。
 */
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.wmj.bean.Person;
import com.wmj.secondhandmasket.R;
import com.wmj.task.ChoosePhotoWay;
import com.wmj.task.Constant;
import com.wmj.util.HttpUtil;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class PublishPostActivity extends BaseActivity implements
		OnClickListener {
	private static final int TAKE_PHOTO = 1;
	private static final int CROP_PHOTO = 2;

	private EditText publishName, publishPrice, publishDescripe;
	private Spinner publishCategory;
	private Button publishButton;
	private ImageView publishPhoto;
	private Person person = null;
	private String shopName, shopCategory, shopPrice, shopPhoto, shopDescripe,
			userTelephone, userName, publishTime;
	private String[] types = new String[] { "电子产品", "电器", "体育用品", "家具", "图书",
			"其他" }; // Spinner的数据源
	private ProgressDialog dialog;
	private PublishInfoAsyncTask changInfoAsyncTask; // 使用AsyncTask进行异步消息处理
	private String time;

	private File file;// 图片在手机中的地址;
	private Uri imageUri;
	private String fileName;
	private ChoosePhotoWay photoWay = new ChoosePhotoWay();
	private boolean isUpLoad = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_post);

		Intent intent = this.getIntent();
		person = (Person) intent.getSerializableExtra("person");
		time = new SimpleDateFormat("yyyy-MM-ddHHMMSS").format(Calendar
				.getInstance().getTime());
		fileName = person.getId() + time + ".jpg";
		file = new File(Environment.getExternalStorageDirectory(), fileName); // 图片在手机中的地址;
		imageUri = Uri.fromFile(file);
		initView();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	public void initView() {
		publishName = (EditText) findViewById(R.id.publish_shopname);
		publishPrice = (EditText) findViewById(R.id.publish_price);
		publishDescripe = (EditText) findViewById(R.id.publish_descripe);
		publishCategory = (Spinner) findViewById(R.id.publish_category);
		publishButton = (Button) findViewById(R.id.publish_button);
		publishPhoto = (ImageView) findViewById(R.id.publish_picture);
		publishPhoto.setOnClickListener(this);
		// 点击发布信息按钮的事件处理
		publishButton.setOnClickListener(this);
		// 配置Spinner的适配器
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, types);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		publishCategory.setAdapter(typeAdapter);
		publishCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				shopCategory = types[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}
   //判断用户输入的信息是否为空
	public boolean isCorrect() {
		boolean result = true;
		String name = publishName.getText().toString();
		String descrip = publishDescripe.getText().toString();
		String price = publishPrice.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(getApplicationContext(), "请输入商品名称",
					Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(descrip)) {
			Toast.makeText(getApplicationContext(), "请输入商品描述",
					Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(price)) {
			Toast.makeText(getApplicationContext(), "请输入商品定价",
					Toast.LENGTH_SHORT).show();
			result = false;
		}
		return result;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish_button:
			shopName = publishName.getText().toString().trim();
			shopPrice = publishPrice.getText().toString().trim();
			shopDescripe = publishDescripe.getText().toString().trim();
			userTelephone = person.getPhone();
			userName = person.getName();
			publishTime = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
					.getInstance().getTime());

			if (isCorrect()) {
				upLoadFile();
				//Toast.makeText(this, "图片的判断" + isUpLoad, 5000).show();
				if (isUpLoad) {
					new PublishInfoAsyncTask().execute();
				}
			}
			break;
		case R.id.publish_picture:
			time = new SimpleDateFormat("yyyy-MM-ddHHMMSS").format(Calendar
					.getInstance().getTime());
			fileName = person.getId() + time + ".jpg";
			file = new File(Environment.getExternalStorageDirectory(), fileName); // 图片在手机中的地址;
			imageUri = Uri.fromFile(file);
			ChooseWay();
			break;
		default:
			break;
		}
	}
  //选择上传图片方式是拍照还是本地图片
	public void ChooseWay() {
		final Context dialogContext = new ContextThemeWrapper(this,
				android.R.style.Theme_Light);
		String cancel = "返回";

		String[] choices = new String[] { "拍照", "手机相片" };
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle("选择获取方式");

		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {

						case 0: // 拍照
							String status = Environment
									.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) { // 判断是否有SD卡

								Intent intent = photoWay.takePhoto(file);
								startActivityForResult(intent, TAKE_PHOTO);

							} else {
								Toast.makeText(getApplicationContext(),
										"请插入SD卡", Toast.LENGTH_SHORT).show();
							}
							break;

						case 1: // 从相册中去获取

							Intent intent = photoWay.givePhoto(file);
							startActivityForResult(intent, TAKE_PHOTO);
							break;
						default:
							break;
						}
					}
				});
		// 点击取消按钮后退出对话框
		builder.setNegativeButton(cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {

				if (data != null) {
					imageUri = data.getData();
				}
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");

				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);

			}
			break;
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				try {

					Bitmap bitmap = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									imageUri));
					publishPhoto.setImageBitmap(bitmap); // 显示裁剪后的图片
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG,
							100 /* ignored for PNG */, bos);
					byte[] bitmapdata = bos.toByteArray();
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(file);

					fos.write(bitmapdata);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}
   //处理图片上传到服务器
	public void upLoadFile() {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";         // 边界标识
		String urlString = Constant.UploadURL;
	
		URL url;
		try {
			url = new URL(urlString);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			if (file != null) // outputImage
			{
				DataOutputStream ds = new DataOutputStream(
						con.getOutputStream());
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("Content-Disposition: form-data; "
						+ "name=\"file1\";filename=\"" + fileName + "\"" + end);
				ds.writeBytes(end);
				/* 取得文件的FileInputStream */
				FileInputStream fStream = new FileInputStream(file.toString());
				/* 设置每次写入1024bytes */
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];

				int length = -1;
				/* 从文件读取数据至缓冲区 */
				while ((length = fStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream中 */
					ds.write(buffer, 0, length);
				}
				ds.writeBytes(end);
				ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

				/* close streams */
				fStream.close();
				ds.flush();
				/* 取得Response内容 */
				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				int res = con.getResponseCode();
				if (res == 200) {
					/* 将Response显示于Dialog */
					Toast.makeText(getApplicationContext(),
							"图片上传成功" + b.toString(), 5000).show();
					publishName.setText("");
					publishDescripe.setText("");
					publishPrice.setText("");
					publishPhoto
							.setImageResource(R.drawable.icon_addpic_focused);
					/* 关闭DataOutputStream */
					ds.close();
					isUpLoad = true;
				}

			} else {
				Toast.makeText(PublishPostActivity.this, "请选择正确的图片", 5000)
						.show();
			}
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "上传失败", 5000).show();
		}
	}

	private class PublishInfoAsyncTask extends
			AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(PublishPostActivity.this);
			dialog.setTitle("提示");
			dialog.setMessage("发布中,请稍后...");
			dialog.setCancelable(true);
			dialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (changInfoAsyncTask != null) {
						changInfoAsyncTask.cancel(true);
						changInfoAsyncTask = null;
						Toast.makeText(getApplicationContext(), "修改被取消", 5000)
								.show();
					}
				}
			});
			dialog.show();
		}

		// 在子线程中实现拼接消息并发送给服务器，发布帖子信息
		@Override
		protected String doInBackground(String... params) {

			String result = "";
			String datastr;
			try {
				datastr = "Action=publish" + "&shopname="
						+ URLEncoder.encode(shopName, "utf-8") + "&shopprice="
						+ URLEncoder.encode(shopPrice, "utf-8")
						+ "&shopdescripe="
						+ URLEncoder.encode(shopDescripe, "utf-8")
						+ "&usertelephone="
						+ URLEncoder.encode(userTelephone, "utf-8")
						+ "&username=" + URLEncoder.encode(userName, "utf-8")
						+ "&shopcategory="
						+ URLEncoder.encode(shopCategory, "utf-8")
						+ "&shopphoto=" + URLEncoder.encode(fileName, "utf-8")
						+ "&publishtime="
						+ URLEncoder.encode(publishTime, "utf-8");

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
				Toast.makeText(getApplicationContext(), "信息发布失败",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				Toast.makeText(getApplicationContext(), "信息发布成功",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
