package com.scnu.zhou.signer.view.mine;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.mine.MyChat;

import java.util.List;

/**
 * Created by zhou on 16/11/23.
 */
public interface IMyChatView {

    void initView();
    void initData();

    void onGetChatDaysSuccess(ResultResponse<List<String>> response);
    void onGetChatDaysError(Throwable e);

    void onGetChatDaysDetailSuccess(ResultResponse<List<MyChat>> response);
    void onGetChatDaysDetailError(Throwable e);
}
