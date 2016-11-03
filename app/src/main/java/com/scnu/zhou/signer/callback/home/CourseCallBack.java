package com.scnu.zhou.signer.callback.home;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.CourseDetail;
import com.scnu.zhou.signer.component.bean.main.SignBean;

import java.util.List;

/**
 * Created by zhou on 16/10/31.
 */
public interface CourseCallBack {

    void onGetCourseDetailSuccess(ResultResponse<CourseDetail> response);
    void onGetCourseDetailError(Throwable e);

    void onGetSignDetailSuccess(ResultResponse<List<SignBean>> response);
    void onGetSignDetailError(Throwable e);
}
