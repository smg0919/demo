package com.syjjkj.kxwq.account;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
public class UserMoneyActivity extends BaseActivity implements View.OnClickListener {
    private TextView TPhone;
    private RelativeLayout Rmoneyrule;
    private RelativeLayout Rtransactionhistory;
    private RelativeLayout Rpaymanager;
    private Button Bmoney_chongzhi;
    private Button Bmoney_tixian;
    private TextView TAccount;
    private String strPhone="";
    private String strAccount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_money);
        initView();
        initData();
        initListener();
    }
    @Override       //这里是实现了自动更新
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initView();
        initData();
        initListener();
    }
    private void initView(){
        TPhone=(TextView)findViewById(R.id.item_phone);
        TAccount=(TextView)findViewById(R.id.item_account);
        Rmoneyrule=(RelativeLayout)findViewById(R.id.money_rule);
        Rtransactionhistory=(RelativeLayout)findViewById(R.id.transaction_history);
        Rpaymanager=(RelativeLayout)findViewById(R.id.pay_Manager);
        Bmoney_chongzhi=(Button)findViewById(R.id.money_chongzhi);
        Bmoney_tixian=(Button)findViewById(R.id.money_tixian);
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
        Rmoneyrule.setOnClickListener(this);
        Rtransactionhistory.setOnClickListener(this);
        Rpaymanager.setOnClickListener(this);
        Bmoney_chongzhi.setOnClickListener(this);
        Bmoney_tixian.setOnClickListener(this);
    }
    private void initData(){
        getUserMoney();
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.money_rule:
                Intent intent1=new Intent(this,UserServiceRuleActivity.class);
                startActivity(intent1);
                break;
            case R.id.transaction_history:
                Intent intent2=new Intent(this,UserQBRecordActivity.class);
                intent2.putExtra("phone",strPhone);
                startActivity(intent2);
                break;
            case R.id.pay_Manager:
                Intent intent3=new Intent(this,UserPaymanagerActivity.class);
                intent3.putExtra("phone",strPhone);
                startActivity(intent3);
                break;
            case R.id.money_tixian:
                Intent intent4=new Intent(this,UserTixianActivity.class);
                startActivity(intent4);
                break;
            case R.id.money_chongzhi:
                Intent intent5=new Intent(this,UserChongzhiActivity.class);
                startActivity(intent5);
                break;
        }
    }
    private static boolean checkInstallation(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
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
                            strPhone=bundle.getString(ParserJsonBean.PHONE,"");
                            strAccount=bundle.getString(ParserJsonBean.ACCOUNT,"");
                            TPhone.setText(strPhone);
                            TAccount.setText("￥"+strAccount);
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
        }
    }
}
