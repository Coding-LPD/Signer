package com.scnu.zhou.signer.view.mine;

import java.util.List;
import java.util.Map;

/**
 * Created by zhou on 16/11/23.
 */
public interface IMySignerView {

    void initView();
    void initData();

    void onGetDays(Map<String, Boolean> note);
    void onGetDaysDetails(List<String> response);
    void onShowError(Throwable e);
    void onShowError(String msg);
    void onShowDetailError(Throwable e);
    void onShowDetailError(String msg);
}
