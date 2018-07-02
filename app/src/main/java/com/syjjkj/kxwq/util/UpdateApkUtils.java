package com.syjjkj.kxwq.util;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.syjjkj.kxwq.homepage.HomePageActivity;
import com.syjjkj.kxwq.homepage.MyApplication;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.DownCallback;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.widget.NotificationProgress;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/6/21.
 */
public class UpdateApkUtils {
    /**
     * 更新APK
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
   public static  void updateAPK(final HomePageActivity context) {
        new AlertDialog.Builder(context)
                .setTitle("发现新版本APK")
                .setMessage("请确认是否更新")
                .setNegativeButton("下次再说",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        })

                .setPositiveButton("立即更新",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    // We don't have permission so prompt the user
                                    ActivityCompat.requestPermissions(
                                            context,
                                            PERMISSIONS_STORAGE,
                                            REQUEST_EXTERNAL_STORAGE
                                    );
                                }
                                else
                                {
                                    context.showWaitDialog("正在下载APK...");
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
                                                if (context.tvPercent != null && total != 0) {
                                                    DecimalFormat df = new DecimalFormat("0");
                                                    context.tvPercent.setText(String.valueOf(df.format((Double
                                                            .valueOf(current) / Double.valueOf(total) * 100)))
                                                            + "%");
                                                    // 更改文字和进度
                                                    context.notiPro.setNoti(String.valueOf(df.format((Double
                                                            .valueOf(current) / Double.valueOf(total) * 100)))
                                                            + "%", Integer.valueOf(df.format((Double
                                                            .valueOf(current) / Double.valueOf(total) * 100))));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onSuccessDownHttp(File responseInfo, int resultCode) {
                                            context.dismissDialog();
                                            switch (resultCode) {
                                                case HttpStaticApi.get_apk:

                                               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    Uri apkUri = FileProvider.getUriForFile(context, "com.syjjkj.kxwq.fileprovider", responseInfo);//在AndroidManifest中的android:authorities值
                                                    Intent install = new Intent(Intent.ACTION_VIEW);
                                                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                                    context.startActivity(install);

                                                } else {
                                                    String fileName=responseInfo.getPath().trim();
                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                                                    context.startActivity(intent);
                                                }*/
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        Uri apkUri = FileProvider.getUriForFile(context, "com.syjjkj.kxwq.fileprovider", responseInfo);//在AndroidManifest中的android:authorities值
                                                        Intent install = new Intent(Intent.ACTION_VIEW);
                                                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                                        context.startActivity(install);
                                                    } else {
                                                        Intent intent = new Intent();
                                                        intent.setAction(android.content.Intent.ACTION_VIEW);
                                                        String fileName=responseInfo.getPath().trim();
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                                                        context.startActivity(intent);
                                                    }

                                                    break;
                                                default:
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onFailureDownHttp(HttpException error, String msg)
                                        {}}, HttpStaticApi.get_apk);
                                    context.notiPro=new NotificationProgress(context);
                                }
//
                            }
                        }).show();

        UserInfoBean.setUpdateAPK(false);//设置不能更新APK

    }
    /**
     *获取服务器的版本号
     */
   public static  void getAPKVersion(HomePageActivity context) {
        if (StringUtil.isNetworkConnected(context)) {
            // showWaitDialog("");
            UserInfoBean.getUserInfo(context);
            String url = UrlConfig.APK_VERSION ;
            Log.e("Log", context.getLocalClassName()+"----"+url);
            HttpUtils httpUtils = new HttpUtils();
            AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, url, null,
                    context, httpUtils, HttpStaticApi.get_apk_version);
        } else {
            ToastUtil.makeLongText(context, "请检查网络");
        }
    }
    /**
     * 获取apk的版本号 currentVersionCode
     *
     * @param ctx
     * @return
     */
    public static int getAPPVersionCodeFromAPP(Context ctx) {
        int currentVersionCode = 0;
        String appVersionName="";
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
            System.out.println(currentVersionCode + " " + appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch blockd
//            e.printStackTrace();
        }catch (NullPointerException e) {
            // TODO: handle exception
        }
        return currentVersionCode;
    }
}
