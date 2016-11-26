package com.scnu.zhou.signer.presenter.home;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scnu.zhou.signer.callback.home.HomeCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.component.cache.ACache;
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

        homeModel.getRelatedCourses(phone, limit, page, "", this);
    }

    @Override
    public void searchRelatedCourses(String phone, int limit, int page, String keyword) {

        homeModel.getRelatedCourses(phone, limit, page, keyword, this);
    }

    @Override
    public void setCourseCache(Context context, List<MainCourse> mData) {

        // 保存最近访问课程信息
        String value = new Gson().toJson(mData);
        //Log.e("put-array", value);
        ACache.get(context).put("course", value);
    }

    @Override
    public List<MainCourse> getCourseCache(Context context) {

        // 获得最近访问课程信息
        String array = ACache.get(context).getAsString("course");
        //Log.e("get-array", array);
        if (!TextUtils.isEmpty(array) && !array.equals("null")){
            List<MainCourse> mData = new Gson().fromJson(array,
                    new TypeToken<List<MainCourse>>(){}.getType());
            return mData;
        }
        else{
            return null;
        }
    }

    @Override
    public void setSearchHistoryCache(Context context, List<String> mData) {

        // 保存搜索记录
        String value = new Gson().toJson(mData);
        //Log.e("insert", value);
        ACache.get(context).put("history", value);
    }

    @Override
    public List<String> getSrearchHistoryCache(Context context) {

        // 获得最近搜索
        String array = ACache.get(context).getAsString("history");
        if (!TextUtils.isEmpty(array) && !array.equals("null")) {
            List<String> historys = new Gson().fromJson(array,
                    new TypeToken<List<String>>(){}.getType());
            return historys;
        }
        else{
            return null;
        }
    }

    @Override
    public void clearSearchHistoryCache(Context context) {

        // 清除历史搜索记录
        ACache.get(context).put("history", "");
    }


    /**
     * implementation for HomeCallBack
     * @param response
     */
    @Override
    public void onGetRelatedCoursesSuccess(ResultResponse<List<MainCourse>> response) {

        if (response.getCode().equals("200")) {
            homeView.onGetRelatedCoursesSuccess(response.getData());
        }
        else{
            homeView.onGetRelatedCoursesError(response.getMsg());
        }
    }

    @Override
    public void onGetRelatedCoursesError(Throwable e) {

        homeView.onGetRelatedCoursesError(e);
    }
}
