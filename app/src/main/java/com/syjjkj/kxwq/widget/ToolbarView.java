package com.syjjkj.kxwq.widget;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;

/**
 * Created by Administrator on 2017/7/6.
 */
public class ToolbarView {
    private TextView tvTitleCenter;
    /**
     * 有返回箭头的ToolBar,中间带题目
     * @param context
     */
    public ToolbarView(final BaseActivity context,String strTitle){
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitleCenter=(TextView)context.findViewById(R.id.tv_title_center);
        tvTitleCenter.setText(strTitle);
        context.setSupportActionBar(toolbar);
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
    }
}
