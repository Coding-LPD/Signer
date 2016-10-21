package com.scnu.zhou.signer.presenter.sign;

import com.scnu.zhou.signer.callback.sign.SignCallback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.model.sign.ISignModel;
import com.scnu.zhou.signer.model.sign.SignModel;
import com.scnu.zhou.signer.view.sign.ISignView;

/**
 * Created by zhou on 16/10/21.
 */
public class SignPresenter implements ISignPresenter, SignCallback {

    private ISignModel signModel;
    private ISignView signView;

    public SignPresenter(ISignView signView){

        this.signModel = new SignModel();
        this.signView = signView;
    }

    @Override
    public void getScanResult(String code) {

        signModel.getScanResult(code, this);
    }

    @Override
    public void onGetScanResultSuccess(ResultResponse<ScanResult> response) {

        signView.onGetScanResultSuccess(response);
    }

    @Override
    public void onGetScanResultError(Throwable e) {

        signView.onGetScanResultError(e);
    }
}
