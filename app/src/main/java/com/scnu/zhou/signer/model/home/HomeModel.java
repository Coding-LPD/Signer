package com.scnu.zhou.signer.model.home;

import com.scnu.zhou.signer.callback.home.HomeCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/10/23.
 */
public class HomeModel implements IHomeModel{

    @Override
    public void getRelatedCourses(String phone, int limit, int page, String keyword, final HomeCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getRelatedCourses(phone, limit, page, keyword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<List<MainCourse>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetRelatedCoursesError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<List<MainCourse>> response) {

                        //Log.e("data", response.getData());

                        callBack.onGetRelatedCoursesSuccess(response);
                    }
                });
    }
}
