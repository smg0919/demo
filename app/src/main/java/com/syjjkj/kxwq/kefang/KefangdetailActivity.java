package com.syjjkj.kxwq.kefang;

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
import com.syjjkj.kxwq.account.KeyBoardListener;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.ticket.PayTypeActivity;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/3.
 */
public class KefangdetailActivity  extends BaseActivity{
    private WebView wv;
    private String price;
    private String room_name;
    private String time;
    private static int high;
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        price=intent.getStringExtra("price");
        room_name=intent.getStringExtra("room_name");
        time=intent.getStringExtra("time");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_kfyd);
        initView();
        initlistener();
        initData();
    }
    private void initView() {
        wv=(WebView)findViewById(R.id.service_yddetail);
        KeyBoardListener.getInstance(this).init();

    }
    private void initlistener()
    {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WindowManager wmManager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        boolean flag=hasSoftKeys(wmManager);
        if(flag)
        {
            setMargins(wv);
        }
        final WebSettings webSettings = wv.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadUrl("https://www.kxhotspring.com/api/protected/apps/html/kfyd/kfydxx.php?roomname="+room_name+"&price="+price+"&time="+time);
        wv.addJavascriptInterface(new JsInterfaces(this), "AndroidWebView");
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
    private class JsInterfaces {
        private Context mContext;
        public JsInterfaces(Context context) {
            this.mContext = context;
        }
        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void  subyd(String roomname,String price,String zhuname,String idcard,String name,String phone,String remark,String playDate)
        {
            if("".equals(zhuname))
            {
                ToastUtil.makeShortText(mContext, "入住人名字不能为空");
                return;
            }
            if("".equals(idcard))
            {
                ToastUtil.makeShortText(mContext, "身份证号不能为空");
                return;
            }
            if(!isIDCard(idcard))
            {
                ToastUtil.makeShortText(mContext, "身份证号不符合");
                return;
            }
            if("".equals(name))
            {
                ToastUtil.makeShortText(mContext, "接收短信人名不能为空");
                return;
            }
            if(!StringUtil.isPhoneNumber(phone))
            {
                ToastUtil.makeShortText(mContext, "手机号必须是11位手机号");
                return;
            }
            KF_order(roomname,price,zhuname,idcard,name,phone,remark,playDate);
        }
    }
    private void KF_order(String roomname,String price,String zhuname,String idcard,String name,String phone,String remark,String playDate)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("room_name", roomname);
        params.addBodyParameter("days","1");
        params.addBodyParameter("rooms","1");
        params.addBodyParameter("playDate",playDate);
        params.addBodyParameter("contact", zhuname);
        params.addBodyParameter("paper_id", idcard);
        params.addBodyParameter("money",price);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("receive_name",name);
        params.addBodyParameter("remark",remark);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.dingfangPay, params,
                this, httpUtils, HttpStaticApi.dingfangpay);
    }
    public boolean isIDCard(String idCard) {
        String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.dingfangpay:
                try {
                    Bundle bundle = ParserJsonBean.parserdingfang(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            String order_id = bundle.getString(ParserJsonBean.ID);
                            String strTotalPrice=bundle.getString("money");
                            String buy_type=bundle.getString("buy_type");
                            Intent intent=new Intent(this,PayTypeActivity.class);
                            intent.putExtra("strTotalPrice",strTotalPrice);
                            intent.putExtra("buy_type",buy_type);
                            intent.putExtra("order_id",order_id);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
