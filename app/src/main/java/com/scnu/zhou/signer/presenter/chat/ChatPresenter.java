package com.scnu.zhou.signer.presenter.chat;

import com.scnu.zhou.signer.callback.chat.ChatCallBack;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.config.SocketClient;
import com.scnu.zhou.signer.view.chat.IChatView;

import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public class ChatPresenter implements IChatPresenter, ChatCallBack{

    private IChatView chatView;

    public ChatPresenter(IChatView chatView){

        this.chatView = chatView;
    }

    @Override
    public void sendMessageListRequest(String courseId, int page) {

        SocketClient.getInstance().sendMessageListRequest(courseId, page, this);
    }

    @Override
    public void onGetMessageListSuccess(ResultResponse<List<ChatMessage>> response) {

        chatView.onGetMessageListSuccess(response);
    }
}
