package com.scnu.zhou.signer.ui.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.scnu.zhou.signer.component.config.SignerServer;

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

        startSocketClient();
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

            }).on("list", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.e("reveice", args[0].toString());
                }
            });

            socket.connect();

        }catch (Exception e){

            e.printStackTrace();
            Log.e(TAG, "发生错误");
        }
    }
}
