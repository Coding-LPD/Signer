package com.scnu.zhou.signer.presenter.chat;

import android.content.Context;

/**
 * Created by zhou on 16/11/17.
 */
public interface IChatPresenter {

    void sendMessageListRequest(String courseId, int page);

    void sendMessageAction(String courseId, String peopleId, String role, String content);

    void saveLatestVisitTime(Context context, String courseId);    // 保存最近访问时间戳
}
