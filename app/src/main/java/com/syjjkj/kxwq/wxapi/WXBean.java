package com.syjjkj.kxwq.wxapi;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

public class WXBean {
	// 自己微信应用的 appId
	public static String WX_APP_ID = "wx9d744cbedca5d6f1";

	public static String MCH_ID = "1483357092";

		
		public static String WX_CODE = "";

		public static IWXAPI wxApi;
		public static boolean isWXLogin = false;
		public static void WXLogout(){
			isWXLogin = false;
		}
		/**
		 * 注册appid
		 * @param context
		 */
		public static void registerWXApi(Context context){
			WXBean.wxApi = WXAPIFactory.createWXAPI(context, WXBean.WX_APP_ID, true);
			WXBean.wxApi.registerApp(WXBean.WX_APP_ID);
		}
		/**
		 * 调起微信
		 */
		public static void sendReq(BaseReq req){
			WXBean.wxApi.sendReq(req);


//			PayReq
		}
	//获取微信
		public static boolean isWeixinAvilible(Context context) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}
		return false;
	}
}
