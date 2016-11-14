package com.scnu.zhou.signer.component.adapter.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;
import com.scnu.zhou.signer.component.util.time.TimeUtil;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_notice02, null);

            viewHolder = new ViewHolder();
            viewHolder.ll_left_corner = (LinearLayout) convertView.findViewById(R.id.ll_left_corner);
            viewHolder.iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
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
            viewHolder.ll_left_corner.setBackgroundResource(R.drawable.bg_card_right_corner);
            viewHolder.iv_state.setImageResource(R.drawable.icon_right);
            viewHolder.tv_sign_state.setText("同意签到");
        }
        else{
            viewHolder.ll_left_corner.setBackgroundResource(R.drawable.bg_card_error_corner);
            viewHolder.iv_state.setImageResource(R.drawable.icon_error);
            viewHolder.tv_sign_state.setText("拒绝签到");
            //viewHolder.tv_sign_state.setTextColor(Color.parseColor("#F83908"));
        }
        viewHolder.tv_sign_at.setText(mData.get(position).getSignAt().split(" ")[0]);
        viewHolder.tv_sign_distance.setText(mData.get(position).getSignDistance() + " m");
        if (mData.get(position).getSignDistance() > 100){
            viewHolder.tv_sign_distance.setTextColor(Color.parseColor("#FF5968"));
        }
        viewHolder.tv_sign_number.setText(mData.get(position).getSignNumber() + "人");
        viewHolder.tv_created_at.setText(
                TimeUtil.setDaysForNow(TimeUtil.stringToLong(mData.get(position).getConfirmAt(),
                        "yyyy-MM-dd HH:mm:ss")));

        return convertView;
    }


    private static class ViewHolder{

        private LinearLayout ll_left_corner;
        private ImageView iv_state;
        private TextView tv_course_name;
        private TextView tv_sign_state;
        private TextView tv_sign_at;
        private TextView tv_sign_distance;
        private TextView tv_sign_number;
        private TextView tv_created_at;
    }
}
