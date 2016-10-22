package com.scnu.zhou.signer.model.login;

import com.scnu.zhou.signer.callback.login.LoginCallback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.login.LoginResult;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/10/19.
 */
public class LoginModel implements ILoginModel{

    @Override
    public void getPublicKey(final LoginCallback callback) {

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

                        //Log.e("data", response.getData());

                        callback.onGetPublicKeySuccess(response);
                    }
                });
    }

    @Override
    public void postLogin(String phone, String password, final LoginCallback callback) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .login(phone, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<LoginResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onPostLoginError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<LoginResult> response) {

                        callback.onPostLoginSuccess(response);
                    }
                });
    }
}
