package com.scnu.zhou.signer.presenter.sign;

import com.scnu.zhou.signer.callback.sign.SignCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.component.bean.sign.SignRecord;
import com.scnu.zhou.signer.model.sign.ISignModel;
import com.scnu.zhou.signer.model.sign.SignModel;
import com.scnu.zhou.signer.view.sign.ISignView;

import java.util.Map;

/**
 * Created by zhou on 16/10/21.
 */
public class SignPresenter implements ISignPresenter, SignCallBack {

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
    public void postSign(Map<String, String> strinfos, Map<String, Integer> numinfos, Map<String,Double> doubleinfos) {

        signModel.postSign(strinfos, numinfos, doubleinfos, this);
    }

    @Override
    public void onGetScanResultSuccess(ResultResponse<ScanResult> response) {

        if (response.getCode().equals("200")) {

            ScanResult result = response.getData();
            if (result != null) {

                //Log.e("data", result.getCourse().getTime());
                //String time = "星期一 1节-3节,星期四 5节-8节";
                String time = result.getCourse().getTime();
                String[] sessions = time.split(",");   // 按逗号分开

                String week = "";
                String session = "";
                for (String se : sessions) {
                    String[] s = se.split("\\s+");    // 按空格分开
                    if (week.equals("")) week += s[0];
                    else week += ";" + s[0];
                    if (session.equals("")) session += s[1];
                    else session += ";" + s[1];
                }

                signView.onGetScanResultSuccess(response.getData(), week, session);
            }
        }
        else if (response.getCode().equals("4000")){
            signView.showUnavailCode();     // 无效签到码
        }
        else{
            signView.onShowError(response.getMsg());
        }
    }

    @Override
    public void onGetScanResultError(Throwable e) {

        signView.onShowError(e);
    }

    @Override
    public void onPostSignSuccess(ResultResponse<SignRecord> response) {

        if (response.getCode().equals("200")) {
            signView.onPostSignSuccess(response.getData());
        }
        else if (response.getCode().equals("4005")){
            signView.showNoNumberDialog();
        }
        else if (response.getCode().equals("4006")){
            signView.showNoAdmittedDialog();
        }
        else{
            signView.onShowError(response.getMsg());
        }
    }

    @Override
    public void onPostSignError(Throwable e) {

        signView.onShowError(e);
    }
}
