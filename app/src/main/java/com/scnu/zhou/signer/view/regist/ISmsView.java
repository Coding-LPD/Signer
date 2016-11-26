package com.scnu.zhou.signer.view.regist;

/**
 * Created by zhou on 16/10/20.
 */
public interface ISmsView {

    void initView();
    void initData();

    void onSendSmsSuccess(String response);
    void onSendSmsError(String msg);
    void onSendSmsError(Throwable e);

    void onVerifySmsSuccess(String response);
    void onVerifySmsError(String msg);
    void onVerifySmsError(Throwable e);
}
