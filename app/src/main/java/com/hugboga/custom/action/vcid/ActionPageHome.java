package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.statistic.sensors.SensorsUtils;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionPageHome extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.PARAMS_PAGE_INDEX, 0);
        context.startActivity(intent);
        if(actionBean.pushId != null){
            SensorsUtils.setPageEvent(getEventSource(), getEventSource(), actionBean.pushId);
        }
    }
    private String getEventSource() {
        return "首页";
    }

}
