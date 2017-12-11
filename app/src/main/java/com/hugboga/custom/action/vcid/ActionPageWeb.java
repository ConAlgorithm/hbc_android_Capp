package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionExam;
import com.hugboga.custom.action.data.ActionWebBean;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionPageWeb extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (actionBean.data != null) {
            ActionWebBean actionWebBean = null;
            if (actionBean.data instanceof String) {
                actionWebBean = (ActionWebBean) JsonUtils.fromJson((String) actionBean.data, ActionWebBean.class);
            } else {
                try {
                    actionWebBean = (ActionWebBean) JsonUtils.fromJson(actionBean.data, ActionWebBean.class);
                } catch (Exception e) {
                    actionWebBean = null;
                }
            }
            if (actionWebBean != null && !TextUtils.isEmpty(actionWebBean.url)) {
                intentWebInfoActivity(actionWebBean.url, actionBean.pushId, actionBean.exam);
            }
        }
    }

    private void intentWebInfoActivity(String _url, String pushId, ActionExam exam) {
        if (getContext() == null || TextUtils.isEmpty(_url)) {
            return;
        }
        Intent intent = new Intent(getContext(), WebInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(WebInfoActivity.WEB_URL, getWebUrl(_url));
        if (exam != null) {
            intent.putExtra(WebInfoActivity.WEB_SHARE_BTN, exam.isShareBtn);
            intent.putExtra(WebInfoActivity.WEB_SHARE_NO, exam.shareNo);
        }
        if (pushId != null) {
            intent.putExtra(Constants.PARAMS_SOURCE, "push" + pushId);
        }
        getContext().startActivity(intent);
    }

    private String getWebUrl(String _url) {
        String result = _url;
        if (TextUtils.isEmpty(_url) && _url.length() <= 2) {
            return result;
        }
        if ('$' == _url.charAt(0)) {
            String type = String.valueOf(_url.charAt(1));
            result = getBaseUrl(type) + _url.substring(2, _url.length());
        }
        return result;
    }

    private String getBaseUrl(String type) {
        String result = "";
        if (type.equalsIgnoreCase("a")) {
            result = UrlLibs.SERVER_IP_HOST_PUBLIC;
        } else if (type.equalsIgnoreCase("b")) {
            result = UrlLibs.H5_HOST;
        } else if (type.equalsIgnoreCase("c")) {
            result = UrlLibs.SHARE_BASE_URL_2;
        } else if (type.equalsIgnoreCase("d")) {
            result = UrlLibs.SHARE_BASE_URL_1;
        } else if (type.equalsIgnoreCase("e")) {
            result = UrlLibs.SHARE_BASE_URL_4;
        }
        return result;
    }
}
