package com.scnu.zhou.signer.ui.activity.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.ChatItemAdapter;
import com.scnu.zhou.signer.component.bean.chat.ChatItem;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/11/7.
 */
public class ChatActivity extends BaseSlideActivity {

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.lv_chat) ListView lv_chat;

    private List<ChatItem> mData;
    private ChatItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        initView();
        initData();
    }


    public void initView(){

        tv_title.setText("聊天室");
        ll_return.setVisibility(View.VISIBLE);
    }


    public void initData(){


        ChatItem item = new ChatItem();
        item.setType(0);

        ChatItem item2 = new ChatItem();
        item2.setType(1);

        mData = new ArrayList<>();
        mData.add(item);
        mData.add(item);
        mData.add(item);
        mData.add(item2);
        mData.add(item);
        mData.add(item);

        adapter = new ChatItemAdapter(this, mData);
        lv_chat.setAdapter(adapter);
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
