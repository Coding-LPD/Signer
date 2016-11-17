package com.scnu.zhou.signer.ui.activity.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.ChatMessageAdapter;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.chat.ChatPresenter;
import com.scnu.zhou.signer.presenter.chat.IChatPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.chat.IChatView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/11/7.
 */
public class ChatActivity extends BaseSlideActivity implements IChatView {

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.lv_chat) ListView lv_chat;
    @Bind(R.id.ll_no_message) LinearLayout ll_no_message;

    @Bind(R.id.et_content) EditText et_content;
    @Bind(R.id.ibtn_send) ImageButton ibtn_send;

    private List<ChatMessage> mData;
    private ChatMessageAdapter adapter;

    private IChatPresenter presenter;

    private String courseId;
    private int page = 0;

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

        mData = new ArrayList<>();

        adapter = new ChatMessageAdapter(this, mData);
        lv_chat.setAdapter(adapter);

        courseId = getIntent().getStringExtra("courseId");

        presenter = new ChatPresenter(this);
        presenter.sendMessageListRequest(courseId, page);
    }


    public void refresh(){

        page = 0;
        presenter.sendMessageListRequest(courseId, page);
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 点击发送消息
    @OnClick(R.id.ibtn_send)
    public void send(){
        Log.e("click", "send");
        presenter.sendMessageAction(courseId, UserCache.getInstance().getId(this),
                et_content.getText().toString());
    }


    /**
     * Implementation for IChatView
     * @param response
     */
    @Override
    public void onGetMessageListSuccess(final ResultResponse<List<ChatMessage>> response) {

        Log.e("response", "msg-list");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (response.getCode().equals("200")){

                    //dismissLoadingDialog();

                    mData = response.getData();
                    adapter = new ChatMessageAdapter(ChatActivity.this, mData);
                    lv_chat.setAdapter(adapter);
                }
                else{
                    //dismissLoadingDialog();
                    String msg = response.getMsg();
                    ToastView toastView = new ToastView(ChatActivity.this, msg);
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                }

                if (mData.size() == 0){
                    ll_no_message.setVisibility(View.VISIBLE);
                }
                else{
                    ll_no_message.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onSendMessageSuccess(final ResultResponse<ChatMessage> response) {

        Log.e("response", "send message success");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (response.getCode().equals("200")){

                    //dismissLoadingDialog();

                    et_content.setText("");

                    ChatMessage message = response.getData();
                    mData.add(message);
                    adapter.notifyDataSetChanged();

                    refresh();
                }
                else{
                    //dismissLoadingDialog();
                    String msg = response.getMsg();
                    ToastView toastView = new ToastView(ChatActivity.this, msg);
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                }
            }
        });
    }
}
