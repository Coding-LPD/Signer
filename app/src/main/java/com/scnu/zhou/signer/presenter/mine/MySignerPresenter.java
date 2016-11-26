package com.scnu.zhou.signer.presenter.mine;

import com.scnu.zhou.signer.callback.mine.MySignerCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.mine.MyChat;
import com.scnu.zhou.signer.component.bean.mine.MySign;
import com.scnu.zhou.signer.model.mine.IMySignerModel;
import com.scnu.zhou.signer.model.mine.MySignerModel;
import com.scnu.zhou.signer.view.mine.IMySignerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhou on 16/11/23.
 */
public class MySignerPresenter implements IMySignerPresenter, MySignerCallBack {

    private IMySignerModel mySignerModel;
    private IMySignerView mySignView;

    public MySignerPresenter(IMySignerView mySignView){

        this.mySignView = mySignView;
        this.mySignerModel = new MySignerModel();
    }

    @Override
    public void getSignDays(String studentId, String date) {

        mySignerModel.getSignDays(studentId, date, this);
    }

    @Override
    public void getSignDaysDetail(String studentId, String date) {

        mySignerModel.getSignDaysDetail(studentId, date, this);
    }

    @Override
    public void getChatDays(String studentId, String date) {

        mySignerModel.getChatDays(studentId, date, this);
    }

    @Override
    public void getChatDaysDetail(String studentId, String date) {

        mySignerModel.getChatDaysDetail(studentId, date, this);
    }


    /**
     * implementation for callback
     */
    @Override
    public void onGetSignDaysSuccess(ResultResponse<List<String>> response) {

        if (response.getCode().equals("200")) {

            List<String> days = response.getData();
            Map<String, Boolean> note = new HashMap<>();
            for (String d:days){
                note.put(d, true);
            }
            mySignView.onGetDays(note);
        }
        else{
            mySignView.onShowError(response.getMsg());
        }
    }

    @Override
    public void onGetSignDaysError(Throwable e) {

        mySignView.onShowError(e);
    }

    @Override
    public void onGetSignDaysDetailSuccess(ResultResponse<List<MySign>> response) {

        if (response.getCode().equals("200")) {
            List<String> data = new ArrayList<>();
            for (MySign sign : response.getData()) {
                data.add(sign.getConfirmAt().substring(11, 16) + "  " + sign.getCourseName());
            }
            mySignView.onGetDaysDetails(data);
        }
        else{
            mySignView.onShowDetailError(response.getMsg());
        }
    }

    @Override
    public void onGetSignDaysDetailError(Throwable e) {

        mySignView.onShowDetailError(e);
    }

    @Override
    public void onGetChatDaysSuccess(ResultResponse<List<String>> response) {

        if (response.getCode().equals("200")) {

            List<String> days = response.getData();
            Map<String, Boolean> note = new HashMap<>();
            for (String d:days){
                note.put(d, true);
            }
            mySignView.onGetDays(note);
        }
        else{
            mySignView.onShowError(response.getMsg());
        }
    }

    @Override
    public void onGetChatDaysError(Throwable e) {

        mySignView.onShowError(e);
    }

    @Override
    public void onGetChatDaysDetailSuccess(ResultResponse<List<MyChat>> response) {

        if (response.getCode().equals("200")) {
            List<String> data = new ArrayList<>();
            for (MyChat chat : response.getData()) {

                data.add(chat.getCourseName() + "  " + chat.getMsgCount() + "条");
            }
            mySignView.onGetDaysDetails(data);
        }
        else{
            mySignView.onShowDetailError(response.getMsg());
        }
    }

    @Override
    public void onGetChatDaysDetailError(Throwable e) {

        mySignView.onShowDetailError(e);
    }
}
