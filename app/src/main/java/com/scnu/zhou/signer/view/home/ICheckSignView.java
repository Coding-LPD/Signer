package com.scnu.zhou.signer.view.home;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.SignBean;

import java.util.List;

/**
 * Created by zhou on 16/11/3.
 */
public interface ICheckSignView {

    void initView();
    void initData();

    void onGetSignDetailSuccess(ResultResponse<List<SignBean>> response);
    void onGetSignDetailError(Throwable e);
}
