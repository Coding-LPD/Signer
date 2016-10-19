package com.scnu.zhou.signer.elss.util.http;

import com.scnu.zhou.signer.elss.config.SignerServer;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhou on 2016/9/19.
 */
public class RetrofitServer {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(SignerServer.Server)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
