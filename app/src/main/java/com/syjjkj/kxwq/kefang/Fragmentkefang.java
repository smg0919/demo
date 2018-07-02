package com.syjjkj.kxwq.kefang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseFragment;

/**
 * Created by Administrator on 2017/10/25.
 */
public class Fragmentkefang extends BaseFragment {
    private WebView wv;
    private TextView Trecord;
    public Fragmentkefang() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        rootView=inflater.inflate(R.layout.fragment_kfyd, container, false);

        initView();
        initlistener();
        initData();
        return rootView;
    }
    private void initView() {
        wv=(WebView)rootView.findViewById(R.id.login_iv_show);
        //Trecord=(TextView)rootView.findViewById(R.id.tv_title_right);

    }
    private void initlistener()
    {
        /*Trecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),KefangRecordActivity.class);
                startActivity(intent);
            }
        });*/

    }
    private void initData() {
        final WebSettings webSettings = wv.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadUrl("https://www.kxhotspring.com/api/Wechat/Rooms/query/kfyd.php");
        wv.addJavascriptInterface(new JsInterfaces(this.getActivity()), "AndroidWebView");
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("http") & !url.startsWith("https")) {
                    return false;

                } else {
                    view.loadUrl(url);
                    return true;
                }
            }
        });
    }
    private class JsInterfaces {
        private Context mContext;

        public JsInterfaces(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void yddetail(String room_name,String price,String time) {
            Intent intent = new Intent(getActivity(), KefangdetailActivity.class);
            intent.putExtra("room_name",room_name);
            intent.putExtra("price",price);
            intent.putExtra("time",time);
            startActivity(intent);
        }
    }
}
