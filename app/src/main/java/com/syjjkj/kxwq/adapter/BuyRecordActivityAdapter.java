package com.syjjkj.kxwq.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.ticket.BuyRecordDetail;
import com.syjjkj.kxwq.util.MyClick;

import java.util.ArrayList;
import java.util.HashMap;


public class BuyRecordActivityAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> list;
    private MyClick myClick;
    Context context;

    public BuyRecordActivityAdapter(Context context, MyClick myClick){
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<HashMap<String, Object>>();
        this.myClick = myClick;
        this.context=context;
        setData(list);
    }

    public void setData(ArrayList<HashMap<String, Object>> list){
        if(list != null){
            this.list = list;
        }else{
            this.list = new ArrayList<HashMap<String, Object>>();
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if(null == convertView){
            convertView = inflater.inflate(R.layout.item_buy_record, null);
            holdView = new HoldView();

            holdView.tvName = (TextView) convertView.findViewById(R.id.item_buy_record_1_tv_1);
            holdView.tvDate = (TextView) convertView.findViewById(R.id.item_buy_record_1_tv_2);
            holdView.tvPrice = (TextView) convertView.findViewById(R.id.item_buy_record_2_tv_1);
            holdView.tvPayState = (TextView) convertView.findViewById(R.id.item_buy_record_2_tv_2);
            holdView.tvPayOrReturn = (TextView) convertView.findViewById(R.id.item_buy_record_tv_pay);
            holdView.tvMan=(TextView)convertView.findViewById(R.id.item_buy_record_man);
            holdView.tvDetail=(TextView)convertView.findViewById(R.id.item_buy_record_detail);
            //holdView.tvPohone=(TextView)convertView.findViewById(R.id.item_buy_record_phone) ;
            convertView.setTag(holdView);
        }else{
            holdView = (HoldView) convertView.getTag();
        }
        final HashMap<String, Object> hashMap = list.get(position);
//        holdView.rl_01.setTag(hashMap);

        if (hashMap.containsKey("suborder"))
        {
            ArrayList<HashMap<String, Object>> arraylist=(ArrayList< HashMap<String, Object>>)hashMap.get("suborder");
            holdView.tvName.setText(arraylist.get(0).get("product_name").toString()+arraylist.get(0).get("product_num").toString()+"张");
        }
        if(hashMap.containsKey("name"))
        {
            holdView.tvMan.setText((String)hashMap.get("name"));
        }
       /* if(hashMap.containsKey("phone"))
        {
            holdView.tvPohone.setText((String)hashMap.get("phone"));
        }*/
        if (hashMap.containsKey("real_price")) {
            holdView.tvPrice.setText("￥"+(String) hashMap.get("real_price"));
        }
        if (hashMap.containsKey("pay_status") && hashMap.containsKey("order_type") &&((String) hashMap.get("order_type")).trim().equals("1") ) {//1是正常票，3是积分支付的票
            //holdView.tvPayOrReturn.setVisibility(View.VISIBLE);
            if (((String) hashMap.get("pay_status")).trim().equals("0")){
                holdView.tvPayState.setText("/未支付");
                holdView.tvPayOrReturn.setVisibility(View.VISIBLE);
                holdView.tvDetail.setVisibility(View.INVISIBLE);
            }
            if (((String) hashMap.get("pay_status")).trim().equals("1")){
                holdView.tvPayState.setText("/已支付");
                holdView.tvPayOrReturn.setVisibility(View.INVISIBLE);
                holdView.tvDetail.setVisibility(View.VISIBLE);
            }
            if (((String) hashMap.get("pay_status")).trim().equals("2")){
                holdView.tvPayState.setText("/申请退款");
                holdView.tvPayOrReturn.setVisibility(View.INVISIBLE);
                holdView.tvDetail.setVisibility(View.VISIBLE);
            }
            if (((String) hashMap.get("pay_status")).trim().equals("3")){
                holdView.tvPayState.setText("/已退款");
                holdView.tvPayOrReturn.setVisibility(View.INVISIBLE);
                holdView.tvDetail.setVisibility(View.INVISIBLE);
            }
            if (((String) hashMap.get("pay_status")).trim().equals("4")){
                holdView.tvPayState.setText("/部分检票");
                holdView.tvPayOrReturn.setVisibility(View.INVISIBLE);
                holdView.tvDetail.setVisibility(View.VISIBLE);
            }
            if (((String) hashMap.get("pay_status")).trim().equals("5")){
                holdView.tvPayState.setText("/已使用");
                holdView.tvPayOrReturn.setVisibility(View.INVISIBLE);
                holdView.tvDetail.setVisibility(View.VISIBLE);
            }
            if (((String) hashMap.get("pay_status")).trim().equals("6")) {
                holdView.tvPayState.setText("/部分退款");
                holdView.tvPayOrReturn.setVisibility(View.INVISIBLE);
                holdView.tvDetail.setVisibility(View.VISIBLE);
            }
            if (((String) hashMap.get("pay_status")).trim().equals("7")) {
                holdView.tvPayState.setText("/已过期订单");
                holdView.tvPayOrReturn.setVisibility(View.INVISIBLE);
                holdView.tvDetail.setVisibility(View.INVISIBLE);
            }

        }else{
            holdView.tvPayOrReturn.setVisibility(View.INVISIBLE);
            holdView.tvDetail.setVisibility(View.INVISIBLE);
        }

        if (hashMap.containsKey("order_time")) {
            holdView.tvDate.setText((String) hashMap.get("order_time"));
        }
//        if (hashMap.containsKey("pics")) {
//            ArrayList<String> pics=(ArrayList<String>) hashMap.get("pics");
//            ShareImgAdapter siAdapter=new ShareImgAdapter(context);
//            siAdapter.setData(pics);
//            holdView.gvPics.setAdapter(siAdapter);
//           siAdapter.notifyDataSetChanged();
//
//        }
////        if (hashMap.containsKey(ParserJsonBean.VIEW_CNT)) {
////            holdView.purchase_tv_count.setText((String) hashMap.get(ParserJsonBean.VIEW_CNT)+"人观看");
////        }
        holdView.tvPayOrReturn.setTag(hashMap);
        holdView.tvPayOrReturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myClick.myClick(v, 1);
            }
        });
        holdView.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("order_tid",(String) hashMap.get("order_tid"));
                i.putExtra("name",(String)hashMap.get("name"));
                i.putExtra("phone",(String)hashMap.get("phone"));
                i.putExtra("real_price",(String)hashMap.get("real_price"));
                i.putExtra("order_time",(String)hashMap.get("order_time"));
                i.putExtra("fuzhuma",(String)hashMap.get("fuzhuma"));
                ArrayList<HashMap<String, Object>> arraylist1=(ArrayList< HashMap<String, Object>>)hashMap.get("suborder");
                i.putExtra("product_name",arraylist1.get(0).get("product_name").toString());
                i.putExtra("product_num",arraylist1.get(0).get("product_num").toString());
                i.putExtra("product_per_price",arraylist1.get(0).get("product_per_price").toString());
                i.setClass(context, BuyRecordDetail.class);
                context.startActivity(i);
            }
        });


        return convertView;
    }
    class HoldView{
        //		ImageView purchase_img;

        TextView tvName;
        TextView tvPrice;
        TextView tvPayState;
        TextView tvDate;
        TextView tvPayOrReturn;
        TextView tvMan;
        TextView tvPohone;
        TextView tvDetail;



    }
}


