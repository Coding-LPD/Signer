package com.scnu.zhou.signer.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.pager.HomePagerAdapter;
import com.scnu.zhou.signer.component.cache.NoticeCache;
import com.scnu.zhou.signer.component.config.SocketClient;
import com.scnu.zhou.signer.component.util.tabbar.TabBarManager;
import com.scnu.zhou.signer.ui.activity.base.BaseFragmentActivity;
import com.scnu.zhou.signer.ui.activity.sign.CaptureActivity;
import com.scnu.zhou.signer.ui.activity.sign.ConfirmSignActivity;
import com.scnu.zhou.signer.ui.fragment.ChatFragment;
import com.scnu.zhou.signer.ui.fragment.HomeFragment;
import com.scnu.zhou.signer.ui.fragment.MineFragment;
import com.scnu.zhou.signer.ui.fragment.NoticeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

public class MainActivity extends BaseFragmentActivity{

    @Bind(R.id.vp_main) ViewPager vp_main;
    @Bind(R.id.tv_tab_warning) TextView tv_tab_warning;

    private List<Fragment> fragments;
    private HomePagerAdapter adapter;

    private TabBarManager manager;
    private static MainActivity instance;

    private Handler handler;
    private class NoticeRunnable implements Runnable{

        @Override
        public void run() {

            int num = NoticeCache.getInstance().getNoticenNumber(MainActivity.this);
            if (num == 0){
                tv_tab_warning.setVisibility(View.GONE);
            }

            handler.postDelayed(this, 500);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService();

        ButterKnife.bind(this);
        instance = this;

        manager = new TabBarManager(this);
        manager.setSelect(0);
        initViewPager();

        handler = new Handler();
        handler.postDelayed(new NoticeRunnable(), 500);

    }

    public static MainActivity getInstance(){
        return instance;
    }

    private void initViewPager(){

        fragments = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        ChatFragment chatFragment = new ChatFragment();
        NoticeFragment noticeFragment = new NoticeFragment();
        MineFragment mineFragment = new MineFragment();

        fragments.add(homeFragment);
        fragments.add(chatFragment);
        fragments.add(noticeFragment);
        fragments.add(mineFragment);

        adapter = new HomePagerAdapter(getSupportFragmentManager(), fragments);
        vp_main.setAdapter(adapter);
        //vp_main.setOnPageChangeListener(this);
    }


    /**
     * 启动Service
     */
    private void startService(){

        //Intent intent = new Intent(this, NoticeService.class);
        //startService(intent);
        //bindService(intent, connection, BIND_AUTO_CREATE);

        SocketClient.getInstance().startSocketClient(this);
        SocketClient.getInstance().setOnNoticeReceiveListener(new SocketClient.OnNoticeReceiveListener() {
            @Override
            public void onNoticeReceive() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // 有新通知到达
                        int num = NoticeCache.getInstance().getNoticenNumber(MainActivity.this);
                        if (num != 0){
                            tv_tab_warning.setVisibility(View.VISIBLE);

                            if (num < 99) {
                                tv_tab_warning.setText(num + "");
                            }
                            else{
                                tv_tab_warning.setText("99");
                            }
                        }
                    }
                });
            }
        });
    }


    // 点击首页tab
    @OnClick(R.id.ll_home)
    public void clickHomeTab(){
        manager.setSelect(0);
        vp_main.setCurrentItem(0, true);
    }


    // 点击发现tab
    @OnClick(R.id.ll_discover)
    public void clickDiscoverTab(){
        manager.setSelect(1);
        vp_main.setCurrentItem(1, true);
    }


    // 点击通知tab
    @OnClick(R.id.ll_notice)
    public void clickNoticeTab(){
        manager.setSelect(2);
        vp_main.setCurrentItem(2, true);
    }


    // 点击我的tab
    @OnClick(R.id.ll_mine)
    public void clickMineTab(){
        manager.setSelect(3);
        vp_main.setCurrentItem(3, true);
    }


    // 点击签到

    @OnClick(R.id.btn_sign)
    public void clickToSign(){

        //打开扫描界面扫描二维码
        Intent openCameraIntent = new Intent(this,CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    // Pager滑动监听
    @OnPageChange(R.id.vp_main)
    public void onPageSelected(int position){
        manager.setSelect(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            //Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ConfirmSignActivity.class);
            intent.putExtra("code", scanResult);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unbindService(connection);
        SocketClient.getInstance().clearOnNoticeReceiveListener();
    }
}
