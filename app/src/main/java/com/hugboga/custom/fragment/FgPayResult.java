package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.PayResultView;

import butterknife.Bind;

/**
 * Created by qingcha on 16/9/6.
 */
public class FgPayResult extends BaseFragment{

    @Bind(R.id.fg_result_view)
    PayResultView payResultView;

    private boolean isPaySucceed;

    @Override
    public int getContentViewId() {
        return R.layout.fg_payresult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
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

    public void initView(boolean _isPaySucceed, String _orderId, int orderType) {
        this.isPaySucceed = _isPaySucceed;
        payResultView.initView(_isPaySucceed, _orderId, orderType);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (isPaySucceed) {
                payResultView.intentHome();
                return true;
            } else {
                payResultView.setStatisticIsRePay(true);
            }
        }
        return false;
    }
}
