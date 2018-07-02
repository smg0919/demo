package com.syjjkj.kxwq.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;

/**
 * Created by Administrator on 2017/11/15.
 */
public class UserPaymanagerActivity extends BaseActivity implements View.OnClickListener{
    private String phone;
    private TextView Tphone;
    private RelativeLayout Rpayword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        phone=intent.getStringExtra("phone");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_paymanager);
        initView();
        initData();
        initListener();
    }
    private void initView(){
        Tphone=(TextView)findViewById(R.id.item_phone);
        Rpayword=(RelativeLayout)findViewById(R.id.payword);
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
        Rpayword.setOnClickListener(this);
    }
    private void initData(){
        Tphone.setText(phone);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.payword:
                Intent intent1=new Intent(this,UserPaywordActivity.class);
                intent1.putExtra("phone",phone);
                startActivity(intent1);
                break;
        }
    }
}
