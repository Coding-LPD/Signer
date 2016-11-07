package com.scnu.zhou.signer.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.scnu.zhou.signer.component.cache.NoticeCache;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.config.SignerServer;
import com.scnu.zhou.signer.component.config.SocketEvent;

/**
 * Created by zhou on 16/11/1.
 */
public class NoticeService extends Service {

    private final String TAG = "NoticeService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startSocketClient();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startSocketClient(){

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
                    socket.emit("student-in", UserCache.getInstance().getId(NoticeService.this));

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

                    int number = NoticeCache.getInstance().getNoticenNumber(NoticeService.this) + 1;
                    NoticeCache.getInstance().setNoticenNumber(NoticeService.this, number);
                }
            });

            socket.connect();

        }catch (Exception e){

            e.printStackTrace();
            Log.e(TAG, "发生错误");
        }
    }
}
