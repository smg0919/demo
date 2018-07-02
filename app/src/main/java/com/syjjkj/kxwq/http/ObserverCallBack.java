package com.syjjkj.kxwq.http;


import com.lidroid.xutils.exception.HttpException;

//method使用在同一个activity中请求后台参数过多的情况判断下使用。
public interface ObserverCallBack {
     public void onStartHttp();

     public void onLoadingHttp(long total, long current, boolean isUploading);

     public void onSuccessHttp(String responseInfo, int resultCode);

     public void onFailureHttp(HttpException error, String msg);
     
    
}