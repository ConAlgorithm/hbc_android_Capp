package com.hugboga.custom.data.net;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.net.ServerCodeHandlerInterface;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CheckVersionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCheckVersion;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.PushUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UpdateResources;
import com.hugboga.custom.widget.DialogUtil;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import java.io.File;
import java.lang.reflect.Field;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;

/**
 * Created by admin on 2016/3/26.
 */
public class ServerCodeHandler implements ServerCodeHandlerInterface {

    public static boolean isCheckedVersion = false;

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
            case 10015://强制升级待处理
                if (isCheckedVersion) {
                    return true;
                }
                isCheckedVersion = true;
                int resourcesVersion = new SharedPre(mContext).getIntValue(SharedPre.RESOURCES_H5_VERSION);
                RequestCheckVersion requestCheckVersion = new RequestCheckVersion(mContext, resourcesVersion);
                HttpRequestUtils.request(mContext, requestCheckVersion, new HttpRequestListener() {
                    @Override
                    public void onDataRequestSucceed(BaseRequest request) {
                        RequestCheckVersion requestCheckVersion = (RequestCheckVersion) request;
                        final CheckVersionBean cvBean = requestCheckVersion.getData();
                        UserEntity.getUser().setIsNewVersion(mContext, cvBean.hasAppUpdate);//是否有新版本
                        final DialogUtil dialogUtil = DialogUtil.getInstance(mContext);
                        if (Constants.CHANNEL_GOOGLE_PLAY.equals(BuildConfig.FLAVOR)) {//google play
                            dialogUtil.showUpdateDialog(cvBean.hasAppUpdate, cvBean.force, cvBean.content, cvBean.url);
                        } else {
                            dialogUtil.showUpdateDialog(cvBean.hasAppUpdate, cvBean.force, cvBean.content, cvBean.url, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean isUpdate = true;
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        isUpdate = !MainActivity.verifyStoragePermissions(mContext, MainActivity.REQUEST_EXTERNAL_STORAGE_UPDATE);
                                    }
                                    if (isUpdate) {
                                        if (cvBean.force && dialogUtil.getVersionDialog()!= null) {
                                            try {
                                                Field field = dialogUtil.getVersionDialog().getClass().getSuperclass().getDeclaredField("mShowing");
                                                field.setAccessible(true);
                                                field.set(dialogUtil.getVersionDialog(), false);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        PushUtils.startDownloadApk(mContext, cvBean.url);
                                    }
                                }
                            },  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isCheckedVersion = false;
                                    boolean isUpdate = true;
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        isUpdate = !MainActivity.verifyStoragePermissions(mContext, MainActivity.REQUEST_EXTERNAL_STORAGE_DB);
                                    }
                                    if (isUpdate) {
                                        //在版本检测后 检测DB
                                        UpdateResources.checkRemoteDB(mContext, cvBean.dbDownloadLink, cvBean.dbVersion, new ServerCodeHandler.CheckVersionCallBack() {
                                            @Override
                                            public void onFinished() {}
                                        });
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onDataRequestCancel(BaseRequest request) {

                    }

                    @Override
                    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

                    }
                }, false);
                return true;
            case 89932://固定线路下单 价格变更
            case 89933://固定线路下单 商品数量变更
            case 300028://超过了验价有效期
                AlertDialogUtils.showAlertDialog(mContext, content, "刷新", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new EventAction(EventType.ORDER_REFRESH));
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return true;
            case 800020://下单接口 秒杀验证异常
                EventBus.getDefault().post(new EventAction(EventType.ORDER_SECKILLS_ERROR, content));
                return true;
        }
        return false;
    }

    private static void gotoLogin(Activity mContext,boolean finish){
        if (mContext instanceof BaseFragmentActivity) {
            BaseFragmentActivity activity = (BaseFragmentActivity) mContext;
            if (finish) {
                mContext.startActivity(new Intent(mContext, MainActivity.class));
            }
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            try {
                // 用户退出清空 注册ID
                SensorsDataAPI.sharedInstance(mContext).logout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    abstract class CheckVersionCallBack implements Callback.ProgressCallback<File> {
        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {

        }

        @Override
        public void onSuccess(File result) {

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {

        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

    }

}
