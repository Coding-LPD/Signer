package com.scnu.zhou.signer.presenter.chat;

import android.content.Context;

import com.scnu.zhou.signer.callback.chat.RoomListCallBack;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.config.SocketClient;
import com.scnu.zhou.signer.view.chat.IRoomView;

import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public class RoomPresenter implements IRoomPresenter, RoomListCallBack{

    private Context context;
    private IRoomView roomView;

    public RoomPresenter(Context context, IRoomView roomListView){

        this.context = context;
        this.roomView = roomListView;
    }

    @Override
    public void sendRoomListRequest(String studentId) {

        SocketClient.getInstance().sendRoomListRequest(studentId, this);
    }

    @Override
    public void onGetRoomListSuccess(ResultResponse<List<ChatRoom>> response) {

        roomView.onGetRoomListSuccess(response);
    }

    @Override
    public void onReceiveNewMessage(ResultResponse<ChatMessage> response) {

        roomView.onReceiveNewMessage(response);
    }
}
