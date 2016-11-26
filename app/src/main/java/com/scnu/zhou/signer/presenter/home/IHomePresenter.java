package com.scnu.zhou.signer.presenter.home;

import android.content.Context;

import com.scnu.zhou.signer.component.bean.main.MainCourse;

import java.util.List;

/**
 * Created by zhou on 16/10/23.
 */
public interface IHomePresenter {

    void getRelatedCourses(String phone, int limit, int page);

    void searchRelatedCourses(String phone, int limit, int page, String keyword);

    void setCourseCache(Context context, List<MainCourse> mData);

    List<MainCourse> getCourseCache(Context context);

    void setSearchHistoryCache(Context context, List<String> mData);

    List<String> getSrearchHistoryCache(Context context);

    void clearSearchHistoryCache(Context context);
}
