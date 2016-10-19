package com.scnu.zhou.signer.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.ui.activity.base.BaseFragmentActivity;
import com.scnu.zhou.signer.ui.activity.sign.CaptureActivity;
import com.scnu.zhou.signer.ui.activity.sign.ConfirmSignActivity;
import com.scnu.zhou.signer.component.adapter.HomePagerAdapter;
import com.scnu.zhou.signer.ui.fragment.DiscoverFragment;
import com.scnu.zhou.signer.ui.fragment.HomeFragment;
import com.scnu.zhou.signer.ui.fragment.MineFragment;
import com.scnu.zhou.signer.ui.fragment.NoticeFragment;
import com.scnu.zhou.signer.component.util.tabbar.TabBarManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

public class MainActivity extends BaseFragmentActivity{

    @Bind(R.id.vp_main) ViewPager vp_main;

    private List<Fragment> fragments;
    private HomePagerAdapter adapter;

    private TabBarManager manager;
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        instance = this;
        initData();
        initView();
    }

    public static MainActivity getInstance(){
        return instance;
    }

    @Override
    public void initView() {

        initViewPager();
    }

    @Override
    public void initData() {

        manager = new TabBarManager(this);
        manager.setSelect(0);
    }


    private void initViewPager(){

        fragments = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        DiscoverFragment discoverFragment = new DiscoverFragment();
        NoticeFragment noticeFragment = new NoticeFragment();
        MineFragment mineFragment = new MineFragment();

        fragments.add(homeFragment);
        fragments.add(discoverFragment);
        fragments.add(noticeFragment);
        fragments.add(mineFragment);

        adapter = new HomePagerAdapter(getSupportFragmentManager(), fragments);
        vp_main.setAdapter(adapter);
        //vp_main.setOnPageChangeListener(this);
    }


    @Override
    public void loadData() {

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
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }
}
