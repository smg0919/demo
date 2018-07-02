package com.syjjkj.kxwq.homepage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.syjjkj.kxwq.util.ToastUtil;

public class UserInfoBean {

	public static String uid = "";
	public static String phone = "";
	public static String token = "";
	public static boolean isLogin = false;
	public static String direct_type="";
	public static String name = "";
	public static String logo = "";
	public static String userLogo = "";
	public static String usersuoshu="";
	public static String signature = "";// 个性签名
	public static String city = "";// 城市id（未填0）
	public static String cname = "";// 城市城市名
	public static String company_id = "";// 公司id（未填0）
	public static String abbreviation = "";// 公司名
	public static String company_yes = "";// 是否创建公司
	
	public static String orderid = "";// 订单
	public static String prepayid = "";// 预付单
	/**
	 * 在SpalishActivity设置为true    在CarFragment页面取消的时候设置为false 
	 */
	private static boolean isUpdateAPK=false;//是否更新APK false不更新 true 更新

	public static boolean getUpdateAPK() {
		return isUpdateAPK;
	}

	public static void setUpdateAPK(boolean isUpdateAPK) {
		UserInfoBean.isUpdateAPK = isUpdateAPK;
	}

	public static String getCompany_id() {
		return company_id;
	}

	public static void setCompany_id(String company_id) {
		UserInfoBean.company_id = company_id;
	}

	/**
	 * 注销登录后，清空用户信息
	 */
	public static void userLogout(Context context) {
		uid = "";
		phone = "";
		token = "";
		name = "";
		logo = "";
		usersuoshu="";
		userLogo = "";
		isLogin = false;
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("uid", "");
		editor.putString("phone", "");
		editor.putString("token", "");
		editor.putString("name", "");
		editor.putString("logo", "");
		editor.putString("userLogo", "");
		editor.putString("","");
		editor.clear();
		editor.commit();
		
	}

	/**
	 * 获取用户信息
	 * 
	 * @param context
	 */
	public static void getUserInfo(Context context) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		if (login_sp.contains("uid")) {
			uid = login_sp.getString("uid", "");
		}
		if (login_sp.contains("phone")) {
			phone = login_sp.getString("phone", "");
		}
		if (login_sp.contains("token")) {
			token = login_sp.getString("token", "");
		}
		if (login_sp.contains("isLogin")) {
			isLogin = login_sp.getBoolean("isLogin", false);
		}
		if (login_sp.contains("name")) {
			name = login_sp.getString("name", "");
		}
		if (login_sp.contains("logo")) {
			logo = login_sp.getString("logo", "");
		}
		if (login_sp.contains("userLogo")) {
			userLogo = login_sp.getString("userLogo", "");
		}

