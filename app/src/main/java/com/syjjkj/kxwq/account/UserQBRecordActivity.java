package com.syjjkj.kxwq.account;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;

/**
 * Created by Administrator on 2017/11/21.
 */
public class UserQBRecordActivity extends BaseActivity {
    private WebView wv;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_moneyrecord);
        initView();
        initListener();
        initData();
    }
    private void initData() {
        wv.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/qianbao/jyjl.php?id="+ UserInfoBean.uid);
    }
    private void initListener() {

    }

    private void initView() {
        wv=(WebView)findViewById(R.id.money_record);
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
}
