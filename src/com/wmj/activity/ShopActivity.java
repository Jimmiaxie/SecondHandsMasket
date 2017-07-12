package com.wmj.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wmj.Adapter.shopListAdapter;
import com.wmj.bean.Shop;
import com.wmj.secondhandmasket.MainActivity;
import com.wmj.secondhandmasket.R;
import com.wmj.task.Constant;
import com.wmj.util.HttpUtil;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.ListView;
/***
 * �õ���Ʒ�б���࣬����ҳ�����Ʒ�����ͣ��������󵽷��������������õ������͵�������Ʒ�����ѽ�����ظ��ͻ��ˣ�
 * �ڿͻ�����ʾ��Ʒ������Ʒ�۸񣬻�����ƷͼƬ��
 * @author Administrator
 *
 */
public class ShopActivity extends BaseActivity {
   private ListView shop_list_lv;
   List<Shop> shopList ;
   private Button btn_back;
   private String types;
   String url;
   ProgressDialog dialog;
   private shopListAdapter adapter;
   private List<Shop> shoplist=null;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
	setContentView(R.layout.shop_list);
	shop_list_lv=(ListView) findViewById(R.id.shop_list_lv);
	btn_back=(Button) findViewById(R.id.btn_back);
	Intent intent=getIntent();
	types=intent.getStringExtra("type");
    Log.d("type----", types);
    //������ҳ
	btn_back.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(ShopActivity.this,MainActivity.class);
			startActivity(intent);
			ShopActivity.this.finish();
		}
	});
	String mtype=null;
	try {
		mtype=URLEncoder.encode(types, "utf-8");
	} catch (UnsupportedEncodingException e) {
		
		e.printStackTrace();
	}
	url=Constant.URL+"��Action=getshoplist&type="+mtype ;
	new GetDataTask().execute(url);
   }
   //��ȡ�������˵���Ʒ��Ϣ
  private class GetDataTask extends AsyncTask<String, Integer, String> {
	    @Override
	    protected void onPreExecute() {
	    // TODO Auto-generated method stub
	    super.onPreExecute();
	    dialog=ProgressDialog.show( ShopActivity.this, "��ʾ", "��ȡ��...");
	    }
		// ȡ����
		protected String doInBackground(String... params) {
			System.out.println("doInBackground---");
			String json=null;
			try {
				 json = HttpUtil.sendHttpRequest(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception---" + e.getMessage());
			}
			return json; 
			
		}
		@Override
		protected void onPostExecute(String  result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			shoplist=new ArrayList<Shop>();
			System.out.print("result----"+result);
			if (result != null&&result.trim().length()>0) {
				try{
					JSONArray jsonArray=new JSONArray(result);
					
					for(int i=0;i<jsonArray.length();i++){
						JSONObject jsonObject=jsonArray.getJSONObject(i);
						Shop shop=new Shop();
						int  id=jsonObject.getInt("id");
						String m_name=jsonObject.getString("shopname");
						String m_desc=jsonObject.getString("descripe");
						String m_category=jsonObject.getString("category");
						String m_picture_url=jsonObject.getString("picture");
						Double m_price=jsonObject.getDouble("price");
						String m_userName=jsonObject.getString("username");
						String m_phone=jsonObject.getString("phone");
						String m_time=jsonObject.getString("time");
						shop.setId(id);
						shop.setShopname(m_name);
						shop.setDecripes(m_desc);
						shop.setCategory(m_category);
						shop.setPicture_url(m_picture_url);
						shop.setPrice(m_price);
						shop.setUserName(m_userName);
						shop.setPhone(m_phone);
						shop.setTime(m_time);
						shoplist.add(shop);
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
			
			}
		
		  adapter=new shopListAdapter(ShopActivity.this,shoplist);
		  shop_list_lv.setAdapter(adapter);
	
		}
	}
  
}
