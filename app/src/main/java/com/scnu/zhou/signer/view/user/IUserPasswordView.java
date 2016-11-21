package com.scnu.zhou.signer.view.user;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.Student;

import java.util.List;

/**
 * Created by zhou on 16/10/20.
 */
public interface IUserPasswordView {

    void initView();
    void initData();

    void onGetUserIdSuccess(ResultResponse<List<Student>> response);
    void onGetUserIdError(Throwable e);

    void onGetPublicKeySuccess(ResultResponse<String> response);
    void onGetPublicKeyError(Throwable e);

    void onUpdateStudentPasswordSuccess(ResultResponse<Student> response);
    void onUpdateStudentPasswordError(Throwable e);
}
