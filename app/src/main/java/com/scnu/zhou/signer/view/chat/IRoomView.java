package com.scnu.zhou.signer.view.chat;

import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;

import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public interface IRoomView {

    void onGetRoomList(List<ChatRoom> response);

    void onReceiveNewMessage(ChatMessage response);

    void onErrorMessageShow(String msg);
}
