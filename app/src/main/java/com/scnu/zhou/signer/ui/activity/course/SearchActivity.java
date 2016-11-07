package com.scnu.zhou.signer.ui.activity.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.MainCourseAdapter;
import com.scnu.zhou.signer.component.adapter.listview.SearchHistoryAdapter;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.component.cache.ACache;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.home.HomePresenter;
import com.scnu.zhou.signer.presenter.home.IHomePresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.listview.PullToRefreshListView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.home.IHomeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * Created by zhou on 16/10/28.
 */
public class SearchActivity extends BaseSlideActivity implements IHomeView, View.OnKeyListener,
        PullToRefreshListView.OnPullToRefreshListener {

    @Bind(R.id.et_search) EditText et_search;
    @Bind(R.id.lv_search_history) ListView lv_search_history;
    @Bind(R.id.plv_search_result) PullToRefreshListView plv_search_result;

    @Bind(R.id.ll_no_result) LinearLayout ll_no_result;
    @Bind(R.id.ll_no_network) LinearLayout ll_no_network;

    private SearchHistoryAdapter historyAdapter;
    private List<String> historys;

    private MainCourseAdapter resultAdapter;
    private List<MainCourse> results;

    private TextView footerView;

    private IHomePresenter presenter;

    private int limit = 10;   // 每页加载数目
    private int page = 0;

    private final int STATE_REFRESH = 0x001;
    private final int STATE_LOADMORE = 0x002;
    private int state = STATE_REFRESH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        initView();
        initData();
    }


    /**
     * implementation for IHomeView
     */
    @Override
    public void initView(){

        et_search.setOnKeyListener(this);
        plv_search_result.setOnPullToRefreshListener(this);
    }

    @Override
    public void initData() {

        presenter = new HomePresenter(this);

        historys = new ArrayList<>();
        historyAdapter = new SearchHistoryAdapter(this, historys);
        results = new ArrayList<>();
        resultAdapter = new MainCourseAdapter(this, results);

        showHistoryList();
    }

    @Override
    public void onGetRelatedCoursesSuccess(ResultResponse<List<MainCourse>> response) {

        if (response.getCode().equals("200")){

            dismissLoadingDialog();

            page++;

            if (state == STATE_REFRESH) {
                results = response.getData();
                plv_search_result.onRefreshCompleted();
            }
            else {
                results.addAll(response.getData());

                if (response.getData().size() < limit){
                    plv_search_result.onLoadMoreAllCompleted();
                }
                else{
                    plv_search_result.onLoadMoreCompleted();
                }
            }

            resultAdapter = new MainCourseAdapter(this, results);
            plv_search_result.setAdapter(resultAdapter);
        }
        else{
            dismissLoadingDialog();
            String msg = response.getMsg();
            ToastView toastView = new ToastView(this, msg);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }


        if (results.size() == 0){
            ll_no_result.setVisibility(View.VISIBLE);
        }
        else{
            ll_no_result.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetRelatedCoursesError(Throwable e) {

        Log.e("error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();

        if (state == STATE_REFRESH) {
            plv_search_result.onRefreshCompleted();
        }
        else{
            plv_search_result.onLoadMoreCompleted();
        }


        if (results.size() == 0){
            ll_no_network.setVisibility(View.VISIBLE);
        }
        else {
            ll_no_network.setVisibility(View.GONE);
        }
    }


    // 搜索词监听
    @OnTextChanged(R.id.et_search)
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (TextUtils.isEmpty(et_search.getText().toString())){

            showHistoryList();
            hideResultList();
        }
    }


    // 添加历史记录ListView底部View
    public void addFooterView(){

        if (historys.size() != 0 && lv_search_history.getFooterViewsCount() == 0) {
            footerView = new TextView(this);
            ViewGroup.LayoutParams parms = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    150);
            footerView.setLayoutParams(parms);
            footerView.setText("清空历史记录");
            footerView.setTextColor(Color.parseColor("#999999"));
            footerView.setTextSize(15);
            footerView.setGravity(Gravity.CENTER);
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ACache.get(SearchActivity.this).put("history", "");
                    historys.clear();
                    lv_search_history.removeFooterView(footerView);
                    showHistoryList();
                }
            });

            lv_search_history.addFooterView(footerView);
        }
    }

    // 取消搜索
    @OnClick(R.id.tv_cancel)
    public void cancel(){
        finish();
    }


    // 取消软键盘
    public void dismissKeyBoard(){

        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {

            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    /**
     * implementation for OnKeyListener
      */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == event.KEYCODE_ENTER) {
            // 执行搜索动作
            Log.e("action", "search >>>>");
            dismissKeyBoard();
            showLoadingDialog("搜索中");
            insertKey();

            hideHistoryList();
            showResultList();
        }
        return false;
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {

        Log.e("state", "refresh");
        state = STATE_REFRESH;
        page = 0;
        results.clear();
        presenter.searchRelatedCourses(UserCache.getInstance().getPhone(this), limit, page,
                et_search.getText().toString());
    }


    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {

        Log.e("state", "load");
        state = STATE_LOADMORE;
        presenter.searchRelatedCourses(UserCache.getInstance().getPhone(this), limit, page,
                et_search.getText().toString());
    }


    /**
     * 刷新超时处理
     */
    @Override
    public void onOutOfTime() {

        ToastView toastView = new ToastView(this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 显示历史搜索
    public void showHistoryList(){

        lv_search_history.setVisibility(View.VISIBLE);

        // 获得最近搜索
        String array = ACache.get(this).getAsString("history");
        if (!TextUtils.isEmpty(array) && !array.equals("null")) historys = new Gson().fromJson(array,
                new TypeToken<List<String>>(){}.getType());
        historyAdapter = new SearchHistoryAdapter(this, historys);
        lv_search_history.setAdapter(historyAdapter);

        addFooterView();
    }

    // 隐藏历史搜索
    public void hideHistoryList(){

        historys.clear();
        historyAdapter.notifyDataSetChanged();
        lv_search_history.setVisibility(View.GONE);
    }

    // 显示搜索结果
    public void showResultList(){

        plv_search_result.setVisibility(View.VISIBLE);
        presenter.searchRelatedCourses(UserCache.getInstance().getPhone(this), 10, 0,
                et_search.getText().toString());
    }

    // 隐藏搜索结果
    public void hideResultList(){

        results.clear();
        resultAdapter.notifyDataSetChanged();
        plv_search_result.setVisibility(View.GONE);
        ll_no_result.setVisibility(View.GONE);
        ll_no_network.setVisibility(View.GONE);
    }


    // 在数据库中保存搜索词
    public void insertKey(){

        int pos = 0;
        String key = et_search.getText().toString();
        for (int i=0; i<historys.size(); i++){

            if (key.equals(historys.get(i))){
                pos = i;
                break;
            }
        }

        if (pos < historys.size()){
            historys.remove(pos);
        }

        for (int i=historys.size(); i>0; i--){
            historys.add(i, historys.get(i-1));
        }
        historys.add(0, key);

        String value = new Gson().toJson(historys);
        Log.e("insert", value);
        ACache.get(this).put("history", value);
    }


    /**
     * 单击事件监听
     */
    @OnItemClick(R.id.lv_search_history)
    public void onHistoryItemClick(AdapterView<?> parent, View view,
                            int position, long id){

        Log.e("action", "search >>>>");
        dismissKeyBoard();
        showLoadingDialog("搜索中");
        et_search.setText(historys.get(position));
        insertKey();

        hideHistoryList();
        showResultList();
    }


    @OnItemClick(R.id.plv_search_result)
    public void onResultItemClick(AdapterView<?> parent, View view,
                            int position, long id){

        if (!plv_search_result.isRefreshing() && position > 0) {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra("title", results.get(position - 1).getName());
            intent.putExtra("courseId", results.get(position - 1).getCourseId());
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }
}
