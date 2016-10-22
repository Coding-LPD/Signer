package com.scnu.zhou.signer.model.regist;

import com.scnu.zhou.signer.callback.regist.RegistCallback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.login.LoginResult;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/10/20.
 */
public class RegistModel implements IRegistModel {

    @Override
    public void getPublicKey(final RegistCallback callback) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getPublicKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onGetPublicKeyError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        callback.onGetPublicKeySuccess(response);
                    }
                });
    }


    @Override
    public void postRegist(String phone, String password, final RegistCallback callback) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .regist(phone, password, "0")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<LoginResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onPostRegistError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<LoginResult> response) {

                        callback.onPostRegistSuccess(response);
                    }
                });
    }
}
