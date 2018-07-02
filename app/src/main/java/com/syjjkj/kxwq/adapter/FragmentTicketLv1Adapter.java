package com.syjjkj.kxwq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.util.MyClick;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentTicketLv1Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> list;
    private MyClick myClick;
    Context context;

    public FragmentTicketLv1Adapter(Context context, MyClick myClick){
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<HashMap<String, Object>>();
        this.myClick = myClick;
        this.context=context;
        setData(list);
    }

    public void setData(ArrayList<HashMap<String, Object>> list){
        if(list != null){
            this.list = list;
        }else{  this.list = new ArrayList<HashMap<String, Object>>();

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
            convertView = inflater.inflate(R.layout.item_tickets_product, null);
            holdView = new HoldView();
            holdView.rl_1=(RelativeLayout) convertView.findViewById(R.id.item_rl_1);

            holdView.tvName = (TextView) convertView.findViewById(R.id.item_tickets_product_tv_2);
            holdView.tvPrice = (TextView) convertView.findViewById(R.id.item_tickets_product_tv_1);

            convertView.setTag(holdView);
        }else{
            holdView = (HoldView) convertView.getTag();
        }
        final HashMap<String, Object> hashMap = list.get(position);
        if (hashMap.containsKey(ParserJsonBean.PRICE)) {
            holdView.tvPrice.setText((String) hashMap.get(ParserJsonBean.PRICE));
        }

        if (hashMap.containsKey(ParserJsonBean.PRODUCT_NAME)) {
            holdView.tvName.setText((String) hashMap.get(ParserJsonBean.PRODUCT_NAME));
        }

        holdView.rl_1.setTag(R.id.item_rl_1,hashMap);
        holdView.rl_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myClick.myClick(v, 1);
            }
        });


        return convertView;
    }
    class HoldView{
RelativeLayout rl_1;
        TextView tvName;
        TextView tvPrice;


    }
}


