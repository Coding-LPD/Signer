package com.scnu.zhou.signer.component.util.tabbar;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/9/5.
 */
public class TabBarManager02 {

    private Activity activity;

    private ImageView[] icons;
    private TextView[] titles;

    private int pos = 0;

    public TabBarManager02(Activity activity){
        this.activity = activity;
        initView();
    }

    private void initView(){

        icons = new ImageView[2];
        titles = new TextView[2];

        icons[0] = (ImageView) activity.findViewById(R.id.iv_discover);
        icons[1] = (ImageView) activity.findViewById(R.id.iv_mine);

        titles[0] = (TextView) activity.findViewById(R.id.tv_discover);
        titles[1] = (TextView) activity.findViewById(R.id.tv_mine);
    }

    public void setSelect(int n){

        icons[pos].setSelected(false);
        titles[pos].setTextColor(Color.parseColor("#bbbbbb"));
        pos = n;
        icons[pos].setSelected(true);
        titles[pos].setTextColor(Color.parseColor("#97cc00"));
    }
}
