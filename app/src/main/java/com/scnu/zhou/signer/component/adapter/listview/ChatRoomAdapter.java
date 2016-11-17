package com.scnu.zhou.signer.component.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.util.image.ImageLoaderUtil;
import com.scnu.zhou.signer.component.util.time.TimeUtil;

import java.util.List;

/**
 * Created by zhou on 16/11/3.
 */
public class ChatRoomAdapter extends BaseAdapter {

    private Context context;
    private List<ChatRoom> mData;

    public ChatRoomAdapter(Context context, List<ChatRoom> mData){

        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ChatRoom getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Viewholder viewholder = null;

        if (convertView == null){

            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_chat, null);
            viewholder = new Viewholder();
            viewholder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewholder.tv_chat_title = (TextView) convertView.findViewById(R.id.tv_chat_title);
            viewholder.tv_chat_content = (TextView) convertView.findViewById(R.id.tv_chat_content);
            viewholder.tv_latest_time = (TextView) convertView.findViewById(R.id.tv_latest_time);
            viewholder.tv_people_count = (TextView) convertView.findViewById(R.id.tv_people_count);

            convertView.setTag(viewholder);
        }
        else{

            viewholder = (Viewholder) convertView.getTag();
        }

        viewholder.tv_chat_title.setText(mData.get(position).getName());
        viewholder.tv_people_count.setText(mData.get(position).getCount() + "");
        if (mData.get(position).getMsg() != null) {
            viewholder.tv_chat_content.setText(mData.get(position).getMsg().getContent());
            viewholder.tv_latest_time.setText(TimeUtil.setDaysForNow(TimeUtil.stringToLong(mData.get(position)
                    .getMsg().getCreatedAt(), "yyyy-MM-dd HH:mm:ss")));
            ImageLoaderUtil.getInstance().displayHeaderImage(viewholder.iv_avatar,
                    mData.get(position).getMsg().getAvatar());
        }
        else{
            viewholder.tv_chat_content.setText("还没有人发言哦");
            ImageLoaderUtil.getInstance().displayHeaderImage(viewholder.iv_avatar,
                    mData.get(position).getAvatar());
        }

        return convertView;
    }

    private static class Viewholder{

        private ImageView iv_avatar;
        private TextView tv_chat_title;
        private TextView tv_chat_content;
        private TextView tv_latest_time;
        private TextView tv_people_count;
    }
}
