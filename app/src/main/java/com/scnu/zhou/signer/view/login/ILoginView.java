package com.scnu.zhou.signer.view.login;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.User;

/**
 * Created by zhou on 16/10/20.
 */
public interface ILoginView {

    void initView();
    void initData();

    void onGetPublicKeySuccess(ResultResponse<String> response);
    void onGetPublicKeyError(Throwable e);

    void onPostLoginSuccess(ResultResponse<User> response);
    void onPostLoginError(Throwable e);
}
