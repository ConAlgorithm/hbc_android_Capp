package com.hugboga.custom.action;

import com.hugboga.custom.utils.CommonUtils;

/**
 * Created by qingcha on 16/7/27.
 */
public interface ActionFactoryBehavior {

    public void doAction(ActionBean _actionBaseBean);

    public void intentPage(ActionBean _actionBaseBean);

    public void doFunction(ActionBean _actionBaseBean);

}
