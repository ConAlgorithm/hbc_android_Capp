package com.hugboga.custom.action;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;

/**
 * Created by qingcha on 16/8/12.
 */
public class ActionUtils {

    public static boolean isLogin(Context context, ActionBean actionBean) {
        boolean isLogin = UserEntity.getUser().isLogin(context);
        if (!isLogin) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(Constants.PARAMS_ACTION, actionBean);
            context.startActivity(intent);
        }
        return isLogin;
    }
}
