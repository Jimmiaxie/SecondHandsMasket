package com.wmj.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

/*
 * ע����棬Ҫ�������û��������룬ȷ�����룬���䣬��ϵ��ʽ����Ϣ���ٽ����ж��Ƿ�Ϸ���������ϸ�Ĳ���
 */
public class RegisteredActivity extends BaseActivity implements OnClickListener {

	private EditText registName, registPassword, ConfirmPassw, registPhone;
	private Button butRegist;
	private ProgressDialog dialog;
	private RegisteAsyncTask registeAsyncTask; // ʹ��AsyncTask�����첽��Ϣ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_registered);
		initView();
	}

	/*
	 * ��ʼ������
	 */
	public void initView() {
		registName = (EditText) findViewById(R.id.regist_name);
		registPassword = (EditText) findViewById(R.id.regist_password);
		ConfirmPassw = (EditText) findViewById(R.id.regist_confirmpassw);
		registPhone = (EditText) findViewById(R.id.regist_telephone);
		butRegist = (Button) findViewById(R.id.regist_confirm);
		butRegist.setOnClickListener(this);

	}

	/*
	 * �ж������Ƿ�Ϸ����ȵõ��û�������Ϣ�����ж�ע��������Ϣ�Ƿ�Ϊ�գ�����пգ�����ʾ���벻��Ϊ��
	 */
	public boolean jugeContent() {
		boolean result = true;
		String mName = registName.getText().toString().trim();
		String mPassw = registPassword.getText().toString().trim();
		String mConfirmPassw = ConfirmPassw.getText().toString().trim();
		String mPhone = registPhone.getText().toString().trim();

		if (TextUtils.isEmpty(mName)) {
			Toast.makeText(this, "�������û���", Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!TextUtils.isEmpty(mName) && TextUtils.isEmpty(mPassw)) {
			Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!TextUtils.isEmpty(mName) && TextUtils.isEmpty(mPhone)) {
			Toast.makeText(this, "��������ϵ��ʽ", Toast.LENGTH_SHORT).show();
			result = false;
		}
		if (!mPassw.equals(mConfirmPassw)) {
			Toast.makeText(this, "�����������벻һ��", Toast.LENGTH_SHORT).show();
			result = false;
		}
		if(mPhone.length() < 11 || mPhone.length() > 11) {
			Toast.makeText(this, "�ֻ������ʽ����", Toast.LENGTH_SHORT).show();
			result = false;
	     }

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * ���ж����룬����Ϸ�ʱ���ٽ�����������Ľ�������
	 */
	@Override
	public void onClick(View arg0) {
		if (jugeContent()) { // �����������Ҫ��
			new RegisteAsyncTask().execute();
		}

	}

	/*
	 * �����첽��Ϣ������Ƶ�AsyncTask���ܸ����������̴߳����ʱ�Ĳ��������ܶ�UI���в���
	 * ��1������һ���Ի����ڽ����ϵ�����ʾ"��¼��,���Ժ�.."����Ϣ ��2�������߳��н����������ƴ�Ӵ��������������н���
	 * ��3�����ݷ��������ص������ж��Ƿ��޸���Ϣ�ɹ�����¼ʧ����Toast��ʽ��֪�û����ɹ����ж��û��Ƿ�ѡ���ס���룬����ת�������档
	 */
	private class RegisteAsyncTask extends AsyncTask<String, Integer, String> {
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(RegisteredActivity.this);
			dialog.setTitle("��ʾ");
			dialog.setMessage("ע����,���Ժ�..");
			dialog.setCancelable(true);
			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (registeAsyncTask != null) {
						registeAsyncTask.cancel(true);
						registeAsyncTask = null;
						Toast.makeText(getApplicationContext(), "ȡ��ע��", Toast.LENGTH_SHORT).show();
					}
				}
			});
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String datastr;
			try {
				datastr = "Action=register"
						+ "&username="
						+ URLEncoder.encode(registName.getText().toString(),
								"utf-8")
						+ "&psd="
						+ URLEncoder.encode(
								registPassword.getText().toString(), "utf-8")
						+ "&telephone="
						+ URLEncoder.encode(registPhone.getText().toString(),
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

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			registeAsyncTask = null;
			dialog.dismiss();
			if (result != null && result.trim().equals("1")) {
				// ע��ɹ�����ת����¼����
				Toast.makeText(getApplicationContext(), "ע��ɹ�,��ȴ�����Ա��׼",  Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(intent);

			} else if (result != null && result.trim().equals("2")) {
				Toast.makeText(getApplicationContext(), "����ϵ��ʽ��ע��", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "���û����Ѿ���ռ��",  Toast.LENGTH_SHORT).show();
			}
		}
	}
}