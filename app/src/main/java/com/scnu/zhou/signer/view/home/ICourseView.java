package com.scnu.zhou.signer.view.home;

import com.scnu.zhou.signer.component.bean.main.CourseDetail;

/**
 * Created by zhou on 16/10/31.
 */
public interface ICourseView {

    void initView();
    void initData();

    void onGetCourseDetailSuccess(CourseDetail response, String week, String session);
    void onGetCourseDetailError(String msg);
    void onGetCourseDetailError(Throwable e);
}
