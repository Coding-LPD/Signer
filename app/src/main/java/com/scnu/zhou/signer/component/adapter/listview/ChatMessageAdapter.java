package com.scnu.zhou.signer.component.adapter.listview;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.emotion.ExpressionUtil;
import com.scnu.zhou.signer.component.util.image.ImageLoaderUtil;
import com.scnu.zhou.signer.ui.widget.image.CircleImageView;

import java.util.List;

/**
 * Created by zhou on 16/11/8.
 */
public class ChatMessageAdapter extends BaseAdapter {

    private Context context;
    private List<ChatMessage> mData;

    public ChatMessageAdapter(Context context, List<ChatMessage> mData){

        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (!mData.get(position).getStudentId().equals(UserCache.getInstance().getId(context))) {
            // 他人的消息
            view = LayoutInflater.from(context).inflate(R.layout.listitem_chat_left, null);
        } else {   // 自己的消息
            view = LayoutInflater.from(context).inflate(R.layout.listitem_chat_right, null);
        }

        CircleImageView iv_avatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);

        ImageLoaderUtil.getInstance().displayHeaderImage(iv_avatar,
                mData.get(position).getAvatar());

        SpannableString spannableString = ExpressionUtil.getExpressionString(context,
                mData.get(position).getContent(), 40);
        tv_content.setText(spannableString );

        String timeStr[] = mData.get(position).getCreatedAt().split(" ");
        String hour = timeStr[1].substring(0, 2);
        String minute = timeStr[1].substring(3, 5);

        String time;
        if (Integer.parseInt(hour) > 12){

            int newHour = Integer.parseInt(hour) - 12;
            if (newHour >= 10) {
                time = newHour + ":" + minute + " PM";
            }
            else{
                time = "0" + newHour + ":" + minute + " PM";
            }
        }
        else{
            time = hour + ":" + minute + " AM";
        }

        tv_time.setText(time);

        return view;
    }
}
