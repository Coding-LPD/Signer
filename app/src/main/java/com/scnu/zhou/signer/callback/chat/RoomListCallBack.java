package com.scnu.zhou.signer.callback.chat;

import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;

import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public interface RoomListCallBack {

    void onGetRoomListSuccess(ResultResponse<List<ChatRoom>> response);

    void onReceiveNewMessage(ResultResponse<ChatMessage> response);
}
