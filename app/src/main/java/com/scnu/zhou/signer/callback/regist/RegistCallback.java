package com.scnu.zhou.signer.callback.regist;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.User;

/**
 * Created by zhou on 16/10/20.
 */
public interface RegistCallback {

    void onGetPublicKeySuccess(ResultResponse<String> response);
    void onGetPublicKeyError(Throwable e);

    void onPostRegistSuccess(ResultResponse<User> response);
    void onPostRegistError(Throwable e);
}
