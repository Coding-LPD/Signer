package com.scnu.zhou.signer.model.regist;

import com.scnu.zhou.signer.callback.regist.RegistCallback;

/**
 * Created by zhou on 16/10/20.
 */
public interface IRegistModel {

    void getPublicKey(RegistCallback callback);

    void postRegist(String phone, String password, RegistCallback callback);
}
