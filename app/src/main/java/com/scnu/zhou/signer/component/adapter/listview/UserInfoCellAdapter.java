package com.scnu.zhou.signer.component.adapter.listview;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.view.CellBean;
import com.scnu.zhou.signer.ui.widget.cell.TitleTextCell;

import java.util.List;

/**
 * Created by zhou on 16/9/7.
 */
public class UserInfoCellAdapter extends BaseAdapter {

    private Activity context;
    private List<CellBean> mData;

    public UserInfoCellAdapter(Activity context, List<CellBean> mData){
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CellBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else{
            convertView = View.inflate(context, R.layout.listitem_cell, null);

            viewHolder = new ViewHolder();
            viewHolder.header = convertView.findViewById(R.id.header);
            viewHolder.cell = (TitleTextCell) convertView.findViewById(R.id.cell);

            convertView.setTag(viewHolder);
        }


        if (position % 3 == 0){
            viewHolder.header.setVisibility(View.VISIBLE);
        }

        viewHolder.cell.setTitle(mData.get(position).getTitle());

        if (!mData.get(position).getText().equals("")) {
            viewHolder.cell.setText(mData.get(position).getText());
        }
        else{
            viewHolder.cell.setText("未填写");
        }

        if (position % 3 == 2){
            viewHolder.cell.setDivider(false);
        }
        else{
            viewHolder.cell.setDivider(true);
        }

        if (position == mData.size()-1){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.cell.getLayoutParams();
            params.setMargins(0, 0, 0, 30);
            viewHolder.cell.setLayoutParams(params);
        }

        return convertView;
    }

    private static class ViewHolder{
        View header;
        TitleTextCell cell;
    }
}
