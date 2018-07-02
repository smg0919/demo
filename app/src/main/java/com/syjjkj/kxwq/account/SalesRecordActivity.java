package com.syjjkj.kxwq.account;

import android.os.Bundle;
import android.widget.ListView;

import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.adapter.FragmentAccountSalesAdapter;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;
import com.syjjkj.kxwq.widget.ToolbarView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class SalesRecordActivity extends BaseActivity{
    private FragmentAccountSalesAdapter adapter;
    private ListView listView;
    private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_record);
        initView();
        initData();
    }
    private void initView(){
        listView=(ListView)findViewById(R.id.account_lv);
        ToolbarView toolbarView=new ToolbarView(this,"销售记录");
    }
    private void initData(){
        adapter=new FragmentAccountSalesAdapter(this);
        listView.setAdapter(adapter);
        getsalesRecord();
    }
    // 获取下级购买记录
    private void getsalesRecord() {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        params = new RequestParams();
        params.addBodyParameter(ParserJsonBean.UID,UserInfoBean.uid);
        params.addBodyParameter(ParserJsonBean.TOKEN, UserInfoBean.token);
//        showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_SALES_RECORDS, params,
                this, httpUtils, HttpStaticApi.get_sales_records);
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {

            case HttpStaticApi.get_sales_records:
                try {
                    Bundle bundle = ParserJsonBean.parseSalesRecords(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {
                            ArrayList<HashMap<String, Object>> arrayListTemp1 = (ArrayList<HashMap<String, Object>>) bundle
                                    .getSerializable("list1");
                            ArrayList<HashMap<String, Object>> arrayListTemp2 = (ArrayList<HashMap<String, Object>>) bundle
                                    .getSerializable("list2");
                            arrayList.clear();
                            if (arrayListTemp1!=null)
                                arrayList.addAll(arrayListTemp1);
                            if (arrayListTemp2!=null)
                                arrayList.addAll(arrayListTemp2);

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

            default:
                break;
        }
    }
}
