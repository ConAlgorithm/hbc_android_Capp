package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.PayResultView;

import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/9/6.
 */
@ContentView(R.layout.fg_payresult)
public class FgPayResult extends BaseFragment{

    @Bind(R.id.fg_result_view)
    PayResultView payResultView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView() {
        fgTitle.setText(getString(R.string.par_result_title));
        fgLeftBtn.setOnClickListener(null);
        fgLeftBtn.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams titleLeftBtnParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(10), RelativeLayout.LayoutParams.MATCH_PARENT);
        fgLeftBtn.setLayoutParams(titleLeftBtnParams);
    }

    public void initView(boolean _isPaySucceed, String _orderId) {
        payResultView.initView(_isPaySucceed, _orderId);
    }

}
