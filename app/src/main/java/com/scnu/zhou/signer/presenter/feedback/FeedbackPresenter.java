package com.scnu.zhou.signer.presenter.feedback;

import com.scnu.zhou.signer.callback.feedback.FeedbackCallBack;
import com.scnu.zhou.signer.component.bean.feedback.Feedback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.model.feedback.FeedbackModel;
import com.scnu.zhou.signer.model.feedback.IFeedbackModel;
import com.scnu.zhou.signer.view.feedback.IFeedbackView;

/**
 * Created by zhou on 16/11/22.
 */
public class FeedbackPresenter implements IFeedbackPresenter, FeedbackCallBack {

    private IFeedbackView feedbackView;
    private IFeedbackModel feedbackModel;

    public FeedbackPresenter(IFeedbackView feedbackView){

        this.feedbackView = feedbackView;
        this.feedbackModel = new FeedbackModel();
    }

    @Override
    public void sendFeedback(Feedback feedback) {

        feedbackModel.sendFeedback(feedback, this);
    }

    @Override
    public void onFeedbackSuccess(ResultResponse<Feedback> response) {

        if (response.getCode().equals("200")) {

            feedbackView.onFeedbackSuccess(response.getData());
        }
        else{

            feedbackView.onFeedbackError(response.getMsg());
        }
    }

    @Override
    public void onFeedbackError(Throwable e) {

        feedbackView.onFeedbackError(e);
    }
}
