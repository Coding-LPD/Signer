package com.scnu.zhou.signer.elss.util.tabbar;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/9/5.
 */
public class TabBarManager{

    private Activity activity;

    private ImageView[] icons;
    private TextView[] titles;

    private int pos = 0;

    public TabBarManager(Activity activity){
        this.activity = activity;
        initView();
    }

    private void initView(){

        icons = new ImageView[4];
        titles = new TextView[4];

        icons[0] = (ImageView) activity.findViewById(R.id.iv_home);
        icons[1] = (ImageView) activity.findViewById(R.id.iv_discover);
        icons[2] = (ImageView) activity.findViewById(R.id.iv_notice);
        icons[3] = (ImageView) activity.findViewById(R.id.iv_mine);

        titles[0] = (TextView) activity.findViewById(R.id.tv_home);
        titles[1] = (TextView) activity.findViewById(R.id.tv_discover);
        titles[2] = (TextView) activity.findViewById(R.id.tv_notice);
        titles[3] = (TextView) activity.findViewById(R.id.tv_mine);
    }

    public void setSelect(int n){
        switch (n){
            case 0:
                icons[pos].setSelected(false);
                titles[pos].setTextColor(Color.parseColor("#bbbbbb"));
                pos = 0;
                icons[pos].setSelected(true);
                titles[pos].setTextColor(Color.parseColor("#97cc00"));
                break;
            case 1:
                icons[pos].setSelected(false);
                titles[pos].setTextColor(Color.parseColor("#bbbbbb"));
                pos = 1;
                icons[pos].setSelected(true);
                titles[pos].setTextColor(Color.parseColor("#97cc00"));
                break;
            case 2:
                icons[pos].setSelected(false);
                titles[pos].setTextColor(Color.parseColor("#bbbbbb"));
                pos = 2;
                icons[pos].setSelected(true);
                titles[pos].setTextColor(Color.parseColor("#97cc00"));
                break;
            case 3:
                icons[pos].setSelected(false);
                titles[pos].setTextColor(Color.parseColor("#bbbbbb"));
                pos = 3;
                icons[pos].setSelected(true);
                titles[pos].setTextColor(Color.parseColor("#97cc00"));
                break;
        }
    }
}
