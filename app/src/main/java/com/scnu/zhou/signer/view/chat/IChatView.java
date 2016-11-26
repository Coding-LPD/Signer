package com.scnu.zhou.signer.view.chat;

import com.scnu.zhou.signer.component.bean.chat.ChatMessage;

import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public interface IChatView {

    void initView();

    void initData();

    void onGetMessageList(List<ChatMessage> response);

    void onReceiveNewMessage(ChatMessage response);

    void onErrorMessageShow(String msg);
}
