package com.wmj.Adapter;

import java.util.List;

import com.wmj.bean.Assess;
import com.wmj.secondhandmasket.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/*
 * 显示评价列表的适配器
 */
public class AssessListAdapter extends BaseAdapter{
	private List<Assess> assessList = null;
	private Context context;
	public AssessListAdapter(Context context,List<Assess> assessList) {
		this.context=context;
		this.assessList = assessList;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return assessList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return assessList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Assess assess = assessList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.assert_list_item, null);
			holder = new ViewHolder();
			holder.assess_name = (TextView) convertView.findViewById(R.id.assert_name);
			holder.assess_content = (TextView) convertView.findViewById(R.id.assert_content);
			holder.assess_time = (TextView) convertView.findViewById(R.id.assert_time);
			convertView.setTag(holder);	

		} else {
			
		   holder = (ViewHolder) convertView.getTag();
	      
		}
		holder.assess_name.setText("用户:"+assess.getAssertName());
		holder.assess_content.setText(assess.getAssertConent());
		holder.assess_time.setText(assess.getAssertTime());

		return convertView;
	}
	static class ViewHolder {
		TextView assess_name;
		TextView assess_content;
		TextView assess_time;
	}

}
