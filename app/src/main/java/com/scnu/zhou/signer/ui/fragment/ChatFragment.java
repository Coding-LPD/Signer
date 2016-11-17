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
import com.scnu.zhou.signer.component.adapter.listview.ChatAdapter;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.chat.ChatPresenter;
import com.scnu.zhou.signer.presenter.chat.IChatPresenter;
import com.scnu.zhou.signer.ui.activity.chat.ChatActivity;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.chat.IRoomListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhou on 16/9/6.
 */
public class ChatFragment extends Fragment implements IRoomListView, PullToRefreshListView.OnPullToRefreshListener,
        AdapterView.OnItemClickListener{

    private Activity context;

    @Bind(R.id.plv_chat) PullToRefreshListView plv_chat;

    @Bind(R.id.ll_no_chat) LinearLayout ll_no_chat;
    @Bind(R.id.ll_no_network) LinearLayout ll_no_network;

    private List<ChatRoom> mData;
    private ChatAdapter adapter;

    private IChatPresenter presenter;

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

        presenter = new ChatPresenter(context, this);

        mData = new ArrayList<>();

        adapter = new ChatAdapter(context, mData);
        plv_chat.setAdapter(adapter);
    }


    /**
     * implmentation for PullToRefreshListener
     */
    @Override
    public void onRefresh() {

        presenter.sendRoomListRequest(UserCache.getInstance().getId(context));
    }

    @Override
    public void onLoadMore() {

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

        Intent intent = new Intent(context, ChatActivity.class);
        startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }


    /**
     * Implementation for IRoomListView
     * @param response
     */
    @Override
    public void onGetRoomListSuccess(final ResultResponse<List<ChatRoom>> response) {

        Log.e("response", response.toString());

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (response.getCode().equals("200")){

                    //dismissLoadingDialog();

                    mData = response.getData();
                    adapter = new ChatAdapter(context, mData);
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
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        onRefresh();
    }
}
