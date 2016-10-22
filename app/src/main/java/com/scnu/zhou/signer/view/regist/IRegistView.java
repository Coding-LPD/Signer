package com.scnu.zhou.signer.view.regist;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.login.LoginResult;

/**
 * Created by zhou on 16/10/20.
 */
public interface IRegistView {

    void initView();
    void initData();

    void onGetPublicKeySuccess(ResultResponse<String> response);
    void onGetPublicKeyError(Throwable e);

    void onPostRegistSuccess(ResultResponse<LoginResult> response);
    void onPostRegistError(Throwable e);
}
