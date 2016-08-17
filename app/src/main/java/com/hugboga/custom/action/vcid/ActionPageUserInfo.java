package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.PersonInfoActivity;

/**
 * Created by qingcha on 16/8/15.
 */
public class ActionPageUserInfo extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (ActionUtils.isLogin(context)) {
            Intent intent = new Intent(context, PersonInfoActivity.class);
            context.startActivity(intent);
        }
    }

}