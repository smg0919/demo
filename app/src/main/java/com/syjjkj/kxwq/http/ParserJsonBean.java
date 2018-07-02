package com.syjjkj.kxwq.http;

import android.os.Bundle;
import android.util.Log;

import com.syjjkj.kxwq.homepage.MyApplication;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ParserJsonBean {
	public static final String STATE = "state";
	public static final String MESSAGE = "message";
	public static final String URL = "url";
	public static final String UID = "uid";
	public static final String ACCOUNT = "account";
	public static final String PHONE = "phone";
	public static final String TOKEN = "token";
	public static final String NAME="name";
	public static final String OPENTIME = "opentime";
	public static final String  need_idcard="need_idcard";
	public static String  need_idcards="";
	public static String  opentime="";
	public static String  desc1="";
	public static String  desc2="";
	public static final String des1 = "des1";
	public static final String des2 = "des2";
	public static String  product_details="";
	public static String  product_tipss="";
	public static final String product_detail="product_detail";
	public static final String product_tips="product_tips";
	public static final String CODE = "code";
	public static final String PASSWORD = "password";
	public static final String LIST = "list";
	public static final String ID = "id";
	public static final String NEWPASS = "newpass";
	public static final String CONTENT = "content";
	public static final String UNAME = "uname";
	public static final String TYPE = "type";
	public static final String USERS = "users";
	public static final String ABBREVIATION = "abbreviation";
	public static final String CITY = "city";
	public static final String ADD_TIME = "add_time";
	public static final String CNAME = "cname";
	public static final String NUMBER = "number";
	public static final String INTRO = "intro";
	public static final String USER_ID = "user_id";
	public static final String ATTENTION = "attention";
	public static final String PIC = "pic";
	public static final String VERSION = "version";
	public static final String VERSION_ID = "version_id";
	public static final String REGISTRATION = "registration";
	public static final String KILOMETRE = "kilometre";
	public static final String GENRE = "genre";
	public static final String SERIES = "series";
	public static final String SERIES_ID = "series_id";
	public static final String FILTER_INFO = "filter_info";
	public static final String CNT = "cnt";
	public static final String UPDATETIME = "updateTime";
	public static final String VIEW_CNT = "view_cnt";
	public static final String sel_date="sel_date";
	public static final String play_date="play_date";
	public static String  sel_dates="";
	public static final String NICKNAME = "nickName";
	public static final String PV = "pv";
	public static final String LEVEL = "level";
	public static final String LOGO = "logo";
	public static final String direct_type="direct_type";
	public static final String lxs = "lxs";

	/**
	 * 展厅标价
	 */
	public static final String EXHIBITION = "exhibition";
	/**
	 * 网络标价
	 */
	public static final String ONLINE = "online";
	/**
	 * 销售底价
	 */
	public static final String MARKET = "market";
	/**
	 * 成本价格
	 */
	public static final String COSTING = "costing";
	public static final String COMPANY_ID = "company_id";
	public static final String COMPANY = "company";
	public static final String BANNER = "banner";
	public static final String SIGNATURE = "signature";
	public static final String IS_FRIEND = "is_friend";
	public static final String REPLY = "reply";
	public static final String NUM = "num";
	public static final String PATH = "path";
	
	public static final String PUSH_TITLE = "title";
	public static final String PUSH_CONTENT = "content";
	/**
	 * @author zuozhiyu
	 * 底价查看开放权限1是0否
	 */
	public static final String FLOOR_PRICE_POWER = "show_floor";
	/**
	 * @author zuozhiyu
	 * 我关注的车  里面的底价查看
	 */
	public static final String FLOOR_PRICE_POWER_ATTENTION = "floor";
	public static final String TIME = "time";
	public static final String INFO = "info";
	public static final String PRICE = "price";
	public static final String DEFAULTPHONE = "13555880573";//默认的是李岸电话


	public static final String MONEY= "money";
	public static final String BANK_TYPE = "bank_type";
	public static final String BANK_NAME = "bank_name";
	public static final String BANK_NUM = "bank_num";
	public static final String BANK_ADDR = "bank_addr";
	public static final String POST_TIME = "post_time";

	public static final String VISIBLE = "visible";
	public static final String PRODUCT_NAME = "product_name";

	public static final String RATE = "rate";
	public static final String EXPIRED = "expired";

	public static final String SID = "sid";
	/**
	 * 判断返回的结果是否是成功的.初步解析result和message,如果成功(result!=0)返回true,调用该方法的代码应该继续解析data
	 * .否则,返回false,data不用再解析.
	 * 
	 * @param bundle
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private static boolean isResultSuccess(Bundle bundle, JSONObject jsonObject)
			throws JSONException {
		boolean isSuc = false;
		if (jsonObject.has(STATE)) {
			int state = jsonObject.optInt(STATE);
			bundle.putInt(STATE, state);
			if (state == 1) {
				isSuc = true;
			}
			if (jsonObject.has(MESSAGE)) {
				bundle.putString(MESSAGE, jsonObject.optString(MESSAGE));
			}
		}
		return isSuc;
	}

	/**
	 * 1.4所在的学校
	 * 
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */

	// public static Bundle parserSchool(String strJson) throws JSONException {
	// JSONObject jsonObject = null; Bundle bundle = new Bundle(); try {
	// jsonObject = new JSONObject(strJson); if (!isResultSuccess(bundle,
	// jsonObject)) {// 如果result==0,不再解析data,直接返回bundle; return bundle; } }
	// catch (JSONException e) { e.printStackTrace(); } if
	// (jsonObject.has(SCHOOL)) { JSONArray jsonArray =
	// jsonObject.getJSONArray(SCHOOL); ArrayList<String> schools = new
	// ArrayList<String>(); for (int i = 0; i < jsonArray.length(); i++) {
	// schools.add(jsonArray.getString(i)); } bundle.putSerializable(SCHOOL,
	// schools); }
	//
	// return bundle; }

	/**
	 * 返回参数只有STATE和MESSAGE时共用的一个一个方法
	 * 
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserPublic(String strJson) throws JSONException {
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}
	/**
	 * 解析CarFragment界面访问APK版本号
	 * 
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserAPKVersion(String strJson) throws JSONException {
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject.has("version")) {
			int str = jsonObject.getInt("version");
			bundle.putInt("version", str);
		}
		
		return bundle;
	}


	/**
	 * 1.1引导页
	 * 
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserStart(String strJson) throws JSONException {
		Log.e("Log", "1.1引导页="+strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject.has(URL)) {
			String url = jsonObject.getString(URL);
			bundle.putString(URL, url);
		}
		return bundle;
	}

	/**
	 * 1.2登陆页
	 * 
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserDoLogin(String strJson) throws JSONException {
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject.has(UID)) {
			String uid = jsonObject.getString(UID);
			bundle.putString(UID, uid);
		}
		if (jsonObject.has(PHONE)) {
			String phone = jsonObject.getString(PHONE);
			bundle.putString(PHONE, phone);
		}

		if (jsonObject.has(TOKEN)) {
			String token = jsonObject.getString(TOKEN);
			bundle.putString(TOKEN, token);
		}
		if (jsonObject.has(RATE)) {
			bundle.putInt(RATE, jsonObject.getInt(RATE));
		}
		if(jsonObject.has(direct_type))
		{
			bundle.putString(direct_type,jsonObject.getString(direct_type));
		}

		return bundle;
	}
	/**
	 * 1.2登陆页
	 *
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserRegisterUser(String strJson) throws JSONException {
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject.has(UID)) {
			String uid = jsonObject.getString(UID);
			bundle.putString(UID, uid);
		}


		return bundle;
	}
	/**
	 * 获取个人账户信息
	 *
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserMyInfo(String strJson) throws JSONException {
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject.has(UID)) {
			String uid = jsonObject.getString(UID);
			bundle.putString(UID, uid);
		}
		if (jsonObject.has(PHONE)) {
			bundle.putString(PHONE, jsonObject.getString(PHONE));
		}
		if (jsonObject.has(NICKNAME)) {
			bundle.putString(NICKNAME, jsonObject.getString(NICKNAME));
		}
		if (jsonObject.has(PV)) {
			bundle.putString(PV, jsonObject.getString(PV));
		}
		if (jsonObject.has(LEVEL)) {
			bundle.putString(LEVEL, jsonObject.getString(LEVEL));
		}
		if (jsonObject.has(LOGO)) {
			bundle.putString(LOGO, jsonObject.getString(LOGO));
		}
		if(jsonObject.has(direct_type))
		{
			bundle.putString(direct_type, jsonObject.getString(direct_type));
		}
		if(jsonObject.has(ACCOUNT))
		{
			bundle.putString(ACCOUNT, jsonObject.getString(ACCOUNT));
		}
		return bundle;
	}
	public static Bundle parserEditLogo(String strJson) throws JSONException {
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject.has(LOGO)) {
			bundle.putString(LOGO, jsonObject.getString(LOGO));
		}
		return bundle;
	}
	public static Bundle parseMaterialList(String strJson) throws JSONException {
		Log.e("log", "分享素材列表==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject!=null&&jsonObject.has(LIST)) {
			JSONArray array = jsonObject.optJSONArray(LIST);
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			JSONObject listObject = null;
			for (int i = 0; i < array.length(); i++) {
				listObject = array.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (listObject.has(ID)) {
					map.put(ID, listObject.getString(ID));
				}

				if (listObject.has(CONTENT)) {
					map.put(CONTENT, listObject.getString(CONTENT));
				}
				if (listObject.has(POST_TIME)) {
					map.put(POST_TIME, listObject.getString(POST_TIME));
				}
				if (listObject.has("pics")) {
					ArrayList<String> pics = new ArrayList<String>();
//					JSONArray arrays = listObject.getJSONArray("pics");
					String arrays = listObject.getString("pics");
					String []arrays1=arrays.split(",");
					for (int j = 0; j < arrays1.length; j++) {
						pics.add(UrlConfig.PIC_PATH+arrays1[j]);
					}
					//bundle.putStringArrayList("pics",pics);
					map.put("pics",pics);
				}


				list.add(map);
			}
			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}

	public static Bundle parseProductType(String strJson) throws JSONException {
		Log.e("log", "门票种类==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		int iTabCount=0;
		if (jsonObject!=null&&jsonObject.has(LIST)) {
			JSONArray array = jsonObject.optJSONArray(LIST);
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			JSONObject listObject = null;
			for (int i = 0; i < array.length(); i++) {
				listObject = array.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (listObject.has(ID)) {
					map.put(ID, listObject.getString(ID));
				}

				if (listObject.has(NAME)) {
					map.put(NAME, listObject.getString(NAME));
				}
				if(listObject.has(OPENTIME))
				{
					map.put(OPENTIME,listObject.getString(OPENTIME));
				}
				if(listObject.has(des1))
				{
					map.put(des1,listObject.getString(des1));
				}
				if(listObject.has(product_tips))
				{
					map.put(product_tips,listObject.getString(product_tips));
				}
				if(listObject.has(product_detail))
				{
					map.put(product_detail,listObject.getString(product_detail));
				}

				if(listObject.has(des2))
				{
					map.put(des2,listObject.getString(des2));
				}
				if (listObject.has(VISIBLE)) {
					map.put(VISIBLE, listObject.getString(VISIBLE));
					LogUtil.e( listObject.getString(VISIBLE));
					if (listObject.getString(VISIBLE).trim().equals("1")){
						iTabCount++;
					}
				}
				UserInfoBean.saveTicketstypeCount(MyApplication.getInstance(),iTabCount);
				list.add(map);
			}
			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}
	public static Bundle parseProduct1(String strJson) throws JSONException {
		Log.e("log", "门票1==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(jsonObject!=null&&jsonObject.has(OPENTIME))
		{
			opentime=(String) jsonObject.getString(OPENTIME);
		}
		if(jsonObject!=null&&jsonObject.has(des1))
		{
			desc1=(String)jsonObject.getString(des1);
		}
		if(jsonObject!=null&&jsonObject.has(product_detail))
		{
			product_details=(String)jsonObject.getString(product_detail);
		}
		if(jsonObject!=null&&jsonObject.has(product_tips))
		{
			product_tipss=(String)jsonObject.getString(product_tips);
		}
		if(jsonObject!=null&&jsonObject.has(des2))
		{
			desc2=(String)jsonObject.getString(des2);
		}
		if (jsonObject!=null&&jsonObject.has(LIST)) {
			JSONArray array = jsonObject.optJSONArray(LIST);
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			JSONObject listObject = null;
			for (int i = 0; i < array.length(); i++) {
				listObject = array.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (listObject.has(ID)) {
					map.put(ID, listObject.getString(ID));
				}

				if (listObject.has(PRODUCT_NAME)) {
					map.put(PRODUCT_NAME, listObject.getString(PRODUCT_NAME));
				}
				if (listObject.has(PRICE)) {
					map.put(PRICE, listObject.getString(PRICE));

				}
				if (listObject.has(EXPIRED)) {
					map.put(EXPIRED, listObject.getString(EXPIRED));

				}
				if (listObject.has(product_detail)) {
					map.put(product_detail, listObject.getString(product_detail));

				}
				if (listObject.has(product_tips)) {
					map.put(product_tips, listObject.getString(product_tips));

				}
				if(listObject.has(sel_date))
				{
					map.put(sel_date,listObject.getString(sel_date));
				}
				if(listObject.has(play_date))
				{
					map.put(play_date,listObject.getString(play_date));
				}
				if(listObject.has(need_idcard))
				{
					map.put(need_idcard,listObject.getString(need_idcard));
				}
				list.add(map);
			}
			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}
	public static Bundle parserQB(String strJson) throws JSONException {
		Log.e("log", "积分1==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject.has(UID)) {
			String uid = jsonObject.getString(UID);
			bundle.putString(UID, uid);
		}
		if (jsonObject.has(ACCOUNT)) {
			bundle.putString(ACCOUNT, jsonObject.getString(ACCOUNT));
		}
		return bundle;
	}
	public static Bundle parserBuyTickets(String strJson) throws JSONException {
		Log.e("log", "购买票==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject.has(ID)) {
			String uid = jsonObject.getString(ID);
			bundle.putString(ID, uid);
		}
		if (jsonObject.has(SID)) {
			bundle.putString(SID, jsonObject.getString(SID));
		}
		if(jsonObject.has("buy_type"))
		{
			bundle.putString("buy_type",jsonObject.getString("buy_type"));
		}
		return bundle;
	}
	public static Bundle parserBuycards(String strJson) throws JSONException {
		Log.e("log", "购买票==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject.has(ID)) {
			String uid = jsonObject.getString(ID);
			bundle.putString(ID, uid);
		}
		if (jsonObject.has(SID)) {
			bundle.putString(SID, jsonObject.getString(SID));
		}
		if(jsonObject.has("buy_type"))
		{
			bundle.putString("buy_type",jsonObject.getString("buy_type"));
		}
		if(jsonObject.has("money"))
		{
			bundle.putString("money",jsonObject.getString("money"));
		}
		return bundle;
	}
	public static Bundle parserdingfang(String strJson) throws JSONException {
		Log.e("log", "购买票==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject.has(ID)) {
			String uid = jsonObject.getString(ID);
			bundle.putString(ID, uid);
		}
		if(jsonObject.has("money"))
		{
			String strTotalPrice=jsonObject.getString("money");
			bundle.putString("money",strTotalPrice);
		}
		if(jsonObject.has("buy_type"))
		{
			bundle.putString("buy_type",jsonObject.getString("buy_type"));
		}
		return bundle;
	}
	public static Bundle parseBuyTicketsRecords(String strJson) throws JSONException {
		Log.e("log", "购票记录==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject!=null&&jsonObject.has(LIST)) {
			JSONArray array = jsonObject.optJSONArray(LIST);
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			JSONObject listObject = null;
			for (int i = 0; i < array.length(); i++) {
				listObject = array.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (listObject.has(ID)) {
					map.put(ID, listObject.getString(ID));
				}

				if (listObject.has(NAME)) {
					map.put(NAME, listObject.getString(NAME));
				}
				if (listObject.has(PHONE)) {
					map.put(PHONE, listObject.getString(PHONE));
				}
				if (listObject.has("real_price")) {
					map.put("real_price", listObject.getString("real_price"));
				}
				if (listObject.has("suborder")) {
//					map.put("suborder", listObject.getString("suborder"));
					JSONArray listObjectArray = listObject.optJSONArray("suborder");
					ArrayList<HashMap<String, Object>> arraylist = new ArrayList<HashMap<String, Object>>();
					JSONObject listObject1 = null;
					for(int j=0;j<listObjectArray.length();j++){
						listObject1 = listObjectArray.getJSONObject(j);
						HashMap<String, Object> map1 = new HashMap<String, Object>();
						if (listObject1.has("product_name")) {
							map1.put("product_name", listObject1.getString("product_name"));
						}
						if (listObject1.has("product_num")) {
							map1.put("product_num", listObject1.getString("product_num"));
						}
						if (listObject1.has("sid")) {
							map1.put("sid", listObject1.getString("sid"));
						}
						if(listObject1.has("product_per_price"))
						{
							map1.put("product_per_price", listObject1.getString("product_per_price"));
						}
						arraylist.add(map1);
					}
					map.put("suborder",arraylist);

				}
				if (listObject.has("order_time")) {
					map.put("order_time", listObject.getString("order_time"));
				}
				if (listObject.has("pay_status")) {
					map.put("pay_status", listObject.getString("pay_status"));
				}
				if (listObject.has("order_type")) {
					map.put("order_type", listObject.getString("order_type"));
				}
				if (listObject.has("fuzhuma"))
				{
					map.put("fuzhuma",listObject.getString("fuzhuma"));
				}
				if(listObject.has("order_tid"))
				{
					map.put("order_tid",listObject.getString("order_tid"));
				}



				list.add(map);
			}
			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}
	public static Bundle parseSalesRecords(String strJson) throws JSONException {
		Log.e("log", "下级购票记录==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject!=null&&jsonObject.has("list1")) {
			JSONArray array = jsonObject.optJSONArray("list1");
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			JSONObject listObject = null;
			for (int i = 0; i < array.length(); i++) {
				listObject = array.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (listObject.has(MONEY)) {
					map.put(MONEY, listObject.getString(MONEY));
				}

				if (listObject.has(NAME)) {
					map.put(NAME, listObject.getString(NAME));
				}
				if (listObject.has(PV)) {
					map.put(PV, listObject.getString(PV));
				}
				if (listObject.has(TIME)) {
					map.put(TIME, listObject.getString(TIME));
				}


				list.add(map);
			}
			bundle.putSerializable("list1", list);
		}
		if (jsonObject!=null&&jsonObject.has("list2")) {
			JSONArray array = jsonObject.optJSONArray("list2");
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			JSONObject listObject = null;
			for (int i = 0; i < array.length(); i++) {
				listObject = array.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (listObject.has(MONEY)) {
					map.put(MONEY, listObject.getString(MONEY));
				}

				if (listObject.has(NAME)) {
					map.put(NAME, listObject.getString(NAME));
				}
				if (listObject.has(PV)) {
					map.put(PV, listObject.getString(PV));
				}
				if (listObject.has(TIME)) {
					map.put(TIME, listObject.getString(TIME));
				}


				list.add(map);
			}
			bundle.putSerializable("list2", list);
		}
		return bundle;
	}

	public static Bundle parserRedDotNum(String strJson) throws JSONException {
		Log.e("log", "红点==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject.has("num1")) {
			bundle.putString("num1", jsonObject.getString("num1"));
		}
		if (jsonObject.has("num2")) {
			bundle.putString("num2", jsonObject.getString("num2"));
		}

		return bundle;
	}

	public static Bundle parserPushMessage(String strJson) throws JSONException {
		Log.e("Log", "1.8.x 推送消息=="+strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (jsonObject.has(PUSH_TITLE)) {
				bundle.putString(PUSH_TITLE, jsonObject.getString(PUSH_TITLE));
			}
			if (jsonObject.has(PUSH_CONTENT)) {
				bundle.putString(PUSH_CONTENT, jsonObject.getString(PUSH_CONTENT));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}
	/**
	 * 车源车辆详情
	 *
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserOrder(String strJson) throws JSONException {
		Log.e("log", "统一下单==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject!=null&&jsonObject.has("orderid")) {
			String id = jsonObject.getString("orderid");
			bundle.putString("orderid", id);
		}
		if (jsonObject.has("appid")) {
			String id = jsonObject.getString("appid");
			bundle.putString("appid", id);
		}
		if (jsonObject.has("partnerid")) {
			String version = jsonObject.getString("partnerid");
			bundle.putString("partnerid", version);
		}
		if (jsonObject.has("prepayid")) {
			String type = jsonObject.getString("prepayid");
			bundle.putString("prepayid", type);
		}
		if (jsonObject.has("package")) {
			String registration = jsonObject.getString("package");
			bundle.putString("package", registration);
		}
		if (jsonObject.has("noncestr")) {
			String kilometre = jsonObject.getString("noncestr");
			bundle.putString("noncestr", kilometre);
		}
		if (jsonObject.has("timestamp")) {
			String selling = jsonObject.getString("timestamp");
			bundle.putString("timestamp", selling);
		}
		if (jsonObject.has("sign")) {
			String add_time = jsonObject.getString("sign");
			bundle.putString("sign", add_time);
		}

		return bundle;
	}
	public static Bundle parseCheckResult(String strJson) throws JSONException {
		Log.e("log", "查询购票付款情况==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject.has(ID)) {
					bundle.putString(ID, jsonObject.getString(ID));
				}

		if (jsonObject.has(NAME)) {
			bundle.putString(NAME, jsonObject.getString(NAME));
		}
		if(jsonObject.has("usage"))
		{
			bundle.putString("usage", jsonObject.getString("usage"));
		}
		if (jsonObject.has(PHONE)) {
			bundle.putString(PHONE, jsonObject.getString(PHONE));
		}
		if(jsonObject.has("order_tid"))
		{
			bundle.putString("order_tid", jsonObject.getString("order_tid"));
		}
		if(jsonObject.has("room_name"))
		{
			bundle.putString("room_name", jsonObject.getString("room_name"));
		}
		if(jsonObject.has("paper_id"))
		{
			bundle.putString("paper_id", jsonObject.getString("paper_id"));
		}
		if (jsonObject.has("real_price")) {
			bundle.putString("real_price", jsonObject.getString("real_price"));
		}

				if (jsonObject.has("suborder")) {
//					map.put("suborder", listObject.getString("suborder"));
//					JSONArray listObjectArray = jsonObject.optJSONArray("suborder");
//					ArrayList<Bundle> arraylist = new ArrayList<Bundle>();
//					JSONObject listObject1 = null;
//					for(int j=0;j<listObjectArray.length();j++){
//						listObject1 = listObjectArray.getJSONObject(j);
//						Bundle bundle1 = new Bundle();
//						if (listObject1.has("product_name")) {
//							bundle1.putString("product_name", listObject1.getString("product_name"));
//						}
//						if (listObject1.has("product_num")) {
//							bundle1.putString("product_num", listObject1.getString("product_num"));
//						}
//
//						arraylist.add(bundle1);
//					}
//					bundle.putParcelableArrayList("suborder",arraylist);
//					JSONObject jsonObject1=jsonObject.getJSONObject("suborder");
					JSONObject jsonObject1=jsonObject.optJSONArray("suborder").getJSONObject(0);
					if (jsonObject1.has("product_name")){
						bundle.putString("product_name",jsonObject1.getString("product_name"));
					}
					if (jsonObject1.has("product_num")){
						bundle.putString("product_num",jsonObject1.getString("product_num"));
					}
				}
				if (jsonObject.has("order_time")) {
					bundle.putString("order_time", jsonObject.getString("order_time"));
				}
				if (jsonObject.has("pay_status")) {
					bundle.putString("pay_status", jsonObject.getString("pay_status"));
				}
				if (jsonObject.has("error")) {
					bundle.putString("error", jsonObject.getString("error"));
				}
				if (jsonObject.has("order_type")) {
					bundle.putString("order_type", jsonObject.getString("order_type"));
				}
				if (jsonObject.has("idCard")) {
					bundle.putString("idCard", jsonObject.getString("idCard"));
				}
				if (jsonObject.has("hy_level")) {
					bundle.putString("hy_level", jsonObject.getString("hy_level"));
				}
				if (jsonObject.has("price")) {
					bundle.putString("price", jsonObject.getString("price"));
				}
		return bundle;
	}
	public static Bundle alllvxingshe(String strJson) throws JSONException {
		Log.e("log", "门票1==" + strJson);
		JSONObject jsonObject = null;
		Bundle bundle = new Bundle();
		try {
			jsonObject = new JSONObject(strJson);
			if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject!=null&&jsonObject.has(lxs)) {
			JSONArray array = jsonObject.optJSONArray(lxs);
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			JSONObject listObject = null;
			for (int i = 0; i < array.length(); i++) {
				listObject = array.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (listObject.has(ID)) {
					map.put(ID, listObject.getString(ID));
				}
				if (listObject.has(NAME)) {
					map.put(NAME, listObject.getString(NAME));
				}
				list.add(map);
			}
			bundle.putSerializable(lxs, list);
		}
		return bundle;
	}
}
