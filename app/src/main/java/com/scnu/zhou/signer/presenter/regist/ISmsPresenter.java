package com.scnu.zhou.signer.presenter.regist;

/**
 * Created by zhou on 16/10/20.
 */
public interface ISmsPresenter {

    void sendSmsCode(String phone);

    void verifySmsCode(String phone, String code);
}
