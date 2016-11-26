package com.scnu.zhou.signer.presenter.chat;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scnu.zhou.signer.callback.chat.RoomListCallBack;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.cache.ACache;
import com.scnu.zhou.signer.component.config.SocketClient;
import com.scnu.zhou.signer.component.util.time.RoomComparator;
import com.scnu.zhou.signer.view.chat.IRoomView;

import java.util.Collections;
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
    public void sendRoomListRequest(String id, String role) {

        SocketClient.getInstance().sendRoomListRequest(id, role, this);
    }

    @Override
    public void saveRoomListCache(List<ChatRoom> mData) {

        String value = new Gson().toJson(mData);
        ACache.get(context).put("chat_room", value);
    }

    @Override
    public List<ChatRoom> getRoomListChache() {

        String array = ACache.get(context).getAsString("chat_room");
        //Log.e("get-array", array);
        if (!TextUtils.isEmpty(array) && !array.equals("null")) {
            List<ChatRoom> mData = new Gson().fromJson(array,
                    new TypeToken<List<ChatRoom>>() {
                    }.getType());
            return mData;
        }
        else{
            return null;
        }
    }


    /**
     * implementation for RoomListCallBack
     * @param response
     */
    @Override
    public void onGetRoomListSuccess(ResultResponse<List<ChatRoom>> response) {

        if (response.getCode().equals("200")) {

            List<ChatRoom> mData = response.getData();
            Collections.sort(mData, new RoomComparator());
            roomView.onGetRoomList(mData);
        }
        else{

            roomView.onErrorMessageShow(response.getMsg());
        }
    }

    @Override
    public void onReceiveNewMessage(ResultResponse<ChatMessage> response) {

        roomView.onReceiveNewMessage(response.getData());
    }
}