		if (login_sp.contains("signature")) {
			signature = login_sp.getString("signature", "");
		}
		if (login_sp.contains("city")) {
			city = login_sp.getString("city", "");
		}
		if (login_sp.contains("cname")) {
			cname = login_sp.getString("cname", "");
		}
		if (login_sp.contains("company_id")) {
			company_id = login_sp.getString("company_id", "");
		}
		if (login_sp.contains("abbreviation")) {
			abbreviation = login_sp.getString("abbreviation", "");
		}
		if (login_sp.contains("yes")) {
			company_yes = login_sp.getString("yes", "");
		}
		if (login_sp.contains("direct_type")) {
			direct_type = login_sp.getString("direct_type", "");
		}

	}

	/**
	 * 保存登录信息
	 * 
	 * @param context
	 * @param userId
	 * @param phone
	 * @param token
	 */
	public static void saveUserInfo(Context context, String userId,
									String phone, String token, String name, String logo,String direct_type) {

		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("uid", userId);
		editor.putString("phone", phone);
		editor.putString("token", token);
		editor.putString("name", name);
		editor.putString("logo", logo);
		editor.commit();
	}
	public static void saveUserInfoups(Context context, String userId,
									String phone, String token,String direct_type) {

		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("uid", userId);
		editor.putString("phone", phone);
		editor.putString("token", token);
		editor.putString("direct_type",direct_type);
		editor.commit();
	}

	/**
	 * 保存登录后，公司和权限信息
	 *
	 *            company_id公司id abbreviation公司简称 sales查看寄售1否2是 publish发布车辆1否2是
	 *            upset查看底价1否2是 pricing定价改价1否2是
	 */
	public static void saveUserInfo2(Context context, String company_id,
									 String abbreviation, String sales, String publish, String upset,
									 String pricing) {

		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("company_id", company_id);
		editor.putString("abbreviation", abbreviation);
		editor.putString("sales", sales);
		editor.putString("publish", publish);
		editor.putString("upset", upset);
		editor.putString("pricing", pricing);
		editor.commit();
	}
	/**
	 * 保存登录后，公司和权限信息
	 * pricing定价改价1否2是
	 */
	public static void savePricing(Context context,
									 String pricing) {

		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("pricing", pricing);
		editor.commit();
	}
	/**
	 * 保存个人资料信息
	 * 
	 * @param context
	 * @param name
	 *            名字
	 * @param signature
	 *            个性签名
	 * @param city
	 *            城市id（未填0）
	 * @param cname
	 *            城市名
	 * @param company_id
	 *            公司id（未填0）
	 * @param abbreviation
	 *            公司名
	 */
	public static void savePersonInfo(Context context, String name,
									  String logo, String signature, String city, String cname,
									  String company_id, String abbreviation) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("name", name);
		editor.putString("signature", signature);
		editor.putString("logo", logo);
		editor.putString("city", city);
		editor.putString("cname", cname);
		editor.putString("company_id", company_id);
		editor.putString("abbreviation", abbreviation);

		editor.commit();
	}

	/**
	 * 保存是否登录
	 * 
	 * @param context
	 * @param isLogin
	 */
	public static void saveIsLogin(Context context, boolean isLogin) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putBoolean("isLogin", isLogin);
		editor.commit();
	}

	/**
	 * 保存公司简称
	 * 
	 * @param context
	 * @param
	 */
	public static void saveAbbreviation(Context context, String abbreviation) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("abbreviation", abbreviation);
		editor.commit();
	}

	/**
	 * 我的头像
	 * 
	 * @param context
	 */
	public static void saveLogo(Context context, String logo) {
		if(context!=null)
		{
			SharedPreferences login_sp = context.getSharedPreferences("login_sp",
					Context.MODE_PRIVATE);
			Editor editor = login_sp.edit();
			editor.putString("logo", logo);
			editor.commit();
		}
		else
		{
			ToastUtil.makeShortText(context, "请重新上传头像");
		}

	}

	/**
	 * 是否创建公司
	 * 
	 * @param context
	 */
	public static void saveCompany_yes(Context context, String logo) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("yes", logo);
		editor.commit();
	}

	/**
	 * 我的微店banner
	 * 
	 * @param context
	 */
	public static void saveBannerEdit(Context context, String bannerEdit) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("bannerEdit", bannerEdit);
		editor.commit();
	}

	/**
	 * 公司id
	 * 
	 * @param context
	 */
	public static void saveCompanyID(Context context, String id) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("company_id", id);
		editor.commit();
	}

	/**
	 * 用户的姓名
	 * 
	 * @param context
	 * @param name
	 */
	public static void saveName(Context context, String name) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("name", name);
		editor.commit();
	}

	/**
	 * 城市名
	 * 
	 * @param context
	 * @param name
	 */
	public static void saveCName(Context context, String name) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("cname", name);
		editor.commit();
	}

	/**
	 * 个性签名
	 * 
	 * @param context
	 * @param name
	 */
	public static void saveSignature(Context context, String name) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("signature", name);
		editor.commit();
	}

	/**
	 * 性别
	 * 
	 * @param context
	 * @param gender
	 */
	public static void saveGender(Context context, String gender) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("gender", gender);
		editor.commit();
	}

	/**
	 * 用户token
	 * 
	 * @param context
	 * @param
	 */
	public static void saveToken(Context context, String token) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("token", token);
		editor.commit();
	}

	/**
	 * 4.31更新车型库
	 * 
	 * @param context
	 * @param
	 */
	public static void saveBand(Context context, String title) {
		SharedPreferences login_sp = context.getSharedPreferences(
				"band_version", Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putString("title", title);
		editor.commit();
	}

	public static String getUid(Context context) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		return login_sp.getString("uid", "");
	}

	/**
	 * 用户手机号，即账号
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhone(Context context) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		return login_sp.getString("phone", "");
	}

	public static String getToken(Context context) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		return login_sp.getString("token", "");
	}

	public static String getLogo(Context context) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		return login_sp.getString("logo", "");
	}

	public static String getCompanyID(Context context) {
		SharedPreferences login_sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		return login_sp.getString("company_id", "");
	}

	/**
	 * 4.31更新车型库
	 * 
	 * @param context
	 * @return
	 */
	public static String getBandVersion(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"band_version", Context.MODE_PRIVATE);
		return band_version.getString("title", "");
	}

	/**
	 * 4.28更换banner 得到banner的url
	 * 
	 * @param context
	 * @return
	 */
	public static String getBannerEdit(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"login_sp", Context.MODE_PRIVATE);
		return band_version.getString("bannerEdit", "");
	}

	/**
	 * 获取用户名 得到banner的url
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"login_sp", Context.MODE_PRIVATE);
		return band_version.getString("name", "");
	}
	/**
	 * 获取定价改价权限信息 pricing
	 *1否2是
	 * @param context
	 * @return
	 */
	public static String getPricing(Context context) {
		SharedPreferences pricing = context.getSharedPreferences(
				"login_sp", Context.MODE_PRIVATE);
		return pricing.getString("pricing", "");
	}

	/**
	 * 设置聊天背景path
	 * 
	 * @param context
	 * @param path
	 * @param i
	 *            0 照相，1相册
	 */

	public static void saveChatbg(Context context, String path, int i) {

		SharedPreferences chat_sp = context.getSharedPreferences("chat_sp",
				Context.MODE_PRIVATE);
		Editor editor = chat_sp.edit();
		editor.putString("chat_bg_path", path);
		editor.putInt("chat_style", i);
		editor.commit();

	}

	/**
	 * 获取聊天背景path
	 * 
	 * @param context
	 * @return 返回2 为默认
	 */
	public static int getChatbgStyle(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"chat_sp", Context.MODE_PRIVATE);
		return band_version.getInt("chat_style", 2);
	}

	/**
	 * 获取聊天背景path
	 * 
	 * @param context
	 * @return
	 */
	public static String getChatbg(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"chat_sp", Context.MODE_PRIVATE);
		return band_version.getString("chat_bg_path", "");
	}

	public static void setChatTop(Context context, String id) {
		SharedPreferences sp = context.getSharedPreferences("chat_sp",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("chattop", id);
		editor.commit();
	}

	public static String getChatTop(Context context) {
		SharedPreferences sp = context.getSharedPreferences("chat_sp",
				Context.MODE_PRIVATE);
		return sp.getString("chattop", "");
	}

	public static void canlceChatTop(Context context) {
		SharedPreferences sp = context.getSharedPreferences("chat_sp",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("chattop", "");
		editor.commit();
	}
	
	/**
	 *城市名
	 * 
	 * @param context
	 * @return
	 */
	public static String getCityName(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"chat_sp", Context.MODE_PRIVATE);
		return band_version.getString("city_name", "");
	}

	public static void setCityName(Context context, String city_name) {
		SharedPreferences sp = context.getSharedPreferences("chat_sp",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("city_name", city_name);
		editor.commit();
	}
	public static void setUserLogo(Context context, String userLogo) {
		SharedPreferences sp = context.getSharedPreferences("login_sp",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("userLogo", userLogo);
		editor.commit();
	}
	/**
	 *城市id
	 * 
	 * @param context
	 * @return
	 */
	public static String getCityId(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"chat_sp", Context.MODE_PRIVATE);
		return band_version.getString("city", "");
	}

	public static void setCityId(Context context, String cityId) {
		SharedPreferences sp = context.getSharedPreferences("chat_sp",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("city", cityId);
		editor.commit();
	}
	/**
	 * 微信支付的订单
	 * @param context
	 * @param orderid
	 */
	public static void setOrderId(Context context, String orderid) {
		SharedPreferences sp = context.getSharedPreferences("pay_order",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("orderid", orderid);
		editor.commit();
	}
	public static String getOrderId(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"pay_order", Context.MODE_PRIVATE);
		return band_version.getString("orderid", "");
	}
	/**
	 * 预付单
	 * @param context
	 * @param prepayId
	 */
	public static void setPrepayId(Context context, String prepayId) {
		SharedPreferences sp = context.getSharedPreferences("pay_order",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("prepayid", prepayId);
		editor.commit();
	}
	public static String getPrepayId(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"pay_order", Context.MODE_PRIVATE);
		return band_version.getString("prepayid", "");
	}
	public static void saveTicketstypeCount(Context context, int iTabCount) {

		SharedPreferences login_sp = context.getSharedPreferences("ticket_info",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putInt("iTabCount",iTabCount);
		editor.commit();
	}
	public static int getTicketstypeCount(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"ticket_info", Context.MODE_PRIVATE);
		return band_version.getInt("iTabCount", 1);
	}

	public static void savePvRate(Context context, int iRate) {

		SharedPreferences login_sp = context.getSharedPreferences("ticket_info",
				Context.MODE_PRIVATE);
		Editor editor = login_sp.edit();
		editor.putInt("iRate",iRate);
		editor.commit();
	}
	public static int getPvRate(Context context) {
		SharedPreferences band_version = context.getSharedPreferences(
				"ticket_info", Context.MODE_PRIVATE);
		return band_version.getInt("iRate", 0);
	}
}
