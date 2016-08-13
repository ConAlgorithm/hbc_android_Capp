package com.hugboga.custom.action;

import com.hugboga.custom.action.data.ActionBean;

/**
 * Created by qingcha on 16/7/27.
 */
public interface ActionFunctionBehavior {

    public void doFunction(ActionBean _actionBean);

    public String getType();

}
