package com.hugboga.custom.fragment;

import com.hugboga.custom.R;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

/**
 * Created by qingcha on 16/5/31.
 */
@ContentView(R.layout.fg_choose_payment)
public class FgChoosePayment extends BaseFragment {

    @Override
    protected void initHeader() {
        fgTitle.setText(getString(R.string.guide_detail_subtitle_title));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }
}
