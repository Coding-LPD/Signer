package com.scnu.zhou.signer.model.mine;

import com.scnu.zhou.signer.callback.mine.MySignerCallBack;

/**
 * Created by zhou on 16/11/23.
 */
public interface IMySignerModel {

    void getSignDays(String studentId, String date, MySignerCallBack callBack);
    void getSignDaysDetail(String studentId, String date, MySignerCallBack callBack);

    void getChatDays(String studentId, String date, MySignerCallBack callBack);
    void getChatDaysDetail(String studentId, String date, MySignerCallBack callBack);
}
