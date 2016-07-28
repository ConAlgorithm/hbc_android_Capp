package com.hugboga.custom.action;

import android.app.Activity;
import android.os.Bundle;

import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.fragment.FgActivity;
import com.hugboga.custom.fragment.FgChooseCityNew;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.fragment.FgWebInfo;
import com.hugboga.custom.utils.CommonUtils;

import java.util.Random;

/**
 * Created by qingcha on 16/7/27.
 */
public class ActionFactory implements ActionFactoryBehavior {

    private BaseFragment fragment;
    private BaseActivity activity;

    public ActionFactory(BaseFragment _fragment) {
        this.fragment = _fragment;
        this.activity = (BaseActivity) fragment.getActivity();
    }

    public ActionFactory(BaseActivity _activity) {
        this.activity = _activity;
    }

    @Override
    public void doAction(final ActionBean _actionBaseBean) {
        if (_actionBaseBean == null) {
            return;
        }
        switch (CommonUtils.getCountInteger(_actionBaseBean.type)) {
            case ActionType.WEB_ACTIVITY:
                if (activity == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, _actionBaseBean.url);
                activity.startFragment(new FgActivity(), bundle);
                break;
            case ActionType.NATIVE_PAGE:
                intentPage(_actionBaseBean);
                break;
            case ActionType.FUNCTION:
                doFunction(_actionBaseBean);
                break;
            default:
                break;
        }
    }

    @Override
    public void intentPage(ActionBean _actionBaseBean) {
        if (activity == null) {
            return;
        }
        Bundle bundle = new Bundle();
        switch (CommonUtils.getCountInteger(_actionBaseBean.vcid)) {
            case ActionType.PageType.WEBVIEW:
                bundle.putString(FgWebInfo.WEB_URL, _actionBaseBean.vcid);
                activity.startFragment(new FgActivity(), bundle);
                break;
            case ActionType.PageType.HOME:
                if (fragment != null) {
                    fragment.bringToFront(FgHome.class, new Bundle());
                }
                break;
            case ActionType.PageType.SEARCH:
                bundle.putInt("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
                bundle.putString("source","小搜索按钮");
                activity.startFragment(new FgChooseCityNew(), bundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void doFunction(ActionBean _actionBaseBean) {
        switch (CommonUtils.getCountInteger(_actionBaseBean.vcid)) {
            case ActionType.FunctionType.LOGOUT:

                break;
            case ActionType.FunctionType.UPDATE:

                break;
            case ActionType.FunctionType.EXTENSION:

                break;
            default:
                break;
        }
    }
}
