package com.scnu.zhou.signer.presenter.chat;

import android.content.Context;

import com.scnu.zhou.signer.callback.chat.ChatCallBack;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.cache.TimeCache;
import com.scnu.zhou.signer.component.config.SocketClient;
import com.scnu.zhou.signer.view.chat.IChatView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhou on 16/11/17.
 */
public class ChatPresenter implements IChatPresenter, ChatCallBack {

    private IChatView chatView;

    public ChatPresenter(IChatView chatView){

        this.chatView = chatView;
    }

    @Override
    public void sendMessageListRequest(String courseId, int page) {

        SocketClient.getInstance().sendMessageListRequest(courseId, page, this);
    }

    @Override
    public void sendMessageAction(String courseId, String peopleId, String role, String content) {

        SocketClient.getInstance().sendMessageAction(courseId, peopleId, role, content);
    }

    @Override
    public void saveLatestVisitTime(Context context, String courseId) {

        // 保存最近访问时间戳
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = format.format(curDate);

        TimeCache.getInstance().setTime(context, courseId, time);
    }


    /**
     * implementation for ChatCallBack
     * @param response
     */
    @Override
    public void onGetMessageListSuccess(ResultResponse<List<ChatMessage>> response) {

        if (response.getCode().equals("200")) {

            chatView.onGetMessageList(response.getData());
        }
        else{

            chatView.onErrorMessageShow(response.getMsg());
        }
    }

    @Override
    public void onReceiveNewMessage(ResultResponse<ChatMessage> response) {

        if (response.getCode().equals("200")) {

            chatView.onReceiveNewMessage(response.getData());
        }
        else{

            chatView.onErrorMessageShow(response.getMsg());
        }

    }
}
