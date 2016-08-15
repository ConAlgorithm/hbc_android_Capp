package com.hugboga.custom.action;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.hugboga.custom.action.constants.ActionPageType;
import com.hugboga.custom.action.constants.ActionType;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.utils.CommonUtils;

import java.lang.reflect.Constructor;

/**
 * Created by qingcha on 16/7/27.
 */
public class ActionController implements ActionControllerBehavior {

    private BaseActivity activity;
    private volatile static ActionController actionController;
    private ArrayMap<Integer, Class> pageMap;

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
                if (TextUtils.isEmpty(_actionBean.vcid)) {
                    nonsupportToast();
                    break;
                }
                if (pageMap == null) {
                    pageMap = ActionMapping.getPageMap();
                }
                Class pageActionClass = pageMap.get(CommonUtils.getCountInteger(_actionBean.vcid));
                if (pageActionClass == null) {
                    nonsupportToast();
                    break;
                }
                ActionPageBase actionPageBase = null;
                try {
                    Class<ActionPageBase> cls = (Class<ActionPageBase>) Class.forName(pageActionClass.getName());
                    actionPageBase = cls.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (actionPageBase == null) {
                    nonsupportToast();
                    break;
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
