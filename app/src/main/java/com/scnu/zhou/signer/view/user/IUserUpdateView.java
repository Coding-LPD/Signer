package com.scnu.zhou.signer.view.user;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.Student;

/**
 * Created by zhou on 16/10/20.
 */
public interface IUserUpdateView {

    void initView();
    void initData();

    void onUpdateStudentInfoSuccess(ResultResponse<Student> response);
    void onUpdateStudentInfoError(Throwable e);
}
