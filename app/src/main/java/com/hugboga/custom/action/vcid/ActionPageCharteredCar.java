package com.hugboga.custom.action.vcid;

import android.content.Context;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.utils.IntentUtils;

/**
 * Created by qingcha on 16/8/15.
 */
public class ActionPageCharteredCar extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        IntentUtils.intentCharterActivity(getContext(), actionBean.source);
    }
}
