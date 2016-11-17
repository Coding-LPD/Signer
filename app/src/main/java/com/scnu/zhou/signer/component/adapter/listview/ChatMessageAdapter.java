package com.scnu.zhou.signer.component.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.image.ImageLoaderUtil;

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
        ViewHolder viewHolder;

        if (!mData.get(position).getStudentId().equals(UserCache.getInstance().getId(context))) {
            // 他人的消息
            view = LayoutInflater.from(context).inflate(R.layout.listitem_chat_left, null);
        } else {   // 自己的消息
            view = LayoutInflater.from(context).inflate(R.layout.listitem_chat_right, null);
        }

        if (convertView == null) {

            viewHolder = new ViewHolder();
            viewHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(viewHolder);
        }
        else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoaderUtil.getInstance().displayHeaderImage(viewHolder.iv_avatar,
                mData.get(position).getAvatar());
        viewHolder.tv_content.setText(mData.get(position).getContent());

        String time[] = mData.get(position).getCreatedAt().split(" ");
        viewHolder.tv_time.setText(time[1]);

        return view;
    }

    private static class ViewHolder{

        private ImageView iv_avatar;
        private TextView tv_content;
        private TextView tv_time;
    }
}
