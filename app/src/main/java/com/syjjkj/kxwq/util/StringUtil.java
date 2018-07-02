package com.syjjkj.kxwq.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 判断字符串是否是數字
	 * 
	 * @author lvliuyan
	 * */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否是手机号
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		boolean isValid = false;
		String expression = "^1[3|5|8|7][0-9]{9}$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 判断当前是否有网络
	 * 
	 * @author lvliuyan
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 根据手机分辨率从 px(像素) 单位 转成 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机分辨率从 dp 单位 转成 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static boolean isPhoneNumberValid(String phoneNumber) {

		boolean isValid = false;
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);

		Pattern pattern2 = Pattern.compile(expression2);
		Matcher matcher2 = pattern2.matcher(inputStr);
		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 判断字符串是否包含中文
	 * 
	 * @author lvliuyan
	 * */
	public static final boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否为空   为空或者“”返回true
	 * 
	 * @author lvliuyan
	 * */
	public static boolean isEmpty(String str) {
		if (str != null && !str.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public static int strToInt(String number) {
		int n = 0;
		try {
			n = Integer.valueOf(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return n;
	}

	public static BigDecimal strToBigDecimal(String number) {
		BigDecimal n = null;
		try {
			n = new BigDecimal(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return n;
	}

	public static double strToDouble(String number) {
		double n = 0l;
		try {
			n = Double.valueOf(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return n;
	}

	/**
	 * 根据毫秒值返回字符串.e.g."0天0小时0分钟0秒"
	 * 
	 * @param millisSecond
	 * @return
	 */
	public static String millisToString(long millisSecond) {
		int s = 1000;
		int m = 60 * s;
		int h = 60 * m;
		int d = 24 * h;
		StringBuffer sb = new StringBuffer();
		if (millisSecond / d > 0) {
			sb.append(millisSecond / d);
			sb.append("天");
		}
		sb.append(millisSecond % d / h);
		sb.append("小时");
		sb.append(millisSecond % d % h / m);
		sb.append("分钟");
		sb.append(millisSecond % d % h % m / s);
		sb.append("秒");
		return sb.toString();
	}
	
	/**
	 * 根据秒值返回数组"  小时、分钟、秒
	 * 
	 * @param millisSecond
	 * @return
	 */
	public static ArrayList<String> getSecondToArrayList(String secondValue) {
		long second= Long.valueOf(secondValue);
		int s = 1;
		int m = 60 * s;
		int h = 60 * m;
		int d = 24 * h;

		ArrayList<String> list=new ArrayList<String>();
		if (second / d > 0) {
			
				list.add("24");
				list.add("00");
				list.add("00");
				return list;
		}
		if (second < 0) {
			list.add("00");
			list.add("00");
			list.add("00");
			return list;
		}
		if(second/h<10){
			list.add("0"+ String.valueOf(second/h));
		}else{
			list.add(String.valueOf(second/h));
		}
		if(second  % h / m<10){
			list.add("0"+ String.valueOf(second  % h / m));
		}else{
			list.add(String.valueOf(second  % h / m));
		}
		if(second % h % m / s<10){
			list.add("0"+ String.valueOf(second % h % m / s));
		}else{
			list.add(String.valueOf(second % h % m / s));
		}
		
		return list;
	}
	/**
	 * 根据秒值返回数组"  小时、分钟、秒
	 * 
	 * @param millisSecond
	 * @return
	 */
	public static ArrayList<String> getSecondToArrayList(Long secondValue) {
		long second=secondValue;
		int s = 1;
		int m = 60 * s;
		int h = 60 * m;
		int d = 24 * h;

		ArrayList<String> list=new ArrayList<String>();
		if (second / d > 0) {
			
				list.add("24");
				list.add("00");
				list.add("00");
				return list;
		}
		if (second < 0) {
			list.add("00");
			list.add("00");
			list.add("00");
			return list;
		}
		if(second/h<10){
			list.add("0"+ String.valueOf(second/h));
		}else{
			list.add(String.valueOf(second/h));
		}
		if(second  % h / m<10){
			list.add("0"+ String.valueOf(second  % h / m));
		}else{
			list.add(String.valueOf(second  % h / m));
		}
		if(second % h % m / s<10){
			list.add("0"+ String.valueOf(second % h % m / s));
		}else{
			list.add(String.valueOf(second % h % m / s));
		}
		
		return list;
	}
	/**
	 * 字符串去除空格的方法
	 * 
	 * @param millisSecond
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	// 隐藏软键盘
	public static void hideSoftKeyboard(Activity context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (context.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (context.getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(context
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	public static String getVersionWithoutYear(String version){
		if(!isEmpty(version)){
			int pos = version.indexOf(' ');
			if(pos == -1) return version;
				
			String retVersion = version.substring(pos+1);
			return retVersion;
		}else
			return version;
		
	}

	public static String getBrandFromVersion(String version){
		int pos1 = version.indexOf(' ');
		if(pos1 == -1) return "";
		String retVersion = version.substring(pos1+1);
		int pos2 = retVersion.indexOf(' ');
		if(pos2 == -1) return "";
		retVersion = retVersion.substring(0, pos2);
		return retVersion;
	}
	public static String str2Price(String price){
		if (isEmpty(price)) return "不限";
		if (price.equals("1")) return "3万以下";
		if (price.equals("2")) return "3-8万";
		if (price.equals("3")) return "8-10万";
		if (price.equals("4")) return "10-15万";
		if (price.equals("5")) return "15-20万";
		if (price.equals("6")) return "20-30万";
		if (price.equals("7")) return "30-50万";
		if (price.equals("8")) return "50-100万";
		if (price.equals("9")) return "100万以上";
		else  return "不限";

	}
	/** 
     * 获取最近12个月的字符串，最后一个是“一年以上 ”
    */  
    public static String[] getLast12Months(){
          
        String[] last12Months = new String[13];
          
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        for(int i=0; i<12; i++){  
            if((cal.get(Calendar.MONTH)+1)<10){
	            last12Months[i] = cal.get(Calendar.YEAR)+ "-0" + (cal.get(Calendar.MONTH)+1);
	        }else{
	        	last12Months[i] = cal.get(Calendar.YEAR)+ "-" + (cal.get(Calendar.MONTH)+1);
            }
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月
        }  
	        last12Months[12]="一年以上";  
	        return last12Months;  
    } 
    public static boolean isToday(String str){
    	if (isEmpty(str)) {
			return false;
		}
		String strs[] = null;
		try {
			str=str.split(" ")[0];
			strs=str.split("-");
		} catch (Exception e) {
			return false;//若是出现异常  直接认为日期不是今天
		}
		
		Calendar c = Calendar.getInstance();
		String y=c.get(Calendar.YEAR)+"";
		String m = c.get(Calendar.MONTH)+1+"";
		m=m.length()>=2? m:"0"+m;
		String d = c.get(Calendar.DAY_OF_MONTH)+"";
		
		if (y.equals(strs[0]) && m.equals(strs[1]) && d.equals(strs[2])) {
			return true;
		}
		return false;
	}
    /**
     * 8.16
     * @param str
     * @return
     */
    public static String getDate(String str){
    	if (isEmpty(str)) {
			return null;
		}
		String strs[] = null;
		try {
			str=str.split(" ")[0];
			strs=str.split("-");
		} catch (Exception e) {
			return null;
		}
		
		if (strs[1].charAt(0)=='0') {
			strs[1]=strs[1].substring(1);
		}
		
		
		return strs[1]+"."+strs[2];
	}
}
