package com.scnu.zhou.signer.model.login;

/**
 * Created by zhou on 16/10/19.
 */
public interface ILoginModel {

    void getPublicKey();

    void postLogin(String phone, String password);
}
