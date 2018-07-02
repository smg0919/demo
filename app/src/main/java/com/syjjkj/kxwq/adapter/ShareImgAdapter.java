package com.syjjkj.kxwq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.MyApplication;
import com.syjjkj.kxwq.util.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/20.
 */
public class ShareImgAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private LayoutInflater inflater;
    private Context content;
    private int screenWidth;

    public ShareImgAdapter(Context content) {
        this.content = content;
        this.inflater = LayoutInflater.from(content);
        list = new ArrayList< String>();
        screenWidth = ScreenUtil.getScreenWidth(content);
    }

    public void setData(ArrayList<String> list) {
        if (list != null) {
            this.list = list;
        } else {
            list = new ArrayList<String>();
        }
        notifyDataSetChanged();
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_share_preview, null);
            holdView = new HoldView();
            holdView.iv = (ImageView) convertView
                    .findViewById(R.id.iv_share_preview);
            holdView.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //holdView.iv.setAdjustViewBounds(true);
            //holdView.iv.setMinimumHeight(10);
           // holdView.iv.setMinimumWidth(10);
            //holdView.iv.setMaxHeight(100);
            //holdView.iv.setMaxWidth(50);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                (screenWidth - StringUtil.dip2px(content, 80)) / 4,
//                (screenWidth - StringUtil.dip2px(content, 80)) / 4);
//        holdView.iv.setLayoutParams(params);
        ImageLoader.getInstance().displayImage(list.get(position), holdView.iv, MyApplication.optionsPerson);
        return convertView;
    }

    class HoldView {
        ImageView iv;
    }
}
