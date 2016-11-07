package com.scnu.zhou.signer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.MainCourseAdapter;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.component.cache.ACache;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.home.HomePresenter;
import com.scnu.zhou.signer.presenter.home.IHomePresenter;
import com.scnu.zhou.signer.ui.activity.course.CourseDetailActivity;
import com.scnu.zhou.signer.ui.activity.course.SearchActivity;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.home.IHomeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/9/6.
 */
public class HomeFragment extends Fragment implements IHomeView, PullToRefreshListView.OnPullToRefreshListener,
        AdapterView.OnItemClickListener{

    private Activity context;

    @Bind(R.id.plv_main) PullToRefreshListView plv_main;
    @Bind(R.id.ll_no_course) LinearLayout ll_no_course;
    @Bind(R.id.ll_no_network) LinearLayout ll_no_network;

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
    public void initData(){

        presenter = new HomePresenter(this);

        String array = ACache.get(context).getAsString("course");
        //Log.e("get-array", array);
        if (!TextUtils.isEmpty(array) && !array.equals("null")) mData = new Gson().fromJson(array,
                new TypeToken<List<MainCourse>>(){}.getType());

        adapter = new MainCourseAdapter(context, mData);
        plv_main.setAdapter(adapter);
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
                plv_main.onRefreshCompleted();
            }
            else {
                mData.addAll(response.getData());

                if (response.getData().size() < limit){
                    plv_main.onLoadMoreAllCompleted();
                }
                else{
                    plv_main.onLoadMoreCompleted();
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

        if (mData.size() == 0){
            ll_no_course.setVisibility(View.VISIBLE);
        }
        else{
            ll_no_course.setVisibility(View.GONE);
        }

        String value = new Gson().toJson(mData);
        Log.e("put-array", value);
        ACache.get(context).put("course", value);
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

        if (mData.size() == 0){
            ll_no_network.setVisibility(View.VISIBLE);
        }
        else{
            ll_no_network.setVisibility(View.GONE);
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 300);
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

        if (!plv_main.isRefreshing() && position > 0) {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("title", mData.get(position - 1).getName());
            intent.putExtra("courseId", mData.get(position - 1).getCourseId());
            startActivity(intent);
            context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }


    // 点击搜索
    @OnClick(R.id.ll_searchbar)
    public void search(){

        Intent intent = new Intent(context, SearchActivity.class);
        startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
