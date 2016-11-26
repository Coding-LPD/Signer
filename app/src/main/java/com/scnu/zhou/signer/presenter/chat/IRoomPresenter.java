package com.scnu.zhou.signer.presenter.chat;

import com.scnu.zhou.signer.component.bean.chat.ChatRoom;

import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public interface IRoomPresenter {

    void sendRoomListRequest(String id, String role);

    void saveRoomListCache(List<ChatRoom> mData);

    List<ChatRoom> getRoomListChache();
}
