package com.scnu.zhou.signer.model.regist;

import com.scnu.zhou.signer.callback.regist.RegistCallBack;

/**
 * Created by zhou on 16/10/20.
 */
public interface IRegistModel {

    void getPublicKey(RegistCallBack callback);

    void postRegist(String phone, String password, RegistCallBack callback);
}
