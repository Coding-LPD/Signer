package com.scnu.zhou.signer.callback.home;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.MainCourse;

import java.util.List;

/**
 * Created by zhou on 16/10/23.
 */
public interface HomeCallBack {

    void onGetRelatedCoursesSuccess(ResultResponse<List<MainCourse>> response);
    void onGetRelatedCoursesError(Throwable e);
}
