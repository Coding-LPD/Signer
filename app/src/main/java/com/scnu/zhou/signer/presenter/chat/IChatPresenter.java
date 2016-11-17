package com.scnu.zhou.signer.presenter.chat;

/**
 * Created by zhou on 16/11/17.
 */
public interface IChatPresenter {

    void sendMessageListRequest(String courseId, int page);

    void sendMessageAction(String courseId, String studentId, String content);
}
