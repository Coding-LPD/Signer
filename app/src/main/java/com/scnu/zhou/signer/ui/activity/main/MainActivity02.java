package com.scnu.zhou.signer.ui.activity.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.pager.HomePagerAdapter;
import com.scnu.zhou.signer.component.config.SocketClient;
import com.scnu.zhou.signer.component.util.tabbar.TabBarManager02;
import com.scnu.zhou.signer.ui.activity.base.BaseFragmentActivity;
import com.scnu.zhou.signer.ui.fragment.ChatFragment;
import com.scnu.zhou.signer.ui.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by zhou on 16/11/24.
 */
public class MainActivity02 extends BaseFragmentActivity {

    @Bind(R.id.vp_main) ViewPager vp_main;

    private List<Fragment> fragments;
    private HomePagerAdapter adapter;

    private TabBarManager02 manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main02);

        startService();

        ButterKnife.bind(this);
        manager = new TabBarManager02(this);
        manager.setSelect(0);

        initViewPager();
    }

    private void initViewPager(){

        fragments = new ArrayList<>();
        ChatFragment chatFragment = new ChatFragment();
        MineFragment mineFragment = new MineFragment();

        fragments.add(chatFragment);
        fragments.add(mineFragment);

        adapter = new HomePagerAdapter(getSupportFragmentManager(), fragments);
        vp_main.setAdapter(adapter);
        //vp_main.setOnPageChangeListener(this);
    }

    /**
     * 启动Service
     */
    private void startService(){

        SocketClient.getInstance().startSocketClient(this);
    }


    // 点击聊天室tab
    @OnClick(R.id.ll_discover)
    public void clickDiscoverTab(){
        manager.setSelect(0);
        vp_main.setCurrentItem(0, true);
    }


    // 点击我的tab
    @OnClick(R.id.ll_mine)
    public void clickMineTab(){
        manager.setSelect(1);
        vp_main.setCurrentItem(1, true);
    }

    // Pager滑动监听
    @OnPageChange(R.id.vp_main)
    public void onPageSelected(int position){
        manager.setSelect(position);
    }
}
