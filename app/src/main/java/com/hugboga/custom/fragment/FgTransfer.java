package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 接送机
 * Created by Administrator on 2016/3/19.
 */
@ContentView(R.layout.fg_transfer)
public class FgTransfer extends BaseFragment {
    @ViewInject(R.id.daily_tap_line1)
    private View tabLine1;
    @ViewInject(R.id.daily_tap_line2)
    private View tabLine2;
    @ViewInject(R.id.daily_tap_1)
    private TextView tabText1;
    @ViewInject(R.id.daily_tap_2)
    private TextView tabText2;
    //    private FgDailyInTown fgInTown;
//    private FgDailyOutTown fgOutTown;
    private FgPick fgPick;
    private FgSend fgSend;
    private FragmentManager fm;

    @Override
    protected void initHeader() {
        fgRightBtn.setVisibility(View.VISIBLE);
        setProgressState(0);
        fgTitle.setText(getString(R.string.title_transfer));
    }

    @Override
    protected void initView() {
        fgPick = new FgPick();
        fgSend = new FgSend();

        Bundle bundle = new Bundle();
        bundle.putString("umeng_from",umeng_from);
        fgPick.setArguments(bundle);
        fgSend.setArguments(bundle);


        fm = getFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.daily_content, fgPick);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Event({R.id.daily_layout_1, R.id.daily_layout_2,})
    private void onClickView(View view) {
        Bundle bundle;
        FragmentTransaction transaction;
        switch (view.getId()) {
            case R.id.daily_layout_1:
                selectTap(0);
                if (!fgPick.isAdded()) {
                    transaction = fm.beginTransaction();
                    transaction.add(R.id.daily_content, fgPick);
//                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (!fgPick.isVisible()) {
                    transaction = fm.beginTransaction();
                    transaction.hide(fgSend);
                    transaction.show(fgPick);
                    transaction.commit();
                }
                break;
            case R.id.daily_layout_2:
                selectTap(1);
                if (!fgSend.isAdded()) {
                    transaction = fm.beginTransaction();
                    transaction.add(R.id.daily_content, fgSend);
                    transaction.hide(fgPick);
//                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (!fgSend.isVisible()) {
                    transaction = fm.beginTransaction();
                    transaction.hide(fgPick);
                    transaction.show(fgSend);
                    transaction.commit();
                }
                break;

        }
    }

//    @Override
//    protected int getBusinessType() {
//        mBusinessType = Constants.BUSINESS_TYPE_DAILY;
//        return mBusinessType;
//    }

    private void selectTap(int index) {
        if (index == 1) {
            tabLine1.setVisibility(View.GONE);
            tabLine2.setVisibility(View.VISIBLE);
            tabText1.setTextColor(getResources().getColor(R.color.my_content_btn_color));
            tabText2.setTextColor(getResources().getColor(R.color.basic_black));
        } else {
            tabLine1.setVisibility(View.VISIBLE);
            tabLine2.setVisibility(View.GONE);
            tabText1.setTextColor(getResources().getColor(R.color.basic_black));
            tabText2.setTextColor(getResources().getColor(R.color.my_content_btn_color));
        }
    }
}
