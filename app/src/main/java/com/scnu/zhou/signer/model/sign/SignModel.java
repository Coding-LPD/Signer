package com.scnu.zhou.signer.model.sign;

import com.scnu.zhou.signer.callback.sign.SignCallback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.component.bean.sign.SignRecord;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/10/21.
 */
public class SignModel implements ISignModel{


    @Override
    public void getScanResult(String code, final SignCallback callback) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getScanResult(code)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<ScanResult>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onGetScanResultError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<ScanResult> response) {

                        callback.onGetScanResultSuccess(response);
                    }
                });
    }

    @Override
    public void postSign(Map<String,String> strinfos, Map<String,Integer> numinfos,
                         Map<String,Double> doubleinfos, final SignCallback callback) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .sign(strinfos, numinfos, doubleinfos)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<SignRecord>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onPostSignError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<SignRecord> response) {

                        callback.onPostSignSuccess(response);
                    }
                });
    }
}
