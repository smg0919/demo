package com.syjjkj.kxwq.member;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.syjjkj.kxwq.adapter.BuyRecordActivityAdapter;
import com.syjjkj.kxwq.homepage.BaseFragment;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.ticket.PayTypeActivity;
import com.syjjkj.kxwq.util.MyClick;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMember.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMember#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMember extends BaseFragment implements MyClick,PullToRefreshBase.OnRefreshListener2<ListView>, PullToRefreshBase.OnLastItemVisibleListener {

    @Override
    public void myClick(View view,int type)
    {
        HashMap<String, Object> hashMap=( HashMap<String, Object>)view.getTag();
        switch (type){
            case 1:
                if (((String) hashMap.get("pay_status")).trim().equals("0")){//按钮上显示支付
                    Intent intent=new Intent(this.getContext(),PayTypeActivity.class);
                    intent.putExtra(ParserJsonBean.MONEY,((String) hashMap.get("real_price")));
                    intent.putExtra(ParserJsonBean.PV,String.valueOf((Float.valueOf(((String) hashMap.get("real_price"))))* UserInfoBean.getTicketstypeCount(this.getContext())));
                    intent.putExtra("order_id",(String)hashMap.get(ParserJsonBean.ID));
                    startActivity(intent);
                }/*else  if (((String) hashMap.get("pay_status")).trim().equals("1")){//按钮上显示退款
                    String orderid=(String)hashMap.get(ParserJsonBean.ID);
                    String sid=((ArrayList< HashMap<String, Object>>)hashMap.get("suborder")).get(0).get("sid").toString();
                    String num=((ArrayList< HashMap<String, Object>>)hashMap.get("suborder")).get(0).get("product_num").toString();
                  getRefund(orderid,sid,num);
                }*/
                break;
        }

    }
    @Override
    public void myLongClick(View view, int type) {

    }
    private PullToRefreshListView listView;
    private BuyRecordActivityAdapter adapter;
    private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
    private boolean isRefresh=false;
    private int page=1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
  /*  private String mParam1;
    private String mParam2;
    private EditText et_name,et_nickname,et_email;
    private Button btn_register;*/

    //private OnFragmentInteractionListener mListener;

    public FragmentMember() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMember.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMember newInstance(String param1, String param2) {
        FragmentMember fragment = new FragmentMember();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
super.onCreateView(inflater, container, savedInstanceState);
        rootView= inflater.inflate(R.layout.activity_buy_record, container, false);
        initView();
        initData();
        initListener();
        return  rootView;
    }

    private void initData() {
        page=1;
        isRefresh=true;
        adapter=new BuyRecordActivityAdapter(this.getContext(),this);
        listView.setAdapter(adapter);
        getBuyRecords(page);
    }

    private void initView(){
        listView=(PullToRefreshListView)rootView.findViewById(R.id.buy_record_lv);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        /*et_name=(EditText) rootView.findViewById(R.id.register_et_username);
        et_nickname=(EditText) rootView.findViewById(R.id.register_et_nickname);
        et_nickname=(EditText) rootView.findViewById(R.id.register_et_nickname);
        et_email=(EditText) rootView.findViewById(R.id.register_et_email);
        btn_register=(Button) rootView.findViewById(R.id.register_btn)*/;
    }
    private void initListener(){
        listView.setOnRefreshListener(this);
       listView.setOnLastItemVisibleListener(this);
       // btn_register.setOnClickListener(this);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
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
        /*mListener = null;*/
    }

   /* @Override
    public void onClick(View view) {
      *//*  switch (view.getId()){
            case R.id.register_btn:
                if (StringUtil.isEmpty(et_name.getText().toString()) || StringUtil.isEmpty(et_nickname.getText().toString()) || StringUtil.isEmpty(et_email.getText().toString())) {
                    ToastUtil.makeShortText(getActivity(), "用户名密码邮箱不能为空");
                    return;
                }
registerUser(UrlConfig.REGISTER_USER,et_name.getText().toString().trim(),et_nickname.getText().toString().trim(),et_email.getText().toString().trim(), HttpStaticApi.registerUser);
                break;
        }*//*
    }*/
    // 登陆方法
   /* private void registerUser(String url, String phone, String nickname,String email,
                           int resultCode) {
        if (!StringUtil.isNetworkConnected(getActivity())) {
            ToastUtil.makeShortText(getActivity(), "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(getActivity());
        params = new RequestParams();
        params.addBodyParameter(ParserJsonBean.PHONE, phone);
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("email", email);
        params.addBodyParameter("uid",UserInfoBean.uid);
        params.addBodyParameter("token",UserInfoBean.token);
        showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, url, params,
                this, httpUtils, resultCode);
    }*/
    private void getBuyRecords(int page)
    {
        if (!StringUtil.isNetworkConnected(this.getContext())) {
            ToastUtil.makeShortText(this.getContext(), "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this.getContext());
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("page", String.valueOf(page));
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_TICKETS_RECORDS, params,
                this, httpUtils, HttpStaticApi.get_tickets_records);
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        super.onSuccessHttp(responseInfo, resultCode);
        dismissDialog();
        listView.onRefreshComplete();
        switch (resultCode) {
            case HttpStaticApi.get_tickets_records:
                try {
                    Bundle bundle = ParserJsonBean.parseBuyTicketsRecords(responseInfo);
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
                            ToastUtil.makeLongText(this.getContext(),
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissDialog();
                break;
            case HttpStaticApi.get_refund:
                try {
                    Bundle bundle = ParserJsonBean.parserPublic(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {

                            ToastUtil.makeLongText(this.getContext(),
                                    "退款申请提交成功!");

                        } else {
                            ToastUtil.makeLongText(this.getContext(),
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissDialog();
                break;
        }
       /* dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.registerUser:

                try {
                    Bundle bundle = ParserJsonBean.parserRegisterUser(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {

                            ToastUtil.makeShortText(getActivity(),
                                   "注册成功，UID为："+bundle.get("uid"));
                        } else {
                            ToastUtil.makeShortText(getActivity(),
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }*/
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    @Override
    public void onLastItemVisible() {

    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        isRefresh=true;
        showWaitDialog("正在努力加载！");
        getBuyRecords(page);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        page++;
        isRefresh=false;
        showWaitDialog("正在努力加载！");
        getBuyRecords(page);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
