package com.scnu.zhou.signer.callback.sign;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;

/**
 * Created by zhou on 16/10/21.
 */
public interface SignCallback {

    void onGetScanResultSuccess(ResultResponse<ScanResult> response);
    void onGetScanResultError(Throwable e);

}
