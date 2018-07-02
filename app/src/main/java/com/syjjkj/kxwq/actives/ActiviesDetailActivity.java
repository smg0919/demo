package com.syjjkj.kxwq.actives;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.util.ToastUtil;
import com.syjjkj.kxwq.wxapi.WXBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/11/30.
 */
public class ActiviesDetailActivity extends BaseActivity {
    private WebView wv;
    private Context context;
    private String activityurl;
    private String activityid;
    private Bitmap bm;
    private String urls;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public void myPermission(String url) {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        else
        {
            Wechatfenx(url);
        }
    }
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
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Wechatfenx(urls);
                    }
                }).start();

            }
            else
            {
                // Permission Denied
                ToastUtil.makeShortText(this, "您拒绝了权限的授予,无法分享");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        // Inflate the layout for this fragment
        Intent intent = getIntent();
        activityurl = intent.getStringExtra("activityurl");
        activityid = intent.getStringExtra("activityid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharedetail);
        initView();
        initlistener();
        initData();

    }
    private void initView() {
        context=this;
        wv=(WebView)findViewById(R.id.share_activitydetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bm != null && !bm.isRecycled()){
                    //bm.recycle(); //此句造成的以上异常
                    bm = null;
                }
                finish();
            }
        });
    }
    @Override       //这里是实现了自动更新
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initView();
        initData();
        initlistener();
    }
    private void initData() {
        final WebSettings webSettings = wv.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        //webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadUrl(activityurl);
        //在js中调用本地java方法
        wv.addJavascriptInterface(new JsInterfaces(context), "AndroidWebView");
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("http") & !url.startsWith("https")) {
                    return false;

                } else {
                    view.loadUrl(url);
                    return true;
                }
            }
        });
    }
    private void  initlistener()
    {

    }
    private class JsInterfaces {
        private Context mContext;
        public JsInterfaces(Context context) {
            this.mContext = context;
        }
        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void fenxiang(String url){
            urls=url;
            myPermission(url);
        }
        @JavascriptInterface
        public String getuserid(){
            return UserInfoBean.uid;
        }
        @JavascriptInterface
        public void yeji(String url){
            Intent intent = new Intent(mContext, UsersalesActivity.class);
            intent.putExtra("yejiurl",url);
            intent.putExtra("activityid",activityid);
            startActivity(intent);
        }
        @JavascriptInterface
        public String getactivityid(){
            return activityid;
        }
    }

   /* private void  fenxiangwe_friend(String url)
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("pics",arrayList);
        arrayList.add(url);
        //Wechat_Favorite(this,"",(ArrayList<String>)hashMap.get("pics"));
    }*/
   /* public static void Wechat_Favorite(final ActiviesDetailActivity context, String strSharetxt, final ArrayList<String> picShareList) {
        // 用Intent 启动系统分享
        final Intent sendIntent = new Intent();
        // 已知微信的Component名，可以直接分享到微信朋友圈，不用进入系统菜单
        sendIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
//        sendIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));

        // 分享的类型
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.setType("image*//*");
        // 设置分享的图片，顺序就是添加的顺序
        //final ArrayList<Uri> imageUris = new ArrayList<Uri>();
        *//*String desc = strSharetxt;
        LogUtil.e("文字描述"+desc);
        // 用系统剪切板，复制需要的文字，以便于分享朋友圈时候直接粘贴文字
        ClipboardManager copy = (ClipboardManager) context.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText(desc);*//*
        //context.showWaitDialog("正在加载图片");
       *//* new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < picShareList.size(); i++) {
                    //String url = UrlConfig.PIC_PATH + picShareList.get(i);
                    String url =  picShareList.get(i);
                    String name = url.substring(url.lastIndexOf("/") + 1,url.lastIndexOf("."));
                    String path = Environment
                            .getExternalStorageDirectory().getPath()
                            + "/kxwq/activity.pngx";
                        HttpDownloader httpDownLoader = new HttpDownloader();
                        httpDownLoader.downfile(url, path);
                        String pathdesc = Environment
                                .getExternalStorageDirectory().getPath()
                                + "/kxwq/activity.png";
                        File file = new File(path);
                        if(file.exists())
                        {
                            Utils.copyFile(path, pathdesc);
                            imageUris.add(Uri.fromFile(new File(pathdesc)));
                        }

                }
                sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                context.startActivity(sendIntent);
            }
        }.start();*//*

    }*/
  /* public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle) {
       int width = bitMap.getWidth();
       int height = bitMap.getHeight();
       // 设置想要的大小
       int newWidth = 80;
       int newHeight = 80;
       // 计算缩放比例
       float scaleWidth = ((float) newWidth) / width;
       float scaleHeight = ((float) newHeight) / height;
       // 取得想要缩放的matrix参数
       Matrix matrix = new Matrix();
       matrix.postScale(scaleWidth, scaleHeight);
       // 得到新的图片
       Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,
               matrix, true);
       if (needRecycle) bitMap.recycle();
       return newBitMap;
   }*/
   /*public void imageShare(String imgurl,int sendtype){
       if (WXBean.isWeixinAvilible(getApplicationContext())){
          *//* bm=returnBitmap(imgurl);
           bm=createBitmapThumbnail(bm,true);
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
           byte[] datas = baos.toByteArray();
           WXImageObject imgObj=new WXImageObject(datas);
           WXMediaMessage msg=new WXMediaMessage();
           msg.mediaObject=imgObj;
           Bitmap thumbBmp=Bitmap.createScaledBitmap(bm,THUMB_SIZE,THUMB_SIZE,true);
           bm.recycle();
           msg.thumbData= Util.bmpToByteArray(thumbBmp,true);
           SendMessageToWX.Req req=new SendMessageToWX.Req();
           req.transaction = String.valueOf(System.currentTimeMillis());
           req.message = msg;
           req.scene=SendMessageToWX.Req.WXSceneTimeline;
           api.sendReq(req);*//*
       }
       else
       {
           ToastUtil.makeShortText(context, "请先下载微信app");
           return;
       }

   }*/
    public  void Wechatfenx(String url) {
        if (WXBean.isWeixinAvilible(getApplicationContext())){
                bm=returnBitmap(url);
                String imagePath = saveBitmap(bm,activityid);  //将图片放到本地
                String imageUri = insertImageToSystem(context, imagePath);   //获取本地图片路径
                Intent shareIntent = new Intent();
                shareIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
                shareIntent.setAction(Intent.ACTION_SEND);
                // shareIntent.putExtra(Intent.EXTRA_TEXT, "一些文字");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri));
                shareIntent.setType("image*//*");
                context.startActivity(shareIntent);//这样分享就不带标题了
        } else
        {
            ToastUtil.makeShortText(context, "请先下载微信app");
            return;
        }

    }
    private  Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private static String saveBitmap(Bitmap bm,String activityid) {
        try {

            String path = Environment
                    .getExternalStorageDirectory().getPath()
                    + "/kxwq/"+System.currentTimeMillis()+".png";
            File f = new File(path);
            if(!f.exists())
            {
                f.getParentFile().mkdirs();
                f.createNewFile();
                FileOutputStream out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            }
            return f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private static String insertImageToSystem(Context context, String imagePath) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, "activity", "分享活动海报");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }

}
