package com.syjjkj.kxwq.account;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.util.LogUtil;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;
import com.syjjkj.kxwq.wxapi.WXBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;

/**
 * Created by Administrator on 2017/11/16.
 */
public class UserChongzhiActivity extends BaseActivity{
    private Button Bchongzhi;
    private WebView Wchongzhi;
    private String messages;
    private String strAccount;
    private int states;
    private IWXAPI api;
    private Context context;
    private static  int high;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_chongzhi);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initData();
        initListener();
        api = WXAPIFactory.createWXAPI(this, WXBean.WX_APP_ID);
    }
    private void initView(){
        KeyBoardListener.getInstance(this).init();
        //Bchongzhi=(Button)findViewById(R.id.Bchongzhis);
        Wchongzhi=(WebView)findViewById(R.id.Wchongzhi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    /**
     * 判断底部navigator是否已经显示
     * @param windowManager
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean hasSoftKeys(WindowManager windowManager){
        Display d = windowManager.getDefaultDisplay();


        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);


        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);


        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        high=realHeight-displayHeight;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }
    public static void setMargins (View view) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.bottomMargin=high;
            view.setLayoutParams(p);
        }
    }
    private void initData() {
        WXBean.registerWXApi(this);
        getUserMoney();
        context=this;
        WindowManager wmManager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        boolean flag=hasSoftKeys(wmManager);
        if(flag)
        {
            setMargins(Wchongzhi);
        }
        final WebSettings webSettings = Wchongzhi.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        Wchongzhi.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/qianbao/cz2.php");    //  加载申请提现的界面
        //在js中调用本地java方法
        Wchongzhi.addJavascriptInterface(new JsInterface(context), "AndroidWebView");
       /* Wchongzhi.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                if(message.contains("@"))
                {
                    //调微信支付
                    String strmoney = message.replace("充值金额","").replace("@","");
                    String a=strmoney;
                    if(WXBean.isWeixinAvilible()getApplicationContext())
                    {
                            chongzhi(strmoney);
                            result.confirm();
                    }
                    else
                    {
                        AlertDialog.Builder b = new AlertDialog.Builder(UserChongzhiActivity.this);
                        b.setTitle("提示");
                        b.setMessage("请先下载微信app");
                        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });
                        b.setCancelable(false);
                        b.create().show();
                    }
                }
                else {
                    AlertDialog.Builder b = new AlertDialog.Builder(UserChongzhiActivity.this);
                    b.setTitle("提示");
                    b.setMessage(message);
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    });
                    b.setCancelable(false);
                    b.create().show();
                }
                return true;
            }
        });*/
    }
   private void chongzhi(String strmoney)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("fee", strmoney);
        //showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.chongZhi, params,
                this, httpUtils, HttpStaticApi.chongZhi);
    }
    private void initListener(){
        /*Bchongzhi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 必须另开线程进行JS方法调用(否则无法调用)
                Wchongzhi.post(new Runnable() {
                    @Override
                    public void run() {
                        // 注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法
                        Wchongzhi.loadUrl("javascript:callJS()");
                    }
                });
            }
        });*/
    }
    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public String getyue(){
            getUserMoney();
            String a=strAccount;
            return strAccount;
        }
        @JavascriptInterface
        public void getchongzhi(String money,String isnum){
            if ("".equals(money)) {
                ToastUtil.makeShortText(mContext, "充值金额不能为空");
                return;
            }
            if ("false".equals(isnum)) {
                ToastUtil.makeShortText(mContext, "充值金额应为数字");
                return;
            }
            if (Double.valueOf(money) < 0.01) {
                ToastUtil.makeShortText(mContext, "提现金额至少为0.01");
                return;
            }
            if(WXBean.isWeixinAvilible(getApplicationContext()))
            {
                chongzhi(money);
            }
            else
            {
                ToastUtil.makeShortText(mContext, "请先下载微信app");
                return;
            }
        }
       /* @JavascriptInterface
        public void czmoney(String price){

        }*/

    }
    private void getUserMoney() {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.MY_INFO, params,
                this, httpUtils, HttpStaticApi.getmyinfo);
    }
    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        super.onSuccessHttp(responseInfo, resultCode);
        dismissDialog();
        switch (resultCode) {
            case HttpStaticApi.getmyinfo:
                try {
                    Bundle bundle = ParserJsonBean.parserMyInfo(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {
                            strAccount=bundle.getString(ParserJsonBean.ACCOUNT,"");
                        } else {
                            ToastUtil.makeLongText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissDialog();
                break;
            case HttpStaticApi.chongZhi:
                try {
                    Bundle bundle = ParserJsonBean.parserOrder(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            PayReq req = new PayReq();
                            req.appId=WXBean.WX_APP_ID;
                            req.partnerId=WXBean.MCH_ID;

                            if(bundle.containsKey("prepayid")){
                                req.prepayId=bundle.getString("prepayid");
                            }
                            if(bundle.containsKey("package")){
                                req.packageValue=bundle.getString("package");
                            }
                            if(bundle.containsKey("noncestr")){
                                req.nonceStr=bundle.getString("noncestr");
                            }
                            if(bundle.containsKey("timestamp")){
                                req.timeStamp=bundle.getString("timestamp");
                            }
                            if(bundle.containsKey("sign")){
                                req.sign=bundle.getString("sign");
                            }
                            if(bundle.containsKey("orderid")){
                                UserInfoBean.setOrderId(this, bundle.getString("orderid"));
                            }
                            if(bundle.containsKey("prepayid")){
                                UserInfoBean.setPrepayId(this, bundle.getString("prepayid"));
                            }
                            LogUtil.e("appid:"+req.appId+" partnerid:"+req.partnerId+"  prepayid:"+req.prepayId+" package:"+req.packageValue+" :nonstr:"+req.nonceStr+" sign:"+req.sign);
                            WXBean.sendReq(req);
                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
