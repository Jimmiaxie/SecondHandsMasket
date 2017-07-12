package com.wmj.Adapter;

import java.util.List;

import com.wmj.bean.PostInfomation;
import com.wmj.secondhandmasket.R;
import com.wmj.util.HttpUtil;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/*
 * ʵ��������ϢLIstView�����������������������ԣ��Լ�ÿ��item��ɾ����ť�ļ����¼�������ʱ����ListView
 */
public class PostInfoAdapter extends ArrayAdapter<PostInfomation> {
	private int resource;
	private List<PostInfomation> list;
	private Context context;
	private int listPosition = 0, id;

	private ProgressDialog dialog;
	@SuppressWarnings("unused")
	private DeletePostAsyncTask deleteAsyncTask;

	public PostInfoAdapter(Context context, int resource,
			List<PostInfomation> list) {
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
	public PostInfomation getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int c) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		PostInfomation postInfo = getItem(position);
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(resource, null);
			holder = new ViewHolder();
			holder.postName = (TextView) view.findViewById(R.id.post_name);
			holder.postDescripe = (TextView) view
					.findViewById(R.id.post_descripe);
			holder.deletePost = (Button) view
					.findViewById(R.id.post_deletepost);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.postName.setText(postInfo.getName());
		holder.postDescripe.setText(postInfo.getDescripe());
		holder.deletePost.setTag(position);
		holder.deletePost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(position);
				id = list.get(position).getId();
				listPosition=position;
				
			}
		});
		return view;
	}

	class ViewHolder {
		TextView postName;
		TextView postDescripe;
		Button deletePost;
		int id;
	}
// ���ɾ����ť������ʾѡ��Ի���ȷ��ɾ����ʹ��AsyncTask����HTTP��������ݸ���������

	public void showDialog(int position) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("����");
		dialog.setMessage("ȷ��Ҫɾ����������");
		dialog.setCancelable(false);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new DeletePostAsyncTask().execute();
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
	 * �����첽��Ϣ������Ƶ�AsyncTask���ܸ����������̴߳����ʱ�Ĳ��������ܶ�UI���в��� ��1������һ���Ի����ڽ����ϵ�����ʾ"ɾ����..."����Ϣ
	 * ��2�������߳��н����������ƴ�Ӵ��������������н��� ��3�����ݷ��������ص������ж��Ƿ�ɾ���ɹ�����Toast��ʽ��֪�û���
	 */
	private class DeletePostAsyncTask extends
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
			// String urlString =
			// "http://192.168.1.104:8080/SecondHandServer/servlet/ServletService";
			String result = "";
			String datastr;
			try {
				datastr = "Action=deletepost" + "&id=" + id;
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
		//���ݷ��������ص������ж��Ƿ�ɾ����Ϣ�ɹ�����Toast��ʽ��֪�û���
		@Override
		protected void onPostExecute(String result) { 
			super.onPostExecute(result);
			deleteAsyncTask = null;
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
