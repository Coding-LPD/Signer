package com.scnu.zhou.signer.presenter.regist;

import com.scnu.zhou.signer.callback.regist.SmsCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.model.regist.ISmsModel;
import com.scnu.zhou.signer.model.regist.SmsModel;
import com.scnu.zhou.signer.view.regist.ISmsView;

/**
 * Created by zhou on 16/10/20.
 */
public class SmsPresenter implements ISmsPresenter, SmsCallBack{

    private ISmsModel smsModel;
    private ISmsView smsView;

    public SmsPresenter(ISmsView smsView){

        this.smsModel = new SmsModel();
        this.smsView = smsView;
    }

    @Override
    public void sendSmsCode(String phone) {

        smsModel.sendSmsCode(phone, this);
    }

    @Override
    public void verifySmsCode(String phone, String code) {

        smsModel.verifySmsCode(phone, code, this);
    }

    @Override
    public void onSendSmsSuccess(ResultResponse<String> response) {

        if (response.getCode().equals("200")) {
            smsView.onSendSmsSuccess(response.getData());
        }
        else{
            smsView.onSendSmsError(response.getMsg());
        }
    }

    @Override
    public void onSendSmsError(Throwable e) {

        smsView.onSendSmsError(e);
    }

    @Override
    public void onVerifySmsSuccess(ResultResponse<String> response) {

        if (response.getCode().equals("200")) {
            smsView.onVerifySmsSuccess(response.getData());
        }
        else{
            smsView.onVerifySmsError(response.getMsg());
        }
    }

    @Override
    public void onVerifySmsError(Throwable e) {

        smsView.onVerifySmsError(e);
    }
}
