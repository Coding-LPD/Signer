package com.scnu.zhou.signer.component.config;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.scnu.zhou.signer.component.cache.NoticeCache;
import com.scnu.zhou.signer.component.cache.UserCache;

/**
 * Created by zhou on 16/11/16.
 */
public class SocketClient {

    private final String TAG = "SocketClient";

    private OnNoticeReceiveListener listener;

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
            final Socket socket = IO.socket(SignerServer.Server + "/sign",opts);

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
            });

            socket.connect();

        }catch (Exception e){

            e.printStackTrace();
            Log.e(TAG, "发生错误");
        }
    }

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
