package com.syjjkj.kxwq.actives;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;

/**
 * Created by Administrator on 2017/12/1.
 */
public class UsersalesActivity extends BaseActivity {
    private WebView wv;
    private Context context;
    private String yejiurl;
    private String activityid;
    @Override
    public void onCreate(Bundle savedInstanceState){
        // Inflate the layout for this fragment
        Intent intent = getIntent();
        yejiurl = intent.getStringExtra("yejiurl");
        activityid = intent.getStringExtra("activityid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharesales);
        initView();
        initlistener();
        initData();
    }
    private void initView() {
        context=this;
        wv=(WebView)findViewById(R.id.share_activitysales);
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
    private void initData() {
        final WebSettings webSettings = wv.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        //webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadUrl(yejiurl);
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
        public String getuserid(){
            return UserInfoBean.uid;
        }
        @JavascriptInterface
        public String getactivityid(){
            return activityid;
        }

    }



}
