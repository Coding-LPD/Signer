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

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.MainCourseAdapter;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.home.HomePresenter;
import com.scnu.zhou.signer.presenter.home.IHomePresenter;
import com.scnu.zhou.signer.ui.activity.course.CourseDetailActivity;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.home.IHomeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhou on 16/9/6.
 */
public class HomeFragment extends Fragment implements IHomeView, PullToRefreshListView.OnPullToRefreshListener,
        AdapterView.OnItemClickListener{

    private Activity context;

    @Bind(R.id.plv_main) PullToRefreshListView plv_main;

    private MainCourseAdapter adapter;
    private List<MainCourse> mData;

    private IHomePresenter presenter;

    private int limit = 10;   // 每页加载数目
    private int page = 0;

    private final int STATE_REFRESH = 0x001;
    private final int STATE_LOADMORE = 0x002;
    private int state = STATE_REFRESH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container,
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


    @Override
    public void initView(){

        mData = new ArrayList<>();
        plv_main.setOnPullToRefreshListener(this);
        plv_main.setOnItemClickListener(this);
    }

    @Override
    public void initData() {

        presenter = new HomePresenter(this);
        //presenter.getRelatedCourses(UserCache.getInstance().getPhone(context), limit, page);
    }


    /**
     * 获取相关课程信息成功
     * @param response
     */
    @Override
    public void onGetRelatedCoursesSuccess(ResultResponse<List<MainCourse>> response) {

        if (response.getCode().equals("200")){

            //dismissLoadingDialog();

            page++;

            if (state == STATE_REFRESH) {
                mData = response.getData();
            }
            else {
                mData.addAll(response.getData());

                if (response.getData().size() < limit){
                    plv_main.onLoadMoreAllCompleted();
                }
            }

            adapter = new MainCourseAdapter(context, mData);
            plv_main.setAdapter(adapter);
        }
        else{
            //dismissLoadingDialog();
            String msg = response.getMsg();
            ToastView toastView = new ToastView(context, msg);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }

        if (state == STATE_REFRESH) {
            plv_main.onRefreshCompleted();
        }
        else{
            plv_main.onLoadMoreCompleted();
        }
    }


    /**
     * 获取相关课程信息失败
     * @param e
     */
    @Override
    public void onGetRelatedCoursesError(Throwable e) {

        Log.e("error", e.toString());

        //dismissLoadingDialog();
        ToastView toastView = new ToastView(context, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();

        if (state == STATE_REFRESH) {
            plv_main.onRefreshCompleted();
        }
        else{
            plv_main.onLoadMoreCompleted();
        }
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {

        Log.e("state", "refresh");
        state = STATE_REFRESH;
        page = 0;
        mData.clear();
        presenter.getRelatedCourses(UserCache.getInstance().getPhone(context), limit, page);
    }


    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {

        Log.e("state", "load");
        state = STATE_LOADMORE;
        presenter.getRelatedCourses(UserCache.getInstance().getPhone(context), limit, page);
    }


    /**
     * 刷新超时处理
     */
    @Override
    public void onOutOfTime() {
        ToastView toastView = new ToastView(context, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
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

        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra("title", mData.get(position - 1).getName());
        startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
