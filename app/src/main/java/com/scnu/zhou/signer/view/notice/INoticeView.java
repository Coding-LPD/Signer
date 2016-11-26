package com.scnu.zhou.signer.view.notice;

import com.scnu.zhou.signer.component.bean.notice.NoticeBean;

import java.util.List;

/**
 * Created by zhou on 16/10/28.
 */
public interface INoticeView {

    void initView();
    void initData();

    void onGetNoticeSuccess(List<NoticeBean> response);
    void onGetNoticeError(Throwable e);
    void onGetNoticeError(String msg);
}
