package com.scnu.zhou.signer.presenter.login;

import android.content.Context;

/**
 * Created by zhou on 16/10/20.
 */
public interface ILoginPresenter {

    void GetPublicKey();

    void login(String phone, String password);

    void setLoginCache(Context context, String id, String phone, String password, String role);

    void setNumberCache(Context context, String number);
}
