package com.scnu.zhou.signer.presenter.chat;

import android.content.Context;

import com.scnu.zhou.signer.callback.chat.RoomListCallBack;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.config.SocketClient;
import com.scnu.zhou.signer.view.chat.IRoomListView;

import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public class ChatPresenter implements IChatPresenter, RoomListCallBack{

    private Context context;
    private IRoomListView roomListView;

    public ChatPresenter(Context context, IRoomListView roomListView){

        this.context = context;
        this.roomListView = roomListView;
    }

    @Override
    public void sendRoomListRequest(String studentId) {

        SocketClient.getInstance().sendRoomListRequest(studentId, this);
    }

    @Override
    public void onGetRoomListSuccess(ResultResponse<List<ChatRoom>> response) {

        roomListView.onGetRoomListSuccess(response);
    }
}
