package com.syjjkj.kxwq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.http.ParserJsonBean;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentAccountSalesAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> list;

    Context context;

    public FragmentAccountSalesAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<HashMap<String, Object>>();
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
            convertView = inflater.inflate(R.layout.item_sales_record, null);
            holdView = new HoldView();

            holdView.tvName = (TextView) convertView.findViewById(R.id.item_sales_record_tv_1);
            holdView.tvPrice = (TextView) convertView.findViewById(R.id.item_sales_record_tv_2);
            holdView.tvReturnPV = (TextView) convertView.findViewById(R.id.item_sales_record_tv_4);
            holdView.tvDate = (TextView) convertView.findViewById(R.id.item_sales_record_tv_6);

            convertView.setTag(holdView);
        }else{
            holdView = (HoldView) convertView.getTag();
        }
        final HashMap<String, Object> hashMap = list.get(position);
//        holdView.rl_01.setTag(hashMap);

        if (hashMap.containsKey(ParserJsonBean.NAME))
        {

            holdView.tvName.setText((String)hashMap.get(ParserJsonBean.NAME));
        }
        if (hashMap.containsKey(ParserJsonBean.MONEY)) {
            holdView.tvPrice.setText((String) hashMap.get(ParserJsonBean.MONEY));
        }
        if (hashMap.containsKey(ParserJsonBean.PV)) {

            holdView.tvReturnPV.setText((String) hashMap.get(ParserJsonBean.PV));
        }

        if (hashMap.containsKey(ParserJsonBean.TIME)) {
            holdView.tvDate.setText((String) hashMap.get(ParserJsonBean.TIME));
        }


        return convertView;
    }
    class HoldView{
        //		ImageView purchase_img;

        TextView tvName;
        TextView tvPrice;
        TextView tvReturnPV;
        TextView tvDate;



    }
}


