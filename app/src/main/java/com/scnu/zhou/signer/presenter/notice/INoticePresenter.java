package com.scnu.zhou.signer.presenter.notice;

/**
 * Created by zhou on 16/11/2.
 */
public interface INoticePresenter {

    void getNotices(String phone, int type, int limit, int page);
}
