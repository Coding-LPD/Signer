package com.scnu.zhou.signer.model.login;

import com.scnu.zhou.signer.callback.login.LoginCallback;

/**
 * Created by zhou on 16/10/19.
 */
public interface ILoginModel {

    void getPublicKey(LoginCallback callback);

    void postLogin(String phone, String password, LoginCallback callback);
}
