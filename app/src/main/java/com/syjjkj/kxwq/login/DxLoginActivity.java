package com.syjjkj.kxwq.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.HomePageActivity;
import com.syjjkj.kxwq.homepage.MyApplication;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.util.LogUtil;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class DxLoginActivity extends BaseActivity implements  OnClickListener{

    private EditText et_login_phone, et_login_verification;
//    private TextView dxlogin_tv;
    private Button login_btn,btn_login_verification;
    private String  strVerification="",strPhone="";
    private  TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dxlogin);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        MyApplication.getInstance().addActivity(this);
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
        et_login_phone = (EditText) findViewById(R.id.login_et_username);
        et_login_verification = (EditText) findViewById(R.id.et_login_verification);
//        dxlogin_tv = (TextView) findViewById(R.id.tv_dxlogin);
btn_login_verification=(Button)findViewById(R.id.btn_verification);
        login_btn= (Button) findViewById(R.id.login_btn_log);

    }

    private void initData() {
        timeCount = new TimeCount(30000, 1000);
    }

    private void initListener() {
        login_btn.setOnClickListener(this);
//        dxlogin_tv.setOnClickListener(this);
        btn_login_verification.setOnClickListener(this);
    }
    public final static String PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[0-9])|(17[0-9])|(16[0-9])|(19[0-9]))\\d{8}$";
    public static boolean isMatchered(String patternStr, CharSequence input) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login_btn_log:
                strPhone = et_login_phone.getText().toString();
                strVerification = et_login_verification.getText().toString();
                if (StringUtil.isEmpty(strPhone) || StringUtil.isEmpty(strVerification)) {
                    ToastUtil.makeShortText(this, "用户名验证码不能为空");
                    return;
                }
                if (!isMatchered(PHONE_PATTERN,strPhone))
                {
                    ToastUtil.makeShortText(this,"请填写11位电话");
                    return;
                }
                doDxLogin(UrlConfig.DODXLOGIN, strPhone, strVerification, HttpStaticApi.doDxLogin);

                break;
            case R.id.btn_verification:
                strPhone = et_login_phone.getText().toString();
                if(StringUtil.isEmpty(strPhone)){
                    ToastUtil.makeShortText(DxLoginActivity.this, "手机号不能为空");
                    return;
                }
                if(!StringUtil.isPhoneNumber(strPhone)){
                    ToastUtil.makeShortText(DxLoginActivity.this, "请填写正确的手机号");
                    return;
                }
                if(!StringUtil.isNetworkConnected(this)){
                    ToastUtil.makeShortText(this, "请检查您的网络");
                    return;
                }
                getDxCode(UrlConfig.GETCODE,strPhone,"1",HttpStaticApi.getDxCode);
                break;
//            case R.id.tv_dxlogin:
//                intent = new Intent(this, DxLoginActivity.class);
//                startActivity(intent);
//                break;
        }
    }

    // 登陆方法
    private void doDxLogin(String url, String phone, String code,
                         int resultCode) {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        params = new RequestParams();
        params.addBodyParameter(ParserJsonBean.PHONE, phone);
        params.addBodyParameter("code", code);
        showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, url, params,
                this, httpUtils, resultCode);
    }
    // 获取短信验证码
    private void getDxCode(String url, String phone, String type,
                           int resultCode) {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        params = new RequestParams();
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("type", type);
        showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, url, params,
                this, httpUtils, resultCode);
    }

    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.doDxLogin:
                try {
                    Bundle bundle = ParserJsonBean.parserDoLogin(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            String uid = bundle.getString(ParserJsonBean.UID);
                            String phone = bundle.getString(ParserJsonBean.PHONE);
                            String token = bundle.getString(ParserJsonBean.TOKEN);
                            String direct_type=bundle.getString(ParserJsonBean.direct_type);
//                            String myname = bundle.getString(ParserJsonBean.NAME);
//                            String logo = bundle.getString(ParserJsonBean.LOGO);
//
//                            String company_id = bundle.getString("company_id");
//                            String abbreviation = bundle.getString("abbreviation");
//                            String sales = bundle.getString("sales");
//                            String publish = bundle.getString("publish");
//                            String upset = bundle.getString("upset");
//                            String pricing = bundle.getString("pricing");
//
//                            String password = bundle.getString("password");

                            UserInfoBean.saveUserInfoups(DxLoginActivity.this, uid,
                                    phone, token,direct_type);
                            UserInfoBean.savePvRate(DxLoginActivity.this,bundle.getInt(ParserJsonBean.RATE));


                            Intent intent = new Intent(this, HomePageActivity.class);
                            LogUtil.i(uid + "");
                            LogUtil.i(token + "");
                            startActivity(intent);
                            this.finish();
                        } else {
                            ToastUtil.makeShortText(DxLoginActivity.this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.getDxCode:

                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {

                            timeCount.start();
                        } else {
                            ToastUtil.makeShortText(DxLoginActivity.this,
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
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_login_verification.setText("重新发送");
            btn_login_verification.setClickable(true);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_login_verification.setClickable(false);
            btn_login_verification.setText(millisUntilFinished / 1000 + "秒后重新获取验证码");


        }
    }
}

