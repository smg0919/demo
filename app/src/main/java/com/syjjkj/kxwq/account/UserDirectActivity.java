package com.syjjkj.kxwq.account;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
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

/**
 * Created by Administrator on 2017/11/7.
 */
public class UserDirectActivity extends BaseActivity  implements MyClick {
    private PullToRefreshListView listView;
    private AlllvxingsheAdapter adapter;
    private AlllvxingsheAdapter.HoldView holdView;
    private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_directtype);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        getCompanys();
        adapter=new AlllvxingsheAdapter(this,this);
        listView.setAdapter(adapter);//加载旅行社
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.alllvxingshes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*Intent intent=new Intent(BuyRecordActivity.this, HomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });

    }

    private void initListener() {

    }

    private void getCompanys() {
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
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_ALLLVXINGSHE, params,
                this, httpUtils, HttpStaticApi.get_alllvxingshe);
    }
    private void changeCompany(String id) {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("compid",id);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.changeCompany, params,
                this, httpUtils, HttpStaticApi.changeCompany);
    }


    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        super.onSuccessHttp(responseInfo, resultCode);
        dismissDialog();
        switch (resultCode) {
            case HttpStaticApi.get_alllvxingshe:
                try {
                    Bundle bundle = ParserJsonBean.alllvxingshe(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {

                            ArrayList<HashMap<String, Object>> arrayListTemp = (ArrayList<HashMap<String, Object>>) bundle.getSerializable(ParserJsonBean.lxs);
                           /* if (arrayList == null || arrayList.size() <= 0) {
//							ToastUtil.makeShortText(getActivity(), "数据空");
//							rl_all.setVisibility(View.GONE);
//							rl_all_background.setVisibility(View.VISIBLE);
                                    return;
                                } else {
//								rl_all.setVisibility(View.VISIBLE);
//								rl_all_background.setVisibility(View.GONE);


                                }*/
                            //arrayList.addAll(arrayListTemp);
                            adapter.setData(arrayListTemp);
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
            case HttpStaticApi.changeCompany:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {

                            ToastUtil.makeLongText(this,
                                    "修改所属成功!");

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
    public void myClick(View view, int type) {
        HashMap<String, Object> hashMap=( HashMap<String, Object>)view.getTag();
        switch (type){
            case 1:
                final String id=hashMap.get("id").toString();
                new AlertDialog.Builder(this)
                        .setTitle("申请修改所属")
                        .setMessage("此账号是否确认申请所属为:"+hashMap.get("name").toString())
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {


                                    }
                                })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {

                                        changeCompany(id);
                                    }
                                }).show();
                break;
        }

    }
    @Override
    public void myLongClick(View view, int type) {

    }
}
