package com.scnu.zhou.signer.model.home;

import com.scnu.zhou.signer.callback.home.CourseCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.CourseDetail;
import com.scnu.zhou.signer.component.bean.main.SignBean;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import java.util.List;

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
                .subscribe(new Observer<ResultResponse<CourseDetail>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetCourseDetailError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<CourseDetail> response) {

                        //Log.e("data", response.getData());

                        callBack.onGetCourseDetailSuccess(response);
                    }
                });
    }

    @Override
    public void getSignDetail(String courseId, String studentId, final CourseCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getSignDetail(courseId, studentId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<List<SignBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetSignDetailError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<List<SignBean>> response) {

                        //Log.e("data", response.getData());

                        callBack.onGetSignDetailSuccess(response);
                    }
                });
    }
}
