package com.scnu.zhou.signer.ui.widget.listview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/10/21.
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener{

    private View HeadView = null;
    private View FootView = null;

    private RotateAnimation upAnimation, downAnimation;
    private RotateAnimation loadingAnimation;

    private int headHeight = 0;
    private int footHeight = 0;
    private int startY = 0;

    private final int STATE_PULL_DOWN = 0x001;
    private final int STATE_PULL_RELEASE = 0x002;
    private final int STATE_REFRESHING = 0x003;

    private final static float RATIO = 1.5f;    // 阻尼系数

    private int state = STATE_PULL_DOWN;

    private TextView tv_tip;
    private ImageView iv_arrow, iv_loading;
    private ImageView iv_bloading;   // 底部加载
    private LinearLayout ll_bottom;

    private OnPullToRefreshListener listener;

    private int firstVisibleItem = 0;
    private boolean isScrollToBottom = false;

    private boolean isLoadingMore = false;
    private boolean isCompleted = false;


    private int newMaxOverScrollY;
    // 这个值控制可以把ListView拉出偏离顶部或底部的距离。
    private final int MAX_OVERSCROLL_Y = 100;

    private final int OUT_OF_TIME = 0x000;
    private final int END_IN_TIME = 0x004;

    private final int END_TIME = 100;
    private int refresh_time = END_TIME;
    private int loadmore_time = END_TIME;

    public PullToRefreshListView(Context context) {
        super(context);
        initView(context);
        initData(context);
        initAnimation();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(context);
        initAnimation();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData(context);
        initAnimation();
    }

    private void initView(Context context){

        HeadView = LayoutInflater.from(context).inflate(R.layout.listview_headview, null);
        this.addHeaderView(HeadView);

        FootView = LayoutInflater.from(context).inflate(R.layout.listview_footview, null);
        this.addFooterView(FootView);

        HeadView.measure(0, 0);
        headHeight = HeadView.getMeasuredHeight();
        HeadView.setPadding(0, - headHeight, 0, 0);

        FootView.measure(0, 0);
        footHeight = FootView.getMeasuredHeight();
        FootView.setPadding(0, - footHeight, 0, 0);

        tv_tip = (TextView) HeadView.findViewById(R.id.tv_tip);
        iv_arrow = (ImageView) HeadView.findViewById(R.id.iv_arrow);
        iv_loading = (ImageView) HeadView.findViewById(R.id.iv_loading);

        iv_bloading = (ImageView) FootView.findViewById(R.id.iv_bloading);
        ll_bottom = (LinearLayout) FootView.findViewById(R.id.ll_bottom);

        this.setOnScrollListener(this);
    }

    private void initData(Context context){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;
        newMaxOverScrollY = (int) (density * MAX_OVERSCROLL_Y);
    }

    private void initAnimation(){
        upAnimation = new RotateAnimation(0f, 180f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        upAnimation.setInterpolator(new LinearInterpolator());
        upAnimation.setDuration(100);
        upAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

        downAnimation = new RotateAnimation(180f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        downAnimation.setInterpolator(new LinearInterpolator());
        downAnimation.setDuration(100);
        downAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上


        loadingAnimation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        loadingAnimation.setInterpolator(new LinearInterpolator());
        loadingAnimation.setRepeatCount(-1);
        loadingAnimation.setDuration(1200);
        loadingAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                // 移动中的y - 按下的y = 间距.
                int diff = (int) (((float)moveY - (float)startY) / RATIO);
                // -头布局的高度 + 间距 = paddingTop
                int paddingTop = - headHeight + diff;

                if (firstVisibleItem == 0
                        && - headHeight < paddingTop) {
                    if (state != STATE_REFRESHING) {
                        if (paddingTop > 0 && state == STATE_PULL_DOWN) {

                            state = STATE_PULL_RELEASE;
                            refreshHeaderView();

                        } else if (paddingTop < 0 && state == STATE_PULL_RELEASE) {
                            state = STATE_PULL_DOWN;
                            refreshHeaderView();
                        }

                        HeadView.setPadding(0, paddingTop, 0, 0);
                        //return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state == STATE_PULL_RELEASE){    // 是否达到松开刷新的条件
                    HeadView.setPadding(0, 0, 0, 0);

                    state = STATE_REFRESHING;

                    refreshHeaderView();

                    if (listener != null) {
                        listener.onRefresh();    // 执行下拉刷新动作
                    }
                }
                else if (state == STATE_PULL_DOWN){
                    HeadView.setPadding(0, - headHeight, 0, 0);
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void refreshHeaderView(){

        switch (state){
            case STATE_PULL_RELEASE:
                tv_tip.setText("释放更新");
                iv_arrow.startAnimation(upAnimation);
                break;
            case STATE_PULL_DOWN:
                tv_tip.setText("下拉刷新");
                iv_arrow.startAnimation(downAnimation);
                break;
            case STATE_REFRESHING:
                iv_arrow.clearAnimation();
                iv_loading.startAnimation(loadingAnimation);
                iv_arrow.setVisibility(GONE);
                iv_loading.setVisibility(VISIBLE);
                tv_tip.setText("正在加载 ...");

                isCompleted = false;
                iv_bloading.clearAnimation();
                FootView.setPadding(0, - footHeight, 0, 0);

                onRefrshTimeWatch();    // 下拉刷新时间监控
                break;
        }

    }

    public void setOnPullToRefreshListener(OnPullToRefreshListener listener){

        this.listener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {
            // 判断当前是否已经到了底部
            if (isScrollToBottom && !isLoadingMore && !isCompleted && state != STATE_REFRESHING) {
                isLoadingMore = true;
                // 当前到底部
                Log.e("load", "加载更多数据");
                FootView.setPadding(0, 0, 0, 0);
                iv_bloading.setVisibility(VISIBLE);
                iv_bloading.startAnimation(loadingAnimation);
                ll_bottom.setVisibility(GONE);
                this.setSelection(this.getCount());

                if (listener != null) {
                    listener.onLoadMore();
                }

                onLoadMoreTimeWatch();
            }
        }
    }


    /**
     * 当滚动时调用
     *
     * @param firstVisibleItem
     *            当前屏幕显示在顶部的item的position
     * @param visibleItemCount
     *            当前屏幕显示了多少个条目的总数
     * @param totalItemCount
     *            ListView的总条目的总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        this.firstVisibleItem = firstVisibleItem;

        if (firstVisibleItem != 0) {
            if (getLastVisiblePosition() == totalItemCount - 1) {
                isScrollToBottom = true;
            } else {
                isScrollToBottom = false;
            }
        }
    }

    public interface OnPullToRefreshListener{

        void onRefresh();    // 下拉刷新
        void onLoadMore();     // 上拉分页
        void onOutOfTime();    // 超时处理
    }


    /**
     * 下拉刷新加载完毕
     */
    public void onRefreshCompleted(){

        hideHeaderView();

        Message msg = new Message();
        msg.what = END_IN_TIME;
        RefreshHandler.sendMessage(msg);
    }


    /**
     * 上拉加载加载完毕
     */
    public void onLoadMoreCompleted(){

        hideFooterView();

        Message msg = new Message();
        msg.what = END_IN_TIME;
        LoadMoreHandler.sendMessage(msg);
    }


    /**
     * 数据全部加载完毕
     */
    public void onLoadMoreAllCompleted(){

        isLoadingMore = false;
        isCompleted = true;
        iv_bloading.clearAnimation();
        iv_bloading.setVisibility(GONE);
        ll_bottom.setVisibility(VISIBLE);

        Log.e("state", "complete");
        FootView.setPadding(0, 0, 0, 0);

        Message msg = new Message();
        msg.what = END_IN_TIME;
        LoadMoreHandler.sendMessage(msg);
    }


    private void hideHeaderView(){
        //HeadView.setPadding(0, - headHeight, 0, 0);

        state = STATE_PULL_DOWN;

        // HeadView回收动画
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, - headHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int currentValue = (int) animation.getAnimatedValue();
                HeadView.setPadding(0, currentValue, 0, 0);

                if (currentValue == - headHeight){
                    iv_loading.clearAnimation();
                    iv_arrow.setVisibility(VISIBLE);
                    iv_loading.setVisibility(GONE);
                    tv_tip.setText("下拉刷新");
                }
            }
        });
        valueAnimator.setDuration(500).start();
    }


    private void hideFooterView(){
        isLoadingMore = false;
        iv_bloading.clearAnimation();
        FootView.setPadding(0, - footHeight, 0, 0);
    }


    //支持到SDK8需要增加@SuppressLint("NewApi")。
    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX, newMaxOverScrollY,
                isTouchEvent);
    }


    /**
     * 下拉刷新时间监控
     */
    private Handler RefreshHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == OUT_OF_TIME){

                if (listener != null){
                    listener.onOutOfTime();
                }

                hideHeaderView();
            }
            else if (msg.what == END_IN_TIME){

                refresh_time = END_TIME;
            }
            super.handleMessage(msg);
        }
    };

    private void onRefrshTimeWatch(){

        refresh_time = 10;
        RefreshHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (refresh_time != END_TIME) {

                    if (refresh_time > 0) {
                        refresh_time--;
                        RefreshHandler.postDelayed(this, 1000);
                    } else {
                        Message msg = new Message();
                        msg.what = OUT_OF_TIME;
                        RefreshHandler.sendMessage(msg);
                    }
                }
            }
        }, 1000);
    }

    /**
     * 上拉加载时间监控
     */
    private Handler LoadMoreHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == OUT_OF_TIME){

                if (listener != null){
                    listener.onOutOfTime();
                }

                hideFooterView();
            }
            else if (msg.what == END_IN_TIME){   // 规定时间内加载完成

                loadmore_time = END_TIME;
            }
            super.handleMessage(msg);
        }
    };


    private void onLoadMoreTimeWatch(){

        loadmore_time = 10;
        LoadMoreHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (loadmore_time != END_TIME) {
                    if (loadmore_time > 0) {
                        loadmore_time--;
                        LoadMoreHandler.postDelayed(this, 1000);
                    } else {
                        Message msg = new Message();
                        msg.what = OUT_OF_TIME;
                        LoadMoreHandler.sendMessage(msg);
                    }
                }
            }
        }, 1000);
    }


    public boolean isRefreshing(){

        if (state == STATE_REFRESHING || isLoadingMore){
            return true;
        }
        else{
            return false;
        }
    }
}
