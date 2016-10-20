package com.scnu.zhou.signer.callback.login;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.User;

/**
 * Created by zhou on 16/10/20.
 */
public interface LoginCallback {

    void onGetPublicKeySuccess(ResultResponse<String> response);
    void onGetPublicKeyError(Throwable e);

    void onPostLoginSuccess(ResultResponse<User> response);
    void onPostLoginError(Throwable e);
}
