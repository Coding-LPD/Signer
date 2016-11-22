package com.scnu.zhou.signer.view.feedback;

import com.scnu.zhou.signer.component.bean.feedback.Feedback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;

/**
 * Created by zhou on 16/11/22.
 */
public interface IFeedbackView {

    void initView();
    void initData();

    void onFeedbackSuccess(ResultResponse<Feedback> response);
    void onFeedbackError(Throwable e);
}
