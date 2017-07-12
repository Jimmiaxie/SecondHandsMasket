package com.wmj.startPager;
import java.util.ArrayList;
import java.util.List;
import com.wmj.Adapter.VpageAdapter;
import com.wmj.secondhandmasket.MainActivity;
import com.wmj.secondhandmasket.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
/*
 * ����ҳ��activity
 */
public class GuidePage extends Activity implements OnPageChangeListener{
   private ViewPager vp;
   private VpageAdapter vad;//����ҳͼƬ������
   private List<View> views;
   private ImageView[] points;
   private int[] ps={R.id.point1,R.id.point2};//��������ҳ�ϵ�������
   private Button intobutton;
   @Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
//	    /**ȫ�����ã����ش�������װ��**/
//	     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//	      WindowManager.LayoutParams.FLAG_FULLSCREEN);
//	      /**����������View�ģ����Դ������е����β��ֱ����غ������Ȼ��Ч,��Ҫȥ������**/
//	     requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guidepage);
		init();
		initPoints();
	}
    private void init() {
	  LayoutInflater inflate=LayoutInflater.from(this);
	  views=new ArrayList<View>();
	  views.add(inflate.inflate(R.layout.first, null));
	  views.add(inflate.inflate(R.layout.second, null));
	  vad=new VpageAdapter(views, this);
	  vp=(ViewPager) findViewById(R.id.viewpager);
	  vp.setAdapter(vad);
	  vp.setOnPageChangeListener(this);
	  intobutton=(Button) views.get(1).findViewById(R.id.into);
	  //���ý���button�ĵ���¼����뵽��������
	  intobutton.setOnClickListener(new OnClickListener() {
	  @Override
		public void onClick(View v) {
		    Intent i=new Intent(GuidePage.this,MainActivity.class);
			startActivity(i);
			finish();
		}
	});
	
    }
    private void initPoints(){
    	points=new ImageView[(views.size())];
    	for(int i=0;i<views.size();i++){
    		points[i]=(ImageView)findViewById(ps[i]);
    	}
    	
    }
	@Override
	public void onPageScrollStateChanged(int arg0) {
	
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	//����ҳ��ѡ������õ��ͼƬΪ��ɫ
	@Override
	public void onPageSelected(int arg0) {
	
		for(int i=0;i<ps.length;i++){
			if(arg0==i){
			points[i].setImageResource(R.drawable.login_point_selected);
			}else{
			points[i].setImageResource(R.drawable.login_point);
			}	
		}
	}
}
