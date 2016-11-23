package com.scnu.zhou.signer.model.mine;

import com.scnu.zhou.signer.callback.mine.MySignerCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.mine.MyChat;
import com.scnu.zhou.signer.component.bean.mine.MySign;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/11/23.
 */
public class MySignerModel implements IMySignerModel {

    @Override
    public void getSignDays(String studentId, String date, final MySignerCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getSignDays(studentId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<List<String>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetSignDaysError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<List<String>> response) {

                        //Log.e("data", response.getData());

                        callBack.onGetSignDaysSuccess(response);
                    }
                });
    }

    @Override
    public void getSignDaysDetail(String studentId, String date, final MySignerCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getSignDaysDetail(studentId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<List<MySign>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetSignDaysDetailError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<List<MySign>> response) {

                        //Log.e("data", response.getData());

                        callBack.onGetSignDaysDetailSuccess(response);
                    }
                });
    }

    @Override
    public void getChatDays(String studentId, String date, final MySignerCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getChatDays(studentId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<List<String>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetChatDaysError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<List<String>> response) {

                        //Log.e("data", response.getData());

                        callBack.onGetChatDaysSuccess(response);
                    }
                });
    }

    @Override
    public void getChatDaysDetail(String studentId, String date, final MySignerCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getChatDaysDetail(studentId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<List<MyChat>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetChatDaysDetailError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<List<MyChat>> response) {

                        //Log.e("data", response.getData());

                        callBack.onGetChatDaysDetailSuccess(response);
                    }
                });
    }
}
