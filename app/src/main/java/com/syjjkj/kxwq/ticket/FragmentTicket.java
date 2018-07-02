package com.syjjkj.kxwq.ticket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.adapter.FragmentTicketLv1Adapter;
import com.syjjkj.kxwq.homepage.BaseFragment;
import com.syjjkj.kxwq.homepage.MyApplication;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.myview.MyListView;
import com.syjjkj.kxwq.util.MyClick;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTicket.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTicket#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTicket extends BaseFragment implements View.OnClickListener,MyClick{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int currentLinePosition;// 底部滑动线所在位置
    private View ivBottomLine;// 标题栏 底部滑动线
    private int screenW;
    private RelativeLayout rl_1,rl_2,rl_3,rl_c3,rl_c4,rl_c5;
    private TextView tv_1,tv_2,tv_3,optime,des1,des2;
    private MyListView lv_1;

    private FragmentTicketLv1Adapter adapter1;


    public FragmentTicket() {
        // Required empty public constructor
    }
    @Override       //这里是实现了自动更新
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initView();
        initData();
        initListener();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTicket.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTicket newInstance(String param1, String param2) {
        FragmentTicket fragment = new FragmentTicket();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView =inflater.inflate(R.layout.fragment_ticket, container, false);
            initView();
            initListener();
            initData();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }
    private void initView(){
        rl_1=(RelativeLayout) rootView.findViewById(R.id.rl_1);
        rl_2=(RelativeLayout) rootView.findViewById(R.id.rl_2);
        rl_3=(RelativeLayout) rootView.findViewById(R.id.rl_3);
        rl_c3 =(RelativeLayout)rootView.findViewById(R.id.rl_content_3);
        rl_c4 =(RelativeLayout)rootView.findViewById(R.id.rl_content_4);
        rl_c5 =(RelativeLayout)rootView.findViewById(R.id.rl_content_5);
        rl_c3.setVisibility(View.INVISIBLE);
        rl_c4.setVisibility(View.INVISIBLE);
        rl_c5.setVisibility(View.INVISIBLE);
        tv_1=(TextView) rootView.findViewById(R.id.rl_1_tv);
        tv_2=(TextView) rootView.findViewById(R.id.rl_2_tv);
        tv_3=(TextView) rootView.findViewById(R.id.rl_3_tv);
        optime=(TextView)rootView.findViewById(R.id.rl_content_4_tv_1);
        lv_1=(MyListView) rootView.findViewById(R.id.ticket_lv_1);
        des1=(TextView)rootView.findViewById(R.id.rl_content_5_tv_1);
        des2=(TextView)rootView.findViewById(R.id.rl_content_5_tv_3);
        tvRight = (TextView) rootView.findViewById(R.id.tv_title_right);
        ivBottomLine = rootView.findViewById(R.id.line_bottom);

    }
    private void initListener(){
        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        tvRight.setOnClickListener(this);

    }
    private void initData(){
        adapter1=new FragmentTicketLv1Adapter(getActivity(),this);
        lv_1.setAdapter(adapter1);
        initWidth();
        getProductType();
        getProduct("1",HttpStaticApi.get_product_1);

    }
    /**
     * 设置滑动线的宽
     */
    private void initWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels / UserInfoBean.getTicketstypeCount(MyApplication.getInstance());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                screenW, 4);
        ivBottomLine.setLayoutParams(params);

         currentLinePosition = 0;
