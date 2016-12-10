package com.scnu.zhou.signer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.NoticeAdapter;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;
import com.scnu.zhou.signer.component.cache.NoticeCache;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.tabbar.TabBarManager;
import com.scnu.zhou.signer.presenter.notice.INoticePresenter;
import com.scnu.zhou.signer.presenter.notice.NoticePresenter;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView.OnPullToRefreshListener;
import com.scnu.zhou.signer.ui.widget.segment.TwoStageSegment;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.notice.INoticeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhou on 16/9/6.
 */
public class NoticeFragment extends Fragment implements INoticeView,
        OnPullToRefreshListener, TwoStageSegment.OnSelectListener{

    private Activity context;

    @Bind(R.id.sg_sign) TwoStageSegment sg_sign;
    @Bind(R.id.plv_notice) PullToRefreshListView plv_notice;

    @Bind(R.id.ll_no_notice) LinearLayout ll_no_notice;
    @Bind(R.id.ll_no_network) LinearLayout ll_no_network;

    private NoticeAdapter adapter;
    private List<NoticeBean> notices;

    private INoticePresenter presenter;

    private int limit = 10;
    private int page = 0;

    private final int STATE_REFRESH = 0x001;
    private final int STATE_LOADMORE = 0x002;
    private int state = STATE_REFRESH;

    private final int STATE_BEFORE = 0;
    private final int STATE_AFTER = 1;
    private int segment = STATE_BEFORE;


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

        initData();
        initView();
    }


    @Override
    public void initView(){

        sg_sign.setOnSelectListener(this);
        plv_notice.setOnPullToRefreshListener(this);

        //onSelectLeft();
    }

    @Override
    public void initData() {

        presenter = new NoticePresenter(this);
        notices = new ArrayList<>();
    }


    /**
     * 检查是否通知为空
     */
    private void checkIsNoNotice(){

        if (notices.size() == 0){
            ll_no_notice.setVisibility(View.VISIBLE);
        }
        else{
            ll_no_notice.setVisibility(View.GONE);
        }
    }


    /**
     * 检查网络连接
     */
    private void checkNetworkIsAvail(){

        if (notices.size() == 0){
            ll_no_network.setVisibility(View.VISIBLE);
        }
        else{
            ll_no_network.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetNoticeSuccess(List<NoticeBean> response) {

        page++;

        if (state == STATE_REFRESH) {

            if (TabBarManager.getInstacne().getCurrentPos() == 2){
                NoticeCache.getInstance().setNoticenNumber(context, 0);
            }
            notices = response;
            plv_notice.onRefreshCompleted();
        }
        else {
            //notices.addAll(response);
            for (NoticeBean bean: response){
                notices.add(bean);
            }

            if (response.size() < limit){
                plv_notice.onLoadMoreAllCompleted();
            }
            else{
                plv_notice.onLoadMoreCompleted();
            }
        }

        adapter = new NoticeAdapter(context, notices);
        plv_notice.setAdapter(adapter);

        checkIsNoNotice();

        // 数据缓存
        if (segment == STATE_BEFORE){
            presenter.setCache(context, "notice_before", notices);
        }
        else{
            presenter.setCache(context, "notice_after", notices);
        }
    }


    @Override
    public void onGetNoticeError(String msg) {

        ToastView toastView = new ToastView(context, msg);
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    @Override
    public void onGetNoticeError(Throwable e) {


        Log.e("error", e.toString());

        //dismissLoadingDialog();
        ToastView toastView = new ToastView(context, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();

        if (state == STATE_REFRESH) {
            plv_notice.onRefreshCompleted();
        }
        else{
            plv_notice.onLoadMoreCompleted();
        }

        checkNetworkIsAvail();
    }

    /**
     * implmentation for PullToRefreshListener
     */
    @Override
    public void onRefresh() {

        Log.e("state", "refresh");
        state = STATE_REFRESH;
        page = 0;

        presenter.getNotices(UserCache.getInstance().getPhone(context), segment, limit, page);
    }

    @Override
    public void onLoadMore() {

        Log.e("state", "load");
        state = STATE_LOADMORE;
        presenter.getNotices(UserCache.getInstance().getPhone(context), segment, limit, page);
    }

    @Override
    public void onOutOfTime() {
        ToastView toastView = new ToastView(context, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    /**
     * implmentation for TwoStageSegment
     */
    @Override
    public void onSelectLeft() {

        plv_notice.resetState();
        if (presenter.getCache(context, "notice_before") != null){
            notices = presenter.getCache(context, "notice_before");
        }
        adapter = new NoticeAdapter(context, notices);
        plv_notice.setAdapter(adapter);

        state = STATE_REFRESH;
        segment = STATE_BEFORE;
        page = 0;
        presenter.getNotices(UserCache.getInstance().getPhone(context), segment, limit, page);
    }

    @Override
    public void onSelectRight() {

        plv_notice.resetState();
        if (presenter.getCache(context, "notice_after") != null){
            notices = presenter.getCache(context, "notice_after");
        }
        adapter = new NoticeAdapter(context, notices);
        plv_notice.setAdapter(adapter);

        state = STATE_REFRESH;
        segment = STATE_AFTER;
        page = 0;
        presenter.getNotices(UserCache.getInstance().getPhone(context), segment, limit, page);
    }


    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                segment = STATE_BEFORE;
                onSelectLeft();
            }
        }, 300);
    }
}
