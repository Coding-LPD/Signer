package com.scnu.zhou.signer.presenter.notice;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scnu.zhou.signer.callback.notice.NoticeCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;
import com.scnu.zhou.signer.component.cache.ACache;
import com.scnu.zhou.signer.model.notice.INoticeModel;
import com.scnu.zhou.signer.model.notice.NoticeModel;
import com.scnu.zhou.signer.view.notice.INoticeView;

import java.util.List;

/**
 * Created by zhou on 16/11/2.
 */
public class NoticePresenter implements INoticePresenter, NoticeCallBack{

    private INoticeView noticeView;
    private INoticeModel noticeModel;

    public NoticePresenter(INoticeView noticeView){

        this.noticeView = noticeView;
        this.noticeModel = new NoticeModel();
    }

    @Override
    public void getNotices(String phone, int type, int limit, int page) {

        noticeModel.getNotices(phone, type, limit, page, this);
    }

    @Override
    public void setCache(Context context, String key, List<NoticeBean> mData) {

        String value = new Gson().toJson(mData);
        //Log.e("notice", value);
        ACache.get(context).put(key, value);
    }

    @Override
    public List<NoticeBean> getCache(Context context, String key) {

        String array = ACache.get(context).getAsString(key);
        //Log.e("get-array", array);
        if (!TextUtils.isEmpty(array) && !array.equals("null")){
            List<NoticeBean> notices = new Gson().fromJson(array,
                    new TypeToken<List<NoticeBean>>(){}.getType());
            return notices;
        }
        else{
            return null;
        }
    }

    @Override
    public void onGetNoticeSuccess(ResultResponse<List<NoticeBean>> response) {

        if (response.getCode().equals("200")) {
            noticeView.onGetNoticeSuccess(response.getData());
        }
        else{
            noticeView.onGetNoticeError(response.getMsg());
        }
    }

    @Override
    public void onGetNoticeError(Throwable e) {

        noticeView.onGetNoticeError(e);
    }
}
