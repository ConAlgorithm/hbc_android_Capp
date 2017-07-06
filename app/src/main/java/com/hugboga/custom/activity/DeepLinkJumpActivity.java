package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.utils.DeepLinkHelper;
import com.hugboga.custom.utils.JsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by zhangqiang on 17/7/2.
 */

public class DeepLinkJumpActivity  extends BaseActivity{
    @Override
    public int getContentViewId() {
        return R.layout.deep_link_layout;
    }
    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        schemeIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        schemeIntent(intent);
    }

    private void schemeIntent(Intent _intent) {
        Intent intent = _intent;
        String scheme = intent.getScheme();
        if("http".equals(scheme) || "https".equals(scheme)){
            String data = null;
            String dataString = intent.getDataString();
            //dataString = "https://m-test.huangbaoche.com/app/detail.html?goodsNo=IC2170110001&amp;capp=%7B%22ai%22%3A%22205%22%2C%22gn%22%3A%22IC2170110001%22%2C%22u%22%3A%22https%3A%2F%2Fm-test.huangbaoche.com%2Fapp%2Fdetail.html%3FgoodsNo%3DIC2170110001%22%7D";

            if (!TextUtils.isEmpty(dataString)) {
                try {
                    dataString = URLDecoder.decode(dataString, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                scheme += "://";
                data = dataString.substring(scheme.length(), dataString.length());
                DeepLinkHelper.Builder builder = new DeepLinkHelper.Builder();

                builder.context(this).url(dataString).parseListner(new DeepLinkHelper.ParseListner() {
                    @Override
                    public void getParseBack(ActionBean actionBean) {
                        ActionBean actionBean1 = actionBean;
                        finish();
                        return;
                    }
                }).build().startParse();
                MLog.i("http/https " + data);
            }
        }
    }
}
