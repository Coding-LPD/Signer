package com.scnu.zhou.signer.model.notice;

import com.scnu.zhou.signer.callback.notice.NoticeCallBack;

/**
 * Created by zhou on 16/11/2.
 */
public interface INoticeModel {

    void getNotices(String phone, int type, int limit, int page, NoticeCallBack callBack);
}
