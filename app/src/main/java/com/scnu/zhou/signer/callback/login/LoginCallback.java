package com.scnu.zhou.signer.callback.login;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.login.LoginResult;

/**
 * Created by zhou on 16/10/20.
 */
public interface LoginCallBack {

    void onGetPublicKeySuccess(ResultResponse<String> response);
    void onGetPublicKeyError(Throwable e);

    void onPostLoginSuccess(ResultResponse<LoginResult> response);
    void onPostLoginError(Throwable e);
}
