package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 17/5/17.
 */
public class FgPickup extends BaseFragment{

    public static final String TAG = FgPickup.class.getSimpleName();

    @Override
    public int getContentViewId() {
        return R.layout.fg_pickup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    protected void initView() {

    }

}
