package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.constants.Constants;

/**
 * Created by qingcha on 16/8/15.
 */
public class ActionPageCharteredCar extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        Intent intent = new Intent(getContext(), CharterFirstStepActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        getContext().startActivity(intent);
    }
}
