package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.TravelPurposeFormActivity;
import com.hugboga.custom.constants.Constants;
/**
 * Created by qingcha on 17/6/14.
 */
public class ActionPagePurposeForm extends ActionPageBase {
    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        Intent intent = new Intent(context, TravelPurposeFormActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        context.startActivity(intent);
    }
}
