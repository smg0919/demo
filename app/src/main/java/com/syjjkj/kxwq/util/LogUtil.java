package com.syjjkj.kxwq.util;

import android.util.Log;

/**
 * simple log output util
 * 
 * @author zhang.zk
 * 
 */
public class LogUtil {

	/**
	 * 简单的log输出，tag为“LogUtil”
	 * 
	 * @param info
	 *            级别
	 */
	public static void i(String info) {
		i(LogUtil.class, info);
	}

	/**
	 * 简单的log输出，tag为“LogUtil”
	 * 
	 * @param error
	 *            级别
	 */
	public static void e(String error) {
		e(LogUtil.class, error);
	}

	// 封装方法
	public static void e(String tag, String msg) {
		Log.e(tag, msg);

	}

	/**
	 * 简单的log输出，tag为当前输出日志的类名
	 * 
	 * @param info
	 *            级别
	 */
	public static void i(Class<?> cls, String info) {
		Log.i(cls.getSimpleName(), info);
	}

	/**
	 * 简单的log输出，tag为当前输出日志的类名
	 * 
	 * @param error
	 *            级别
	 */
	public static void e(Class<?> cls, String error) {
		Log.e(cls.getSimpleName(), error);
	}
}
