package com.scnu.zhou.signer.presenter.regist;

import com.scnu.zhou.signer.callback.regist.RegistCallback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.login.LoginResult;
import com.scnu.zhou.signer.model.regist.IRegistModel;
import com.scnu.zhou.signer.model.regist.RegistModel;
import com.scnu.zhou.signer.view.regist.IRegistView;

/**
 * Created by zhou on 16/10/20.
 */
public class RegistPresenter implements IRegistPresenter, RegistCallback {

    private IRegistModel registModel;
    private IRegistView registView;

    public RegistPresenter(IRegistView registView){
        registModel = new RegistModel();
        this.registView = registView;
    }

    @Override
    public void getPublicKey() {
        registModel.getPublicKey(this);
    }

    @Override
    public void regist(String phone, String password) {
        registModel.postRegist(phone, password, this);
    }

    @Override
    public void onGetPublicKeySuccess(ResultResponse<String> response) {

        registView.onGetPublicKeySuccess(response);
    }

    @Override
    public void onGetPublicKeyError(Throwable e) {

        registView.onGetPublicKeyError(e);
    }

    @Override
    public void onPostRegistSuccess(ResultResponse<LoginResult> response) {

        registView.onPostRegistSuccess(response);
    }

    @Override
    public void onPostRegistError(Throwable e) {

        registView.onPostRegistError(e);
    }
}
