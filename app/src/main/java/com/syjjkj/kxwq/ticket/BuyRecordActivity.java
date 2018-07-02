package com.syjjkj.kxwq.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.adapter.BuyRecordActivityAdapter;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.HomePageActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.util.MyClick;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyRecordActivity extends BaseActivity implements  PullToRefreshBase.OnRefreshListener2<ListView>, PullToRefreshBase.OnLastItemVisibleListener,MyClick {
    private PullToRefreshListView listView;
    private BuyRecordActivityAdapter adapter;
    private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
    private boolean isRefresh=false;
    private int page=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_record);
        initView();
        initData();
        initListener();
    }
    private void initData(){
        page=1;
        isRefresh=true;
        adapter=new BuyRecordActivityAdapter(this,this);
        listView.setAdapter(adapter);
        getBuyRecords(page);

    }
    private void initView(){
        listView=(PullToRefreshListView) findViewById(R.id.buy_record_lv);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                Intent intent=new Intent(BuyRecordActivity.this, HomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
    private void initListener() {
        listView.setOnRefreshListener(this);
        listView.setOnLastItemVisibleListener(this);
    }
    // 获取动态分享的素材
    private void getBuyRecords(int page)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("page", String.valueOf(page));
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_TICKETS_RECORDS, params,
                this, httpUtils, HttpStaticApi.get_tickets_records);
    }
    // 获取动态分享的素材
    private void getRefund(String strOrderId,String strSid,String strNum)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("orderid", strOrderId);
        params.addBodyParameter("sid", strSid);
        params.addBodyParameter("num", strNum);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_REFUND, params,
                this, httpUtils, HttpStaticApi.get_refund);
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        super.onSuccessHttp(responseInfo, resultCode);
        dismissDialog();
        listView.onRefreshComplete();
        switch (resultCode) {
            case HttpStaticApi.get_tickets_records:
                try {
                    Bundle bundle = ParserJsonBean.parseBuyTicketsRecords(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {

                            ArrayList<HashMap<String, Object>> arrayListTemp = (ArrayList<HashMap<String, Object>>) bundle
                                    .getSerializable(ParserJsonBean.LIST);
                            if (isRefresh) {
                                arrayList.clear();
                                arrayList.addAll(arrayListTemp);
                            } else {
                                if (arrayList == null || arrayList.size() <= 0) {
//							ToastUtil.makeShortText(getActivity(), "数据空");
//							rl_all.setVisibility(View.GONE);
//							rl_all_background.setVisibility(View.VISIBLE);
                                    return;
                                } else {
//								rl_all.setVisibility(View.VISIBLE);
//								rl_all_background.setVisibility(View.GONE);
                                    arrayList.addAll(arrayListTemp);

                                }
                            }
                            adapter.setData(arrayList);
                            adapter.notifyDataSetChanged();

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
            case HttpStaticApi.get_refund:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {

                            ToastUtil.makeLongText(this, "退款申请提交成功!");

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
    @Override
    public void onLastItemVisible() {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        isRefresh=true;
        showWaitDialog("正在努力加载！");
        getBuyRecords(page);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        page++;
        isRefresh=false;
        showWaitDialog("正在努力加载！");
        getBuyRecords(page);
    }

    @Override
    public void myClick(View view, int type) {
        HashMap<String, Object> hashMap=( HashMap<String, Object>)view.getTag();
        switch (type){
            case 1:
                if (((String) hashMap.get("pay_status")).trim().equals("0")){//按钮上显示支付
                    Intent intent=new Intent(this,PayTypeActivity.class);
                    intent.putExtra(ParserJsonBean.MONEY,((String) hashMap.get("real_price")));
                    intent.putExtra(ParserJsonBean.PV,String.valueOf((Float.valueOf(((String) hashMap.get("real_price"))))* UserInfoBean.getTicketstypeCount(this)));
                    intent.putExtra("order_id",(String)hashMap.get(ParserJsonBean.ID));
                    intent.putExtra("strTotalPrice",((String) hashMap.get("real_price")));
                    startActivity(intent);
                }/*else  if (((String) hashMap.get("pay_status")).trim().equals("1")){//按钮上显示退款
                    String orderid=(String)hashMap.get(ParserJsonBean.ID);
                    String sid=((ArrayList< HashMap<String, Object>>)hashMap.get("suborder")).get(0).get("sid").toString();
                    String num=((ArrayList< HashMap<String, Object>>)hashMap.get("suborder")).get(0).get("product_num").toString();
                  getRefund(orderid,sid,num);
                }*/
                break;
        }

    }

    @Override
    public void myLongClick(View view, int type) {

    }
}
