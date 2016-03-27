package com.hugboga.custom.data.net;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.net.ServerCodeHandlerInterface;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.fragment.FgLogin;
import com.hugboga.custom.widget.DialogUtil;

/**
 * Created by admin on 2016/3/26.
 */
public class ServerCodeHandler implements ServerCodeHandlerInterface {

    public  boolean handleServerCode(final Activity mContext,String content, final int state, final BaseRequest request,final HttpRequestListener listener) {
        DialogUtil dialogUtil = DialogUtil.getInstance(mContext);
        switch (state) {
            case 10011://AccessKey
                UserSession.getUser().setAccessKey(mContext,null);
                HttpRequestUtils.request(mContext,request,listener);
                break;
            case 10012://userToken不合法或已失效，登录信息失效，请重新登录
                UserEntity.getUser().setUserId(mContext, null);
                UserEntity.getUser().setUserToken(mContext, null);
                UserSession.getUser().setUserToken(mContext, null);
                dialogUtil.showCustomDialog(content, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoLogin(mContext);
                    }
                });
                break;
            case 10013:
                //  设备禁止访问，则直接退出重新登录
                UserEntity.getUser().clean(mContext);
                UserSession.getUser().setUserToken(mContext, null);
                dialogUtil.showCustomDialog(content, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoLogin(mContext);
                    }
                });
                break;

        }
        return false;
    }

    private static void gotoLogin(Activity mContext){
        if (mContext instanceof BaseFragmentActivity) {
            BaseFragmentActivity activity = (BaseFragmentActivity) mContext;
            BaseFragment fragment = null;
            for (Fragment fg : activity.getSupportFragmentManager().getFragments()) {
                if (fg != null && fg instanceof BaseFragment) {
                    fragment = (BaseFragment) fg;
                    break;
                }
            }
            if (fragment != null) {
                fragment.bringToFront(FgHome.class, new Bundle());
                fragment.startFragment(new FgLogin());
            }
        }
    }

}
