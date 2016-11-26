package com.scnu.zhou.signer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.ChatRoomAdapter;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.chat.IRoomPresenter;
import com.scnu.zhou.signer.presenter.chat.RoomPresenter;
import com.scnu.zhou.signer.ui.activity.chat.ChatActivity;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.chat.IRoomView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhou on 16/9/6.
 */
public class ChatFragment extends Fragment implements IRoomView, PullToRefreshListView.OnPullToRefreshListener,
        AdapterView.OnItemClickListener{

    private Activity context;

    @Bind(R.id.plv_chat) PullToRefreshListView plv_chat;

    @Bind(R.id.ll_no_chat) LinearLayout ll_no_chat;
    @Bind(R.id.ll_no_network) LinearLayout ll_no_network;

    private List<ChatRoom> mData;
    private ChatRoomAdapter adapter;

    private IRoomPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container,
                false);// 关联布局文件
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        initView();
        initData();
    }


    public void initView(){

        plv_chat.setOnPullToRefreshListener(this);
        plv_chat.setOnItemClickListener(this);
    }


    public void initData(){

        presenter = new RoomPresenter(context, this);

        mData = new ArrayList<>();

        adapter = new ChatRoomAdapter(context, mData);
        plv_chat.setAdapter(adapter);
    }


    /**
     * 检查是不是没有聊天
     */
    private void checkIsNoChat(){

        if (mData.size() == 0){
            ll_no_chat.setVisibility(View.VISIBLE);
        }
        else{
            ll_no_chat.setVisibility(View.GONE);
        }
    }


    /**
     * implmentation for PullToRefreshListener
     */
    @Override
    public void onRefresh() {

        presenter.sendRoomListRequest(UserCache.getInstance().getId(context),
                UserCache.getInstance().getRole(context));
    }

    @Override
    public void onLoadMore() {

        plv_chat.onLoadMoreAllCompleted();

    }

    @Override
    public void onOutOfTime() {

        ToastView toastView = new ToastView(context, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }



    /**
     * 单击处理
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position > 0) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("courseId", mData.get(position - 1).getCourseId());
            intent.putExtra("courseName", mData.get(position - 1).getName());
            intent.putExtra("peopleNum", mData.get(position - 1).getCount());
            startActivityForResult(intent, 0);
            context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }

    }


    /**
     * Implementation for IRoomView
     * @param response
     */
    @Override
    public void onGetRoomList(final List<ChatRoom> response) {

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mData = response;
                adapter = new ChatRoomAdapter(context, mData);
                plv_chat.setAdapter(adapter);

                checkIsNoChat();

                plv_chat.onRefreshCompleted();
            }
        });

        // 数据缓存
        presenter.saveRoomListCache(mData);
    }

    @Override
    public void onReceiveNewMessage(ChatMessage response) {

        onRefresh();
    }

    @Override
    public void onErrorMessageShow(String msg) {

        ToastView toastView = new ToastView(context, msg);
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();

        checkIsNoChat();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (presenter.getRoomListChache() != null){
            mData = presenter.getRoomListChache();
            adapter = new ChatRoomAdapter(context, mData);
            plv_chat.setAdapter(adapter);
        }

        onRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("result", ">>");
        onRefresh();
    }
}
