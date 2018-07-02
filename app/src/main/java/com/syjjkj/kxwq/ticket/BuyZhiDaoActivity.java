package com.syjjkj.kxwq.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;

/**
 * Created by Administrator on 2017/10/26.
 */
public class BuyZhiDaoActivity extends BaseActivity {
    private String tips;
    private ImageView back;
    private WebView tvtips;
    private Button Bback;
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        tips = intent.getStringExtra("tips");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_buy_ticketzhidao);
        initView();
        initListener();
        initData();
    }
    private void initData() {
        tvtips.loadDataWithBaseURL(null, tips.toString(), "text/html" , "utf-8", null);
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        back=(ImageView)findViewById(R.id.back);
        Bback=(Button)findViewById(R.id.subback);
        tvtips=(WebView)findViewById(R.id.text);
    }
}
