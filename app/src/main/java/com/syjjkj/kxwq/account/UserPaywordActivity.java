package com.syjjkj.kxwq.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

/**
 * Created by Administrator on 2017/11/15.
 */
public class UserPaywordActivity extends BaseActivity implements View.OnClickListener {
    private String phone;
    private String newpayword;
    private String subpayword;
    private String code;
    private TextView Tphone;
    private Button  Bgetcode;
    private  TimeCount timeCount;
    private EditText Eet_login_verification;
    private EditText Enewpayword;
    private EditText Esubpayword;
    private Button  Bsubandsave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        phone=intent.getStringExtra("phone");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payword);
        initView();
        initData();
        initListener();
    }
    private void initView(){
        Tphone=(TextView)findViewById(R.id.item_phone);
        Bgetcode=(Button)findViewById(R.id.btn_verification);
        Eet_login_verification=(EditText)findViewById(R.id.et_login_verification);
        Enewpayword=(EditText)findViewById(R.id.newpaywords);
        Esubpayword=(EditText)findViewById(R.id.subpaywords);
        Bsubandsave=(Button) findViewById(R.id.login_btn_log);
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
    private void initListener() {
        Bgetcode.setOnClickListener(this);
        Bsubandsave.setOnClickListener(this);
        Enewpayword.setOnClickListener(this);
        Esubpayword.setOnClickListener(this);
    }
    private void initData(){
        Tphone.setText(phone);
        timeCount = new TimeCount(30000, 1000);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verification:
                if(!StringUtil.isNetworkConnected(this)){
                    ToastUtil.makeShortText(this, "请检查您的网络");
                    return;
                }
                getDxCode(UrlConfig.GETCODE,phone,"3", HttpStaticApi.getDxCode);
                break;
            case R.id.login_btn_log:
                if(!StringUtil.isNetworkConnected(this)){
                    ToastUtil.makeShortText(this, "请检查您的网络");
                    return;
                }
                newpayword =Enewpayword .getText().toString();
                subpayword =Esubpayword .getText().toString();
                code=Eet_login_verification.getText().toString();
                if (StringUtil.isEmpty(code)) {
                    ToastUtil.makeShortText(this, "验证码不能为空");
                    return;
                }
                if (StringUtil.isEmpty(newpayword)||StringUtil.isEmpty(subpayword)) {
                    ToastUtil.makeShortText(this, "密码和确认密码不能为空");
                    return;
                }
                if (!newpayword.equals(subpayword)) {
                    ToastUtil.makeShortText(this, "密码和确认密码不一致");
                    return;
                }
                Updatepayword(newpayword,code);
        }
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.getDxCode:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            timeCount.start();
                        } else {
                            ToastUtil.makeShortText(UserPaywordActivity.this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.modifyPayPwd:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            /*Intent intent = new Intent(this, UserMoneyActivity.class);
                            startActivity(intent);*/
                            Intent intent=new Intent(UserPaywordActivity.this, UserMoneyActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            ToastUtil.makeLongText(this,
                                    "支付密码设置成功!");
                        } else {
                            ToastUtil.makeShortText(UserPaywordActivity.this,
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
    private void Updatepayword(String newpayword,String code)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("newpwd", newpayword);
        params.addBodyParameter("code",code);
        httpUtils = new HttpUtils();
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.MODIFYPAYPWD, params,
                this, httpUtils, HttpStaticApi.modifyPayPwd);
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Bgetcode.setText("重新发送");
            Bgetcode.setClickable(true);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            Bgetcode.setClickable(false);
            Bgetcode.setText(millisUntilFinished / 1000 + "秒后重新获取验证码");


        }
    }
}
