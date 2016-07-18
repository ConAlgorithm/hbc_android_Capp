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
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.fragment.FgLogin;
import com.hugboga.custom.widget.DialogUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by admin on 2016/3/26.
 */
public class ServerCodeHandler implements ServerCodeHandlerInterface {

    public  boolean handleServerCode(final Activity mContext,String content, final int state, final BaseRequest request,final HttpRequestListener listener) {
        DialogUtil dialogUtil = DialogUtil.getInstance(mContext);
        switch (state) {
            case 10011://AccessKey失效，在切换服务器或者是 服务器找到该AccessKey
                UserSession.getUser().setAccessKey(mContext,null);
                HttpRequestUtils.request(mContext,request,listener);
                EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOOUT));
                return true;
            case 10012://userToken不合法或已失效，登录信息失效，请重新登录;在其他设备上登录
                UserEntity.getUser().setUserId(mContext, null);
                UserEntity.getUser().setUserToken(mContext, null);
                UserSession.getUser().setUserToken(mContext, null);
                dialogUtil.showCustomDialog(content, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoLogin(mContext,true);
                    }
                });
                EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOOUT));
                return true;
            case 10013:
                //  设备禁止访问，则直接退出重新登录
                UserEntity.getUser().clean(mContext);
                dialogUtil.showCustomDialog(content, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoLogin(mContext,true);
                    }
                });
                EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOOUT));
                return true;

        }
        return false;
    }

    private static void gotoLogin(Activity mContext,boolean finish){
        if (mContext instanceof BaseFragmentActivity) {
            BaseFragmentActivity activity = (BaseFragmentActivity) mContext;
            BaseFragment fragment = null;
            ArrayList<com.huangbaoche.hbcframe.fragment.BaseFragment> fragmentList = activity.getFragmentList();
            for(int i =fragmentList.size()-1;i>=0;i--){
                Fragment fg  = fragmentList.get(i);
                if (fg != null && fg instanceof BaseFragment&&fg.isAdded()) {
                    fragment = (BaseFragment) fg;
                    break;
                }
            }
            if (fragment != null) {
                if(finish)
                fragment.bringToFront(FgHome.class, new Bundle());
                fragment.startFragment(new FgLogin());
            }
        }
    }

}
