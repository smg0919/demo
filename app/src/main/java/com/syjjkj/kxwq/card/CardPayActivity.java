package com.syjjkj.kxwq.card;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.HomePageActivity;
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

import org.json.JSONException;

import java.util.Random;

/**
 * Created by Administrator on 2018/4/13.
 */
public class CardPayActivity extends BaseActivity {
    private static int high;
    private Context context;
    private WebView Wpay;
    private String order_ids;
    private String money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pay);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        Wpay = (WebView) findViewById(R.id.Wpay);
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
     *
     * @param windowManager
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean hasSoftKeys(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();


        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);


        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);


        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        high = realHeight - displayHeight;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    public static void setMargins(View view) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.bottomMargin = high;
            view.setLayoutParams(p);
        }
    }
    private void initData(){
        WXBean.registerWXApi(this);
        Intent intent=getIntent();
        order_ids=intent.getStringExtra("order_id");
        money=intent.getStringExtra("money");
        WindowManager wmManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        boolean flag = hasSoftKeys(wmManager);
        if (flag) {
            setMargins(Wpay);
        }
        final WebSettings webSettings = Wpay.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        Random random = new Random();
        Wpay.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/member/zf.php?uid="+UserInfoBean.uid+"&money="+money);    //  加载申请提现的界面
        //在js中调用本地java方法
        Wpay.addJavascriptInterface(new JsInterface(context), "AndroidWebView");
        Wpay.setWebViewClient(new WebViewClient() {
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
    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void pay(String paytype) {
            if ("no".equals(paytype)) {
                ToastUtil.makeShortText(mContext, "请选择支付类型");
                return;
            }
            if("wx".equals(paytype)){   // 微信支付
                if(WXBean.isWeixinAvilible(getApplicationContext()))
                {

                    onWXPayCard(order_ids);//调微信
                }
                else
                {
                    ToastUtil.makeShortText(mContext, "请先下载微信app");
                }
            }
            if("qb".equals(paytype))       //钱包支付
            {
                final EditText inputServer = new EditText(mContext);
                inputServer.setHint("请输入支付密码");
                inputServer.setSingleLine(true);
                inputServer.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("请输入支付密码").setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String paypwd=inputServer.getText().toString();     // 支付密码
                        onQBPayCard(order_ids,paypwd);
                    }
                });
                builder.show();
            }
            if("hh".equals(paytype))    // 混合支付
            {
                if(WXBean.isWeixinAvilible(getApplicationContext()))
                {
                    onHybridPayCard(order_ids); //调微信
                }
                else
                {
                    ToastUtil.makeShortText(mContext, "请先下载微信app");
                }
            }
        }
    }
    // 混合充值
    private void onHybridPayCard(String order_id)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("order_id", order_id);
        httpUtils = new HttpUtils();
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.HybridPayCard, params,
                this, httpUtils, HttpStaticApi.HybridPayCard);
    }
    // 钱包购卡
    private void onQBPayCard(String order_id,String paypwd)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        if ("".equals(paypwd)) {
            ToastUtil.makeShortText(this, "支付密码不能为空");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("orderid", order_id);
        params.addBodyParameter("pwd",paypwd);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.QbPayCard, params,
                this, httpUtils, HttpStaticApi.QbPayCard);
    }
    // 微信购票
    private void onWXPayCard(String order_id)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("order_id", order_id);
        httpUtils = new HttpUtils();
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.PayCard, params,
                this, httpUtils, HttpStaticApi.PayCard);
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.PayCard:
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
            case HttpStaticApi.HybridPayCard:
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
            case HttpStaticApi.QbPayCard:
                try {
                    Bundle bundle = ParserJsonBean.parserOrder(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            Intent intent=new Intent(CardPayActivity.this, HomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            ToastUtil.makeLongText(this,"支付成功");
                        } else {
                            ToastUtil.makeShortText(this,bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
