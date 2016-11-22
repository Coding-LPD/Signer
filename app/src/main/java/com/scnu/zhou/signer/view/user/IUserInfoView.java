package com.scnu.zhou.signer.view.user;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.ActiveInfo;
import com.scnu.zhou.signer.component.bean.user.Student;

import java.util.List;

/**
 * Created by zhou on 16/10/20.
 */
public interface IUserInfoView {

    void initView();
    void initData();
    void loadData();

    void onGetStudentInfoSuccess(ResultResponse<List<Student>> response);
    void onGetStudentInfoError(Throwable e);

    void onGetActivtInfoSuccess(ResultResponse<ActiveInfo> response);
    void onGetActivtInfoError(Throwable e);

    void onUpdateStudentInfoSuccess(ResultResponse<Student> response);
    void onUpdateStudentInfoError(Throwable e);
}
