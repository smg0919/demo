package com.syjjkj.kxwq.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.activity.PicShowActivity;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.myview.MyGridView;
import com.syjjkj.kxwq.util.MyClick;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentDynamicAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> list;
    private MyClick myClick;
    Context context;

    public FragmentDynamicAdapter(Context context, MyClick myClick){
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
            convertView = inflater.inflate(R.layout.item_material, null);
            holdView = new HoldView();
//			holdView.purchase_img = (ImageView) convertView.findViewById(R.id.iv_car);
            holdView.ivHead = (ImageView) convertView.findViewById(R.id.item_iv_head);

            holdView.tvName = (TextView) convertView.findViewById(R.id.item_tv_name);
            holdView.tvContent = (TextView) convertView.findViewById(R.id.item_tv_contant);
            holdView.tvDate = (TextView) convertView.findViewById(R.id.item_tv_date);
            holdView.tvShare = (TextView) convertView.findViewById(R.id.item_tv_share);
            holdView.gvPics=(MyGridView) convertView.findViewById(R.id.item_gv_pics);
            convertView.setTag(holdView);
        }else{
            holdView = (HoldView) convertView.getTag();
        }
        final HashMap<String, Object> hashMap = list.get(position);
        final  ArrayList<String> ha=(ArrayList<String>)list.get(position).get("pics");
        for(String m:ha)
        {
            Log.v("msg",m);
        }
//        holdView.rl_01.setTag(hashMap);
        String logo="";
//        if (hashMap.containsKey(ParserJsonBean.CONTENT))
//        {
//            logo = (String)hashMap.get(ParserJsonBean.LOGO);
//        }
//        ImageLoader.getInstance().displayImage(UrlConfig.ROOT_URL_UPLOAD + logo, holdView.purchase_img, MyApplication.optionsCar);
        if (hashMap.containsKey(ParserJsonBean.CONTENT)) {
            holdView.tvContent.setText((String) hashMap.get(ParserJsonBean.CONTENT));
        }

        if (hashMap.containsKey(ParserJsonBean.POST_TIME)) {
            holdView.tvDate.setText((String) hashMap.get(ParserJsonBean.POST_TIME));
        }
        if (hashMap.containsKey("pics")) {
            ArrayList<String> pics=(ArrayList<String>) hashMap.get("pics");
            ShareImgAdapter siAdapter=new ShareImgAdapter(context);
            siAdapter.setData(pics);
            holdView.gvPics.setAdapter(siAdapter);
            siAdapter.notifyDataSetChanged();

        }
        holdView.gvPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if((ArrayList<String>) hashMap.get("pics")!=null){
                    Intent intent = new Intent(context, PicShowActivity.class);
                    intent.putStringArrayListExtra("pics", (ArrayList<String>) hashMap.get("pics"));
                    intent.putExtra("position", i);
                    context.startActivity(intent);

                }
            }
        });
//        if (hashMap.containsKey(ParserJsonBean.VIEW_CNT)) {
//            holdView.purchase_tv_count.setText((String) hashMap.get(ParserJsonBean.VIEW_CNT)+"人观看");
//        }
        holdView.tvShare.setTag(hashMap);
        holdView.tvShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myClick.myClick(v, 1);
            }
        });


        return convertView;
    }
    class HoldView{
        //		ImageView purchase_img;
        ImageView ivHead;
        TextView tvName;
        TextView tvContent;
        TextView tvDate;
        TextView tvShare;
        MyGridView gvPics;

    }
}


