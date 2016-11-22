package com.scnu.zhou.signer.presenter.user;

import com.scnu.zhou.signer.callback.user.UserInfoCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.Student;
import com.scnu.zhou.signer.component.bean.user.User;
import com.scnu.zhou.signer.model.user.IUserModel;
import com.scnu.zhou.signer.model.user.UserModel;
import com.scnu.zhou.signer.view.user.IUserHeaderView;
import com.scnu.zhou.signer.view.user.IUserInfoView;
import com.scnu.zhou.signer.view.user.IUserPasswordView;
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
    private IUserPasswordView userPasswordView;

    private int ViewType = 1;    // 1为IUserInfoView, 2为IUserUpdateView, 3为IUserHeaderView, 4为IPasswordwordView

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

    public UserPresenter(IUserPasswordView userPasswordView){

        this.userModel = new UserModel();
        this.userPasswordView = userPasswordView;
        this.ViewType = 4;
    }

    @Override
    public void getPublicKey() {
        userModel.getPublicKey(this);
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
    public void updateUserPassword(String phone) {

    }



    /**
     * userInfoView
     */
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


    /**
     * userHeaderView
     */
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


    /**
     * userPasswordView
     */
    @Override
    public void onGetPublicKeySuccess(ResultResponse<String> response) {

        userPasswordView.onGetPublicKeySuccess(response);
    }

    @Override
    public void onGetPublicKeyError(Throwable e) {

        userPasswordView.onGetPublicKeyError(e);
    }

    @Override
    public void onUpdatePasswordSuccess(ResultResponse<User> response) {

        userPasswordView.onUpdatePasswordSuccess(response);
    }

    @Override
    public void onUpdatePasswordError(Throwable e) {

        userPasswordView.onUpdatePasswordError(e);
    }
}
