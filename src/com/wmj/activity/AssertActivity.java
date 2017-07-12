package com.wmj.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.wmj.Adapter.AssessListAdapter;
import com.wmj.bean.Assess;
import com.wmj.bean.Person;
import com.wmj.secondhandmasket.R;
import com.wmj.task.Constant;
import com.wmj.task.MyApplication;
import com.wmj.util.HttpUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * �����û�����Ʒ�����ۣ������ȵõ���Ʒ���������ۣ�Ȼ������û���Ҫ�������۵Ļ������ദ�������󵽷�����
 * 
 * @author Administrator
 * 
 */
public class AssertActivity extends BaseActivity {
	private ListView assess_list_lv;
	List<Assess> assessList;
	private Button me_assess_btn;
	ProgressDialog dialog;
	private AssessListAdapter adapter;
	Person person = null;
	private MyApplication myApplication;
	String name, publisgTime, personName, input, shopname;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assert_list);
		myApplication = (MyApplication) this.getApplicationContext();
		assess_list_lv = (ListView) findViewById(R.id.assert_list_lv);
		me_assess_btn = (Button) findViewById(R.id.me_assess_btn);
		publisgTime = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime());
		Intent intent = getIntent();
		name = intent.getStringExtra("shopname");
		shopname = name;
		try {
			name = URLEncoder.encode(name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = Constant.URL + "��Action=getassertlist&shopname=" + name;
		new GetDataTask().execute(url);
		me_assess_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				person = (Person) myApplication.userMap.get("person");
				if (person != null) {
					//����û��Ѿ���¼���Ϳ��Զ�����Ʒ��������
					personName = person.getName();
					System.out.print(personName);
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							AssertActivity.this);
					final EditText et = new EditText(myApplication
							.getApplicationContext());
					dialog.setTitle("������������");
					dialog.setView(et);
					dialog.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									input = et.getText().toString();
									if (input.equals("")) {
										Toast.makeText(AssertActivity.this,
												"�����������Ϊ��", Toast.LENGTH_SHORT)
												.show();
									} else {
										new InsertAssessTask().execute();
									}
								}
							});
					dialog.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					dialog.show();
				} else {
					showDialog();
				}

			}

		});

	}
	 //���������������,�����ݿ��еõ��û��Ը���Ʒ������
	private class GetDataTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(AssertActivity.this, "��ʾ", "��ȡ��...");
		}

		// ȡ����
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			assessList = new ArrayList<Assess>();
			System.out.print("result----" + result);
			if (result != null && result.trim().length() > 0) {
				try {
					JSONArray jsonArray = new JSONArray(result);

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Assess assess = new Assess();
						String m_name = jsonObject.getString("shopname");
						String m_assessname = jsonObject
								.getString("assessname");
						String m_content = jsonObject
								.getString("assesscontent");
						String m_time = jsonObject.getString("assesstime");
						assess.setAssertShopName(m_name);
						assess.setAssertName(m_assessname);
						assess.setAssertConent(m_content);
						assess.setAssertTime(m_time);
						assessList.add(assess);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			adapter = new AssessListAdapter(AssertActivity.this, assessList);
			assess_list_lv.setAdapter(adapter);

		}
	}
	//û�е�¼ʱ�����ѶԻ���
	private void showDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				AssertActivity.this);
		dialog.setTitle("����");
		dialog.setMessage("����û�е�¼�����ȵ�¼");
		dialog.setCancelable(false);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(AssertActivity.this,
						LoginActivity.class);
				startActivity(intent);

			}
		});
		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}
    //�����û������ۣ�����������������������������ؽ�����۳ɹ�������ʧ��
	private class InsertAssessTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(AssertActivity.this, "��ʾ", "�ύ��...");
		}

		protected String doInBackground(String... params) {
			// ȡ����
			String result = "";
			String datastr;
			try {
				datastr = "Action=assess" + "&shopname="
						+ URLEncoder.encode(shopname, "utf-8") + "&assessname="
						+ URLEncoder.encode(personName, "utf-8")
						+ "&assesscontent=" + URLEncoder.encode(input, "utf-8")
						+ "&assesstime="
						+ URLEncoder.encode(publisgTime, "utf-8");
				Log.d("datastr--------------", datastr);
				Log.d("shopname----------", shopname);
				Log.d("assessname----------", personName);
				Log.d("assesscontent----------", input);
				Log.d("assesstime----------", publisgTime);
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

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result != null && result.trim().equals("1")) {
				Toast.makeText(getApplicationContext(), "���۳ɹ�",
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(getApplicationContext(), "�����ύʧ��",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
