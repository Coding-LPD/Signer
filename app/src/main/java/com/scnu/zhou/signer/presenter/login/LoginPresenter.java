package com.scnu.zhou.signer.presenter.login;

import com.scnu.zhou.signer.callback.login.LoginCallback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.User;
import com.scnu.zhou.signer.model.login.ILoginModel;
import com.scnu.zhou.signer.model.login.LoginModel;
import com.scnu.zhou.signer.view.login.ILoginView;

/**
 * Created by zhou on 16/10/20.
 */
public class LoginPresenter implements ILoginPresenter, LoginCallback {

    private ILoginModel loginModel;
    private ILoginView loginView;

    public LoginPresenter(ILoginView loginView){
        loginModel = new LoginModel();
        this.loginView = loginView;
    }

    @Override
    public void GetPublicKey() {
        loginModel.getPublicKey(this);
    }

    @Override
    public void login(String phone, String password) {
        loginModel.postLogin(phone, password, this);
    }

    @Override
    public void onGetPublicKeySuccess(ResultResponse<String> response) {

        loginView.onGetPublicKeySuccess(response);
    }

    @Override
    public void onGetPublicKeyError(Throwable e) {

        loginView.onGetPublicKeyError(e);
    }

    @Override
    public void onPostLoginSuccess(ResultResponse<User> response) {

        loginView.onPostLoginSuccess(response);
    }

    @Override
    public void onPostLoginError(Throwable e) {

        loginView.onPostLoginError(e);
    }
}
