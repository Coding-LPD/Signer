package com.scnu.zhou.signer.presenter.notice;

import com.scnu.zhou.signer.callback.notice.NoticeCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;
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
    public void onGetNoticeSuccess(ResultResponse<List<NoticeBean>> response) {

        noticeView.onGetNoticeSuccess(response);
    }

    @Override
    public void onGetNoticeError(Throwable e) {

        noticeView.onGetNoticeError(e);
    }
}
