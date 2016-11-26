package com.scnu.zhou.signer.presenter.notice;

import android.content.Context;

import com.scnu.zhou.signer.component.bean.notice.NoticeBean;

import java.util.List;

/**
 * Created by zhou on 16/11/2.
 */
public interface INoticePresenter {

    void getNotices(String phone, int type, int limit, int page);

    void setCache(Context context, String key, List<NoticeBean> mData);

    List<NoticeBean> getCache(Context context, String key);
}
