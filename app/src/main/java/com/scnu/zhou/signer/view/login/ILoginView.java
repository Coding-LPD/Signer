package com.scnu.zhou.signer.view.login;

import com.scnu.zhou.signer.component.bean.login.LoginResult;

/**
 * Created by zhou on 16/10/20.
 */
public interface ILoginView {

    void initView();
    void initData();

    void onGetPublicKeySuccess(String response);
    void onPostLoginSuccess(LoginResult response);

    void onPostLoginError(String msg);
    void onPostLoginError(Throwable e);
}
