package com.scnu.zhou.signer.presenter.login;

/**
 * Created by zhou on 16/10/20.
 */
public interface ILoginPresenter {

    void GetPublicKey();

    void login(String phone, String password);
}
