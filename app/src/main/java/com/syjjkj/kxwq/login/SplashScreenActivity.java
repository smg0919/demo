package com.syjjkj.kxwq.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.HomePageActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.util.StringUtil;

/**
 * 引导页
 * 
 * @author Administrator
 * 
 */
public class SplashScreenActivity extends BaseActivity {
	private AlphaAnimation aa;
	private View view;
	private ImageView iv;
	private DisplayMetrics displaysMetrics;
	private int width, height;
	private boolean isPass;
	private long sleepTime = 300;// 睡眠时间
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(this, R.layout.layout_splashscreen, null);

		setContentView(view);
		displaysMetrics = this.getResources().getDisplayMetrics();
		width = displaysMetrics.widthPixels;
		height = displaysMetrics.heightPixels;
		// mRequestManager = PoCRequestManager.from(this);
		iv = (ImageView) findViewById(R.id.imageView1);
//		start(UrlConfig.START, HttpStaticApi.start);
		iv.setImageResource(R.mipmap.splash2);
		UserInfoBean.setUpdateAPK(true);//设置可以更新APK



		// 渐变展示启动屏
		aa = new AlphaAnimation(1.0f, 1.0f);
		aa.setDuration(sleepTime);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});

	}

	// 跳转
	@SuppressLint("InlinedApi")
	private void redirectTo() {
		String uid = UserInfoBean.getUid(this);
		String token = UserInfoBean.getToken(this);

		if (!StringUtil.isEmpty(uid) && !StringUtil.isEmpty(token)) {



			Intent intent = new Intent(this, HomePageActivity.class);
			startActivity(intent);
			finish();
		} else {

			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		 if(iv !=  null &&  iv.getDrawable() != null)
		 { 
		      Bitmap oldBitmap =  ((BitmapDrawable) iv.getDrawable()).getBitmap(); 
		      iv.setImageDrawable(null);    
		      if(oldBitmap !=  null)
		      {    
		    	  oldBitmap.recycle();    
		    	  oldBitmap =  null;
		      }
		 }
		 System.gc();
	}


	private String getAppDir() {
		return (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? (Environment
				.getExternalStorageDirectory().getPath()) : (getCacheDir()
				.getPath()))
				+ getPackageName();
	}


}