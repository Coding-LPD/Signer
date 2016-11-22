package com.scnu.zhou.signer.model.feedback;

import com.scnu.zhou.signer.callback.feedback.FeedbackCallBack;
import com.scnu.zhou.signer.component.bean.feedback.Feedback;

/**
 * Created by zhou on 16/11/22.
 */
public interface IFeedbackModel {

    void sendFeedback(Feedback feedback, FeedbackCallBack callBack);
}
