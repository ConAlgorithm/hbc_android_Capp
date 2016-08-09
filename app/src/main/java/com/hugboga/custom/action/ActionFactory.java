package com.hugboga.custom.action;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.activity.CouponActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.utils.CommonUtils;

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
            Log.i("qingcha_push", "doAction  wocao   ");
            return;
        }
        Log.i("qingcha_push", "doAction  yeyeye   ");
        switch (CommonUtils.getCountInteger(_actionBaseBean.type)) {
            case ActionType.WEB_ACTIVITY:
                if (activity == null) {
                    return;
                }
                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL,  _actionBaseBean.url);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                activity.startActivity(intent);
                break;
            case ActionType.NATIVE_PAGE:
                intentPage(_actionBaseBean);
                Log.i("qingcha_push", "doAction  hahahah");
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
        } else {
            if (activity.getFragmentList().size() > 3) {
                for (int i = activity.getFragmentList().size() - 1; i >= 3; i--) {
                    activity.getFragmentList().get(i).finish();
                }
            }
        }
        Intent intent = null;
        switch (CommonUtils.getCountInteger(_actionBaseBean.vcid)) {
            case ActionType.PageType.WEBVIEW:
                intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL,  _actionBaseBean.vcid);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                activity.startActivity(intent);
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

    private boolean isLogin() {
        boolean isLogin = UserEntity.getUser().isLogin(activity);
        if (!isLogin) {
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.startActivity(new Intent(activity, LoginActivity.class));
        }
        return isLogin;
    }
}
