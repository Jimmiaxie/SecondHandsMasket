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
 * 实现帖子信息LIstView的适配器，包括配置其属性，以及每个item中删除按钮的监听事件处理并及时更新ListView
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
// 点击删除按钮跳出显示选择对话框，确定删除就使用AsyncTask发送HTTP请求和数据给服务器端

	public void showDialog(int position) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("提醒");
		dialog.setMessage("确定要删除此帖子吗？");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new DeletePostAsyncTask().execute();
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}
	/*
	 * 给予异步消息处理机制的AsyncTask，能更方便在子线程处理耗时的操作并且能对UI进行操作 （1）给出一个对话框，在界面上弹出显示"删除中..."的信息
	 * （2）在子线程中将请求和数据拼接传给服务器并进行交互 （3）根据服务器返回的数据判断是否删除成功并以Toast形式告知用户。
	 */
	private class DeletePostAsyncTask extends
			AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setTitle("");
			dialog.setMessage("删除中...");
			dialog.setCancelable(false); // 设置按下Back键不能取消对话框
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
		//根据服务器返回的数据判断是否删除信息成功并以Toast形式告知用户。
		@Override
		protected void onPostExecute(String result) { 
			super.onPostExecute(result);
			deleteAsyncTask = null;
			dialog.dismiss();
			if (result.trim().length() == 0) {
				Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
				return;
			} else {
				Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
				list.remove(listPosition);
				notifyDataSetChanged();
			}
		}
	}

}
