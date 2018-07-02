package com.syjjkj.kxwq.card;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;

/**
 * Created by Administrator on 2018/4/17.
 */
public class CardProtocolActivity extends BaseActivity{
    private WebView wv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_rule);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        wv.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/member/protocol.php");
    }

    private void initListener() {


    }

    private void initView() {
        wv = (WebView) findViewById(R.id.card_rule);
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
