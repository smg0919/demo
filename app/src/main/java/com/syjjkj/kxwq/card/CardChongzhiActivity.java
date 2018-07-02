package com.syjjkj.kxwq.card;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import android.webkit.WebViewClient;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;
import com.syjjkj.kxwq.wxapi.WXBean;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/4/10.
 */
public class CardChongzhiActivity  extends BaseActivity {
    private static int high;
    private Context context;
    private WebView Wcardrchongzhi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_chongzhi);
        initView();
        initData();
    }
    private void initView(){
        context=this;
        Wcardrchongzhi=(WebView)findViewById(R.id.Wchongzhi);
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
        WindowManager wmManager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        boolean flag=hasSoftKeys(wmManager);
        if(flag)
        {
            setMargins(Wcardrchongzhi);
        }
        final WebSettings webSettings = Wcardrchongzhi.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        Wcardrchongzhi.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/member/card_chongzhi.php");    //  加载申请提现的界面
        //在js中调用本地java方法
        Wcardrchongzhi.addJavascriptInterface(new JsInterface(context), "AndroidWebView");
        Wcardrchongzhi.setWebViewClient(new WebViewClient() {
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
    public static boolean isMatchered(String patternStr, CharSequence input) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
    public final static String PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[0-9])|(17[0-9])|(16[0-9])|(19[0-9]))\\d{8}$";
    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void chongzhi(String phone,String code,String money){
            if ("".equals(phone)) {
                ToastUtil.makeShortText(mContext, "手机号不能为空");
                return;
            }
            if(!isMatchered(PHONE_PATTERN,phone))
            {
                ToastUtil.makeShortText(mContext,"请填写11位手机号");
                return;
            }
            if ("".equals(code)) {
                ToastUtil.makeShortText(mContext, "验证码不能为空");
                return;
            }
            if ("".equals(money)) {
                ToastUtil.makeShortText(mContext, "金额不能为空");
                return;
            }
            if ("0".equals(money)) {
                ToastUtil.makeShortText(mContext, "充值金额不能为0");
                return;
            }
            buyCards(phone,code,money);
            //生成订单
        }
        @JavascriptInterface
        public void getcode(String tel) {
            if ("".equals(tel)) {
                ToastUtil.makeShortText(mContext, "手机号不能为空");
                return;
            }
            if (!isMatchered(PHONE_PATTERN,tel))
            {
                ToastUtil.makeShortText(mContext,"请填写11位手机号");
                return;
            }
            //根据卡号给客户发送验证码
            getCode(tel,"1"); //注册//发送短信
            //getDxCode(UrlConfig.GETCODE,strPhone,"1", HttpStaticApi.getDxCode);
        }
    }
    //按钮的点击事件
    public void click(View view){
        //java调用JS方法
        Wcardrchongzhi.loadUrl("javascript:getSafeCode()");
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.RechargeCard:
                try {
                    Bundle bundle = ParserJsonBean.parserBuycards(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            String order_id = bundle.getString(ParserJsonBean.ID);
                            String money = bundle.getString("money");;
                            Intent intent=new Intent(CardChongzhiActivity.this,CardPayActivity.class);
                            intent.putExtra("order_id",order_id);
                            intent.putExtra("money",money);
                            startActivity(intent);
                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.RegeorChongzhigetCode:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            click(Wcardrchongzhi);
                            ToastUtil.makeShortText(CardChongzhiActivity.this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        } else {
                            ToastUtil.makeShortText(CardChongzhiActivity.this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
    //充值发送验证码
    private void getCode(String phone,String type)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone",phone);
        params.addBodyParameter("type",type);
        httpUtils = new HttpUtils();
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.RegeorChongzhigetCode, params,
                this, httpUtils, HttpStaticApi.RegeorChongzhigetCode);
    }
    // 充值
    private void buyCards(String phone,String code,String strTotalPrice)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("vcode",code);
        params.addBodyParameter("phone",phone);
        params.addBodyParameter("price",strTotalPrice);
        httpUtils = new HttpUtils();
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.RechargeCard, params,
                this, httpUtils, HttpStaticApi.RechargeCard);
    }
}
