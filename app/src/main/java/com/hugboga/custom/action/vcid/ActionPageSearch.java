package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.constants.Constants;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionPageSearch extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        Intent intent = new Intent(context, ChooseCityNewActivity.class);
        intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
        intent.putExtra("source","小搜索按钮");
        context.startActivity(intent);
    }

}
