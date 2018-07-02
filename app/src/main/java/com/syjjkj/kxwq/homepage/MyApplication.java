package com.syjjkj.kxwq.homepage;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.http.ObserverCallBack;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Administrator 全局存储容器 整个应用程序唯一实例 描述： 当android程序启动时系统会创建一个
 *         application对象，用来存储系统的一些 信息。
 *         android系统会为每个程序运行时创建一个Application类的对象且仅创建一个(单例)。
 *         且application对象的生命周期是整个程序中最长的，它的生命周期就等于这个程序的生命周期。
 *         因为它是全局的单例的，所以在不同的Activity,Service中获得的对象都是同一个对象。
 *         所以通过Application来进行一些，数据传递，数据共享,数据缓存等操作。
 * 
 */

public class MyApplication extends Application implements ObserverCallBack {

	private List<Activity> activitys = new LinkedList<Activity>();
	private List<Activity> detailsActivitys = new LinkedList<Activity>();
	private List<Activity> stateActivitys = new LinkedList<Activity>();
	private static MyApplication application;
	public static Handler handler = new Handler();
	public static Context applicationContext;
	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";


	public static DisplayImageOptions optionsComanpy;
	public static DisplayImageOptions optionsPerson;
	public static DisplayImageOptions optionsCar;
	public static DisplayImageOptions optionsNoCacheCar;
	/**
	 * 分享车辆模板选择，没有下载中的默认图片，只有下载失败以及空网址的图片
	 */
	public static DisplayImageOptions optionsModel;

	public static MyApplication getInstance() {
		if (application == null) {
			application = new MyApplication();
		}
		return application;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}

	}

	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
		if (!StringUtil.isNetworkConnected(this)) {
			ToastUtil.makeShortText(this, "请检查网络");
			return;
		}
		RequestParams params = new RequestParams();
		HttpUtils httpUtils = new HttpUtils();
			
//		String url = UrlConfig.UPDATECLIENTID + "&uid=" + UserInfoBean.getUid(this)
//				+ "&token=" + UserInfoBean.getToken(this) + "&client_type=1&clientid=";
//
//		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, url, params,
//				this, httpUtils, HttpStaticApi.updateGeTuiClientID);
	}

	//
	public void addDetailActivity(Activity activity) {
		if (detailsActivitys != null && detailsActivitys.size() > 0) {
			if (!detailsActivitys.contains(activity)) {
				detailsActivitys.add(activity);
			}
		} else {
			detailsActivitys.add(activity);
		}

	}

	public void exitDetailActivity() {
		if (detailsActivitys != null && detailsActivitys.size() > 0) {
			for (Activity activity : detailsActivitys) {
				activity.finish();
			}
		}
	}

	public void addStateActivity(Activity activity) {
		if (stateActivitys != null && stateActivitys.size() > 0) {
			if (!stateActivitys.contains(activity)) {
				stateActivitys.add(activity);
			}
		} else {
			stateActivitys.add(activity);
		}

	}

	public void exitStateActivity() {
		if (stateActivitys != null && stateActivitys.size() > 0) {
			for (Activity activity : stateActivitys) {
				activity.finish();
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CrashReport.initCrashReport(this, "b0244e62b8", false); //腾讯bugly的使用
		application = this;
		applicationContext = this;
		initConfig(getApplicationContext());
		optionsCar = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_default_car)
				.showImageForEmptyUri(R.drawable.icon_default_car)
				.showImageOnFail(R.drawable.icon_default_car)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.build();
		optionsModel = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.icon_default_car)
				.showImageOnFail(R.drawable.icon_default_car)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.build();
		optionsPerson = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_defalut_person)
				.showImageForEmptyUri(R.drawable.icon_defalut_person)
				.showImageOnFail(R.drawable.icon_defalut_person)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.build();

		optionsComanpy = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_default_company)
				.showImageForEmptyUri(R.drawable.icon_default_company)
				.showImageOnFail(R.drawable.icon_default_company)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.build();
		
		optionsNoCacheCar= new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_default_car)
		.showImageForEmptyUri(R.drawable.icon_default_car)
		.showImageOnFail(R.drawable.icon_default_car)
		.cacheInMemory(false).cacheOnDisk(false).considerExifParams(true)
		.build();
		/**
		 * this function will initialize the HuanXin SDK
		 * 
		 * @return boolean true if caller can continue to call HuanXin related
		 *         APIs after calling onInit, otherwise false.
		 * 
		 *         环信初始化SDK帮助函数
		 *         返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
		 * 
		 *         for example: 例子：
		 * 
		 *         public class DemoHXSDKHelper extends HXSDKHelper
		 * 
		 *         HXHelper = new DemoHXSDKHelper();
		 *         if(HXHelper.onInit(context)){ // do HuanXin related work }
		 */


	}

	/**
	 * 
	 * 
	 * @param context
	 */
	public static void initConfig(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				Constant.SDCARD_PATH + "/cache/");
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
				// it)/设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)
				// .推荐1-5
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(4 * 1024 * 1024)
				.diskCacheSize(50 * 1024 * 1024)
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																				// (5
																				// s),
																				// readTimeout
																				// (30
				// s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		ImageLoader.getInstance().init(config);
	}


	@SuppressWarnings("unchecked")
	public void onSuccessHttp(String responseInfo, int resultCode) {
		if(responseInfo.equals("{\"state\":-1,\"message\":\"token\\u503c\\u4e0d\\u6b63\\u786e\"}"))
		{
//			UserInfoBean.userLogout(this);
//			MyApplication.getInstance().exit();
//			Intent intent = new Intent(this, LoginActivity.class);
//			startActivity(intent);
//			finish();
		}
		switch (resultCode) {
//			case HttpStaticApi.updateGeTuiClientID:
//				break;
		}
	}



	@Override
	public void onLoadingHttp(long total, long current, boolean isUploading) {
	}

	@Override
	public void onFailureHttp(HttpException error, String msg) {
	}

	@Override
	public void onStartHttp() {
	}

	
}
