package com.syjjkj.kxwq.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.MyApplication;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.login.LoginActivity;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

public class ModifyPassActivity extends BaseActivity implements View.OnClickListener {
    private EditText etModifyPassOld, etModifyPassNew, etModifyPassConform;
    private String password,newpassword,querenpassword;
    private Button btnModify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pass);

        initView();
        initData();
        initListener();
    }
    private void initView() {
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
        etModifyPassOld = (EditText) findViewById(R.id.modify_et_oldpass);
        etModifyPassNew = (EditText) findViewById(R.id.modify_et_newpass);
        etModifyPassConform = (EditText) findViewById(R.id.modify_et_conformpass);
        btnModify=(Button) findViewById(R.id.modify_btn);

    }
    private void initData() {

    }
    private void initListener() {
        btnModify.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_btn:
                password = etModifyPassOld.getText().toString();
                newpassword = etModifyPassNew.getText().toString();
                querenpassword = etModifyPassConform.getText().toString();
                if(StringUtil.isEmpty(querenpassword)){
                    ToastUtil.makeShortText(this, "确认密码不能为空");
                    return;
                }
                if(!newpassword.equals(querenpassword)){
                    ToastUtil.makeShortText(this, "新密码和确认密码不一致");
                    return;
                }
                String uid = UserInfoBean.getUid(this);
                String token = UserInfoBean.getToken(this);
                PasswordEdit(UrlConfig.PASSWORDEDIT, newpassword, password,uid,token, HttpStaticApi.passwordEdit);
                break;
        }
    }


    private void PasswordEdit(String url,String newpass,String password,String uid,String token,int resultCode) {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        params = new RequestParams();
        params.addBodyParameter("newpwd", newpass);
        params.addBodyParameter("orgpwd", password);
        params.addBodyParameter(ParserJsonBean.UID, uid);
        params.addBodyParameter(ParserJsonBean.TOKEN, token);
        showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, url, params,
                this, httpUtils, HttpStaticApi.passwordEdit);
    }

    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        super.onSuccessHttp(responseInfo, resultCode);
        dismissDialog();
        switch (resultCode) {
            case HttpStaticApi.passwordEdit:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
//                            String token = bundle.getString(ParserJsonBean.TOKEN);
//                            LogUtil.i(token+"");
                            UserInfoBean.userLogout(this);
                            MyApplication.getInstance().exit();
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            ToastUtil.makeShortText(this, "修改成功,请重新登录");
                            finish();
                        } else {
                            ToastUtil.makeShortText(ModifyPassActivity.this,
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
