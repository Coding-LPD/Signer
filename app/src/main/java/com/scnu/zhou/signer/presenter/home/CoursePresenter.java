package com.scnu.zhou.signer.presenter.home;

import com.scnu.zhou.signer.callback.home.CourseCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.CourseDetail;
import com.scnu.zhou.signer.model.home.CourseModel;
import com.scnu.zhou.signer.model.home.ICourseModel;
import com.scnu.zhou.signer.view.home.ICourseView;

/**
 * Created by zhou on 16/10/31.
 */
public class CoursePresenter implements ICoursePresenter, CourseCallBack {

    private ICourseView courseView;
    private ICourseModel courseModel;

    public CoursePresenter(ICourseView courseView){

        this.courseView = courseView;
        this.courseModel = new CourseModel();
    }

    @Override
    public void getCourseDetail(String courseId) {

        courseModel.getCourseDetail(courseId, this);
    }

    @Override
    public void onGetCourseDetailSuccess(ResultResponse<CourseDetail> response) {

        courseView.onGetCourseDetailSuccess(response);
    }

    @Override
    public void onGetCourseDetailError(Throwable e) {

        courseView.onGetCourseDetailError(e);
    }
}
