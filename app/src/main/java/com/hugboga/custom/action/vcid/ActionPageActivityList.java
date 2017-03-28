package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionPageActivityList extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (ActionUtils.isLogin(context)) {
            MobClickUtils.onEvent(StatisticConstant.LAUNCH_ACTLIST, (Map)new HashMap<>().put("source", "个人中心-活动"));
            Intent intent = new Intent(context, WebInfoActivity.class);
            intent.putExtra(WebInfoActivity.WEB_URL,  UrlLibs.H5_ACTIVITY + UserEntity.getUser().getUserId(context) + "&t=" + new Random().nextInt(100000));
            intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
            context.startActivity(intent);
        }
    }

}
