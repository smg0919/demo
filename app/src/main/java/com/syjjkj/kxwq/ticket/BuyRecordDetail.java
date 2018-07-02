package com.syjjkj.kxwq.ticket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
import com.syjjkj.kxwq.myview.AmountView;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

/**
 * Created by Administrator on 2017/7/25.
 */
public class BuyRecordDetail extends BaseActivity implements View.OnClickListener {
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvtolPrice;
    private TextView tvNum;
    private TextView tvMan;
    private TextView tvPhone;
    private TextView tvOrderid;
    private TextView tvFuzhuid;
    private TextView order_tim;
    //private ImageView back;
    private Double totalprice;
    private String id;
    private String name;
    private String phone;
    private String real_price;
    private String product_per_price;
    private String order_time;
    private String fuzhuma;
    private String product_name;
    private String product_num;
    private Button Bbackmoney;
    private AmountView mAmountView;
    private int iTicketCount=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        id = intent.getStringExtra("order_tid");
        name=intent.getStringExtra("name");
        phone=intent.getStringExtra("phone");
        real_price=intent.getStringExtra("real_price");
        order_time=intent.getStringExtra("order_time");
        fuzhuma=intent.getStringExtra("fuzhuma");
        product_name=intent.getStringExtra("product_name");
        product_num=intent.getStringExtra("product_num");
        product_per_price=intent.getStringExtra("product_per_price");
        totalprice=Double.valueOf(product_num)*Double.valueOf(product_per_price);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_buy_ticketdetail);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        tvtolPrice.setText("￥"+String.valueOf(totalprice));
        tvPrice.setText("￥"+product_per_price);
        tvPhone.setText(phone);
        tvOrderid.setText(id);
        tvMan.setText(name);
        tvName.setText(product_name);
        tvFuzhuid.setText(fuzhuma);
        tvNum.setText(product_num);
        order_tim.setText(order_time);
        mAmountView = (AmountView) findViewById(R.id.buy_ticket_amount_views);
        mAmountView.setGoods_storage(Integer.MAX_VALUE);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
//                Toast.makeText(getApplicationContext(), "Amount=>  " + amount, Toast.LENGTH_SHORT).show();
                iTicketCount=amount;
            }
        });


    }

    private void initListener() {
        Bbackmoney.setOnClickListener(this);

    }

    private void initView() {
        tvName=(TextView) findViewById(R.id.item_buy_recorddetailname); //票种
        tvMan=(TextView)findViewById(R.id.item_buy_recorddetailman);  //联系人
        tvFuzhuid=(TextView)findViewById(R.id.item_buy_recorddetailfuzhuid);
        tvNum=(TextView)findViewById(R.id.item_buy_recorddetailnum);
        tvOrderid=(TextView)findViewById(R.id.item_buy_recorddetailorderid);
        tvPhone=(TextView)findViewById(R.id.item_buy_recorddetailphone);
        tvPrice=(TextView)findViewById(R.id.item_buy_recorddetailprice);
        tvtolPrice=(TextView)findViewById(R.id.item_buy_recorddetaitotalprice);
        order_tim=(TextView)findViewById(R.id.item_buy_recorddetailordertime);
        Bbackmoney=(Button)findViewById(R.id.backmoney);
        //back=(ImageView)findViewById(R.id.back);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backmoney:
                if (StringUtil.isEmpty(iTicketCount+"")){
                    ToastUtil.makeShortText(this,"请选择退款票数");
                    return;
                }
                if (iTicketCount>Integer.valueOf(product_num)){
                    ToastUtil.makeShortText(this,"退票数量大于购票数量");
                    return;
                }
                new AlertDialog.Builder(this)
                        .setTitle("退票申请")
                        .setMessage("请确认是否退票")


                        .setPositiveButton("确认",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {

                                        TuiTickets(id,iTicketCount+"");
                                    }
                                })
                        .setNegativeButton("返回",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();

                break;

        }


    }
    private void TuiTickets(String id,String iTicketCount)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("orderid", id);
        params.addBodyParameter("num",iTicketCount);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_REFUND, params,
                this, httpUtils, HttpStaticApi.get_refund);
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.get_refund:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {

                            ToastUtil.makeLongText(this,
                                    "退款申请提交成功!");

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
