package com.syjjkj.kxwq.dynamic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.adapter.FragmentDynamicAdapter;
import com.syjjkj.kxwq.homepage.BaseFragment;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.util.MyClick;
import com.syjjkj.kxwq.util.ShareUtils;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentDynamicKX.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class FragmentDynamicKX extends BaseFragment implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView>, PullToRefreshBase.OnLastItemVisibleListener,MyClick{

    private PullToRefreshListView list;
    private FragmentDynamicAdapter adapter;
    private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
private boolean isRefresh=false;
    private int page=1;


    public FragmentDynamicKX() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView =inflater.inflate(R.layout.fragment_dynamic, container, false);

            initView();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
     //   return inflater.inflate(R.layout.fragment_dynamic, container, false);
    }

    private void initView() {

        list = (PullToRefreshListView) rootView.findViewById(R.id.item_lv);
        list.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new FragmentDynamicAdapter(getActivity(), this);
        list.setAdapter(adapter);
    }
    private void initListener() {
        list.setOnRefreshListener(this);
        list.setOnLastItemVisibleListener(this);
    }

    // 获取动态分享的素材
    private void getMaterials(int page)
    {
        if (!StringUtil.isNetworkConnected(getActivity())) {
            ToastUtil.makeShortText(getActivity(), "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("page", String.valueOf(page));
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_MATERIAL, params,
                this, httpUtils, HttpStaticApi.get_material);
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        super.onSuccessHttp(responseInfo, resultCode);
        dismissDialog();
        list.onRefreshComplete();
        switch (resultCode) {
            case HttpStaticApi.get_material:
                try {
                    Bundle bundle = ParserJsonBean.parseMaterialList(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {

                            ArrayList<HashMap<String, Object>> arrayListTemp = (ArrayList<HashMap<String, Object>>) bundle
                                    .getSerializable(ParserJsonBean.LIST);
                            if (isRefresh) {
                                arrayList.clear();
                                arrayList.addAll(arrayListTemp);
                            } else {
                                if (arrayList == null || arrayList.size() <= 0) {
//							ToastUtil.makeShortText(getActivity(), "数据空");
//							rl_all.setVisibility(View.GONE);
//							rl_all_background.setVisibility(View.VISIBLE);
                                    return;
                                } else {
//								rl_all.setVisibility(View.VISIBLE);
//								rl_all_background.setVisibility(View.GONE);
                                    arrayList.addAll(arrayListTemp);

                                }
                            }
                            adapter.setData(arrayList);
                            adapter.notifyDataSetChanged();

                        } else {
                            ToastUtil.makeLongText(getActivity(),
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
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        super.onResume();
        isRefresh=true;
        page=1;
        getMaterials(page);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLastItemVisible() {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        isRefresh=true;
        showWaitDialog("正在努力加载！");
        getMaterials(page);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        page++;
        isRefresh=false;
        showWaitDialog("正在努力加载！");
        getMaterials(page);
    }

    @Override
    public void myClick(View view, int type) {
        switch (type){
            case 1:
                HashMap<String, Object> hashMap =(HashMap<String, Object>)view.getTag();
                ShareUtils.Wechat_Favorite(this,(String)hashMap.get(ParserJsonBean.CONTENT),(ArrayList<String>)hashMap.get("pics"));//分享至微信朋友圈
                break;
            default:
                break;
        }
    }

    @Override
    public void myLongClick(View view, int type) {

    }
}
