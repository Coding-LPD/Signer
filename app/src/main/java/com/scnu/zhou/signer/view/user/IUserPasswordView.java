package com.scnu.zhou.signer.view.user;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.User;

/**
 * Created by zhou on 16/10/20.
 */
public interface IUserPasswordView {

    void initView();
    void initData();

    void onGetPublicKeySuccess(ResultResponse<String> response);
    void onGetPublicKeyError(Throwable e);

    void onUpdatePasswordSuccess(ResultResponse<User> response);
    void onUpdatePasswordError(Throwable e);
}
