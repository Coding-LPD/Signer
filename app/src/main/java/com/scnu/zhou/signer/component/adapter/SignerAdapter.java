package com.scnu.zhou.signer.component.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.signer.Signer;
import com.scnu.zhou.signer.component.util.image.ImageLoaderUtil;
import com.scnu.zhou.signer.ui.widget.image.CircleImageView;

import java.util.List;

/**
 * Created by zhou on 16/10/19.
 */
public class SignerAdapter extends BaseAdapter{

    private Context context;
    private List<Signer> mData;

    public SignerAdapter(Context context, List<Signer> mData){

        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Signer getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){
            convertView = View.inflate(context, R.layout.griditem_signer, null);

            viewHolder = new ViewHolder();
            viewHolder.header = (CircleImageView) convertView.findViewById(R.id.civ_header);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoaderUtil.getInstance().displayHeaderImage(viewHolder.header, mData.get(position).getAvatar());
        viewHolder.name.setText(mData.get(position).getName());

        return convertView;
    }

    private static class ViewHolder{

        CircleImageView header;
        TextView name;
    }
}
