package com.scnu.zhou.signer.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.model.view.CellModel;
import com.scnu.zhou.signer.view.cell.TitleTextCell;

import java.util.List;

/**
 * Created by zhou on 16/9/7.
 */
public class UserInfoCellAdapter extends BaseAdapter {

    private Activity context;
    private List<CellModel> mData;

    public UserInfoCellAdapter(Activity context, List<CellModel> mData){
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CellModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.listitem_cell, null);

        View header = view.findViewById(R.id.header);
        if (position % 3 == 0){
            header.setVisibility(View.VISIBLE);
        }

        TitleTextCell cell = (TitleTextCell) view.findViewById(R.id.cell);
        cell.setTitle(mData.get(position).getTitle());

        if (!mData.get(position).getText().equals("")) {
            cell.setText(mData.get(position).getText());
        }
        else{
            cell.setText("未填写");
        }

        if (position % 3 == 2){
            cell.setDivider(false);
        }
        else{
            cell.setDivider(true);
        }

        if (position == mData.size()-1){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cell.getLayoutParams();
            params.setMargins(0, 0, 0, 30);
            cell.setLayoutParams(params);
        }

        return view;
    }
}
