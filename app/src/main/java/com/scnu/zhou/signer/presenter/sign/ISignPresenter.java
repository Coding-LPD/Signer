package com.scnu.zhou.signer.presenter.sign;

import java.util.Map;

/**
 * Created by zhou on 16/10/21.
 */
public interface ISignPresenter {

    void getScanResult(String code);

    void postSign(Map<String,String> strinfos, Map<String,Integer> numinfos, Map<String,Double> doubleinfos);
}
