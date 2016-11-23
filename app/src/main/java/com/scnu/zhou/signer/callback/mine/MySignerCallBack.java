package com.scnu.zhou.signer.callback.mine;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.mine.MyChat;
import com.scnu.zhou.signer.component.bean.mine.MySign;

import java.util.List;

/**
 * Created by zhou on 16/11/23.
 */
public interface MySignerCallBack {

    void onGetSignDaysSuccess(ResultResponse<List<String>> response);
    void onGetSignDaysError(Throwable e);

    void onGetSignDaysDetailSuccess(ResultResponse<List<MySign>> response);
    void onGetSignDaysDetailError(Throwable e);

    void onGetChatDaysSuccess(ResultResponse<List<String>> response);
    void onGetChatDaysError(Throwable e);

    void onGetChatDaysDetailSuccess(ResultResponse<List<MyChat>> response);
    void onGetChatDaysDetailError(Throwable e);
}
