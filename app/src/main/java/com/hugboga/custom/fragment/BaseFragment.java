package com.hugboga.custom.fragment;

import android.os.Bundle;

import com.hugboga.custom.R;



public abstract class BaseFragment extends com.huangbaoche.hbcframe.fragment.BaseFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentId = R.id.drawer_layout;
    }


}
