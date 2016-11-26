package com.scnu.zhou.signer.view.home;

import java.util.Map;

/**
 * Created by zhou on 16/11/3.
 */
public interface ICheckSignView {

    void initView();
    void initData();

    void onGetSignDetailSuccess(Map<String, Boolean> note01, Map<String, Boolean> note02);
    void onGetSignDetailError(String msg);
    void onGetSignDetailError(Throwable e);
}
