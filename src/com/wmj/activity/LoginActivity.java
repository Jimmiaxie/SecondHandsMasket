package com.wmj.activity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.wmj.bean.Person;
import com.wmj.secondhandmasket.MainActivity;
import com.wmj.secondhandmasket.R;
import com.wmj.task.MyApplication;
import com.wmj.util.HttpUtil;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
/*
 * ��½���棬ͨ���Ƿ�ѡ���ס�����������¼�û��������룬��ͨ�����������Ϣ����ƥ���Ƿ����ںϷ���½
 */
import android.widget.Toast;

/**
 * �û���¼����,ͨ��SharedPreferences�е��������ж�֮ǰ�Ƿ��Ѿ���ǵ�¼�������������ʾ�ڽ����ϣ�
 * ��¼ʱ���ж��Ƿ�ѡ���ס���룬��½�ɹ�ʱ�ͽ���Ϣ���沢��ת��������
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private Button butLogin, butRegiste;
	private EditText edtName, edtPassword;
	private SharedPreferences share; // �����洢�û�����������Ƿ��ס����
	private CheckBox isRemember;
	private MyApplication myApplication;
	private ProgressDialog dialog;
	private LoginAsyncTask loginAsyncTask; // ʹ��AsyncTask�����첽��Ϣ����
	@SuppressWarnings("unused")
	private List<Person> personList; // �����Ʒ��Ϣ��list�б�
	int id,isAdmin,isApprove;
	String name, password, telephone;
	private Person person;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		myApplication = (MyApplication) this.getApplicationContext();
		share = PreferenceManager.getDefaultSharedPreferences(this); // �õ�ʵ��
		initView();
	}

	// ��ʼ������
	public void initView() {
		butLogin = (Button) findViewById(R.id.login_login);
		butRegiste = (Button) findViewById(R.id.login_registered);
		edtName = (EditText) findViewById(R.id.login_name);
		edtPassword = (EditText) findViewById(R.id.login_password);
		isRemember = (CheckBox) findViewById(R.id.login_isremember);
		butLogin.setOnClickListener(this);
		butRegiste.setOnClickListener(this);
		boolean isremember = share.getBoolean("remember_infomation", false);
		if (isremember) { // ֮ǰ�б����û���������
			edtName.setText(share.getString("name", ""));
			edtPassword.setText(share.getString("password", ""));
			isRemember.setChecked(true);
		}
	}

	/*
	 * ��1��ѡ���½��ťʱ�����������Ϣ����ƥ�䣬�ɹ����жϵ�ǰ�û���Ϣ�Ƿ�洢����תҳ�棬������ʾ��Ϣ ��2��ѡ��ע�ᰴťʱ����ת��ע�ᰴť
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_login:

			String loginname = edtName.getText().toString().trim();
			String loginpassword = edtPassword.getText().toString().trim();

			if (TextUtils.isEmpty(loginname)) {
				Toast.makeText(this, "�û�������Ϊ��", Toast.LENGTH_SHORT).show();
			} else if (!TextUtils.isEmpty(loginname)
					&& TextUtils.isEmpty(loginpassword)) {
				Toast.makeText(this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
			} else {
				// ����������ݿ���Ϣ���Ƚ�
				new LoginAsyncTask().execute();
			}
			break;
		case R.id.login_registered:
			Intent intent = new Intent(this, RegisteredActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/*
	 * �����첽��Ϣ������Ƶ�AsyncTask���ܸ����������̴߳����ʱ�Ĳ��������ܶ�UI���в���
	 * ��1������һ���Ի����ڽ����ϵ�����ʾ"��¼��,���Ժ�.."����Ϣ ��2�������߳��н����������ƴ�Ӵ��������������н���
	 * ��3�����ݷ��������ص������ж��Ƿ��޸���Ϣ�ɹ�����¼ʧ����Toast��ʽ��֪�û����ɹ����ж��û��Ƿ�ѡ���ס���룬����ת�������档
	 */
	private class LoginAsyncTask extends AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setTitle("��ʾ");
			dialog.setMessage("��¼��,���Ժ�..");
			dialog.setCancelable(true);
			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (loginAsyncTask != null) {
						loginAsyncTask.cancel(true);
						loginAsyncTask = null;
						Toast.makeText(getApplicationContext(), "��¼��ȡ��", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
			dialog.show();
		}

		// �����߳���ʵ��ƴ����Ϣ�����͸��������������¼
		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String datastr;
			try {
				datastr = "Action=login"
						+ "&username="
						+ URLEncoder.encode(edtName.getText().toString(),
								"utf-8")
						+ "&psd="
						+ URLEncoder.encode(edtPassword.getText().toString(),
								"utf-8");

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

		// ���ݷ��������ص������ж��Ƿ��¼�ɹ�����¼ʧ����Toast��ʽ��֪�û����ɹ���������ҳ�档
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			loginAsyncTask = null;
			dialog.dismiss();
			if (result != null && result.trim().equals("2")) {
				Toast.makeText(LoginActivity.this, "�������",Toast.LENGTH_SHORT).show();
			} else if (result != null && result.trim().equals("3")) {
				Toast.makeText(LoginActivity.this, "���û�������", Toast.LENGTH_SHORT).show();
			} else if (result == null) {
				Toast.makeText(LoginActivity.this, "�޷����ӷ�����", Toast.LENGTH_SHORT).show();
			} 
			else {
				personList = new ArrayList<Person>();
				JSONArray jsonArray;

				try {
					jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						person = new Person();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						int id = jsonObject.getInt("id");
						name = jsonObject.getString("name");
						password = jsonObject.getString("password");
						telephone = jsonObject.getString("telephone");
						isAdmin=jsonObject.getInt("isadmin");
						isApprove=jsonObject.getInt("isapprove");
						person.setId(id);
						person.setName(name);
						person.setPassword(password);
						person.setPhone(telephone);
						person.setIsAdmin(isAdmin);
						person.setIsApprove(isApprove);
						myApplication.userMap.put("person", person);
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

				SharedPreferences.Editor editor = share.edit();
				if (isRemember.isChecked()) // ѡ���ס����
				{
					editor.putBoolean("remember_infomation", true);
					editor.putInt("id", id);
					editor.putString("name", name);
					editor.putString("password", password);
					editor.putString("telephone", telephone);
					editor.putInt("isAdmin", isAdmin);
					editor.commit();
				} else {
					editor.clear();
				}

				// ��¼�ɹ�������ת
				if(isAdmin==0&&isApprove==1){
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("person", person);
				startActivity(intent);
				}else if(isAdmin==1){
				    Intent intent = new Intent(getApplicationContext(),
							AdminActivity.class);
					intent.putExtra("person", person);
					startActivity(intent);
				}else{
					Toast.makeText(LoginActivity.this, "��ȴ�����Ա��׼....", 5000).show();
				}
			}
		}
	}
}
