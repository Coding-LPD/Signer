package com.scnu.zhou.signer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.ChatRoomAdapter;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.cache.ACache;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.time.RoomComparator;
import com.scnu.zhou.signer.presenter.chat.IRoomPresenter;
import com.scnu.zhou.signer.presenter.chat.RoomPresenter;
import com.scnu.zhou.signer.ui.activity.chat.ChatActivity;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.chat.IRoomView;

import java.util.ArrayList;
import java.util.Collections;
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
            startActivityForResult(intent, 0);
            context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }

    }


    /**
     * Implementation for IRoomView
     * @param response
     */
    @Override
    public void onGetRoomListSuccess(final ResultResponse<List<ChatRoom>> response) {

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (response.getCode().equals("200")){

                    //dismissLoadingDialog();

                    mData = response.getData();
                    Collections.sort(mData, new RoomComparator());
                    adapter = new ChatRoomAdapter(context, mData);
                    plv_chat.setAdapter(adapter);
                }
                else{
                    //dismissLoadingDialog();
                    String msg = response.getMsg();
                    ToastView toastView = new ToastView(context, msg);
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                }

                if (mData.size() == 0){
                    ll_no_chat.setVisibility(View.VISIBLE);
                }
                else{
                    ll_no_chat.setVisibility(View.GONE);
                }

                plv_chat.onRefreshCompleted();
            }
        });

        String value = new Gson().toJson(mData);
        ACache.get(context).put("chat_room", value);

    }

    @Override
    public void onReceiveNewMessage(ResultResponse<ChatMessage> response) {

        onRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();

        String array = ACache.get(context).getAsString("chat_room");
        //Log.e("get-array", array);
        if (!TextUtils.isEmpty(array) && !array.equals("null")) mData = new Gson().fromJson(array,
                new TypeToken<List<ChatRoom>>(){}.getType());
        adapter = new ChatRoomAdapter(context, mData);
        plv_chat.setAdapter(adapter);

        onRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("result", ">>");
        onRefresh();
    }
}
