package com.syjjkj.kxwq.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements  OnClickListener{

    private EditText login_et_username, login_et_password;
    private TextView dxlogin_tv;
    private Button login_btn;
    private String name, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        MyApplication.getInstance().addActivity(this);
        login_et_username = (EditText) findViewById(R.id.login_et_username);
        login_et_password = (EditText) findViewById(R.id.login_et_password);
        dxlogin_tv = (TextView) findViewById(R.id.tv_dxlogin);
        login_btn= (Button) findViewById(R.id.login_btn_log);
    }

    private void initData() {

    }

    private void initListener() {
        login_btn.setOnClickListener(this);
        dxlogin_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login_btn_log:
                name = login_et_username.getText().toString();
                pwd = login_et_password.getText().toString();
                if (StringUtil.isEmpty(name) || StringUtil.isEmpty(pwd)) {
                    ToastUtil.makeShortText(this, "用户名密码不能为空");
                    return;
                }
                if (!StringUtil.isPhoneNumber(name)) {
                    ToastUtil.makeShortText(this, "用户名必须是11位手机号");
                    return;
                }
                doLogin(UrlConfig.DO_LOGIN, name, pwd, HttpStaticApi.dologin);
                break;
            case R.id.tv_dxlogin:
                intent = new Intent(this, DxLoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    // 登陆方法
    private void doLogin(String url, String phone, String password,
                         int resultCode) {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        params = new RequestParams();
        params.addBodyParameter(ParserJsonBean.PHONE, phone);
        params.addBodyParameter(ParserJsonBean.PASSWORD, password);
        showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, url, params,
                this, httpUtils, resultCode);
    }

    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.dologin:
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

                            UserInfoBean.saveUserInfoups(LoginActivity.this, uid,
                                    phone, token,direct_type);
                            UserInfoBean.savePvRate(LoginActivity.this,bundle.getInt(ParserJsonBean.RATE));
                            Intent intent = new Intent(this, HomePageActivity.class);
                            LogUtil.i(uid + "");
                            LogUtil.i(token + "");
                            startActivity(intent);
                            this.finish();
                        } else {
                            ToastUtil.makeShortText(LoginActivity.this,
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

