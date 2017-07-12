package com.wmj.activity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.wmj.bean.Person;
import com.wmj.secondhandmasket.R;
import com.wmj.util.HttpUtil;



import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * �����û��������,Ҫ���û�����ԭ�����롢�����롢ȷ���������Ϣ���Ƚ��������жϣ��ٽ�����Ϣ�Ϸ����жϣ�
 * �������Ϸ������ύ���������������ݸ���
 */
public class ChangePasswordActivity extends BaseActivity {
	private EditText currentPassw, newPassw, confirmPassw;
	private Button changePassw;
	private int id;
	private ProgressDialog dialog;
	private ChangePasswAsyncTask changePasswAsyncTask;
	private Person person = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_changepassword);
		Intent intent = this.getIntent();
		person = (Person) intent.getSerializableExtra("person");
		id = person.getId();
		initView();
	}

	/*
	 * ��ʼ������
	 */
	public void initView() {
		currentPassw = (EditText) findViewById(R.id.change_passw);
		newPassw = (EditText) findViewById(R.id.change_newpassw);
		confirmPassw = (EditText) findViewById(R.id.change_confirmpassw);
		changePassw = (Button) findViewById(R.id.change_butconfirm);
		changePassw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changePassword();
			}
		});
	}

	/*
	 * 1.�ж�Ҫ�����������ֵ�Ƿ�Ϊ�ջ�ո� 2.�ж���������ȷ������������Ƿ�һ�� 3.���ж���Ϣ�Ƿ���ȷ����ȷ���޸�����ɹ�������ʧ��
	 */
	public void changePassword() {
		String curPassw = currentPassw.getText().toString().trim();
		String newPassword = newPassw.getText().toString().trim();
		String conPassw = confirmPassw.getText().toString();
		if (TextUtils.isEmpty(curPassw) || TextUtils.isEmpty(conPassw)
				|| TextUtils.isEmpty(newPassword)) {
			Toast.makeText(this, "�޸�����������Ϣ������Ϊ��", Toast.LENGTH_SHORT).show();
		} else {
			if (!newPassword.equals(conPassw)) {
				Toast.makeText(this, "ȷ�������������벻һ�£�������ȷ��", Toast.LENGTH_SHORT)
						.show();
			} else {
				new ChangePasswAsyncTask().execute();
			}
		}
	}

	/*
	 * �����첽��Ϣ������Ƶ�AsyncTask���ܸ����������̴߳����ʱ�Ĳ��������ܶ�UI���в���
	 * ��1������һ���Ի����ڽ����ϵ�����ʾ"����������,���Ժ�.."����Ϣ ��2�������߳��н����������ƴ�Ӵ��������������н���
	 * ��3�����ݷ��������ص������ж��Ƿ��޸���Ϣ�ɹ�����Toast��ʽ��֪�û���
	 */
	private class ChangePasswAsyncTask extends
			AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ChangePasswordActivity.this);
			dialog.setTitle("��ʾ");
			dialog.setMessage("����������,���Ժ�..");
			dialog.setCancelable(true);
			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (changePasswAsyncTask != null) {
						changePasswAsyncTask.cancel(true);
						changePasswAsyncTask = null;
						Toast.makeText(getApplicationContext(), "�޸����뱻ȡ��", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
			dialog.show();
		}

		// �����߳���ʵ��ƴ����Ϣ�����͸��������������޸�����
		@Override
		protected String doInBackground(String... params) {
		    String result="";
			String datastr;
			try {
				datastr = "Action=changepassw"
						+ "&id="
						+ id
						+ "&psd="
						+ URLEncoder.encode(currentPassw.getText().toString(),
								"utf-8")
						+ "&newpassword="
						+ URLEncoder.encode(newPassw.getText().toString(),
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

		// ���ݷ��������ص������ж��Ƿ��޸�����ɹ�����Toast��ʽ��֪�û���
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			changePasswAsyncTask = null;
			dialog.dismiss();
			if (result != null && result.trim().equals("1")) {
				// ע��ɹ�����ת����¼����
				Toast.makeText(getApplicationContext(), "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "ԭ�����������", Toast.LENGTH_SHORT).show();

			}
		}
	}
}
