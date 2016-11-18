package com.scnu.zhou.signer.ui.activity.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.ChatMessageAdapter;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.cache.TimeCache;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.chat.ChatPresenter;
import com.scnu.zhou.signer.presenter.chat.IChatPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.chat.IChatView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/11/7.
 */
public class ChatActivity extends BaseSlideActivity implements IChatView, AbsListView.OnScrollListener {

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
    private boolean isRefresh = true;
    private boolean isCompleted = false;

    private View headerView = null;
    private View footerView = null;

    private int headerHeight = 0;

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

        lv_chat.setOnScrollListener(this);


        // 添加头布局
        headerView = LayoutInflater.from(this).inflate(R.layout.listview_loadingview, null);
        headerView.measure(0, 0);
        headerHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, - headerHeight, 0, 0);
        lv_chat.addHeaderView(headerView);

        // 添加底部布局
        footerView = new View(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, 40);
        footerView.setLayoutParams(params);
        lv_chat.addFooterView(footerView);
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
        isRefresh = true;
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

        ChatMessage message = new ChatMessage();
        message.setAvatar(UserCache.getInstance().getAvatar(this));
        message.setStudentId(UserCache.getInstance().getId(this));
        message.setCourseId(courseId);
        message.setContent(et_content.getText().toString());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = format.format(curDate);

        message.setCreatedAt(time);
        mData.add(message);
        adapter.notifyDataSetChanged();
        lv_chat.setSelection(lv_chat.getBottom());

        presenter.sendMessageAction(courseId, UserCache.getInstance().getId(this),
                et_content.getText().toString());

        et_content.setText("");
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
                    if (response.getData() == null || response.getData().size() < 18){  // 每次返回18条信息
                        Log.e("update", "completed");
                        isCompleted = true;
                    }

                    if (page == 0) {
                        Collections.reverse(response.getData());
                        mData = response.getData();
                        adapter = new ChatMessageAdapter(ChatActivity.this, mData);
                        lv_chat.setAdapter(adapter);

                        lv_chat.setSelection(lv_chat.getBottom());
                    }
                    else{

                        headerView.setPadding(0, - headerHeight, 0, 0);

                        int size = response.getData().size();
                        Collections.reverse(mData);
                        mData.addAll(response.getData());
                        Collections.reverse(mData);
                        adapter = new ChatMessageAdapter(ChatActivity.this, mData);
                        lv_chat.setAdapter(adapter);

                        lv_chat.setSelection(size);
                    }

                    isRefresh = false;
                    page++;
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
    public void onReceiveNewMessage(final ResultResponse<ChatMessage> response) {

        Log.e("response", "receive new message");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (response.getCode().equals("200")){

                    //dismissLoadingDialog();

                    //et_content.setText("");

                    ChatMessage message = response.getData();
                    mData.add(message);
                    adapter.notifyDataSetChanged();
                    lv_chat.setSelection(lv_chat.getBottom());
                    //refresh();
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


    /**
     * implementation for Scrolllistener
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem == 0 && !isRefresh && !isCompleted){
            View firstVisibleItemView = lv_chat.getChildAt(0);
            if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                Log.e("ListView", "##### 滚动到顶部 #####");
                isRefresh = true;
                headerView.setPadding(0, 0, 0, 0);
                presenter.sendMessageListRequest(courseId, page);
            }
        }
    }

    @Override
    public void finish() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = format.format(curDate);

        Log.e("time", time);
        TimeCache.getInstance().setTime(this, courseId, time);

        super.finish();
    }
}
