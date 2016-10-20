package com.scnu.zhou.signer.model.regist;

import com.scnu.zhou.signer.callback.regist.SmsCallBack;

/**
 * Created by zhou on 16/10/20.
 */
public interface ISmsModel {

    void sendSmsCode(String phone, SmsCallBack callBack);

    void verifySmsCode(String phone, String code, SmsCallBack callBack);
}
