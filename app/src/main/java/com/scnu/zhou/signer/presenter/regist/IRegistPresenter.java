package com.scnu.zhou.signer.presenter.regist;

/**
 * Created by zhou on 16/10/20.
 */
public interface IRegistPresenter {

    void getPublicKey();

    void regist(String phone, String password);
}
