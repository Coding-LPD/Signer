package com.scnu.zhou.signer.presenter.login;

import android.content.Context;

import com.scnu.zhou.signer.callback.login.LoginCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.login.LoginResult;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.model.login.ILoginModel;
import com.scnu.zhou.signer.model.login.LoginModel;
import com.scnu.zhou.signer.view.login.ILoginView;

/**
 * Created by zhou on 16/10/20.
 */
public class LoginPresenter implements ILoginPresenter, LoginCallBack {

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
    public void setLoginCache(Context context, String id, String phone, String password, String role) {

        // 保存登录信息
        UserCache.getInstance().login(context, phone, password, role);

        UserCache.getInstance().setId(context, id);
    }

    @Override
    public void setNumberCache(Context context, String number) {

        // 记录学生用户
        UserCache.getInstance().setNumber(context, number);
    }


    /**
     * implementation for LoginCallBack
     * @param response
     */
    @Override
    public void onGetPublicKeySuccess(ResultResponse<String> response) {

        if (response.getCode().equals("200")) {
            loginView.onGetPublicKeySuccess(response.getData());
        }
        else{
            loginView.onPostLoginError(response.getMsg());
        }
    }

    @Override
    public void onGetPublicKeyError(Throwable e) {

        loginView.onPostLoginError(e);
    }

    @Override
    public void onPostLoginSuccess(ResultResponse<LoginResult> response) {

        if (response.getCode().equals("200")) {
            loginView.onPostLoginSuccess(response.getData());
        }
        else{
            loginView.onPostLoginError(response.getMsg());
        }
    }

    @Override
    public void onPostLoginError(Throwable e) {

        loginView.onPostLoginError(e);
    }
}
