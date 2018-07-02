package com.syjjkj.kxwq.actives;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseFragment;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.member.FragmentMember;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMember.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMember#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentactives extends BaseFragment{
    private WebView wv;
    public fragmentactives() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        rootView=inflater.inflate(R.layout.share1, container, false);
        initView();
        //initlistener();
        initData();
        return rootView;
    }
    private void initView() {
        wv=(WebView)rootView.findViewById(R.id.activitys);
    }
    private void initData() {;
        final WebSettings webSettings = wv.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        //webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        Random random = new Random();
        wv.loadUrl("http://www.kxhotspring.com/api/protected/apps/html/huodong/list.php?type=Android&uid="+ UserInfoBean.uid);
        //在js中调用本地java方法
        wv.addJavascriptInterface(new JsInterface(this.getActivity()), "AndroidWebView");
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
    private class JsInterface {
        private Context mContext;
        public JsInterface(Context context) {
            this.mContext = context;
        }
        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void activityurl(String url,String activityid){
            Intent intent = new Intent(mContext, ActiviesDetailActivity.class);
            intent.putExtra("activityurl",url);
            intent.putExtra("activityid",activityid);
            startActivity(intent);
        }


    }
}
