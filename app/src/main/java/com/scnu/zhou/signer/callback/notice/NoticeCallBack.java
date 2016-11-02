package com.scnu.zhou.signer.callback.notice;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;

import java.util.List;

/**
 * Created by zhou on 16/11/2.
 */
public interface NoticeCallBack {

    void onGetNoticeSuccess(ResultResponse<List<NoticeBean>> response);
    void onGetNoticeError(Throwable e);
}
