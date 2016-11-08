package com.scnu.zhou.signer.component.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.chat.ChatItem;

import java.util.List;

/**
 * Created by zhou on 16/11/8.
 */
public class ChatItemAdapter extends BaseAdapter {

    private Context context;
    private List<ChatItem> mData;

    public ChatItemAdapter(Context context, List<ChatItem> mData){

        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ChatItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (mData.get(position).getType() == 0){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_chat_left, null);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.listitem_chat_right, null);
        }
        return view;
    }
}
