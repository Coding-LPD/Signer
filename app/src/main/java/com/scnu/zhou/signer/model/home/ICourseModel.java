package com.scnu.zhou.signer.model.home;

import com.scnu.zhou.signer.callback.home.CourseCallBack;

/**
 * Created by zhou on 16/10/31.
 */
public interface ICourseModel {

    void getCourseDetail(String courseId, CourseCallBack callBack);
}
