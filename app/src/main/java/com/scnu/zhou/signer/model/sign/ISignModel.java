package com.scnu.zhou.signer.model.sign;

import com.scnu.zhou.signer.callback.sign.SignCallback;

import java.util.Map;

/**
 * Created by zhou on 16/10/21.
 */
public interface ISignModel {

    void getScanResult(String code, SignCallback callback);

    void postSign(Map<String,String> strinfos, Map<String,Integer> numinfos,
                  Map<String,Double> doubleinfos, SignCallback callback);    // 执行签到动作
}
