package com.syjjkj.kxwq.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseFragment;

/**
 * Created by Administrator on 2018/4/10.
 */
public class fragmentcard extends BaseFragment implements View.OnClickListener {
    private RelativeLayout Rcard_rege;
    private RelativeLayout Rcard_chongzhi;
    private RelativeLayout Rcard_detail;
    //private RelativeLayout Rcard_exit;
    private RelativeLayout Rcard_rule;
    private RelativeLayout Rcard_update;
    private RelativeLayout Rcard_money;
    public fragmentcard() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        rootView=inflater.inflate(R.layout.fragment_card, container, false);
        initView();
        initlistener();
        //initData();
        return rootView;
    }
    private void initView(){
        Rcard_rege=(RelativeLayout) rootView.findViewById(R.id.card_rege);
        Rcard_chongzhi=(RelativeLayout) rootView.findViewById(R.id.card_chongzhi);
        Rcard_detail=(RelativeLayout)rootView.findViewById(R.id.card_detail) ;
        Rcard_rule=(RelativeLayout)rootView.findViewById(R.id.card_rule) ;
        Rcard_update=(RelativeLayout)rootView.findViewById(R.id.card_update);
        Rcard_money=(RelativeLayout)rootView.findViewById(R.id.card_money);
        //Rcard_exit=(RelativeLayout)rootView.findViewById(R.id.card_exit) ;
    }
    private void initlistener(){
        Rcard_rege.setOnClickListener(this);
        Rcard_chongzhi.setOnClickListener(this);
        Rcard_detail.setOnClickListener(this);
        Rcard_rule.setOnClickListener(this);
        Rcard_update.setOnClickListener(this);
        Rcard_money.setOnClickListener(this);
        //Rcard_exit.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.card_rege:
                intent = new Intent(getActivity(), CardRegeActivity.class);
                startActivity(intent);
                break;
            case R.id.card_chongzhi:
                intent = new Intent(getActivity(), CardChongzhiActivity.class);
                startActivity(intent);
                break;
            case R.id.card_detail:
                intent = new Intent(getActivity(), CardDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.card_rule:
                intent = new Intent(getActivity(), CardProtocolActivity.class);
                startActivity(intent);
                break;
            case R.id.card_update:
                intent = new Intent(getActivity(),CardUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.card_money:
                intent = new Intent(getActivity(),CardSelmoneyActivity.class);
                startActivity(intent);
                break;
            /*case R.id.card_exit:
                intent = new Intent(getActivity(), CardExitActivity.class);
                startActivity(intent);
                break;*/
        }
    }
}
