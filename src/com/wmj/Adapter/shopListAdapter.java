package com.wmj.Adapter;

import java.util.List;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.wmj.activity.ShopDetailActivity;
import com.wmj.bean.Shop;
import com.wmj.secondhandmasket.R;
import com.wmj.task.Constant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @author Administrator 显示商品列表的适配器
 * 
 */
public class shopListAdapter extends BaseAdapter{
	private List<Shop> shopList = null;
	private Context context;
	ImageLoader loader;
	public shopListAdapter(Context context,List<Shop> shopList) {
		this.context=context;
		this.shopList = shopList;
		RequestQueue queue=Volley.newRequestQueue(context);
		loader=new ImageLoader(queue, new BitmapCache());

	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shopList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return shopList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Shop shop = shopList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_list_item, null);
			holder = new ViewHolder();
			holder.iv_icon = (NetworkImageView) convertView.findViewById(R.id.network_image_view);
			holder.tv_sname = (TextView) convertView.findViewById(R.id.tv_sname);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			convertView.setTag(holder);	

		} else {
			
		   holder = (ViewHolder) convertView.getTag();
	      
		}
		holder.tv_sname.setText(shop.getShopname());
		holder.tv_price.setText("单价:￥" + shop.getPrice());
		System.out.print(shop.getPicture_url());
		String url =Constant.FileURL;
		holder.iv_icon.setDefaultImageResId(R.drawable.image_loading_fali);
		holder.iv_icon.setErrorImageResId(R.drawable.image_loading_fali);
		holder.iv_icon.setImageUrl(url+shop.getPicture_url(), loader);
		Log.d("shop-----", shop.getPicture_url());
		 convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,ShopDetailActivity.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("shopinfo", shop);
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
			});
		return convertView;

	}
	static class ViewHolder {
		NetworkImageView iv_icon;
		TextView tv_sname;
		TextView tv_price;
	}
	/*
	 * 将一张图片存储到LruCache中 key 图片的url地址，drawable从网络下载下来的图片
	 */
	public class BitmapCache implements ImageCache{
	private LruCache<String, Bitmap> mCache;
	public BitmapCache() {
			
		int MaxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = MaxMemory / 8;
		mCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap drawable) {
				
				return drawable.getRowBytes()*drawable.getHeight();
			}
		};
	}


	/**
	 * 从LruCache中获取一张图片
	 * 
	 */
	@Override
	public Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		// TODO Auto-generated method stub
		mCache.put(url, bitmap);
	}
  }
}
