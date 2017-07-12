package com.wmj.Adapter;

import java.util.List;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wmj.activity.HavePostActivity;

import com.wmj.bean.Person;
import com.wmj.secondhandmasket.R;
import com.wmj.util.HttpUtil;
/**
 *  ����Ա������ʾ�û���������
 * @author Administrator
 *
 */
public class UserAgreeAdapter extends ArrayAdapter<Person> {
	private int resource;
	private List<Person> list;
	private Context context;
	private int listPosition = 0;
	String name;
	private ProgressDialog dialog;
	public UserAgreeAdapter(Context context, int resource, List<Person> list) {
		super(context, resource, list);
		this.resource = resource;
		this.list = list;
		this.context = context;
	}

	@Override
	public Context getContext() {
		return context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Person getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Person person = getItem(position);
		View view;
		ViewHolder holder;
		if (convertView == null) {
		
			view = LayoutInflater.from(context).inflate(resource, null);
			holder = new ViewHolder();
			holder.userName = (TextView) view.findViewById(R.id.user_agree_name);
			holder.userPhone = (TextView) view.findViewById(R.id.user_agree_phone);
			holder.deleteBtn = (Button) view.findViewById(R.id.delete_btn);
			
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.userName.setText("�û���:"+person.getName());
		holder.userPhone.setText("�绰����:"+person.getPhone());
		holder.deleteBtn.setTag(position);
		holder.deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogDelete(position);
				name = list.get(position).getName();
				listPosition=position;
				
			}
		});
		//Ϊÿ��item���ü�����,�����ѷ������ӽ���
		view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,HavePostActivity.class);
					intent.putExtra("person", person);
					context.startActivity(intent);
				}
			});
		return view;
	}

	class ViewHolder {
		TextView userName;
		TextView userPhone;
		Button   deleteBtn;
		int id;
	}

	// ���ɾ����ť������ʾѡ��Ի���ȷ��ɾ����ʹ��AsyncTask����HTTP��������ݸ���������

	public void showDialogDelete(int position) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("����");
		dialog.setMessage("ȷ��Ҫɾ�����û���");
		dialog.setCancelable(false);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new DeleteUserAsyncTask().execute();
			}
		});
		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}
	
	/*
	 * �����첽��Ϣ������Ƶ�AsyncTask���ܸ����������̴߳����ʱ�Ĳ��������ܶ�UI���в���
	 * ��1������һ���Ի����ڽ����ϵ�����ʾ"ɾ����..."����Ϣ ��2�������߳��н����������ƴ�Ӵ��������������н���
	 * ��3�����ݷ��������ص������ж��Ƿ�ɾ���ɹ�����Toast��ʽ��֪�û���
	 */
	private class DeleteUserAsyncTask extends
			AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setTitle("");
			dialog.setMessage("ɾ����...");
			dialog.setCancelable(false); // ���ð���Back������ȡ���Ի���
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String datastr;
			try {
				datastr = "Action=deleteagreeuser" + "&name=" + name;
				result = HttpUtil.sendHttpRequests(datastr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// ���ݷ��������ص������ж��Ƿ�ɾ����Ϣ�ɹ�����Toast��ʽ��֪�û���
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result.trim().length() == 0) {
				Toast.makeText(context, "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
				return;
			} else {
				Toast.makeText(context, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
				list.remove(listPosition);
				notifyDataSetChanged();
			}
		}
	}


}
