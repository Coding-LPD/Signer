package com.scnu.zhou.signer.component.adapter.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;

import java.util.List;

/**
 * Created by zhou on 16/10/23.
 */
public class NoticeAdapter extends BaseAdapter {

    private Context context;
    private List<NoticeBean> mData;

    public NoticeAdapter(Context context, List<NoticeBean> mData){

        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public NoticeBean getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_notice, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_course_name = (TextView) convertView.findViewById(R.id.tv_course_name);
            viewHolder.tv_sign_state = (TextView) convertView.findViewById(R.id.tv_sign_state);
            viewHolder.tv_sign_at = (TextView) convertView.findViewById(R.id.tv_sign_at);
            viewHolder.tv_sign_distance = (TextView) convertView.findViewById(R.id.tv_sign_distance);
            viewHolder.tv_sign_number = (TextView) convertView.findViewById(R.id.tv_sign_number);
            viewHolder.tv_created_at = (TextView) convertView.findViewById(R.id.tv_created_at);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_course_name.setText(mData.get(position).getCourseName());
        if (mData.get(position).getSignState() == 1) {
            viewHolder.tv_sign_state.setText("同意了");
        }
        else{
            viewHolder.tv_sign_state.setText("拒绝了");
            viewHolder.tv_sign_state.setTextColor(Color.parseColor("#F83908"));
        }
        viewHolder.tv_sign_at.setText(mData.get(position).getSignAt());
        viewHolder.tv_sign_distance.setText(mData.get(position).getSignDistance() + " m");
        if (mData.get(position).getSignDistance() > 100){
            viewHolder.tv_sign_distance.setTextColor(Color.parseColor("#F83908"));
        }
        viewHolder.tv_sign_number.setText("已有" + mData.get(position).getSignNumber() + "人参与签到");
        viewHolder.tv_created_at.setText(mData.get(position).getCreatedAt());

        return convertView;
    }


    private static class ViewHolder{

        private TextView tv_course_name;
        private TextView tv_sign_state;
        private TextView tv_sign_at;
        private TextView tv_sign_distance;
        private TextView tv_sign_number;
        private TextView tv_created_at;
    }
}
