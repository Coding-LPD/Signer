package com.scnu.zhou.signer.component.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scnu.zhou.signer.R;

import java.util.List;

/**
 * Created by zhou on 16/10/28.
 */
public class SearchHistoryAdapter extends BaseAdapter {

    private Context context;
    private List<String> mData;

    public SearchHistoryAdapter(Context context, List<String> mData){

        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_search, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_title.setText(mData.get(position));

        return convertView;
    }

    private static class ViewHolder{
        private TextView tv_title;
    }
}
