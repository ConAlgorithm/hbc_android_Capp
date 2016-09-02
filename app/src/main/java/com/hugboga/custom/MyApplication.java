package com.hugboga.custom;


import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.huangbaoche.hbcframe.HbcApplication;
import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.adapter.viewholder.MsgViewHolderTip;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.ServerCodeHandler;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestAccessKey;
import com.hugboga.custom.map.GdMapProvider;
import com.hugboga.custom.utils.LogUtils;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.netease.nim.uikit.ImageLoaderKit;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.session.module.MsgForwardFilter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * Created by admin on 2016/2/25.
 */
public class MyApplication extends HbcApplication {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.setDebugMode(HbcConfig.IS_DEBUG);
        x.Ext.setDebug(true);
        initUrlHost();
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        initConfig();
        mAppContext = this.getApplicationContext();
        Log.e("hbcApplication", "debug " + BuildConfig.DEBUG);
        try {
            CrashReport.initCrashReport(this, "900024779", false);
            Reservoir.init(this, 4096);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (inMainProcess(mAppContext)) {
           UnicornUtils.initUnicorn();
        }

        //initRongIm(this); // 初始化融云IM
        initNim(this);
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    private void initUrlHost() {
        MLog.e("urlHost=" + BuildConfig.API_SERVER_URL);
        MLog.e("UrlLibs.H5_HOST=" + UrlLibs.H5_HOST);
        if(TextUtils.isEmpty(BuildConfig.API_SERVER_URL)) {
            String channel = BuildConfig.FLAVOR;
            MLog.e("channel=" + channel);
            //根据工程渠道标识，设置访问的服务器全局信息，没有标识则默认访问正式服务器
            if (TextUtils.isEmpty(channel)) channel = "formal";
            String host = UrlLibs.SERVER_IP_HOST_PUBLIC_FORMAL;
            String scheme = UrlLibs.SERVER_HTTP_SCHEME_HTTPS;
            MLog.e("channel = " + channel);
            UrlLibs.UrlHost urlHost = UrlLibs.UrlHost.valueOf(channel.toUpperCase());

            if (urlHost != null) {
                host = urlHost.url;
            }
            if("formal".equals(channel)) {
                scheme = UrlLibs.SERVER_HTTP_SCHEME_HTTPS;
            }else{
                scheme = UrlLibs.SERVER_HTTP_SCHEME_HTTP;
            }
            UrlLibs.SERVER_IP_HOST_PUBLIC = scheme + host;
        }else {
            UrlLibs.SERVER_IP_HOST_PUBLIC = BuildConfig.API_SERVER_URL;
        }
        UrlLibs.SHARE_BASE_URL_1 = BuildConfig.SHARE_BASE_URL_1;
        UrlLibs.SHARE_BASE_URL_2 = BuildConfig.SHARE_BASE_URL_2;
        UrlLibs.SHARE_BASE_URL_3 = BuildConfig.SHARE_BASE_URL_3;
        UrlLibs.SHARE_BASE_URL_4 = BuildConfig.SHARE_BASE_URL_4;
        UrlLibs.SHARE_APPID = BuildConfig.SHARE_APPID;
        UrlLibs.H5_HOST = BuildConfig.H5_HOST;

        LogUtils.e(UrlLibs.SHARE_BASE_URL_1+"\n" +UrlLibs.SHARE_BASE_URL_2
                +"\n"+UrlLibs.SHARE_BASE_URL_3+"\n"+UrlLibs.SHARE_BASE_URL_4
                +"\n"+UrlLibs.H5_HOST);
    }

    private void initConfig() {
        HbcConfig.serverHost = UrlLibs.SERVER_IP_HOST_PUBLIC;
        HbcConfig.accessKeyRequest = RequestAccessKey.class;
        HbcConfig.dialogUtil = DialogUtil.class;
        HbcConfig.serverCodeHandler = ServerCodeHandler.class;
        HbcConfig.PACKAGE_NAME = BuildConfig.APPLICATION_ID;
        HbcConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        HbcConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        HbcConfig.APP_NAME = getString(R.string.app_name);
        x.Ext.setDebug(HbcConfig.IS_DEBUG);
        HbcConfig.WX_APP_ID = Constants.WX_APP_ID;
    }

    /**
     * 初始化融云IM
     */
    public static void initRongIm(Context context) {
        try {
            if (context.getApplicationInfo().packageName.equals(getCurProcessName(context.getApplicationContext())) || "io.rong.push".equals(getCurProcessName(context.getApplicationContext()))) {
                RongIM.init(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static final String getProcessName(Context context) {
        String processName = null;

        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }

            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void initNim(Context context){
        NIMClient.init(context, null, getOptions(context));
        if (inMainProcess(context)) {
            initUIKit(context);
            // 注册通知消息过滤器,暂时不需要
            //registerIMMessageFilter();
            // 初始化消息提醒
            NIMClient.toggleNotification(false);
        }
    }


    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getCurProcessName(context);
        return packageName.equals(processName);
    }


    private static  SDKOptions getOptions(Context context) {
        SDKOptions options = new SDKOptions();
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();;
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        options.statusBarNotificationConfig = config;
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;
        options.databaseEncryptKey = "NETEASE";
        options.preloadAttach = true;
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();
        options.userInfoProvider = infoProvider;

        return options;
    }

    private static UserInfoProvider infoProvider = new UserInfoProvider() {
        @Override
        public UserInfo getUserInfo(String account) {
            UserInfo user = NimUserInfoCache.getInstance().getUserInfo(account);
            if (user == null) {
                NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
            }

            return user;
        }

        @Override
        public int getDefaultIconResId() {
            return R.mipmap.chat_head;
        }

        @Override
        public Bitmap getTeamIcon(String teamId) {
            Drawable drawable = mAppContext.getResources().getDrawable(R.drawable.nim_avatar_group);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            return null;
        }

        @Override
        public Bitmap getAvatarForMessageNotifier(String account) {
            /**
             * 注意：这里最好从缓存里拿，如果读取本地头像可能导致UI进程阻塞，导致通知栏提醒延时弹出。
             */
            UserInfo user = getUserInfo(account);
            return (user != null) ? ImageLoaderKit.getNotificationBitmapFromCache(user) : null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            String nick = null;
            if (sessionType == SessionTypeEnum.P2P) {
                nick = NimUserInfoCache.getInstance().getAlias(account);
            } else if (sessionType == SessionTypeEnum.Team) {
                nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
                if (TextUtils.isEmpty(nick)) {
                    nick = NimUserInfoCache.getInstance().getAlias(account);
                }
            }
            // 返回null，交给sdk处理。如果对方有设置nick，sdk会显示nick
            if (TextUtils.isEmpty(nick)) {
                return null;
            }

            return nick;
        }
    };

//    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
//        @Override
//        public String makeNotifyContent(String nick, IMMessage message) {
//            return null; // 采用SDK默认文案
//        }
//
//        @Override
//        public String makeTicker(String nick, IMMessage message) {
//            return null; // 采用SDK默认文案
//        }
//    };

    private static void initUIKit(Context context) {
        // 初始化，需要传入用户信息提供者
        NimUIKit.init(context, infoProvider, null);
        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new GdMapProvider());
        // 会话窗口的定制初始化。
        //SessionHelper.init();
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getDirect() == MsgDirectionEnum.In
                        && (message.getAttachStatus() == AttachStatusEnum.transferring
                        || message.getAttachStatus() == AttachStatusEnum.fail)) {
                    // 接收到的消息，附件没有下载成功，不允许转发
                    return true;
                }
                return false;
            }
        });

        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
    }


}
