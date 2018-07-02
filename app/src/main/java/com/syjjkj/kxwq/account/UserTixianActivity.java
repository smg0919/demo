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
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

import java.util.Random;

/**
 * Created by Administrator on 2017/11/16.
 */
public class UserTixianActivity extends BaseActivity{
    private Button Btixian;
    private WebView Wtixian;
    private String messages;
    private String strAccount;
    private int states;
    private Context context;
    private static int high;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tixian);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initData();
        initListener();
    }
    private void initView(){
        context=this;
        KeyBoardListener.getInstance(this).init();
        //Btixian=(Button)findViewById(R.id.Btixians);
        Wtixian=(WebView)findViewById(R.id.Wtixians);
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
        getUserMoney();
        WindowManager wmManager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        boolean flag=hasSoftKeys(wmManager);
        if(flag)
        {
            setMargins(Wtixian);
        }
        final WebSettings webSettings = Wtixian.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        Random random = new Random();
        Wtixian.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/qianbao/tx1.php");    //  加载申请提现的界面
        //在js中调用本地java方法
        Wtixian.addJavascriptInterface(new JsInterface(context), "AndroidWebView");
       /* Wtixian.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                if(message.contains("@"))
                {
                    String aap[]=message.split("\\@");
                    String strmoney = aap[0].replace("提现金额","");
                    String bankid = aap[1].replace("银行卡号","");
                    String bankname = aap[2].replace("银行名称","");
                    String name = aap[3].replace("姓名","");
                    result.confirm();
                    tixian(strmoney,bankid,bankname,name);

                }
                else {
                    AlertDialog.Builder b = new AlertDialog.Builder(UserTixianActivity.this);
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
    private void tixians(String strmoney,String bankid,String bankname,String name)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("fee", strmoney);
        params.addBodyParameter("bankcard", bankid);
        params.addBodyParameter("bank", bankname);
        params.addBodyParameter("name", name);
        //showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.tiXian, params,
                this, httpUtils, HttpStaticApi.tixian);
    }
    private void initListener(){
      /*Btixian.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 必须另开线程进行JS方法调用(否则无法调用)
                Wtixian.post(new Runnable() {
                    @Override
                    public void run() {
                        // 注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法
                        Wtixian.loadUrl("javascript:callJS()");
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
            return strAccount;
        }
        @JavascriptInterface
        public void getxieyi(){
            Intent intent = new Intent(mContext, UserServiceRuleActivity.class);
            startActivity(intent);
        }
        @JavascriptInterface
        public void tixian(String strmoney,String bankid,String bankname,String name,String isCheck,String isnum) {
            String r = "/^\\d+(\\.\\d+)?$/";
            if ("".equals(strmoney)) {
                ToastUtil.makeShortText(mContext, "提现金额不能为空");
                return;
            }
            if ("false".equals(isnum)) {
                ToastUtil.makeShortText(mContext, "提现金额应为数字");
                return;
            }
            if (Double.valueOf(strmoney) < 0.01) {
                ToastUtil.makeShortText(mContext, "提现金额至少为0.01");
                return;
            }
            if (Double.valueOf(strmoney) > Double.valueOf(strAccount)) {
                ToastUtil.makeShortText(mContext, "提现金额不能高于钱包余额");
                return;
            }
            if ("".equals(bankid)) {
                ToastUtil.makeShortText(mContext, "银行卡号不能为空");
                return;

            }
            if ("".equals(bankname)) {
                ToastUtil.makeShortText(mContext, "银行名字不能为空");
                return;

            }
            if ("".equals(name)) {
                ToastUtil.makeShortText(mContext, "姓名不能为空");
                return;
            }
            if (!"1".equals(isCheck))
            {
                ToastUtil.makeShortText(mContext, "请同意康溪温泉服务协议!");
                return;
            }
            tixians(strmoney,bankid,bankname,name);
        }


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
            case HttpStaticApi.tixian:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {
                            ToastUtil.makeShortText(this, "提现成功");
                            finish();
                        } else {
                            ToastUtil.makeShortText(this, "提现失败");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissDialog();
                break;

        }
    }
}