//        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
//        viewPager.setCurrentItem(0);
    }
    // 获取门票类型
    private void getProductType ()
    {
        if (!StringUtil.isNetworkConnected(getActivity())) {
            ToastUtil.makeShortText(getActivity(), "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_PRODUCT_TYPE, params,
                this, httpUtils, HttpStaticApi.get_product_type);
    }
    // 获取门票
    private void getProduct (String type,int resultCode)
    {
        if (!StringUtil.isNetworkConnected(getActivity())) {
            ToastUtil.makeShortText(getActivity(), "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("type", type);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_PRODUCT, params,
                this, httpUtils, resultCode);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Animation animation = null;
        switch (view.getId()){
            case R.id.rl_1:
                animation = new TranslateAnimation(currentLinePosition, 0, 0, 0);
                currentLinePosition = 0;
                if (animation==null) {

                }
                else
                {
                    animation.setFillAfter(true);
                    animation.setDuration(300);
                    ivBottomLine.startAnimation(animation);
                }
                getProduct("1",HttpStaticApi.get_product_1);
                break;
            case R.id.rl_2:
                animation = new TranslateAnimation(currentLinePosition,
                        screenW, 0, 0);
                currentLinePosition = screenW;
                if (animation==null) {

                }
                else
                {
                    animation.setFillAfter(true);
                    animation.setDuration(300);
                    ivBottomLine.startAnimation(animation);
                }
                getProduct("2", HttpStaticApi.get_product_1);
                break;
            case  R.id.rl_3:
                animation = new TranslateAnimation(currentLinePosition,
                        screenW * 2, 0, 0);
                currentLinePosition = screenW * 2;
                if (animation==null) {

                }
                else
                {
                    animation.setFillAfter(true);
                    animation.setDuration(300);
                    ivBottomLine.startAnimation(animation);
                }
                getProduct("3",HttpStaticApi.get_product_1);
                break;
           case R.id.tv_title_right:
               if (animation==null) {

               }
               else
               {
                   animation.setFillAfter(true);
                   animation.setDuration(300);
                   ivBottomLine.startAnimation(animation);
               }
                Intent intent=new Intent(getActivity(),BuyRecordActivity.class);
                startActivity(intent);
                break;
             }
    }

    @Override
    public void myClick(View view, int type) {
        Intent intent;
        switch (type){
            case 1:
              HashMap<String, Object> map= (HashMap<String, Object>) view.getTag(R.id.item_rl_1);
                intent=new Intent(getActivity(),BuyTicketActivity.class);
                intent.putExtra(ParserJsonBean.ID, (String) map.get(ParserJsonBean.ID));
                intent.putExtra(ParserJsonBean.PRODUCT_NAME, (String) map.get(ParserJsonBean.PRODUCT_NAME));
                intent.putExtra(ParserJsonBean.PRICE, (String) map.get(ParserJsonBean.PRICE));
                intent.putExtra(ParserJsonBean.EXPIRED, (String) map.get(ParserJsonBean.EXPIRED));
                intent.putExtra(ParserJsonBean.product_tips,(String)map.get(ParserJsonBean.product_tips));
                intent.putExtra(ParserJsonBean.product_detail,(String)map.get(ParserJsonBean.product_detail));
                intent.putExtra(ParserJsonBean.sel_date,(String)map.get(ParserJsonBean.sel_date));
                intent.putExtra(ParserJsonBean.play_date,(String)map.get(ParserJsonBean.play_date));
                intent.putExtra(ParserJsonBean.need_idcard,(String)map.get(ParserJsonBean.need_idcard));
                startActivity(intent);
                break;
        }

    }

    @Override
    public void myLongClick(View view, int type) {

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        super.onSuccessHttp(responseInfo, resultCode);
        dismissDialog();
        switch (resultCode) {
            case HttpStaticApi.get_product_type:
                try {
                    Bundle bundle = ParserJsonBean.parseProductType(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {
                            ArrayList<HashMap<String, Object>> arrayListTemp = (ArrayList<HashMap<String, Object>>) bundle
                                    .getSerializable(ParserJsonBean.LIST);
                            if (UserInfoBean.getTicketstypeCount(MyApplication.getInstance())>=3){
                                rl_1.setVisibility(View.VISIBLE);
                                rl_2.setVisibility(View.VISIBLE);
                                rl_3.setVisibility(View.VISIBLE);
                                tv_1.setText((String)arrayListTemp.get(0).get(ParserJsonBean.NAME));
                                tv_2.setText((String)arrayListTemp.get(1).get(ParserJsonBean.NAME));
                                tv_3.setText((String)arrayListTemp.get(2).get(ParserJsonBean.NAME));
                            }else if(UserInfoBean.getTicketstypeCount(MyApplication.getInstance())==2){
                                rl_1.setVisibility(View.VISIBLE);
                                rl_2.setVisibility(View.VISIBLE);
                                rl_3.setVisibility(View.GONE);
                                tv_1.setText((String)arrayListTemp.get(0).get(ParserJsonBean.NAME));
                                tv_2.setText((String)arrayListTemp.get(1).get(ParserJsonBean.NAME));
                            }else if(UserInfoBean.getTicketstypeCount(MyApplication.getInstance())==1){
                                rl_1.setVisibility(View.VISIBLE);
                                rl_2.setVisibility(View.GONE);
                                rl_3.setVisibility(View.GONE);
                                tv_1.setText((String)arrayListTemp.get(0).get(ParserJsonBean.NAME));
                            }


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
            case HttpStaticApi.get_product_1:
                try {
                    Bundle bundle = ParserJsonBean.parseProduct1(responseInfo);
                    if (bundle != null) {
                        int state = bundle.getInt(ParserJsonBean.STATE);
                        if (state == 1) {
                            ArrayList<HashMap<String, Object>> arrayListTemp = (ArrayList<HashMap<String, Object>>) bundle
                                    .getSerializable(ParserJsonBean.LIST);
                            optime.setText((String)(ParserJsonBean.opentime));
                            des1.setText((String)(ParserJsonBean.desc1));
                            des2.setText((String)(ParserJsonBean.desc2));
                            adapter1.setData(arrayListTemp);
                            adapter1.notifyDataSetChanged();


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
}
