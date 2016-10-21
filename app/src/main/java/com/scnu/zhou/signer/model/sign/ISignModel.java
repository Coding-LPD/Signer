package com.scnu.zhou.signer.model.sign;

import com.scnu.zhou.signer.callback.sign.SignCallback;

/**
 * Created by zhou on 16/10/21.
 */
public interface ISignModel {

    void getScanResult(String code, SignCallback callback);
}
