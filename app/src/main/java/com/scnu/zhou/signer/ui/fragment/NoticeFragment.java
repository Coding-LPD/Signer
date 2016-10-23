package com.scnu.zhou.signer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.NoticeAdapter;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhou on 16/9/6.
 */
public class NoticeFragment extends Fragment {

    private Activity context;

    @Bind(R.id.plv_notice) PullToRefreshListView plv_notice;

    private NoticeAdapter adapter;
    private List<NoticeBean> notices;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notice, container,
                false);// 关联布局文件
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        initView();
    }

    public void initView(){

        notices = new ArrayList<>();

        NoticeBean bean = new NoticeBean();
        bean.setCourseName("软件工程");
        bean.setSignState(1);
        bean.setSignAt("2016-10-23");
        bean.setSignDistance(14);
        bean.setSignNumber(34);
        bean.setCreatedAt("12分钟前");

        notices.add(bean);
        notices.add(bean);

        NoticeBean bean2 = new NoticeBean();
        bean2.setCourseName("C++课程ssssssss");
        bean2.setSignState(0);
        bean2.setSignAt("2016-10-23");
        bean2.setSignDistance(102);
        bean2.setSignNumber(14);
        bean2.setCreatedAt("19分钟前");
        notices.add(bean2);

        adapter = new NoticeAdapter(context, notices);
        plv_notice.setAdapter(adapter);
        plv_notice.setOnPullToRefreshListener(new PullToRefreshListView.OnPullToRefreshListener() {
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
        });
    }
}
