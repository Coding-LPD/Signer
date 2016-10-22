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
import com.scnu.zhou.signer.component.adapter.listview.MainCourseAdapter;
import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhou on 16/9/6.
 */
public class HomeFragment extends Fragment {

    private Activity context;

    @Bind(R.id.plv_main) PullToRefreshListView plv_main;

    private MainCourseAdapter adapter;

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
    }

    private void initView(){

        List<MainCourse> mData = new ArrayList<>();

        MainCourse mainCourse = new MainCourse();

        List<String> avatars = new ArrayList<>();
        avatars.add("http://file.bmob.cn/M03/08/D3/oYYBAFb8qjyAXEu7AAE6_OuqxNA225.jpg");
        avatars.add("http://file.bmob.cn/M03/1C/AA/oYYBAFcGCJOAG61ZAAA3Ib0ByO0134.jpg");
        avatars.add("http://file.bmob.cn/M03/25/FB/oYYBAFcLwyGAWGzIAABcvCOB-lw271.jpg");
        avatars.add("http://file.bmob.cn/M03/0A/D5/oYYBAFb96vGAAuYOAAB9QkCuJto725.jpg");

        mainCourse.setName("软件工程");
        mainCourse.setNumber(3);
        mainCourse.setAvatars(avatars);

        mData.add(mainCourse);
        mData.add(mainCourse);
        mData.add(mainCourse);
        mData.add(mainCourse);

        adapter = new MainCourseAdapter(context, mData);
        plv_main.setAdapter(adapter);

        plv_main.setOnPullToRefreshListener(new PullToRefreshListView.OnPullToRefreshListener() {
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
