package com.scnu.zhou.signer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.NoticeAdapter;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;
import com.scnu.zhou.signer.component.cache.ACache;
import com.scnu.zhou.signer.component.cache.NoticeCache;
import com.scnu.zhou.signer.component.cache.UserCache;
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

    @Override
    public void onGetNoticeSuccess(ResultResponse<List<NoticeBean>> response) {


        if (response.getCode().equals("200")){

            //dismissLoadingDialog();

            page++;

            if (state == STATE_REFRESH) {
                NoticeCache.getInstance().setNoticenNumber(context, 0);
                notices = response.getData();
                plv_notice.onRefreshCompleted();
            }
            else {
                notices.addAll(response.getData());

                if (response.getData().size() < limit){
                    plv_notice.onLoadMoreAllCompleted();
                }
                else{
                    plv_notice.onLoadMoreCompleted();
                }
            }

            adapter = new NoticeAdapter(context, notices);
            plv_notice.setAdapter(adapter);
        }
        else{
            //dismissLoadingDialog();
            String msg = response.getMsg();
            ToastView toastView = new ToastView(context, msg);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }

        if (notices.size() == 0){
            ll_no_notice.setVisibility(View.VISIBLE);
        }
        else{
            ll_no_notice.setVisibility(View.GONE);
        }

        String value = new Gson().toJson(notices);
        Log.e("notice", value);
        if (segment == STATE_BEFORE){
            ACache.get(context).put("notice_before", value);
        }
        else{
            ACache.get(context).put("notice_after", value);
        }
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

        Log.e(">>", "no newtwork" + notices.size());
        if (notices.size() == 0){
            ll_no_network.setVisibility(View.VISIBLE);
        }
        else{
            ll_no_network.setVisibility(View.GONE);
        }
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

        String array = ACache.get(context).getAsString("notice_before");
        //Log.e("get-array", array);
        if (!TextUtils.isEmpty(array) && !array.equals("null")) notices = new Gson().fromJson(array,
                new TypeToken<List<NoticeBean>>(){}.getType());
        adapter = new NoticeAdapter(context, notices);
        plv_notice.setAdapter(adapter);

        state = STATE_REFRESH;
        segment = STATE_BEFORE;
        page = 0;
        presenter.getNotices(UserCache.getInstance().getPhone(context), segment, limit, page);
    }

    @Override
    public void onSelectRight() {

        String array = ACache.get(context).getAsString("notice_after");
        //Log.e("get-array", array);
        if (!TextUtils.isEmpty(array) && !array.equals("null")) notices = new Gson().fromJson(array,
                new TypeToken<List<NoticeBean>>(){}.getType());
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
