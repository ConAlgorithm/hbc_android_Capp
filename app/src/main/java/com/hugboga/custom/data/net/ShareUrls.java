package com.hugboga.custom.data.net;


import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.utils.CommonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by qingcha on 16/5/30.
 */
public final class ShareUrls {

    private ShareUrls() {}

    //参数顺序不能变
    private static String SHARE_BASE_WECHAT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + UrlLibs.SHARE_APPID + "&redirect_uri=";

    /**
     * 分享司导
     */
    private static final String SHARE_GUIDE = UrlLibs.SHARE_BASE_URL_2 + "/h5/cactivity/shareGuide/index.html";

    /**
     * 邀请好友页面，30元大礼包
     */
    private static final String SHARE_THIRTY_COUPON = UrlLibs.SHARE_BASE_URL_2 + "/h5/cactivity/toFriends/index.html";

    private static String getScope(String scope) {
        return "&response_type=code&scope=" + scope + "&state=STATE#wechat_redirect";
    }

    /**
     * 分享司导
     */
    public static String getShareGuideUrl(GuidesDetailData data, String userId) {
        ArrayMap<String, String> params = new ArrayMap<String, String>();
        params.put("gid", CommonUtils.getDoubleEncodedString(data.guideId));//司导ID
        params.put("uid", CommonUtils.getDoubleEncodedString(userId));
        params.put("reurl", SHARE_GUIDE);
        return SHARE_BASE_WECHAT_URL + getUri(BuildConfig.SHARE_BASE_URL_1, params, true) + getScope("snsapi_base");
    }

    /**
     * 邀请好友页面，30元大礼包
     */
    public static String getShareThirtyCouponUrl(String avatar, String name, String qcode) {
        ArrayMap<String, String> params = new ArrayMap<String, String>();
        params.put("avatar", avatar);
        params.put("name", CommonUtils.getEncodedString(name));
        params.put("qcode", qcode);//邀请码
        return getUri(SHARE_THIRTY_COUPON, params, false);
    }

    private static String getUri(String _baseUrl, ArrayMap<String, String> _params, boolean isEncode) {
        String params = null;
        if (_params != null) {
            ArrayList<String> ps = new ArrayList<String>(_params.size());
            for (Map.Entry<String, String> entry : _params.entrySet()) {
                ps.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
            }
            params = TextUtils.join("&", ps);
        }
        String result = null;
        if (isEncode) {
            try {
                result = URLEncoder.encode(String.format("%s?%s", _baseUrl, params), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            result = String.format("%s?%s", _baseUrl, params);
        }
        return result;
    }
}
