package com.syjjkj.kxwq.account;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;

/**
 * Created by Administrator on 2017/11/15.
 */
public class MoneyRuleActivity extends BaseActivity {
    private WebView wv;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_moneyrule);
        initView();
        initListener();
        initData();
    }
    private void initData() {
        wv.loadUrl("https://www.kxhotspring.com/api/protected/apps/html/qzbm/index.php");
    }

    private void initListener() {


    }

    private void initView() {
        wv=(WebView)findViewById(R.id.money_ruledetail);
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
