package com.scnu.zhou.signer.view.notice;

/**
 * Created by zhou on 16/10/28.
 */
public interface INoticeView {

    void initView();
    void initData();

    void onGetNoticeSuccess();
    void onGetNoticeError(Throwable e);
}
