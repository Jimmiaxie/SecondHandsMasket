package com.wmj.fragment;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wmj.secondhandmasket.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

/*
 * ��ҳ�е�ͼƬ�ֲ�
 */
public class Tab1PictureFragment extends FrameLayout {
	private Context context;
	private ImageLoader imageLoader=ImageLoader.getInstance();
	//�ֲ�ͼƬ����
	private final static int IMG_COUNT=4;
	//�ֲ�ͼƬ���
	private final static int IMG_TIME=5;
	//�ֲ�ͼƬ����
	private final static boolean isAutoPlay=true;
	//�ֲ�ͼƬ��Դ
    private String[] imageUrls;  
    //���ֲ�ͼƬ��ImageView ��list  
    private List<ImageView> imageViews;       
   //��Բ���View��list  
    private List<View> dotViewsList;  
    private ViewPager viewPager;  
   //��ǰ�ֲ�ҳ  
   private int currentItem  = 0;  
   //��ʱ����  
   private ScheduledExecutorService  scheduledExecutorService;
   private Handler handler=new Handler(){
	   public void handleMessage(android.os.Message msg)
	   {
		   super.handleMessage(msg);
		   viewPager.setCurrentItem(currentItem);
	   };
   };
   public Tab1PictureFragment(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		InitImage(context);
		initData();  
		if(isAutoPlay){
			play();
		}
	}
  
   public Tab1PictureFragment(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	
   public Tab1PictureFragment(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
   //��ʼ����
   private void play() {
	// TODO Auto-generated method stub
	   scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();  
       scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);  
   }
   /** 
    * ֹͣ�ֲ�ͼ�л� 
    */  
   private void stopPlay(){  
       scheduledExecutorService.shutdown();  
   }  
   //��ʼ��ͼƬ����
	public static void InitImage(Context context) {
	 
		ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY-2)  //���õ�ǰ�̵߳����ȼ�
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator()) //��MD5��url���м���
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() //��ӡlog
		.build();
		ImageLoader.getInstance().init(config);
	 
    }
	 private void initData() {
		 imageViews = new ArrayList<ImageView>();  
	      dotViewsList = new ArrayList<View>();  
	      new GetDataTask().execute("");  
	  
			
    }
	private void initView(Context context) {
    	
		if(imageUrls==null||imageUrls.length==0)
			return;
	    LayoutInflater.from(context).inflate(R.layout.guide_picture, this, true);  
	    LinearLayout dotLayout = (LinearLayout)findViewById(R.id.point);  
	    dotLayout.removeAllViews(); 
	    for(int i=0;i<imageUrls.length;i++){
	    	ImageView view =  new ImageView(context);  
            view.setTag(imageUrls[i]);  
            if(i==0)//��һ��Ĭ��ͼ  
                view.setBackgroundResource(R.drawable.morentu);  
            view.setScaleType(ScaleType.FIT_XY);  
            imageViews.add(view);
            ImageView pointView =  new ImageView(context);  
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;  
            params.rightMargin = 4; 
            dotLayout.addView(pointView,params);
            dotViewsList.add(pointView);
	    }
	    viewPager=(ViewPager) findViewById(R.id.viewPager);
	    viewPager.setFocusable(true);  
	    viewPager.setAdapter(new MyPagerAdapter());
	    viewPager.setOnPageChangeListener(new MyPageChangeListener());  
	}
   
    /** 
     * ���ViewPager��ҳ�������� 
     *  
     */  
    private class MyPagerAdapter  extends PagerAdapter{  
  
        @Override  
        public void destroyItem(View container, int position, Object object) { 
            //((ViewPag.er)container).removeView((View)object);  
            ((ViewPager)container).removeView(imageViews.get(position));  
        }  
  
        @Override  
        public Object instantiateItem(View container, int position) {  
            ImageView imageView = imageViews.get(position);  
  
            imageLoader.displayImage(imageView.getTag() + "", imageView);  
              
            ((ViewPager)container).addView(imageViews.get(position));  
            return imageViews.get(position);  
        }  
  
        @Override  
        public int getCount() { 
            return imageViews.size();  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;  
        }  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
  
        }  
  
        @Override  
        public Parcelable saveState() { 
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) { 
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {
              
        }  
          
    }  
    class MyPageChangeListener implements OnPageChangeListener{
    	boolean isAutoPlay = false;  
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			  case 1:// ���ƻ�����������  
	               isAutoPlay = false;  
	               break;  
	          case 2:// �����л���  
	               isAutoPlay = true;  
	               break;  
	          case 0:
	        	  if(viewPager.getCurrentItem()==viewPager.getAdapter().getCount()-1&&!isAutoPlay){
	        		  viewPager.setCurrentItem(0);
	        	  }
	        	  else if(viewPager.getCurrentItem()==0&&!isAutoPlay){
	        		  viewPager.setCurrentItem(viewPager.getAdapter().getCount()-1);
	        	  }

			
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			 currentItem = arg0;  
	            for(int i=0;i < dotViewsList.size();i++){  
	                if(i == arg0){  
	                    ((View)dotViewsList.get(arg0)).setBackgroundResource(R.drawable.dianhong);  
	                }else {  
	                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dianbai);  
	         
	            }  
	        }  
		}
    	
    }
     private class SlideShowTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (viewPager) {
				currentItem=(currentItem+1)%imageViews.size(); 
				handler.obtainMessage().sendToTarget();
				
			}
		}
    	
    }
     /** 
      * ����ImageView��Դ�������ڴ� 
      *  
      */  
     private void destoryBitmaps() {  
   
         for (int i = 0; i <IMG_COUNT; i++) {  
             ImageView imageView = imageViews.get(i);  
             Drawable drawable = imageView.getDrawable();  
             if (drawable != null) {  
                 //���drawable��view������  
                 drawable.setCallback(null);  
             }  
         }  
     }  
    
    /*
     * �ڲ����������ͼƬ
     */
    class GetDataTask extends AsyncTask<String,Integer,Boolean> {

   	 
	@Override
   	protected Boolean doInBackground(String... params) {
   		// TODO Auto-generated method stub
   		try{
   		 imageUrls=new String[]{  
   				"http://img2.imgtn.bdimg.com/it/u=2106790143,3390346299&fm=21&gp=0.jpg",  
                   "http://img2.imgtn.bdimg.com/it/u=3785982440,2863235884&fm=21&gp=0.jpg",  
                   "http://img1.imgtn.bdimg.com/it/u=1873754960,1819233185&fm=23&gp=0.jpg",  
                   "http://img4.imgtn.bdimg.com/it/u=1953369279,565980577&fm=21&gp=0.jpg"  
             };  
   		 return true;
   		}
   		catch(Exception e) {
   			e.printStackTrace();
   			return false;
   		}
   	}
   	@Override
   	protected void onPostExecute(Boolean result) {
   		// TODO Auto-generated method stub
   		super.onPostExecute(result);
   		if(result){
   			initView(context);
   		 }
    	}
    }
}



	
	

	

	
