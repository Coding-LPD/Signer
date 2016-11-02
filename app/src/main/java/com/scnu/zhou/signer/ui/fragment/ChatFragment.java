package com.scnu.zhou.signer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhou on 16/9/6.
 */
public class ChatFragment extends Fragment implements PullToRefreshListView.OnPullToRefreshListener{

    private Activity context;

    @Bind(R.id.plv_chat) PullToRefreshListView plv_chat;

    @Bind(R.id.ll_no_chat) LinearLayout ll_no_chat;
    @Bind(R.id.ll_no_network) LinearLayout ll_no_network;

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
    }


    public void initView(){

        plv_chat.setOnPullToRefreshListener(this);
    }


    /**
     * implmentation for PullToRefreshListener
     */
    @Override
    public void onRefresh() {

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
}
