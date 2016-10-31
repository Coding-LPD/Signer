package com.scnu.zhou.signer.callback.sign;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.component.bean.sign.SignRecord;

/**
 * Created by zhou on 16/10/21.
 */
public interface SignCallBack {

    void onGetScanResultSuccess(ResultResponse<ScanResult> response);
    void onGetScanResultError(Throwable e);

    void onPostSignSuccess(ResultResponse<SignRecord> response);
    void onPostSignError(Throwable e);
}
