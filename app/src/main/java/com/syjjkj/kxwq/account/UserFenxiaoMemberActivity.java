package com.syjjkj.kxwq.account;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/5/29.
 */
public class UserFenxiaoMemberActivity extends BaseActivity {
    private static int high;
    private Context context;
    private WebView Wfenxiaomember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenxiao_member);
        initView();
        initData();
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    private void initView() {
        context = this;
        Wfenxiaomember = (WebView) findViewById(R.id.Wfenxiaomember);
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

    private void initData() {
        WXBean.registerWXApi(this);
        WindowManager wmManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        boolean flag = hasSoftKeys(wmManager);
        if (flag) {
            setMargins(Wfenxiaomember);
        }
        final WebSettings webSettings = Wfenxiaomember.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        Wfenxiaomember.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/fxmanage/fxusermanage.php?uid="+ UserInfoBean.uid);    //  加载申请提现的界面
        //在js中调用本地java方法
        Wfenxiaomember.addJavascriptInterface(new JsInterface(context), "AndroidWebView");
        Wfenxiaomember.setWebViewClient(new WebViewClient() {
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
    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }
        @JavascriptInterface
        public void selusersale(String id) {
            Intent intent=new Intent(mContext,UserFenxiaoSaleActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        }
        @JavascriptInterface
        public void detail(String id) {
            Intent intent=new Intent(mContext,UserFenxiaoUporDelActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        }
        @JavascriptInterface
        public void share(String sign) {
            if("".equals(sign))
            {
                ToastUtil.makeShortText(mContext,"获取链接失败。");
                return;
            }
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", "www.kxhotspring.com/api/protected/apps/html/fxmanage/fxregister.php?sign="+sign);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            ToastUtil.makeShortText(mContext,"获取链接成功,粘贴即可分享");

        }
    }
}
