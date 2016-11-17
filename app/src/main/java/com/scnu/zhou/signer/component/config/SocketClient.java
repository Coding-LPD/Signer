package com.scnu.zhou.signer.component.config;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scnu.zhou.signer.callback.chat.RoomListCallBack;
import com.scnu.zhou.signer.component.bean.chat.ChatRoom;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.cache.NoticeCache;
import com.scnu.zhou.signer.component.cache.UserCache;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhou on 16/11/16.
 */
public class SocketClient {

    private final String TAG = "SocketClient";

    private Socket socket;

    private OnNoticeReceiveListener listener;
    private RoomListCallBack roomListCallBack;

    private SocketClient(){

    }

    public static SocketClient getInstance(){

        return SocketClientHolder.instance;
    }

    private static class SocketClientHolder {

        private static final SocketClient instance = new SocketClient();
    }

    public void startSocketClient(final Context context){

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;
        opts.port = 3000;
        try {
            socket = IO.socket(SignerServer.Server + "/sign",opts);

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.e(TAG,"EVENT_CONNECT");

                    // 传回StudentId
                    socket.emit("student-in", UserCache.getInstance().getId(context));

                }

            }).on(Socket.EVENT_CONNECT_TIMEOUT,new Emitter.Listener(){

                @Override
                public void call(Object... args){

                    Log.e(TAG,"EVENT_CONNECT_TIMEOUT");
                }
            }).on(Socket.EVENT_CONNECT_ERROR,new Emitter.Listener(){

                @Override
                public void call(Object... args){

                    Log.e(TAG,"EVENT_CONNECT_ERROR");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.e(TAG, "EVENT_DISCONNECT");

                }

            }).on(SocketEvent.NOTICE_ENVENT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    // 监听未读通知
                    Log.e(TAG, "notice");

                    int number = NoticeCache.getInstance().getNoticenNumber(context) + 1;
                    NoticeCache.getInstance().setNoticenNumber(context, number);

                    if (listener != null){

                        listener.onNoticeReceive();
                    }

                }
            }).on(SocketEvent.ROOMLIST_EVENT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    // 获取聊天室列表
                    Log.e(TAG, "room-list");

                    if (roomListCallBack != null) {
                        String response = ((JSONObject) args[0]).toString();
                        ResultResponse<List<ChatRoom>> mData = new ResultResponse<List<ChatRoom>>();

                        Log.e(TAG, response);

                        if (!TextUtils.isEmpty(response) && !response.equals("null"))
                            mData = new Gson().fromJson(response,
                                    new TypeToken<ResultResponse<List<ChatRoom>>>() {
                                    }.getType());

                        roomListCallBack.onGetRoomListSuccess(mData);
                    }

                }

            });

            socket.connect();

        }catch (Exception e){

            e.printStackTrace();
            Log.e(TAG, "发生错误");
        }
    }


    public void sendRoomListRequest(String studentId, RoomListCallBack callBack){

        socket.emit("room-list", studentId);
        this.roomListCallBack = callBack;
    }


    /**
     * Listener for NoticeReceive
     * @param listener
     */
    public void setOnNoticeReceiveListener(OnNoticeReceiveListener listener){

        this.listener = listener;
    }

    public void clearOnNoticeReceiveListener(){

        this.listener = null;
    }

    public interface OnNoticeReceiveListener{

        void onNoticeReceive();
    }
}
