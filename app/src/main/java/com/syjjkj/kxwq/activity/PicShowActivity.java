package com.syjjkj.kxwq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.MyApplication;
import com.syjjkj.kxwq.util.ScreenUtil;
import com.syjjkj.kxwq.widget.PhotoView;

import java.util.ArrayList;

/**
 * 车源图片
 */
public class PicShowActivity extends BaseActivity implements OnClickListener {
	
	private PopupWindow pw;// 右上角弹出框
	private ViewPager viewPager;
	private RadioGroup ll_rg;// 小圆点的布局
//	private ArrayList<ImageView> images;// 轮播时ImageView的集合
	private ArrayList<String> picList;// 车俩图片
	private int currentItem = 0; // 当前图片的索引号
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_show);
		setTitle("图片", true, TITLE_TYPE_IMG, R.drawable.back9, false, -1, -1);
		initView();
		initData();
		initListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	private void initData() {
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		int screenWidth = ScreenUtil.getScreenWidth(this);
		//viewPager.setLayoutParams(new RelativeLayout.LayoutParams(
		//		RelativeLayout.LayoutParams.MATCH_PARENT, screenWidth / 4 * 3));

		ll_rg = (RadioGroup) findViewById(R.id.ll_rg);
		picList = new ArrayList<String>();			
		picList =  getIntent().getStringArrayListExtra("pics");//4s记录的
		currentItem=getIntent().getIntExtra("position", 0);
		if (picList != null) {
			setBinner();
		}
	}
	
	/**
	 * 轮播图适配器
	 */
	/*PagerAdapter pagerAdapter = new PagerAdapter() {
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(images.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {

			((ViewPager) container).addView(images.get(position));
//			util.displayForHeader(images.get(position),
//					UrlConfig.ROOT_URL_UPLOAD + picList.get(position));
			
			ImageLoader.getInstance().displayImage(UrlConfig.ROOT_URL_UPLOAD + picList.get(position),
					images.get(position), MyApplication.optionsCar);
			
			return images.get(position);
		}
		

		@Override
		public int getCount() {
			return images.size();
		}
	};*/
	
	PagerAdapter pagerAdapter=new PagerAdapter() {

		

		@Override
		public int getCount() {
			return picList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			ImageLoader.getInstance().displayImage( picList.get(position),
					photoView, MyApplication.optionsCar);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	};
	
	/**
	 * 设置轮播图
	 */
	private void setBinner() {
		LayoutInflater inflater = LayoutInflater.from(this);
//		images = new ArrayList<ImageView>();
//		ImageView iv = null;
		// 初始化轮播图图片和圆点
		ll_rg.removeAllViews();
		for (int i = 0; i < picList.size(); i++) {
//			iv = (ImageView) inflater.inflate(R.layout.item_picture, null);
//			iv.setScaleType(ScaleType.FIT_CENTER);
			RadioButton dotButton = new RadioButton(this);
			dotButton.setId(i);
			dotButton.setLayoutParams(new RadioGroup.LayoutParams(
					RadioGroup.LayoutParams.WRAP_CONTENT,
					RadioGroup.LayoutParams.WRAP_CONTENT));
			dotButton.setPadding(1, 1, 1, 1);
			// dotButton.setBackgroundResource(R.drawable.dot_bg);//不能用backgroundresource，
			dotButton.setButtonDrawable(R.drawable.dot_bg);// 设置按钮背景图片
			dotButton.setTag(i);// 设置当前位置

//			images.add(iv);
			ll_rg.addView(dotButton);
		}	
		viewPager.setAdapter(pagerAdapter);
		if (picList.size() > 0) {
			ll_rg.check(currentItem);
			viewPager.setCurrentItem(currentItem);
		}
		pagerAdapter.notifyDataSetChanged();
		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.getParent().getParent()
							.requestDisallowInterceptTouchEvent(true);
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL
						|| event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					v.getParent().getParent()
							.requestDisallowInterceptTouchEvent(false);
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					int y = (int) event.getY();
					if (y > v.getBottom()) {
						event.setAction(MotionEvent.ACTION_UP);
						v.getParent().getParent()
								.requestDisallowInterceptTouchEvent(true);
					}
				}

				return v.onTouchEvent(event);
			}
		});
		// 轮播图监听
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				currentItem = position;
				((RadioButton) ll_rg.getChildAt(position)).setChecked(true);
				viewPager.setCurrentItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				switch (state) {
				case ViewPager.SCROLL_STATE_IDLE:

					break;
				}
			}
		});
	}

	private void initListener() {
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		default:
			break;
		}
	}
}
