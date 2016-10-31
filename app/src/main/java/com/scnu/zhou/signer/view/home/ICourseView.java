package com.scnu.zhou.signer.view.home;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;

/**
 * Created by zhou on 16/10/31.
 */
public interface ICourseView {

    void initView();
    void initData();

    void onGetCourseDetailSuccess(ResultResponse<ScanResult> response);
    void onGetCourseDetailError(Throwable e);
}
