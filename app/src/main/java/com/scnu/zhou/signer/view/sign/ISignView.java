package com.scnu.zhou.signer.view.sign;

import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.component.bean.sign.SignRecord;

/**
 * Created by zhou on 16/10/21.
 */
public interface ISignView {

    void initView();
    void initData();

    void onGetScanResultSuccess(ScanResult response, String week, String session);

    void onPostSignSuccess(SignRecord response);

    void onShowError(String error);
    void onShowError(Throwable e);

    void showUnavailCode();   // 无效签到码
    void showNoNumberDialog();   // 没有学号
    void showNoAdmittedDialog();   // 不属于该课程学生
}
