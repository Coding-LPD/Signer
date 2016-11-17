package com.scnu.zhou.signer.view.chat;

import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;

import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public interface IRoomListView {

    void onGetRoomListSuccess(ResultResponse<List<ChatRoom>> response);
}
