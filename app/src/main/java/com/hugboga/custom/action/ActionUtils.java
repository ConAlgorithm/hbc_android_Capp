package com.hugboga.custom.action;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 16/8/12.
 */
public class ActionUtils {

    public static boolean isLogin(Context context, ActionBean actionBean) {
        boolean isLogin = UserEntity.getUser().isLogin(context);
        if (!isLogin) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(Constants.PARAMS_ACTION, actionBean);
            intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
            context.startActivity(intent);
        }
        return isLogin;
    }

    public static void doAction(Context context, String action, String source) {
        ActionBean actionBean = (ActionBean) JsonUtils.fromJson(action, ActionBean.class);
        if (actionBean != null) {
            actionBean.source = source;
            ActionController actionFactory = ActionController.getInstance();
            actionFactory.doAction(context, actionBean);
        }
    }

    public static void doAction(Context context, final ActionBean _actionBean) {
        if (_actionBean == null) {
            return;
        }
        ActionController actionFactory = ActionController.getInstance();
        actionFactory.doAction(context, _actionBean);
    }
}
