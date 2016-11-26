package com.scnu.zhou.signer.view.feedback;

import com.scnu.zhou.signer.component.bean.feedback.Feedback;

/**
 * Created by zhou on 16/11/22.
 */
public interface IFeedbackView {

    void initView();
    void initData();

    void onFeedbackSuccess(Feedback response);
    void onFeedbackError(String msg);
    void onFeedbackError(Throwable e);
}
