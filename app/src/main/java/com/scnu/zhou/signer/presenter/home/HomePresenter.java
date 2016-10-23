package com.scnu.zhou.signer.presenter.home;

import com.scnu.zhou.signer.callback.home.HomeCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.model.home.HomeModel;
import com.scnu.zhou.signer.model.home.IHomeModel;
import com.scnu.zhou.signer.view.home.IHomeView;

import java.util.List;

/**
 * Created by zhou on 16/10/23.
 */
public class HomePresenter implements  IHomePresenter, HomeCallBack {

    private IHomeView homeView;
    private IHomeModel homeModel;

    public HomePresenter(IHomeView homeView){

        this.homeView = homeView;
        this.homeModel = new HomeModel();
    }

    @Override
    public void getRelatedCourses(String phone, int limit, int page) {

        homeModel.getRelatedCourses(phone, limit, page, this);
    }

    @Override
    public void onGetRelatedCoursesSuccess(ResultResponse<List<MainCourse>> response) {

        homeView.onGetRelatedCoursesSuccess(response);
    }

    @Override
    public void onGetRelatedCoursesError(Throwable e) {

        homeView.onGetRelatedCoursesError(e);
    }
}
