package com.hugboga.custom.action;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.activity.CouponActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.activity.TravelFundRecordActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.PushMessage;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;

import java.util.Random;

/**
 * Created by on 16/7/27.
 */
public class ActionFactory implements ActionFactoryBehavior {

    private BaseActivity activity;

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
                intentWebInfoActivity(_actionBaseBean.url);
                break;
            case ActionType.NATIVE_PAGE:
                intentPage(_actionBaseBean);
                break;
            case ActionType.FUNCTION:
                doFunction(_actionBaseBean);
                break;
            default:
                nonsupportToast();
                break;
        }
    }

    @Override
    public void intentPage(ActionBean _actionBaseBean) {
        if (activity == null) {
            return;
        }
        Intent intent = null;
        switch (CommonUtils.getCountInteger(_actionBaseBean.vcid)) {
            case ActionType.PageType.WEBVIEW:
                if (!TextUtils.isEmpty(_actionBaseBean.data)) {
                    ActionWebBean actionWebBean = (ActionWebBean) JsonUtils.fromJson(_actionBaseBean.data, ActionWebBean.class);
                    intentWebInfoActivity(actionWebBean.url);
                }
                break;
            case ActionType.PageType.HOME:
                intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                break;
            case ActionType.PageType.SEARCH:
                intent = new Intent(activity, ChooseCityNewActivity.class);
                intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
                intent.putExtra("source","小搜索按钮");
                activity.startActivity(intent);
                break;
            case ActionType.PageType.COUPON:
                if (isLogin()) {
                    intent = new Intent(activity, CouponActivity.class);
                    activity.startActivity(intent);
                }
                break;
            case ActionType.PageType.TRAVEL_FUND:
                if (isLogin()) {
                    intent = new Intent(activity, TravelFundActivity.class);
                    activity.startActivity(intent);
                }
                break;
            case ActionType.PageType.ACTIVITY_LIST:
                if (isLogin()) {
                    intent = new Intent(activity, WebInfoActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL,  UrlLibs.H5_ACTIVITY + UserEntity.getUser().getUserId(activity) + "&t=" + new Random().nextInt(100000));
                    intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                    activity.startActivity(intent);
                }
                break;
            case ActionType.PageType.TRAVEL_FUND_USED_BILL:
                if (isLogin()) {
                    intent = new Intent(activity, TravelFundRecordActivity.class);
                    intent.putExtra(Constants.PARAMS_TYPE, TravelFundRecordActivity.TYPE_USE_Bill);
                    activity.startActivity(intent);
                }
                break;
            default:
                nonsupportToast();
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
                nonsupportToast();
                break;
        }
    }

    private void intentWebInfoActivity(String _url) {
        if (activity == null || TextUtils.isEmpty(_url)) {
            return;
        }
        Intent intent = new Intent(activity, WebInfoActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL,  ActionUtils.getWebUrl(_url));
        intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
        activity.startActivity(intent);
    }

    private boolean isLogin() {
        boolean isLogin = UserEntity.getUser().isLogin(activity);
        if (!isLogin) {
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.startActivity(new Intent(activity, LoginActivity.class));
        }
        return isLogin;
    }

    private void nonsupportToast() {
        CommonUtils.showToast("版本较低，请升级到最新版本，体验新功能！");
    }
}
