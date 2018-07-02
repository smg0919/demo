package com.syjjkj.kxwq.ticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
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
import com.syjjkj.kxwq.kefang.KefangRecordActivity;
import com.syjjkj.kxwq.util.LogUtil;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;
import com.syjjkj.kxwq.wxapi.WXBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;

import java.text.DecimalFormat;

public class PayTypeActivity extends BaseActivity implements View.OnClickListener{
    private String strTotalPrice;
    private String QB;
    private String direct_type;
    private String strOrderId;
    private TextView tvTotalPrice;
    private String buy_type;
    //private TextView tvReturnPv;
    private RelativeLayout r4;
    private RelativeLayout r5;
    private RelativeLayout r6;
    private RelativeLayout r7;
    private RelativeLayout r8;
    private TextView tvTotalQB;
    private TextView tvTotalYue;
    private Button btnPay;
    private CheckedTextView checkWX,checkQB,checkHunHe;
    private boolean bWX=true,bQB=false,bHunHe=false;
    //private String strPV;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_type);
        initView();
        initListener();
        initData();
        api = WXAPIFactory.createWXAPI(this, WXBean.WX_APP_ID);
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
        tvTotalPrice=(TextView) findViewById(R.id.pay_type_total_price);
        //tvReturnPv=(TextView) findViewById(R.id.pay_type_return_pv);
        tvTotalQB=(TextView) findViewById(R.id.pay_type_total_jf);
        tvTotalYue=(TextView)findViewById(R.id.yue);
        btnPay=(Button)findViewById(R.id.pay_type_btn_bottom);
        checkWX=(CheckedTextView) findViewById(R.id.pay_type_check_tv_pay_wx);
        checkQB=(CheckedTextView) findViewById(R.id.pay_type_check_tv_pay_jf);
        checkHunHe=(CheckedTextView)findViewById(R.id.pay_type_check_tv_hunhe);
        r4=(RelativeLayout)findViewById(R.id.rl_4);
        r5=(RelativeLayout)findViewById(R.id.rl_5);
        r6=(RelativeLayout)findViewById(R.id.rl_6);
        r7=(RelativeLayout)findViewById(R.id.rl_7);
        r8=(RelativeLayout)findViewById(R.id.rl_8);
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
    private void initListener(){
        btnPay.setOnClickListener(this);
        checkQB.setOnClickListener(this);
        checkWX.setOnClickListener(this);
        checkHunHe.setOnClickListener(this);
    }
    // 微信卡
    private void onPay(String order_id)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("order_id", order_id);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.UNIFED_ORDER, params,
                this, httpUtils, HttpStaticApi.unified_order);
    }
    //微信购房
    private void onPaykefang(String order_id)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("order_id", order_id);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.Payroom, params,
                this, httpUtils, HttpStaticApi.Payroom);
    }
    //钱包购房
    private void payQB_KF(String order_id,String paypwd)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        if ("".equals(paypwd)) {
            ToastUtil.makeShortText(this, "支付密码不能为空");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("orderid", order_id);
        params.addBodyParameter("pwd",paypwd);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.PayQBroom, params,
                this, httpUtils, HttpStaticApi.payQB_KF);
    }
    //混合购房
    private void hybridPay_KF(String order_id)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("order_id", order_id);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.PayHunheroom, params,
                this, httpUtils, HttpStaticApi.hybridPay_KF);
    }
    //混合购买
    private void hybridPay(String order_id)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("order_id", order_id);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.hybridPay, params,
                this, httpUtils, HttpStaticApi.hybridPay);
    }
    // 钱包购票
    private void onQBPay(String order_id,String paypwd)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        if ("".equals(paypwd)) {
            ToastUtil.makeShortText(this, "支付密码不能为空");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("orderid", order_id);
        params.addBodyParameter("pwd",paypwd);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.QBbuyTicket, params,
                this, httpUtils, HttpStaticApi.QBBugticket);
    }
    private void initData(){
        WXBean.registerWXApi(this);
        Intent intent=getIntent();
        strTotalPrice=intent.getStringExtra("strTotalPrice");
        //strReturnPv=intent.getStringExtra(ParserJsonBean.PV);
        strOrderId=intent.getStringExtra("order_id");
        tvTotalPrice.setText(strTotalPrice+"元");
        buy_type=intent.getStringExtra("buy_type");
        //tvReturnPv.setText(strReturnPv);
        getMyInfo(UrlConfig.MY_INFO,HttpStaticApi.getmyinfo);


    }

    /**
     * 1是微信支付
     * 2钱包支付
     * 3混合支付
     * @param i
     */
    private void setPayCheck(int i){
        if (i==1){
            checkWX.setChecked(true);
            bWX=true;
            checkQB.setChecked(false);
            bQB=false;
            checkHunHe.setChecked(false);
            bHunHe=false;
        }else if(i==2){
            checkWX.setChecked(false);
            bWX=false;
            checkQB.setChecked(true);
            bQB=true;
            checkHunHe.setChecked(false);
            bHunHe=false;
        }
        else
        {
            checkWX.setChecked(false);
            bWX=false;
            checkQB.setChecked(false);
            bQB=false;
            checkHunHe.setChecked(true);
            bHunHe=true;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pay_type_btn_bottom:
                if (bWX){
                    if(WXBean.isWeixinAvilible(getApplicationContext()))
                    {
                        if("1".equals(buy_type))
                        {
                            onPay(strOrderId);
                        }
                        else if("3".equals(buy_type))
                        {
                            onPaykefang(strOrderId);
                        }
                    }
                    else
                    {
                        ToastUtil.makeShortText(this, "请先下载微信app");
                    }

                }else if(bQB){
                    /*if(Double.valueOf(QB)<Double.valueOf(strTotalPrice))
                    {
                        ToastUtil.makeShortText(this, "钱包余额小于商品金额");
                    }*/
                    /*else*/
                    {
                        final EditText inputServer = new EditText(this);
                        inputServer.setHint("请输入支付密码");
                        inputServer.setSingleLine(true);
                        inputServer.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("请输入支付密码").setView(inputServer)
                                .setNegativeButton("取消", null);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String paypwd=inputServer.getText().toString();
                                if("1".equals(buy_type))
                                {
                                    onQBPay(strOrderId,paypwd);
                                }
                                else if("3".equals(buy_type))
                                {
                                    payQB_KF(strOrderId,paypwd);
                                    //钱包购房
                                }

                            }
                        });
                        builder.show();
                    }

                }
                else if(bHunHe)
                {
                    if(Double.valueOf(strTotalPrice)-Double.valueOf(QB)<0)
                    {
                        ToastUtil.makeShortText(this, "钱包剩余金额大于支付金额,请选择钱包支付!");
                        return;
                    }
                    else
                    {
                        if("1".equals(buy_type))
                        {
                            hybridPay(strOrderId);
                        }
                        else if("3".equals(buy_type))
                        {
                            hybridPay_KF(strOrderId);
                            //混合支付购房
                        }

                    }
                }
                break;
            case R.id.pay_type_check_tv_pay_wx:
                setPayCheck(1);
                break;
            case R.id.pay_type_check_tv_pay_jf:
                setPayCheck(2);
                break;
            case R.id.pay_type_check_tv_hunhe:
                setPayCheck(3);
                break;
        }
    }
    // 获取钱包余额
    private void getMyInfo(String url,
                           int resultCode) {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        params = new RequestParams();
        params.addBodyParameter(ParserJsonBean.UID,UserInfoBean.uid);
        params.addBodyParameter(ParserJsonBean.TOKEN, UserInfoBean.token);
//        showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, url, params,
                this, httpUtils, resultCode);
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.unified_order:
                try {
                    Bundle bundle = ParserJsonBean.parserOrder(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            PayReq req = new PayReq();

//                            if(bundle.containsKey("appid")){
//                                req.appId=bundle.getString("appid");
//                            }
//                            if(bundle.containsKey("partnerid")){
//                                req.partnerId=bundle.getString("partnerid");
//                            }
                            req.appId=WXBean.WX_APP_ID;
                            req.partnerId=WXBean.MCH_ID;

                            if(bundle.containsKey("prepayid")){
                                req.prepayId=bundle.getString("prepayid");
                            }
                            if(bundle.containsKey("package")){
                                req.packageValue=bundle.getString("package");
                            }
                            if(bundle.containsKey("noncestr")){
                                req.nonceStr=bundle.getString("noncestr");
                            }
                            if(bundle.containsKey("timestamp")){
                                req.timeStamp=bundle.getString("timestamp");
                            }
                            if(bundle.containsKey("sign")){
                                req.sign=bundle.getString("sign");
                            }
                            if(bundle.containsKey("orderid")){
                                UserInfoBean.setOrderId(this, bundle.getString("orderid"));
                            }
                            if(bundle.containsKey("prepayid")){
                                UserInfoBean.setPrepayId(this, bundle.getString("prepayid"));
                            }
                            LogUtil.e("appid:"+req.appId+" partnerid:"+req.partnerId+"  prepayid:"+req.prepayId+" package:"+req.packageValue+" :nonstr:"+req.nonceStr+" sign:"+req.sign);
                            WXBean.sendReq(req);




//                            api.sendReq(req);


                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.hybridPay:
                try {
                    Bundle bundle = ParserJsonBean.parserOrder(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            PayReq req = new PayReq();

//                            if(bundle.containsKey("appid")){
//                                req.appId=bundle.getString("appid");
//                            }
//                            if(bundle.containsKey("partnerid")){
//                                req.partnerId=bundle.getString("partnerid");
//                            }
                            req.appId=WXBean.WX_APP_ID;
                            req.partnerId=WXBean.MCH_ID;

                            if(bundle.containsKey("prepayid")){
                                req.prepayId=bundle.getString("prepayid");
                            }
                            if(bundle.containsKey("package")){
                                req.packageValue=bundle.getString("package");
                            }
                            if(bundle.containsKey("noncestr")){
                                req.nonceStr=bundle.getString("noncestr");
                            }
                            if(bundle.containsKey("timestamp")){
                                req.timeStamp=bundle.getString("timestamp");
                            }
                            if(bundle.containsKey("sign")){
                                req.sign=bundle.getString("sign");
                            }
                            if(bundle.containsKey("orderid")){
                                UserInfoBean.setOrderId(this, bundle.getString("orderid"));
                            }
                            if(bundle.containsKey("prepayid")){
                                UserInfoBean.setPrepayId(this, bundle.getString("prepayid"));
                            }
                            LogUtil.e("appid:"+req.appId+" partnerid:"+req.partnerId+"  prepayid:"+req.prepayId+" package:"+req.packageValue+" :nonstr:"+req.nonceStr+" sign:"+req.sign);
                            WXBean.sendReq(req);

//                            api.sendReq(req);


                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.getmyinfo:
                try {
                    Bundle bundle = ParserJsonBean.parserMyInfo(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            String uid = bundle.getString(ParserJsonBean.UID);
                            QB=bundle.getString(ParserJsonBean.ACCOUNT,"");
                            direct_type=bundle.getString(ParserJsonBean.direct_type,"");
                            if("3".equals(direct_type)) //是否为分销
                            {
                                r4.setVisibility(View.VISIBLE);
                                r5.setVisibility(View.VISIBLE);
                                r8.setVisibility(View.VISIBLE);
                                if(Double.valueOf(QB)-Double.valueOf(strTotalPrice)>=0)
                                {
                                    r6.setVisibility(View.INVISIBLE);
                                    r7.setVisibility(View.INVISIBLE);
                                }
                                else
                                {
                                    r6.setVisibility(View.VISIBLE);
                                    r7.setVisibility(View.VISIBLE);

                                }
                            }
                            else
                            {
                                r4.setVisibility(View.INVISIBLE);
                                r5.setVisibility(View.INVISIBLE);
                                r6.setVisibility(View.INVISIBLE);
                                r7.setVisibility(View.INVISIBLE);
                                r8.setVisibility(View.INVISIBLE);;
                            }
                            tvTotalQB.setText(QB);
                            DecimalFormat df   = new DecimalFormat("######0.00");
                            tvTotalYue.setText(String.valueOf(df.format(Double.valueOf(strTotalPrice)-Double.valueOf(QB))));
                        } else {
                            ToastUtil.makeShortText(this, bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.QBBugticket:
                try {
                    Bundle bundle = ParserJsonBean.parserOrder(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            ToastUtil.makeShortText(this,
                                    "购买成功");
                            Intent intent=new Intent(this,BuyRecordActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.Payroom:
                try {
                    Bundle bundle = ParserJsonBean.parserOrder(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            PayReq req = new PayReq();

//                            if(bundle.containsKey("appid")){
//                                req.appId=bundle.getString("appid");
//                            }
//                            if(bundle.containsKey("partnerid")){
//                                req.partnerId=bundle.getString("partnerid");
//                            }
                            req.appId=WXBean.WX_APP_ID;
                            req.partnerId=WXBean.MCH_ID;

                            if(bundle.containsKey("prepayid")){
                                req.prepayId=bundle.getString("prepayid");
                            }
                            if(bundle.containsKey("package")){
                                req.packageValue=bundle.getString("package");
                            }
                            if(bundle.containsKey("noncestr")){
                                req.nonceStr=bundle.getString("noncestr");
                            }
                            if(bundle.containsKey("timestamp")){
                                req.timeStamp=bundle.getString("timestamp");
                            }
                            if(bundle.containsKey("sign")){
                                req.sign=bundle.getString("sign");
                            }
                            if(bundle.containsKey("orderid")){
                                UserInfoBean.setOrderId(this, bundle.getString("orderid"));
                            }
                            if(bundle.containsKey("prepayid")){
                                UserInfoBean.setPrepayId(this, bundle.getString("prepayid"));
                            }
                            LogUtil.e("appid:"+req.appId+" partnerid:"+req.partnerId+"  prepayid:"+req.prepayId+" package:"+req.packageValue+" :nonstr:"+req.nonceStr+" sign:"+req.sign);
                            WXBean.sendReq(req);

//                            api.sendReq(req);


                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.payQB_KF:
                try {
                    Bundle bundle = ParserJsonBean.parserOrder(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            ToastUtil.makeShortText(this,
                                    "预订成功");
                            Intent intent=new Intent(this,KefangRecordActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.hybridPay_KF:
                try {
                    Bundle bundle = ParserJsonBean.parserOrder(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            ToastUtil.makeShortText(this,
                                    "预订成功");
                            Intent intent=new Intent(this,KefangRecordActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtil.makeShortText(this,
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
