package com.wmj.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import com.wmj.activity.ShopActivity;
import com.wmj.secondhandmasket.R;
/**
 * �����棬�����ϲ������ֲ�ͼ���м���6�����͵���Ʒ���û�����ѡ����ͼƬ����������������κ�һ�����͵���Ʒ
 * ������Կ�����������Ʒ���б�
 * @author Administrator
 *
 */
public class Tab1Fragment extends Fragment{
   private int[] images={R.drawable.home1,R.drawable.home2,R.drawable.home3,R.drawable.home4,
		   R.drawable.home5,R.drawable.home6,};
   private String[] titles={"����","������Ʒ","ͼ��","���Ӳ�Ʒ","�Ҿ�","����"};
   private GridView mGridView;
   private Intent intent;
   @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.tab1, container, false);
		mGridView=(GridView) view.findViewById(R.id.shop_category);
		mGridView.setAdapter(getMenu(titles, images));
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					intent = new Intent(getActivity(),
							ShopActivity.class);
					intent.putExtra("type", "����");
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(getActivity(),
							ShopActivity.class);
					intent.putExtra("type", "������Ʒ");
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(getActivity(),
							ShopActivity.class);
					intent.putExtra("type", "ͼ��");
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(getActivity(),
							ShopActivity.class);
					intent.putExtra("type", "���Ӳ�Ʒ");
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(getActivity(),
							ShopActivity.class);
					intent.putExtra("type", "�Ҿ�");
					startActivity(intent);
					break;
				case 5:
					intent = new Intent(getActivity(),
							ShopActivity.class);
					intent.putExtra("type", "����");
					startActivity(intent);
					break;
				default:
					break;
				}
				
			}
		});
		return view;
	}
   //����ͼƬ������
    private SimpleAdapter getMenu(String[] titles, int[] images) {
	// TODO Auto-generated method stub
    	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < titles.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", images[i]);
			map.put("itemText", titles[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(getActivity().getApplicationContext(), data,
				R.layout.grid_item,
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.grid_item_img, R.id.grid_item_title});
		return simperAdapter;
    }
   
}
