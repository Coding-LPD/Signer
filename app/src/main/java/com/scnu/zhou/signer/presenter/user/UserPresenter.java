package com.scnu.zhou.signer.presenter.user;

import com.scnu.zhou.signer.callback.user.UserInfoCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.Student;
import com.scnu.zhou.signer.model.user.IUserModel;
import com.scnu.zhou.signer.model.user.UserModel;
import com.scnu.zhou.signer.view.user.IUserHeaderView;
import com.scnu.zhou.signer.view.user.IUserInfoView;
import com.scnu.zhou.signer.view.user.IUserUpdateView;

import java.io.File;
import java.util.List;

/**
 * Created by zhou on 16/10/20.
 */
public class UserPresenter implements IUserPresenter, UserInfoCallBack{

    private IUserModel userModel;
    private IUserInfoView userInfoView;
    private IUserUpdateView userUpdateView;
    private IUserHeaderView userHeaderView;

    private int ViewType = 1;    // 1为IUserInfoView, 2为IUserUpdateView, 3为IUserHeaderView

    public UserPresenter(IUserInfoView userInfoView){

        this.userModel = new UserModel();
        this.userInfoView = userInfoView;
        this.ViewType = 1;
    }

    public UserPresenter(IUserUpdateView userUpdateView){

        this.userModel = new UserModel();
        this.userUpdateView = userUpdateView;
        this.ViewType = 2;
    }

    public UserPresenter(IUserHeaderView userHeaderView){

        this.userModel = new UserModel();
        this.userHeaderView = userHeaderView;
        this.ViewType = 3;
    }

    @Override
    public void getStudentInfo(String phone) {

        userModel.getStudentInfo(phone, this);
    }

    @Override
    public void updateStudentInfo(String userid, String key, String value) {

        userModel.updateStudentInfo(userid, key, value, this);
    }

    @Override
    public void getDefaultImageUrl(int pos) {

        userModel.getDefaultImageUrl(pos, this);
    }

    @Override
    public void uploadStudentAvatar(File file) {

        userModel.uploadStudentAvatar(file, this);
    }


    @Override
    public void onGetSuccess(ResultResponse<List<Student>> response) {

        userInfoView.onGetStudentInfoSuccess(response);
    }

    @Override
    public void onGetError(Throwable e) {

        userInfoView.onGetStudentInfoError(e);
    }

    @Override
    public void onUpdateSuccess(ResultResponse<Student> response) {

        if (ViewType == 1) {
            userInfoView.onUpdateStudentInfoSuccess(response);
        }
        else if (ViewType == 2){
            userUpdateView.onUpdateStudentInfoSuccess(response);
        }
        else if (ViewType == 3){
            userHeaderView.onUpdateAvatarSuccess(response);
        }
    }

    @Override
    public void onUpdateError(Throwable e) {

        if (ViewType == 1) {
            userInfoView.onUpdateStudentInfoError(e);
        }
        else if (ViewType == 2){
            userUpdateView.onUpdateStudentInfoError(e);
        }
        else if (ViewType == 3){
            userHeaderView.onUpdateAvatarError(e);
        }
    }

    @Override
    public void onGetImageSuccess(ResultResponse<String> response) {

        userHeaderView.onGetDefaultImageSuccess(response);
    }

    @Override
    public void onGetImageError(Throwable e) {

        userHeaderView.onGetDefaultImageError(e);
    }

    @Override
    public void onUploadImageSuccess(ResultResponse<String> response) {

        userHeaderView.onUploadAvatarSuccess(response);
    }

    @Override
    public void onUploadImageError(Throwable e) {

        userHeaderView.onUploadAvatarError(e);
    }
}
