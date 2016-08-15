package com.hugboga.custom.action;

import com.hugboga.custom.action.constants.ActionPageType;
import com.hugboga.custom.action.constants.ActionType;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.utils.CommonUtils;

/**
 * Created by qingcha on 16/7/27.
 */
public class ActionController implements ActionControllerBehavior {

    private BaseActivity activity;
    private volatile static ActionController actionController;

    private ActionController(BaseActivity activity) {
        this.activity = activity;
    }

    public static ActionController getInstance(BaseActivity _activity) {
        if (actionController == null) {
            synchronized(ActionController.class) {
                if (actionController == null) {
                    actionController = new ActionController(_activity);
                }
            }
        }
        return actionController;
    }

    @Override
    public void doAction(final ActionBean _actionBean) {
        if (_actionBean == null) {
            return;
        }
        switch (CommonUtils.getCountInteger(_actionBean.type)) {
            case ActionType.WEB_ACTIVITY:
                _actionBean.type = "" + ActionType.NATIVE_PAGE;
                _actionBean.vcid = "" + ActionPageType.WEBVIEW;
                _actionBean.data = "{\"u\":\"" + _actionBean.url + "\"}";
            case ActionType.NATIVE_PAGE:
                ActionPageBase actionPageBase = ActionMapping.getActionPage(_actionBean.vcid);
                if (actionPageBase == null) {
                    nonsupportToast();
                } else {
                    actionPageBase.intentPage(activity, _actionBean);
                }
                break;
            case ActionType.FUNCTION:

                break;
            default:
                nonsupportToast();
                break;
        }
    }

    @Override
    public void handleAction(final ActionBean _actionBean) {

    }

    private void nonsupportToast() {
        CommonUtils.showToast("版本较低，请升级到最新版本，体验新功能！");
    }

}
