package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionWebBean;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionPageWeb extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!TextUtils.isEmpty(actionBean.data)) {
            ActionWebBean actionWebBean = (ActionWebBean) JsonUtils.fromJson(actionBean.data, ActionWebBean.class);
            intentWebInfoActivity(actionWebBean.url);
        }
    }

    private void intentWebInfoActivity(String _url) {
        if (getContext() == null || TextUtils.isEmpty(_url)) {
            return;
        }
        Intent intent = new Intent(getContext(), WebInfoActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, getWebUrl(_url));
        intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
        getContext().startActivity(intent);
    }

    private String getWebUrl(String _url) {
        String result = _url;
        if (TextUtils.isEmpty(_url) && _url.length() <= 2) {
            return result;
        }
        if ('$' == _url.charAt(0)) {
            String type = String.valueOf(_url.charAt(1));
            result = getBaseUrl(type);
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
