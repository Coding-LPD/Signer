package com.scnu.zhou.signer.ui.activity.course;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.SearchHistoryAdapter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/10/28.
 */
public class SearchActivity extends BaseSlideActivity implements View.OnKeyListener{

    @Bind(R.id.et_search) EditText et_search;
    @Bind(R.id.lv_search_history) ListView lv_search_history;

    private SearchHistoryAdapter adapter;
    private List<String> mData;

    private TextView footerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        initView();
    }

    public void initView(){

        et_search.setOnKeyListener(this);

        mData = new ArrayList<>();

        mData.add("软件工程");
        mData.add("计算机科学导论");
        mData.add("数据结构");

        adapter = new SearchHistoryAdapter(this, mData);
        lv_search_history.setAdapter(adapter);

        addFooterView();

    }


    // 添加ListView底部View
    public void addFooterView(){

        if (mData.size() != 0) {
            footerView = new TextView(this);
            ViewGroup.LayoutParams parms = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    190);
            footerView.setLayoutParams(parms);
            footerView.setText("清空历史记录");
            footerView.setTextColor(Color.parseColor("#999999"));
            footerView.setTextSize(15);
            footerView.setGravity(Gravity.CENTER);

            lv_search_history.addFooterView(footerView);
        }
    }

    // 取消搜索
    @OnClick(R.id.tv_cancel)
    public void cancel(){
        finish();
    }


    // 取消软键盘
    public void dismissKeyBoard(){

        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {

            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    /**
     * implementation for OnKeyListener
      */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == event.KEYCODE_ENTER) {
            // 执行搜索动作
            Log.e("action", "search >>>>");
            dismissKeyBoard();
        }
        return false;
    }

}
