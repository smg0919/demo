package com.syjjkj.kxwq.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

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

public class WithDrawActivity extends BaseActivity implements View.OnClickListener {
private String strPV,strMoney,strName,strNum,strAddr;
    private TextView tvPV;
    private EditText etMoney,etName,etNum,etAddr;
    private Button btnOK;
    private CheckBox cbYes;
    private boolean bChecked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);
        initView();
        initListener();
    }
private void initView(){
    tvPV=(TextView)findViewById(R.id.withdraw_tv_pv);
    etMoney=(EditText) findViewById(R.id.withdraw_et_money);
    etName=(EditText) findViewById(R.id.withdraw_et_name);
    etNum=(EditText) findViewById(R.id.withdraw_et_number);
    etAddr=(EditText) findViewById(R.id.withdraw_et_bank);
    btnOK=(Button) findViewById(R.id.withdraw_btn_ok);
    cbYes=(CheckBox) findViewById(R.id.withdraw_cb_deal);
}
    private void initListener(){
btnOK.setOnClickListener(this);
       cbYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bChecked = isChecked;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        strPV=intent.getStringExtra("pv");
        tvPV.setText(strPV);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.withdraw_btn_ok:
                strMoney=etMoney.getText().toString().trim();
                strNum=etNum.getText().toString().trim();
                strName=etName.getText().toString().trim();
                strAddr=etAddr.getText().toString().trim();

                if (StringUtil.isEmpty(strMoney) ||StringUtil.isEmpty(strNum) ||StringUtil.isEmpty(strName)||StringUtil.isEmpty(strAddr)){
                ToastUtil.makeShortText(this,"持卡人、卡号、提现金额、开户行不能为空！");
                return;
                 }
                if (bChecked==false){
                    ToastUtil.makeShortText(this,"请同意用户协议");
                    return;
                }
                 getWithdraw(UrlConfig.GET_WITHDRAW,strMoney,strAddr,strName,strNum,"", HttpStaticApi.withdraw);
                break;
        }

    }
    // 登陆方法
    private void getWithdraw(String url,String money,String bank_type,String bank_name,String bank_num,String bank_addr,
                           int resultCode) {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        params = new RequestParams();
        params.addBodyParameter(ParserJsonBean.UID,UserInfoBean.uid);
        params.addBodyParameter(ParserJsonBean.TOKEN, UserInfoBean.token);
        params.addBodyParameter(ParserJsonBean.MONEY, money);
        params.addBodyParameter(ParserJsonBean.BANK_TYPE,bank_type);
        params.addBodyParameter(ParserJsonBean.BANK_NAME, bank_name);
        params.addBodyParameter(ParserJsonBean.BANK_NUM, bank_num);
        params.addBodyParameter(ParserJsonBean.BANK_ADDR, bank_addr);
        showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, url, params,
                this, httpUtils, resultCode);
    }

    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        super.onSuccessHttp(responseInfo, resultCode);
        dismissDialog();
        switch (resultCode){
            case HttpStaticApi.withdraw:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {

                            ToastUtil.makeShortText(this,
                                    bundle.getString("提现成功！"));


                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
