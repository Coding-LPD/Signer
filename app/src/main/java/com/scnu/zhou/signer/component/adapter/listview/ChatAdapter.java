package com.scnu.zhou.signer.component.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.chat.ChatBean;

import java.util.List;

/**
 * Created by zhou on 16/11/3.
 */
public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<ChatBean> mData;

    public ChatAdapter(Context context, List<ChatBean> mData){

        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ChatBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){

            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_chat, null);
        }

        return convertView;
    }
}
