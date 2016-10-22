package com.scnu.zhou.signer.component.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.component.util.image.ImageLoaderUtil;
import com.scnu.zhou.signer.ui.widget.image.CircleImageView;

import java.util.List;

/**
 * Created by zhou on 16/10/22.
 */
public class MainCourseAdapter extends BaseAdapter {

    private Context context;
    private List<MainCourse> mData;

    public MainCourseAdapter(Context context, List<MainCourse> mData){
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MainCourse getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_main, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
            viewHolder.ll_avatar = (LinearLayout) convertView.findViewById(R.id.ll_avatar);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tv_title.setText(mData.get(position).getName());
        viewHolder.tv_text.setText(mData.get(position).getNumber() + "人参与签到");

        for (String header: mData.get(position).getAvatars()){

            View item_header = LayoutInflater.from(context).inflate(R.layout.item_header, null);
            CircleImageView iv_header = (CircleImageView) item_header.findViewById(R.id.iv_header);
            ImageLoaderUtil.getInstance().displayHeaderImage(iv_header, header);

            viewHolder.ll_avatar.addView(item_header);
        }

        return convertView;
    }

    private static class ViewHolder{

        private TextView tv_title;
        private TextView tv_text;
        private LinearLayout ll_avatar;
    }
}
