package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionPageHome extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
