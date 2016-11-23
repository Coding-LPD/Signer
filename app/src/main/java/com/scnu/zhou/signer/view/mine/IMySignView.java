package com.scnu.zhou.signer.view.mine;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.mine.MySign;

import java.util.List;

/**
 * Created by zhou on 16/11/23.
 */
public interface IMySignView {

    void initView();
    void initData();

    void onGetSignDaysSuccess(ResultResponse<List<String>> response);
    void onGetSignDaysError(Throwable e);

    void onGetSignDaysDetailSuccess(ResultResponse<List<MySign>> response);
    void onGetSignDaysDetailError(Throwable e);
}
