package com.scnu.zhou.signer.callback.feedback;

import com.scnu.zhou.signer.component.bean.feedback.Feedback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;

/**
 * Created by zhou on 16/11/22.
 */
public interface FeedbackCallBack {

    void onFeedbackSuccess(ResultResponse<Feedback> response);
    void onFeedbackError(Throwable e);
}
