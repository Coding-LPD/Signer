package com.scnu.zhou.signer.view.regist;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;

/**
 * Created by zhou on 16/10/20.
 */
public interface ISmsView {

    void initView();
    void initData();

    void onSendSmsSuccess(ResultResponse<String> response);
    void onSendSmsError(Throwable e);

    void onVerifySmsSuccess(ResultResponse<String> response);
    void onVerifySmsError(Throwable e);
}
