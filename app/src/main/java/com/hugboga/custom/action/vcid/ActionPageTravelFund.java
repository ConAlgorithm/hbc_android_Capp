package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.constants.Constants;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionPageTravelFund extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (ActionUtils.isLogin(context, actionBean)) {
            Intent intent = new Intent(context, TravelFundActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
            context.startActivity(intent);
        }
    }
}
