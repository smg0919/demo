package com.syjjkj.kxwq.card;

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

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.util.ToastUtil;

import java.util.Random;

/**
 * Created by Administrator on 2018/4/10.
 */
public class CardExitActivity extends BaseActivity {
    private static int high;
    private Context context;
    private WebView Wexitcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_exit);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initData();
    }
    private void initView(){
        context=this;
        //Btixian=(Button)findViewById(R.id.Btixians);
        Wexitcard=(WebView)findViewById(R.id.Wexitcard);
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
        WindowManager wmManager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        boolean flag=hasSoftKeys(wmManager);
        if(flag)
        {
            setMargins(Wexitcard);
        }
        final WebSettings webSettings = Wexitcard.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        Random random = new Random();
        Wexitcard.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/member/exit_card.php");    //  加载申请提现的界面
        //在js中调用本地java方法
        Wexitcard.addJavascriptInterface(new JsInterface(context), "AndroidWebView");
    }
    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void cancel(String card,String code){
            if ("".equals(card)) {
                ToastUtil.makeShortText(mContext, "卡号不能为空");
                return;
            }
            if ("".equals(code)) {
                ToastUtil.makeShortText(mContext, "验证码不能为空");
                return;
            }
        }
        @JavascriptInterface
        public void getcode(String card) {
            if ("".equals(card)) {
                ToastUtil.makeShortText(mContext, "卡号不能为空");
                return;
            }
            ToastUtil.makeShortText(mContext, "已发送信息");
            //getDxCode(UrlConfig.GETCODE,strPhone,"1", HttpStaticApi.getDxCode);
        }
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
        }
    }
}

