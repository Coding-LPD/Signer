package com.scnu.zhou.signer.model.login;

import com.scnu.zhou.signer.callback.login.LoginCallBack;

/**
 * Created by zhou on 16/10/19.
 */
public interface ILoginModel {

    void getPublicKey(LoginCallBack callback);

    void postLogin(String phone, String password, LoginCallBack callback);
}
