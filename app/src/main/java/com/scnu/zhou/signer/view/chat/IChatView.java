package com.scnu.zhou.signer.view.chat;

import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;

import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public interface IChatView {

    void onGetMessageListSuccess(ResultResponse<List<ChatMessage>> response);

    void onSendMessageSuccess(ResultResponse<ChatMessage> response);
}
