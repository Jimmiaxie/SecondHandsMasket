package com.wmj.activity;
/**
 * 
 * �������ӵĽ��棬�û�����������Ʒ��Ϣ���������Ϊ�գ�ϵͳ����ʾ���룬���Ҽ۸����������������ַ������������ȷ��������͵�������
 * ���������д�����������ɹ��������ʾ�����ɹ������򷢲�ʧ�ܡ�
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
	private String[] types = new String[] { "���Ӳ�Ʒ", "����", "������Ʒ", "�Ҿ�", "ͼ��",
			"����" }; // Spinner������Դ
	private ProgressDialog dialog;
	private PublishInfoAsyncTask changInfoAsyncTask; // ʹ��AsyncTask�����첽��Ϣ����
	private String time;

	private File file;// ͼƬ���ֻ��еĵ�ַ;
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
		file = new File(Environment.getExternalStorageDirectory(), fileName); // ͼƬ���ֻ��еĵ�ַ;
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
		// ���������Ϣ��ť���¼�����
		publishButton.setOnClickListener(this);
		// ����Spinner��������
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
   //�ж��û��������Ϣ�Ƿ�Ϊ��
	public boolean isCorrect() {
		boolean result = true;
		String name = publishName.getText().toString();
		String descrip = publishDescripe.getText().toString();
		String price = publishPrice.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(getApplicationContext(), "��������Ʒ����",
					Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(descrip)) {
			Toast.makeText(getApplicationContext(), "��������Ʒ����",
					Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(price)) {
			Toast.makeText(getApplicationContext(), "��������Ʒ����",
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
				//Toast.makeText(this, "ͼƬ���ж�" + isUpLoad, 5000).show();
				if (isUpLoad) {
					new PublishInfoAsyncTask().execute();
				}
			}
			break;
		case R.id.publish_picture:
			time = new SimpleDateFormat("yyyy-MM-ddHHMMSS").format(Calendar
					.getInstance().getTime());
			fileName = person.getId() + time + ".jpg";
			file = new File(Environment.getExternalStorageDirectory(), fileName); // ͼƬ���ֻ��еĵ�ַ;
			imageUri = Uri.fromFile(file);
			ChooseWay();
			break;
		default:
			break;
		}
	}
  //ѡ���ϴ�ͼƬ��ʽ�����ջ��Ǳ���ͼƬ
	public void ChooseWay() {
		final Context dialogContext = new ContextThemeWrapper(this,
				android.R.style.Theme_Light);
		String cancel = "����";

		String[] choices = new String[] { "����", "�ֻ���Ƭ" };
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle("ѡ���ȡ��ʽ");

		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {

						case 0: // ����
							String status = Environment
									.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) { // �ж��Ƿ���SD��

								Intent intent = photoWay.takePhoto(file);
								startActivityForResult(intent, TAKE_PHOTO);

							} else {
								Toast.makeText(getApplicationContext(),
										"�����SD��", Toast.LENGTH_SHORT).show();
							}
							break;

						case 1: // �������ȥ��ȡ

							Intent intent = photoWay.givePhoto(file);
							startActivityForResult(intent, TAKE_PHOTO);
							break;
						default:
							break;
						}
					}
				});
		// ���ȡ����ť���˳��Ի���
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
					publishPhoto.setImageBitmap(bitmap); // ��ʾ�ü����ͼƬ
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
   //����ͼƬ�ϴ���������
	public void upLoadFile() {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";         // �߽��ʶ
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
				/* ȡ���ļ���FileInputStream */
				FileInputStream fStream = new FileInputStream(file.toString());
				/* ����ÿ��д��1024bytes */
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];

				int length = -1;
				/* ���ļ���ȡ������������ */
				while ((length = fStream.read(buffer)) != -1) {
					/* ������д��DataOutputStream�� */
					ds.write(buffer, 0, length);
				}
				ds.writeBytes(end);
				ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

				/* close streams */
				fStream.close();
				ds.flush();
				/* ȡ��Response���� */
				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				int res = con.getResponseCode();
				if (res == 200) {
					/* ��Response��ʾ��Dialog */
					Toast.makeText(getApplicationContext(),
							"ͼƬ�ϴ��ɹ�" + b.toString(), 5000).show();
					publishName.setText("");
					publishDescripe.setText("");
					publishPrice.setText("");
					publishPhoto
							.setImageResource(R.drawable.icon_addpic_focused);
					/* �ر�DataOutputStream */
					ds.close();
					isUpLoad = true;
				}

			} else {
				Toast.makeText(PublishPostActivity.this, "��ѡ����ȷ��ͼƬ", 5000)
						.show();
			}
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "�ϴ�ʧ��", 5000).show();
		}
	}

	private class PublishInfoAsyncTask extends
			AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(PublishPostActivity.this);
			dialog.setTitle("��ʾ");
			dialog.setMessage("������,���Ժ�...");
			dialog.setCancelable(true);
			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (changInfoAsyncTask != null) {
						changInfoAsyncTask.cancel(true);
						changInfoAsyncTask = null;
						Toast.makeText(getApplicationContext(), "�޸ı�ȡ��", 5000)
								.show();
					}
				}
			});
			dialog.show();
		}

		// �����߳���ʵ��ƴ����Ϣ�����͸�������������������Ϣ
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

		// ���ݷ��������ص������ж��Ƿ��޸���Ϣ�ɹ�����Toast��ʽ��֪�û���
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			changInfoAsyncTask = null;
			dialog.dismiss();

			if (result.trim().length() == 0) {
				Toast.makeText(getApplicationContext(), "��Ϣ����ʧ��",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				Toast.makeText(getApplicationContext(), "��Ϣ�����ɹ�",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
