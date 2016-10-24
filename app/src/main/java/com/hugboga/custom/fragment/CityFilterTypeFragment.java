package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.R;

import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/22.
 */
@ContentView(R.layout.city_filiter_list_fragment)
public class CityFilterTypeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

//    @Override
//    protected void initView() {
//        fgTitle.setText(getString(R.string.par_result_title));
//        fgLeftBtn.setOnClickListener(null);
//        fgLeftBtn.setVisibility(View.INVISIBLE);
//        RelativeLayout.LayoutParams titleLeftBtnParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(10), RelativeLayout.LayoutParams.MATCH_PARENT);
//        fgLeftBtn.setLayoutParams(titleLeftBtnParams);
//    }
//
//    public void initView(boolean _isPaySucceed, String _orderId) {
//        this.isPaySucceed = _isPaySucceed;
//        payResultView.initView(_isPaySucceed, _orderId);
//    }

//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//            if (isPaySucceed) {
//                payResultView.intentHome();
//                return true;
//            } else {
//                payResultView.setStatisticIsRePay(true);
//            }
//        }
//        return false;
//    }
}
