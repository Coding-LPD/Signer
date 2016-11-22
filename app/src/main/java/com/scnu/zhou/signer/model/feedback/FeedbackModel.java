package com.scnu.zhou.signer.model.feedback;

import com.scnu.zhou.signer.callback.feedback.FeedbackCallBack;
import com.scnu.zhou.signer.component.bean.feedback.Feedback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/11/22.
 */
public class FeedbackModel implements IFeedbackModel {

    @Override
    public void sendFeedback(Feedback feedback, final FeedbackCallBack callBack) {

        Map<String, String> infos = new HashMap<>();
        infos.put("studentId", feedback.getStudentId());
        infos.put("teacherId", feedback.getTeacherId());
        infos.put("name", feedback.getName());
        infos.put("phone", feedback.getPhone());
        infos.put("content", feedback.getContent());

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .sendFeedback(infos)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<Feedback>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onFeedbackError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<Feedback> response) {

                        callBack.onFeedbackSuccess(response);
                    }
                });
    }

}
