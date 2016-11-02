package com.scnu.zhou.signer.model.notice;

import com.scnu.zhou.signer.callback.notice.NoticeCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/11/2.
 */
public class NoticeModel implements INoticeModel {

    @Override
    public void getNotices(String phone, int type, int limit, int page, final NoticeCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getNotices(phone, type, limit, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<List<NoticeBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetNoticeError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<List<NoticeBean>> response) {

                        //Log.e("data", response.getData());

                        callBack.onGetNoticeSuccess(response);
                    }
                });
    }
}
