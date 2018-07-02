package com.syjjkj.kxwq.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.util.FileOperation;
import com.syjjkj.kxwq.util.ImageOperate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 异步数据请求
 */
public class AnsynHttpRequest {
	public static final int POST = 1; // post 提交
	public static final int GET = 2; // get 提交

	/***
	 * get和post请求方法
	 * 
	 * @param sendType
	 *            请求类型：get和post
	 * @param context
	 *            上下文
	 * @param url
	 *            请求地址
	 * @param params
	 *            post使用到的参数
	 * @param callBack
	 *            异步回调
	 * @param httpUtils
	 *            HttpUtils最终请求类
	 * @param i
	 *            请求的方法对应的int值（整个项目中唯一不重复的）
	 */
	public static void requestGetOrPost(final int sendType, String url,
			final RequestParams params, final ObserverCallBack callBack,
			HttpUtils httpUtils, final int i) {
		httpUtils.configCurrentHttpCacheExpiry(0);
		url = Utf8URLencode(url);
		System.out.println("wjm=========url===:" + url);
		switch (sendType) {
		case POST:
			httpUtils.send(HttpRequest.HttpMethod.POST, url, params,
					new RequestCallBack<String>() {

						@Override
						public void onStart() {
							callBack.onStartHttp();
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							callBack.onLoadingHttp(total, current, isUploading);
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							Log.e("Log", "POST访问成功==" + responseInfo.result);
							callBack.onSuccessHttp(responseInfo.result, i);
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Log.e("Log",
									"POST访问失败==" + error.getExceptionCode()
											+ ":" + msg);
							callBack.onFailureHttp(error, msg);
						}
					});
			break;
		case GET:
			httpUtils.send(HttpRequest.HttpMethod.GET, url,
					new RequestCallBack<String>() {
						@Override
						public void onStart() {
							callBack.onStartHttp();
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							Log.e("Log", "GET访问中:" + "total="+total+"current="+current+"isUploading="+isUploading);
							callBack.onLoadingHttp(total, current, isUploading);
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							Log.e("Log", "GET访问成功==" + responseInfo.result);
							callBack.onSuccessHttp(responseInfo.result, i);
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Log.e("Log", "GET访问失败==" + error.getExceptionCode()
									+ ":" + msg);
							callBack.onFailureHttp(error, msg);
						}
					});
			break;
		default:
			break;
		}

	}

	/**
	 * Utf8URL编码
	 * 
	 * @params
	 * @return
	 */
	public static String Utf8URLencode(String text) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= 0 && c <= 255) {
				result.append(c);
			} else {
				byte[] b = new byte[0];
				try {
					b = Character.toString(c).getBytes("UTF-8");
				} catch (Exception ex) {
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return result.toString();
	}

	public static void uploadFile(final Context context, final String url,
								  final String uid, final String token, final String parameter, final Bitmap bitmap,
								  final ObserverCallBack callBack, final int resultCode) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				RequestParams params = new RequestParams();
				params.addBodyParameter("uid", uid);
				params.addBodyParameter("token", token);
				File file = FileOperation.saveImg(context,
						ImageOperate.getBitmapByte(bitmap, "png"));
				if (file != null) {
					if("logo".equals(parameter)){
						params.addBodyParameter("logo", file);
					}else if(ParserJsonBean.BANNER.equals(parameter)){
						params.addBodyParameter(ParserJsonBean.BANNER, file);
					}
				}
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.POST, url, params,
						new RequestCallBack<String>() {

							@Override
							public void onStart() {

								callBack.onStartHttp();
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								callBack.onLoadingHttp(total, current,
										isUploading);
							}

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								callBack.onSuccessHttp(responseInfo.result,
										resultCode);
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								callBack.onFailureHttp(error, msg);

							}
						});
				}
		}).start();
	}
	/**
	 * 下载文件
	 * @param context
	 * @param url
	 * @param user_id
	 * @param token
	 * @param parameter
	 * @param bitmap
	 * @param callBack
	 * @param resultCode
	 */
	public static void downloadFile(final Context context, final String url, final String target,
									final DownCallback callBack, final int resultCode) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				
				HttpUtils http = new HttpUtils();
				http.download(url, target,new RequestCallBack<File>() {

							@Override
							public void onStart() {
								callBack.onStartDownHttp();
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								callBack.onLoadingDownHttp(total, current,
										isUploading);
							}
							@Override
							public void onSuccess(ResponseInfo<File> responseInfo) {
								callBack.onSuccessDownHttp(responseInfo.result,
										resultCode);
								
							}
						
							

							@Override
							public void onFailure(HttpException error,
									String msg) {
								callBack.onFailureDownHttp(error, msg);
							}
				});
						
				}
		}).start();
	}
	public static void uploadPic(final Context context, final String url,
								 final Bitmap bitmap, final String type,
								 final ObserverCallBack callBack, final int resultCode) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				RequestParams params = new RequestParams();
				File file = FileOperation.saveImg(context,
						ImageOperate.getBitmapByte(bitmap, "png"));
				if (file != null) {
					params.addBodyParameter(ParserJsonBean.PIC, file);
				}
				params.addBodyParameter(ParserJsonBean.TYPE, type);
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.POST, url, params,
						new RequestCallBack<String>() {

							@Override
							public void onStart() {
								callBack.onStartHttp();
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								callBack.onLoadingHttp(total, current,
										isUploading);
							}

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								callBack.onSuccessHttp(responseInfo.result,
										resultCode);
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								callBack.onFailureHttp(error, msg);
							}
						});
				}
		}).start();
	}
	
	/**
	 * 上传二进制流文件
	 * @param context
	 * @param url
	 * @param type
	 * @param path
	 * @param callBack
	 * @param resultCode
	 */
	public static void uploadFile(final Context context, final String url,
								  final String type, final String path, final ObserverCallBack callBack, final int resultCode) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				RequestParams params = new RequestParams();
				params.addBodyParameter("type", type);
				File file = new File(path);
				if (file != null) {
					params.addBodyParameter("pic", file);
				}
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.POST, url, params,
						new RequestCallBack<String>() {

							@Override
							public void onStart() {
								callBack.onStartHttp();
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								callBack.onLoadingHttp(total, current,
										isUploading);
							}

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								callBack.onSuccessHttp(responseInfo.result,
										resultCode);
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								callBack.onFailureHttp(error, msg);
							}
						});
				}
		}).start();
	}
	/**
	 * 上传bitmap
	 * @param context
	 * @param url
	 * @param type
	 * @param bitmap
	 * @param callBack
	 * @param resultCode
	 */
	public static void uploadFile(final Context context, final String url,
								  final String type, final Bitmap bitmap, final ObserverCallBack callBack, final int resultCode) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				RequestParams params = new RequestParams();
				params.addBodyParameter("type", type);
				File file = FileOperation.saveImg(context,
						ImageOperate.getBitmapByte(bitmap, "png"));
				if (file != null) {
					params.addBodyParameter("pic", file);
				}
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.POST, url, params,
						new RequestCallBack<String>() {

							@Override
							public void onStart() {
								callBack.onStartHttp();
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								callBack.onLoadingHttp(total, current,
										isUploading);
							}

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								callBack.onSuccessHttp(responseInfo.result,
										resultCode);
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								callBack.onFailureHttp(error, msg);
							}
						});
				}
		}).start();
	}
}
