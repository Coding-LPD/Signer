package com.scnu.zhou.signer.component.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.scnu.zhou.signer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 16/9/8.
 */
public class MenuPickerAdapter {

    private Context context;
    private List<String> items;
    private List<View> views;

    public MenuPickerAdapter(Context context, List<String> items){
        this.context = context;
        this.items = items;
        this.views = new ArrayList<>();

        initView();
    }

    private void initView(){

        // 除了cancel之外,只有一个item的情况
        if (items.size() == 1){
            Button button = new Button(context);
            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText(items.get(0));
            button.setTextSize(16);
            button.setTextColor(Color.parseColor("#E94E38"));
            button.setBackgroundResource(R.drawable.picker_cancel_selector);

            views.add(button);
        }
        else {

            for (int i = 0; i < items.size(); i++) {

                if (i == 0) {    // 第一个

                    Button button = new Button(context);
                    button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    button.setText(items.get(i));
                    button.setTextSize(16);
                    button.setBackgroundResource(R.drawable.picker_top_selector);

                    View line = new View(context);
                    line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    line.setBackgroundColor(Color.parseColor("#cccccc"));

                    views.add(button);
                    views.add(line);
                } else if (i == items.size() - 1) {
                    Button button = new Button(context);
                    button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    button.setText(items.get(i));
                    button.setTextSize(16);
                    button.setBackgroundResource(R.drawable.picker_bottom_selector);

                    views.add(button);
                } else {
                    Button button = new Button(context);
                    button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    button.setText(items.get(i));
                    button.setTextSize(16);
                    button.setBackgroundResource(R.drawable.picker_center_selector);

                    View line = new View(context);
                    line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    line.setBackgroundColor(Color.parseColor("#cccccc"));

                    views.add(button);
                    views.add(line);
                }
            }
        }
    }


    public int getSize(){

        return views.size();
    }

    public View getView(int pos){

        return views.get(pos);
    }
}
