package com.scnu.zhou.signer.presenter.mine;

/**
 * Created by zhou on 16/11/23.
 */
public interface IMySignerPresenter {

    void getSignDays(String studentId, String date);
    void getSignDaysDetail(String studentId, String date);
}
