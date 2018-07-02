package com.syjjkj.kxwq.http;

import com.lidroid.xutils.exception.HttpException;

import java.io.File;

public interface DownCallback {
	//method使用在同一个activity中请求后台参数过多的情况判断下使用。
	
	     public void onStartDownHttp();

	     public void onLoadingDownHttp(long total, long current, boolean isUploading);

	     public void onSuccessDownHttp(File responseInfo, int resultCode);

	     public void onFailureDownHttp(HttpException error, String msg);
	     
	     
	
}
