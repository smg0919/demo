package com.syjjkj.kxwq.account;

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

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.util.ToastUtil;
import com.syjjkj.kxwq.wxapi.WXBean;

/**
 * Created by Administrator on 2018/5/28.
 */
public class UserFenxiaoManagementActivity extends BaseActivity {
    private static int high;
    private Context context;
    private WebView WfenxiaoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenxiao_main);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        WfenxiaoMain = (WebView) findViewById(R.id.WfenxiaoMain);
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
    @Override
    public void onResume() {
        super.onResume();
        initData();
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

    private void initData() {
        WXBean.registerWXApi(this);
        WindowManager wmManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        boolean flag = hasSoftKeys(wmManager);
        if (flag) {
            setMargins(WfenxiaoMain);
        }
        final WebSettings webSettings = WfenxiaoMain.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        WfenxiaoMain.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/fxmanage/fxmain.php?uid="+ UserInfoBean.uid);    //  加载申请提现的界面
        //在js中调用本地java方法
        WfenxiaoMain.addJavascriptInterface(new JsInterface(context), "AndroidWebView");
        WfenxiaoMain.setWebViewClient(new WebViewClient() {
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
        @JavascriptInterface
        public void setup(String val) {
            if("".equals(val))
            {
                ToastUtil.makeShortText(mContext, "至少选择一名成员");
                return ;
            }
            Intent intent=new Intent(mContext,UserFenxiaoPriceActivity.class);
            intent.putExtra("val",val);
            startActivity(intent);
        }
        @JavascriptInterface
        public void detail(String id) {
            Intent intent=new Intent(mContext,UserFenxiaoUporDelActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        }
        @JavascriptInterface
        public void selmember() {
            Intent intent=new Intent(mContext,UserFenxiaoMemberActivity.class);
            startActivity(intent);
        }
    }
}
