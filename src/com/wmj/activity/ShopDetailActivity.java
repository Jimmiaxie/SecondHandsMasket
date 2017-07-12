package com.wmj.activity;
import com.wmj.bean.Person;
import com.wmj.bean.Shop;
import com.wmj.secondhandmasket.R;
import com.wmj.task.Constant;
import com.wmj.task.MyApplication;
import com.wmj.util.HttpUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/***
 * 得到商品详细信息的类，从商品列表中的类中传shop对象过来从而获得商品的详细信息
 * @author Administrator
 *
 */
public class ShopDetailActivity extends BaseActivity {
	
	private Shop shop;
	private TextView shopname_tv;
	private TextView category_tv;
	private TextView price_tv;
	private TextView name_tv;
	private TextView time_tv;
	private TextView descripe_tv;
	private ImageView image_iv;
	private Button phone_btn;
	private Button assess_btn;
	Person person = null;
	private MyApplication myApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.shopdetail_list);
	    myApplication = (MyApplication) this.getApplicationContext();
		Intent intent=getIntent();
	    shop=(Shop) intent.getSerializableExtra("shopinfo");
		System.out.print(shop.getPrice());
		
		initView();
		
	}
	@SuppressLint("HandlerLeak")
	private Handler handler=new Handler(){
	  public void handleMessage(Message msg) 
	  {
		  super.handleMessage(msg);
		  Bitmap bitmap=(Bitmap)msg.obj;
		  image_iv.setImageBitmap(bitmap);
	  }
	};
	private void initView() {
		shopname_tv=(TextView)findViewById(R.id.shopname_tv);
		shopname_tv.setText("商品名:"+shop.getShopname());
		category_tv=(TextView)findViewById(R.id.category_tv);
		category_tv.setText("商品类型:"+shop.getCategory());
		price_tv=(TextView)findViewById(R.id.price_tv);
		price_tv.setText("单价:￥"+shop.getPrice());
		name_tv=(TextView)findViewById(R.id.name_tv);
		name_tv.setText("发布者:"+shop.getUserName());
		time_tv=(TextView)findViewById(R.id.time_tv);
		time_tv.setText("发布时间:"+shop.getTime());
		descripe_tv=(TextView)findViewById(R.id.descripe_tv);
		descripe_tv.setText("商品描述:"+shop.getDecripes());
		image_iv=(ImageView)findViewById(R.id.image_iv);
		assess_btn=(Button) findViewById(R.id.assess_btn);
		assess_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(ShopDetailActivity.this,AssertActivity.class);
				intent.putExtra("shopname", shop.getShopname());
				ShopDetailActivity.this.startActivity(intent);
			}
		});
		phone_btn=(Button) findViewById(R.id.phone_btn);
		phone_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 person = (Person) myApplication.userMap.get("person");
				 //如果用户已登陆，则可以打电话联系发布者
					if (person != null) {
						Intent intent=new Intent();
						intent.setAction(Intent.ACTION_DIAL);
						intent.setData(Uri.parse("tel:"+shop.getPhone()));
						ShopDetailActivity.this.startActivity(intent);
					}else{
						showDialog();
					}
				   
				
			}

			
		});
		final String url = Constant.FileURL;
		//开启子线程加载商品图片，并把信息给主线程处理
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap bitmap=HttpUtil.getPicture(url+shop.getPicture_url());
				Message message=Message.obtain();
				message.obj=bitmap;
				handler.sendMessage(message);
			
			}
		}).start();
		
	}
	//未登陆提示对话框
	private  void showDialog() {
		AlertDialog.Builder dialog=new AlertDialog.Builder(ShopDetailActivity.this);
		dialog.setTitle("警告");
		dialog.setMessage("您还没有登录，请先登录");
		dialog.setCancelable(false);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(ShopDetailActivity.this, LoginActivity.class);
				startActivity(intent);
				
			}
		});
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
}
