package com.scnu.zhou.signer.view.regist;

import com.scnu.zhou.signer.component.bean.login.LoginResult;

/**
 * Created by zhou on 16/10/20.
 */
public interface IRegistView {

    void initView();
    void initData();

    void onGetPublicKeySuccess(String response);

    void onPostRegistSuccess(LoginResult response);

    void onShowError(String msg);
    void onShowError(Throwable e);
}
