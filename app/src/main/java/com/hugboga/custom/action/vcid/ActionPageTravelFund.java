package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionPageTravelFund extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (ActionUtils.isLogin(context, actionBean)) {
            Intent intent = new Intent(context, WebInfoActivity.class);
            intent.putExtra(WebInfoActivity.WEB_URL, UserEntity.getUser().isProxyUser(context) ? UrlLibs.H5_RAVEL_FUND_RULE_AGENTS : UrlLibs.H5_RAVEL_FUND_RULE);
            intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
            context.startActivity(intent);
        }
    }
}
