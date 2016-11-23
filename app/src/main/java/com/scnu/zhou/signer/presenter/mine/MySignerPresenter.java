package com.scnu.zhou.signer.presenter.mine;

import com.scnu.zhou.signer.callback.mine.MySignerCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.mine.MySign;
import com.scnu.zhou.signer.model.mine.IMySignerModel;
import com.scnu.zhou.signer.model.mine.MySignerModel;
import com.scnu.zhou.signer.view.mine.IMySignView;

import java.util.List;

/**
 * Created by zhou on 16/11/23.
 */
public class MySignerPresenter implements IMySignerPresenter, MySignerCallBack {

    private IMySignerModel mySignerModel;
    private IMySignView mySignView;

    public MySignerPresenter(IMySignView mySignView){

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


    /**
     * implementation for callback
     */
    @Override
    public void onGetSignDaysSuccess(ResultResponse<List<String>> response) {

        mySignView.onGetSignDaysSuccess(response);
    }

    @Override
    public void onGetSignDaysError(Throwable e) {

        mySignView.onGetSignDaysError(e);
    }

    @Override
    public void onGetSignDaysDetailSuccess(ResultResponse<List<MySign>> response) {

        mySignView.onGetSignDaysDetailSuccess(response);
    }

    @Override
    public void onGetSignDaysDetailError(Throwable e) {

        mySignView.onGetSignDaysDetailError(e);
    }
}
