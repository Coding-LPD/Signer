package com.scnu.zhou.signer.view.sign;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;

/**
 * Created by zhou on 16/10/21.
 */
public interface ISignView {

    void initView();
    void initData();

    void onGetScanResultSuccess(ResultResponse<ScanResult> response);
    void onGetScanResultError(Throwable e);
}
