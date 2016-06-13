package com.hugboga.custom.data.net;


import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by qingcha on 16/5/30.
 */
public final class ShareUrls {

    private ShareUrls() {}

    /**
     * 分享司导
     */
    private static final String SHARE_GUIDE = UrlLibs.SHARE_BASE_URL + "h5/cactivity/shareGuide/index.html";

    /**
     * 邀请好友页面，30元大礼包
     */
    private static final String SHARE_THIRTY_COUPON = UrlLibs.SHARE_BASE_URL + "h5/cactivity/toFriends/index.html";

    /**
     * 分享司导
     */
    public static String getShareGuideUrl(GuidesDetailData data, String userId) {
        ArrayMap<String, String> params = new ArrayMap<String, String>();
        params.put("gid", CommonUtils.getEncodedString(data.getGuideId()));//司导ID
        params.put("uid", CommonUtils.getEncodedString(userId));
        return getUri(SHARE_GUIDE, params);
    }


    /**
     * 邀请好友页面，30元大礼包
     */
    public static String getShareThirtyCouponUrl(String avatar, String name, String qcode) {
        ArrayMap<String, String> params = new ArrayMap<String, String>();
        params.put("avatar", avatar);
        params.put("name", CommonUtils.getEncodedString(name));
        params.put("qcode", qcode);//邀请码
        return getUri(SHARE_THIRTY_COUPON, params);
    }


    private static String getUri(String _baseUrl, ArrayMap<String, String> _params ) {
        String params = null;
        if (_params != null) {
            ArrayList<String> ps = new ArrayList<String>(_params.size());
            for (Map.Entry<String, String> entry : _params.entrySet()) {
                ps.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
            }
            params = TextUtils.join("&", ps);
        }
        return String.format("%s?%s", _baseUrl, params);
    }
}
