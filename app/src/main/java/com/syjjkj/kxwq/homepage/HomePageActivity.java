package com.syjjkj.kxwq.homepage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.account.FragmentAccount;
import com.syjjkj.kxwq.actives.fragmentactives;
import com.syjjkj.kxwq.adapter.HomePageFragmentAdapter;
import com.syjjkj.kxwq.card.fragmentcard;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.DownCallback;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.kefang.Fragmentkefang;
import com.syjjkj.kxwq.myview.LazyViewPager;
import com.syjjkj.kxwq.myview.LazyViewPager.OnPageChangeListener;
import com.syjjkj.kxwq.ticket.FragmentTicket;
import com.syjjkj.kxwq.util.FileOperation;
import com.syjjkj.kxwq.util.ImageOperate;
import com.syjjkj.kxwq.util.LogUtil;
import com.syjjkj.kxwq.util.ScreenUtil;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;
import com.syjjkj.kxwq.util.UpdateApkUtils;
import com.syjjkj.kxwq.util.Utils;
import com.syjjkj.kxwq.widget.NotificationProgress;

import org.json.JSONException;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 首页页面
 *
 * @author Administrator
 *
 */
public class HomePageActivity extends BaseFragmentActivity implements
		OnCheckedChangeListener,DownCallback {
	private static final String TAG = "HomePageActivity";
	private LazyViewPager mPager;// 自定义ViewPager实现滚动
	private RadioGroup mTabRg;// 底部导航
	private ArrayList<Fragment> fragmentsList;// 装Fragment的集合
	//private FragmentDynamicKX fragmentDynamicKX;
	private Fragmentkefang fragmentkefang;
	//	private BookedCarFragment bookedFragment;
//	private SettingFragment settingFragment;
	private FragmentTicket fragmentTicket;
	//private FragmentMember fragmentMember;
	private FragmentAccount fragmentAccount;
	private fragmentactives fragmentactives;
	private fragmentcard fragmentcard;
	//	private MessageFragment messageFragment;
//	private ContactFragment contractFragment;
	// 当前fragment的index
	private int currentTabIndex;
	// 环信
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;
	//	private NewMessageBroadcastReceiver msgReceiver;
	private boolean flag;
	public static HomePageActivity homepageActivity;
	private ImageView ivUnReadMsg;
	private TextView tvUnReadMsg1;
	private TextView tvUnReadMsg2;
	public NotificationProgress notiPro;//notification的进度栏

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
			PushManager.getInstance().initialize(this.getApplicationContext(), KxwqPushService.class);*/
		String clientId = PushManager.getInstance().getClientid(this);
		LogUtil.e("个推ID:"+clientId);
		initGeTui(clientId);
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
				false)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理

			finish();
			// startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			finish();
			// startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		setContentView(R.layout.home_page);
		homepageActivity = this;
		initView();
		initListener();
		initDate();
		initGeTui(clientId);
		hxData();

		contactPeople();

		// 发送检查所有文件夹媒体文件指令
		//ScanMidia();
		LogUtil.e("HomeActivity.class oncreate  启动");
		//从微信支付界面返回的时候 跳转到批发或者是预定的界面
		Intent reIntent=getIntent();
		if(reIntent!=null&&reIntent.hasExtra("activity")&&reIntent.hasExtra("isPayOK")){
			if(reIntent.getStringExtra("activity").equals("WXPayEntryActivity")){
				if(reIntent.getBooleanExtra("isPayOK", false)==false){
					mPager.setCurrentItem(0);//跳转到批发的界面
				}else if(reIntent.getBooleanExtra("isPayOK", false)==true){
					mPager.setCurrentItem(1);//跳转到预定的界面
				}
			}
		}
	}
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{

		if (requestCode == REQUEST_EXTERNAL_STORAGE)
		{
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				/**
				 * 下载线程
				 */
						showWaitDialog("正在下载APK...");
						String path = Environment
								.getExternalStorageDirectory()
								.getPath()
								+ "/kxwq.apk";
						AnsynHttpRequest.downloadFile(MyApplication.getInstance(), UrlConfig.GET_APK, path, new DownCallback() {
							@Override
							public void onStartDownHttp() {

							}

							@Override
							public void onLoadingDownHttp(long total, long current, boolean isUploading) {
								if (isUploading == false) {
									if (tvPercent != null && total != 0) {
										DecimalFormat df = new DecimalFormat("0");
										tvPercent.setText(String.valueOf(df.format((Double
												.valueOf(current) / Double.valueOf(total) * 100)))
												+ "%");
										// 更改文字和进度
										notiPro.setNoti(String.valueOf(df.format((Double
												.valueOf(current) / Double.valueOf(total) * 100)))
												+ "%", Integer.valueOf(df.format((Double
												.valueOf(current) / Double.valueOf(total) * 100))));
									}
								}
							}

							@Override
							public void onSuccessDownHttp(File responseInfo, int resultCode) {
								dismissDialog();
								switch (resultCode) {
									case HttpStaticApi.get_apk:
										if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
											Uri apkUri = FileProvider.getUriForFile(HomePageActivity.this, "com.syjjkj.kxwq.fileprovider", responseInfo);//在AndroidManifest中的android:authorities值
											Intent install = new Intent(Intent.ACTION_VIEW);
											install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
											install.setDataAndType(apkUri, "application/vnd.android.package-archive");
											HomePageActivity.this.startActivity(install);
										} else {
											Intent intent = new Intent();
											intent.setAction(android.content.Intent.ACTION_VIEW);
											String fileName=responseInfo.getPath().trim();
											intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
											HomePageActivity.this.startActivity(intent);
										}
										break;
									default:
										break;
								}
							}

							@Override
							public void onFailureDownHttp(HttpException error, String msg)
							{}}, HttpStaticApi.get_apk);
						notiPro=new NotificationProgress(HomePageActivity.this);
						UserInfoBean.setUpdateAPK(false);//设置不能更新APK

			}
			else
			{
				// Permission Denied
				ToastUtil.makeShortText(this, "您拒绝了权限的授予,无法更新");
			}
			return;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	// 更新个推clientid
	private void initGeTui(String clientId) {
		if (!StringUtil.isNetworkConnected(this)) {
			ToastUtil.makeShortText(this, "请检查网络");
			return;
		}
		RequestParams params = new RequestParams();
		HttpUtils httpUtils = new HttpUtils();
		params.addBodyParameter(ParserJsonBean.UID,UserInfoBean.getUid(this));
		params.addBodyParameter(ParserJsonBean.TOKEN,UserInfoBean.getToken(this));
		params.addBodyParameter("cid",clientId);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.UPDATE_CID, params,
				this, httpUtils, HttpStaticApi.update_cid);
	}

	@SuppressWarnings("unchecked")
	public void onSuccessHttp(String responseInfo, int resultCode) {
		/*if(responseInfo.equals("{\"state\":-1,\"message\":\"token\\u503c\\u4e0d\\u6b63\\u786e\"}"))
		{
			UserInfoBean.userLogout(this);
			MyApplication.getInstance().exit();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		LogUtil.e("头像返回:"+responseInfo);*/
		super.onSuccessHttp(responseInfo, resultCode);
		switch (resultCode) {
			case HttpStaticApi.logoEdit:
				ToastUtil.makeShortText(this, "更新成功,请稍后");
				break;
			case HttpStaticApi.update_cid:
				break;
			case HttpStaticApi.get_apk_version:
			try {
				Bundle bundle = ParserJsonBean.parserAPKVersion(responseInfo);
					if (bundle != null) {
						int state = bundle.getInt(ParserJsonBean.STATE);
						if (state == 1) {
							int version = bundle.getInt("version",0);
							if (UpdateApkUtils.getAPPVersionCodeFromAPP(this)<version&&UserInfoBean.getUpdateAPK()==true) {
								UpdateApkUtils.updateAPK(this);
							}
						} else {
							ToastUtil.makeLongText(this,
								bundle.getString(ParserJsonBean.MESSAGE));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case HttpStaticApi.red_dot_num:
				try {
					Bundle bundle = ParserJsonBean.parserRedDotNum(responseInfo);
					if (bundle != null) {
						if (bundle.getInt(ParserJsonBean.STATE) == 1) {

//							strPhone=bundle.getString(ParserJsonBean.PHONE,"");
//							strLevel=bundle.getString(ParserJsonBean.LEVEL,"");
//							strPv=bundle.getString(ParserJsonBean.PV,"");
//							UserInfoBean.saveLogo(getActivity(),bundle.getString(ParserJsonBean.LOGO,""));
						String strNum1=bundle.getString("num1","0");
							String strNum2=bundle.getString("num2","0");
							if (Integer.parseInt(strNum1)>0){
								tvUnReadMsg1.setVisibility(View.VISIBLE);
							}else {
								tvUnReadMsg1.setVisibility(View.GONE);
							}
							if (Integer.parseInt(strNum2)>0){
								tvUnReadMsg2.setVisibility(View.VISIBLE);
							}else {
								tvUnReadMsg2.setVisibility(View.GONE);
							}

						} else {
//							ToastUtil.makeShortText(this,
//									bundle.getString(ParserJsonBean.MESSAGE));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;


		}
	}
	/**
	 * 这个是将保存的文件（图片更新）否则在相册中不立即显示。但是android4.3以上会有以上  现在使用MediaConnection
	 */
	private void ScanMidia()
	{
		File root = Environment.getExternalStorageDirectory();
		File files[] = root.listFiles();
		if(files != null){
			for (File f : files){
				if(f.isDirectory() && !f.isHidden())
				{
					Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
					scanIntent.setData(Uri.fromFile(f));
					sendBroadcast(scanIntent);
				}
			}
		}
	}
	private void initDate() {
		fragmentsList = new ArrayList<Fragment>();
		//fragmentDynamicKX=new FragmentDynamicKX();
		fragmentkefang=new Fragmentkefang();
		fragmentTicket=new FragmentTicket();
		fragmentactives=new fragmentactives();
		fragmentcard=new fragmentcard();
//		bookedFragment=new BookedCarFragment();
	//	fragmentMember=new FragmentMember();

//		contractFragment = new ContactFragment();
//		settingFragment = new SettingFragment();
		fragmentAccount=new FragmentAccount();

		fragmentsList.add(fragmentTicket);
		fragmentsList.add(fragmentcard);
	//	fragmentsList.add(fragmentMember);
		//fragmentsList.add(fragmentDynamicKX);
		fragmentsList.add(fragmentkefang);
		fragmentsList.add(fragmentactives);
//		fragmentsList.add(contractFragment);

		fragmentsList.add(fragmentAccount);

		mPager.setAdapter(new HomePageFragmentAdapter(
				getSupportFragmentManager(), fragmentsList));
		mPager.setCurrentItem(0);
		tvUnReadMsg1.setVisibility(View.GONE);
		tvUnReadMsg2.setVisibility(View.GONE);
	}

	private void initView() {
		MyApplication.getInstance().addActivity(this);
		mPager = (LazyViewPager) findViewById(R.id.vPager);
		mPager.setEnabled(false);
		mTabRg = (RadioGroup) findViewById(R.id.tab_rg_menu);
		int location = ScreenUtil.getScreenWidth(this);

		tvUnReadMsg1 = (TextView) findViewById(R.id.tv_unread_message_1);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(StringUtil.dip2px(this, 10),StringUtil.dip2px(this, 10));
		param.setMargins(location/2-location/10, 0, 0, 0);
		tvUnReadMsg1.setLayoutParams(param);



		tvUnReadMsg2 = (TextView) findViewById(R.id.tv_unread_message_2); //未读的求购消息
		RelativeLayout.LayoutParams paramS = new RelativeLayout.LayoutParams(StringUtil.dip2px(this, 20),StringUtil.dip2px(this, 20));
		paramS.setMargins(location/2-location/10, 0, 0, 0);
		tvUnReadMsg2.setLayoutParams(paramS);
	}

	private void initListener() {
		mTabRg.setOnCheckedChangeListener(this);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		getRedDotNum();
		switch (checkedId) {
			case R.id.tab_rb_1:
				mPager.setCurrentItem(0);
				break;
			case R.id.tab_rb_2:
				mPager.setCurrentItem(1);
				break;
			case R.id.tab_rb_3:
				mPager.setCurrentItem(2);
				break;
			case R.id.tab_rb_4:
				mPager.setCurrentItem(3);
				break;
			case R.id.tab_rb_5:
				mPager.setCurrentItem(4);
				break;
			default:
				break;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
				case 0:
					((RadioButton) mTabRg.getChildAt(0)).setChecked(true);
					break;
				case 1:
					((RadioButton) mTabRg.getChildAt(1)).setChecked(true);
					break;
				case 2:
					((RadioButton) mTabRg.getChildAt(2)).setChecked(true);
					break;
				case 3:
					((RadioButton) mTabRg.getChildAt(3)).setChecked(true);
					break;
				case 4:
					((RadioButton) mTabRg.getChildAt(4)).setChecked(true);
					break;

			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		refreshRedState();
		UpdateApkUtils.getAPKVersion(this);
//		getRedDotNum();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
				case Utils.PHOTORESOULT://这里是是账户Fragment上传头像的结果
						Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap photo = extras.getParcelable("data");
						String uid = UserInfoBean.getUid(this);
						String token = UserInfoBean.getToken(this);
						RequestParams params = new RequestParams();
						params.addBodyParameter("uid", uid);
						params.addBodyParameter("token", token);
						File file = FileOperation.saveImg(this,
								ImageOperate.getBitmapByte(photo, "png"));
						if (file != null) {
							if("logo".equals(ParserJsonBean.LOGO)){
								params.addBodyParameter("logo", file);
							}else if(ParserJsonBean.BANNER.equals(ParserJsonBean.LOGO)){
								params.addBodyParameter(ParserJsonBean.BANNER, file);
							}
						}
						AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.UPLOADPIC,params,
								this, httpUtils, HttpStaticApi.logoEdit);
							/*/*//*AnsynHttpRequest.uploadFile(this,
									UrlConfig.UPLOADPIC, uid, token,
									ParserJsonBean.LOGO, photo, this,
									HttpStaticApi.logoEdit);*/
							LogUtil.e("头像上传1");
						}
					break;
			}

	}

	/**
	 * 返回事件
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				new AlertDialog.Builder(this)
						.setTitle("退出系统")
						.setMessage("请确认是否退出")
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
														int which) {
									}
								})
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int whichButton) {
										HomePageActivity.this.finish();
									}
								}).show();
			}
			return false;
		}
		return super.dispatchKeyEvent(event);

	}

	private void hxData() {

		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}


	}
	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;

		String st = getResources().getString(R.string.Logoff_notification);
		if (!HomePageActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							HomePageActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();
								conflictBuilder = null;
								finish();
								// startActivity(new Intent(MainActivity.this,
								// LoginActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {

			}
		}
	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;

		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!HomePageActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(
							HomePageActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();
								accountRemovedBuilder = null;
								finish();
								// startActivity(new Intent(MainActivity.this,
								// LoginActivity.class));
							}
						});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {

			}

		}

	}


















	private void contactPeople() {
		// String url = String.format(UrlConfig.YH_CONTACTPEOPLE, application
		// .getUserinfobean().getUser_id(), application.getUserinfobean()
		// .getToken());
		// AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, this, url,
		// map,
		// this, MyApplication.getRequestQueue(this),
		// HttpStaticApi.HTTP_CONTACTPEOPLE);
	}

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	public void onStartDownHttp() {

	}

	@Override
	public void onLoadingDownHttp(long total, long current, boolean isUploading) {

	}

	@Override
	public void onSuccessDownHttp(File responseInfo, int resultCode) {

	}

	@Override
	public void onFailureDownHttp(HttpException error, String msg) {

	}
	/**
	 * 控制底部导航，“消息”上的红点状态
	 */
//	public void refreshRedState(){
//		try {//控制底部导航，“消息”红点显示
//			List<String> list = EMChatManager.getInstance().getConversationsUnread();
//			if (list.size() > 0) {
//				ivUnReadMsg.setVisibility(View.VISIBLE);
//			} else {
//				ivUnReadMsg.setVisibility(View.GONE);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	// 底部红点
	private void getRedDotNum() {
		if (!StringUtil.isNetworkConnected(this)) {
			ToastUtil.makeShortText(this, "请检查网络");
			return;
		}
		UserInfoBean.getUserInfo(this);
		params = new RequestParams();
		params.addBodyParameter(ParserJsonBean.UID,UserInfoBean.uid);
		params.addBodyParameter(ParserJsonBean.TOKEN, UserInfoBean.token);
//		showWaitDialog("正在努力加载...");

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.RED_DOT_NUM, params,
				this, httpUtils, HttpStaticApi.red_dot_num);
	}
}
