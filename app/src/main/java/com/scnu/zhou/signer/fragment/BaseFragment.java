package com.scnu.zhou.signer.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by zhou on 16/9/6.
 */
public abstract class BaseFragment extends Fragment{

    public abstract void initView();

    public abstract void initData();

    public abstract void loadData();
}
