package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.DailyWebInfoActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;

/**
 * Created by qingcha on 16/8/15.
 */
public class ActionPageCharteredCar extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (ActionUtils.isLogin(context)) {
            Bundle bundle = new Bundle();
            String userId = UserEntity.getUser().getUserId(context);
            String params = "";
            if (!TextUtils.isEmpty(userId)) {
                params += "?userId=" + userId;
            }
            Intent intent = new Intent(context, DailyWebInfoActivity.class);
            intent.putExtras(bundle);
            intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY + params);
            this.getContext().startActivity(intent);
        }
    }
}
