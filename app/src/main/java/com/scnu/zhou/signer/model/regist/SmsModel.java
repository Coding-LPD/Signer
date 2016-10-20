package com.scnu.zhou.signer.model.regist;

import com.scnu.zhou.signer.callback.regist.SmsCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/10/20.
 */
public class SmsModel implements ISmsModel {

    @Override
    public void sendSmsCode(String phone, final SmsCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .sendSmsCode(phone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onSendSmsError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        callBack.onSendSmsSuccess(response);
                    }
                });
    }


    @Override
    public void verifySmsCode(String phone, String code, final SmsCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .verifySmsCode(phone, code)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onVerifySmsError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        callBack.onVerifySmsSuccess(response);
                    }
                });
    }
}
