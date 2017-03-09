package com.hugboga.custom.action;

import android.content.Context;

import com.hugboga.custom.action.data.ActionBean;

/**
 * Created by qingcha on 16/8/13.
 */
public interface ActionControllerBehavior {

    public void handleAction(Context context, ActionBean _actionBean);

    public void doAction(Context context, ActionBean _actionBean);

}
