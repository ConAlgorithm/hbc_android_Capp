package com.hugboga.custom.action;

import com.hugboga.custom.action.data.ActionBean;

/**
 * Created by qingcha on 16/8/13.
 */
public interface ActionControllerBehavior {

    public void handleAction(ActionBean _actionBean);

    public void doAction(ActionBean _actionBean);

}
