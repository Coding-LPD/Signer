package com.scnu.zhou.signer.model.home;

import com.scnu.zhou.signer.callback.home.CourseCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/10/31.
 */
public class CourseModel implements ICourseModel {

    @Override
    public void getCourseDetail(String courseId, final CourseCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getCourseDetail(courseId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<ScanResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetCourseDetailError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<ScanResult> response) {

                        //Log.e("data", response.getData());

                        callBack.onGetCourseDetailSuccess(response);
                    }
                });
    }
}
