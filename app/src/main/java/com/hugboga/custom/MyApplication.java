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
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.adapter.viewholder.MsgViewHolderTip;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.ServerCodeHandler;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestAccessKey;
import com.hugboga.custom.developer.DeveloperOptionsActivity;
import com.hugboga.custom.map.GdMapProvider;
import com.hugboga.custom.utils.LogUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UmengADPlus;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.netease.nim.uikit.ImageLoaderKit;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.contact.ContactProvider;
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
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by admin on 2016/2/25.
 */
public class MyApplication extends HbcApplication {

    private static Context mAppContext;

    // 数据接收的 URL（神策）
    final String SA_SERVER_URL = "https://scdata.huangbaoche.com/sa?project=customer_m";
    // 配置分发的 URL（神策）
    final String SA_CONFIGURE_URL = "https://scadmin.hbc.tech/config?project=customer_m";
    // Debug 模式选项（神策）
    //   SensorsDataAPI.DebugMode.DEBUG_OFF - 关闭 Debug 模式
    //   SensorsDataAPI.DebugMode.DEBUG_ONLY - 打开 Debug 模式，校验数据，但不进行数据导入
    //   SensorsDataAPI.DebugMode.DEBUG_AND_TRACK - 打开 Debug 模式，校验数据，并将数据导入到 Sensors Analytics 中
    // 注意！请不要在正式发布的 App 中使用 Debug 模式！
    final SensorsDataAPI.DebugMode SA_DEBUG_MODE = SensorsDataAPI.DebugMode.DEBUG_OFF;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this.getApplicationContext();
        MobclickAgent.setDebugMode(HbcConfig.IS_DEBUG);
        x.Ext.setDebug(true);
        initUrlHost();
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        initConfig();
        Log.e("hbcApplication", "debug " + BuildConfig.DEBUG);
        try {
//            CrashReport.initCrashReport(this, "900024779", false);
            Reservoir.init(this, 4096);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UmengADPlus umengADPlus = new UmengADPlus();
        umengADPlus.sendMessage(this,"55ccb4cfe0f55ab500004a9d");
        initNim(this);

        boolean inMainProcess = inMainProcess(mAppContext);
        if (inMainProcess) {
            UnicornUtils.initUnicorn(); // 七鱼
            initSensorsData();  // 初始化神策
        }
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    private void initUrlHost() {
//        if (setDebugUrlHost()) {
//            return;
//        }
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

    public static boolean setDebugUrlHost() {
        if (!HbcConfig.IS_DEBUG) {
            return false;
        }
        int environmentType = SharedPre.getInteger(DeveloperOptionsActivity.CURRENT_ENVIRONMENT, -1);
        switch (environmentType) {
            case 1:
                UrlLibs.SERVER_IP_HOST_PUBLIC = UrlLibs.SERVER_HTTP_SCHEME_HTTP + UrlLibs.SERVER_IP_HOST_PUBLIC_DEV;
                UrlLibs.H5_HOST = UrlLibs.DEV_H5_HOST;
                UrlLibs.SHARE_BASE_URL_1 = UrlLibs.DEV_SHARE_BASE_URL_1;
                UrlLibs.SHARE_BASE_URL_2 = UrlLibs.DEV_SHARE_BASE_URL_2;
                UrlLibs.SHARE_BASE_URL_3 = UrlLibs.DEV_SHARE_BASE_URL_3;
                UrlLibs.SHARE_BASE_URL_4 = UrlLibs.DEV_SHARE_BASE_URL_4;
                UrlLibs.SHARE_APPID = UrlLibs.DEV_SHARE_APPID;
                return true;
            case 2:
                UrlLibs.SERVER_IP_HOST_PUBLIC = UrlLibs.SERVER_HTTP_SCHEME_HTTP + UrlLibs.SERVER_IP_HOST_PUBLIC_EXAMINATION;
                UrlLibs.H5_HOST = UrlLibs.TEST_H5_HOST;
                UrlLibs.SHARE_BASE_URL_1 = UrlLibs.TEST_SHARE_BASE_URL_1;
                UrlLibs.SHARE_BASE_URL_2 = UrlLibs.TEST_SHARE_BASE_URL_2;
                UrlLibs.SHARE_BASE_URL_3 = UrlLibs.TEST_SHARE_BASE_URL_3;
                UrlLibs.SHARE_BASE_URL_4 = UrlLibs.TEST_SHARE_BASE_URL_4;
                UrlLibs.SHARE_APPID = UrlLibs.TEST_SHARE_APPID;
                return true;
            case 3:
                UrlLibs.SERVER_IP_HOST_PUBLIC = UrlLibs.SERVER_HTTP_SCHEME_HTTPS + UrlLibs.SERVER_IP_HOST_PUBLIC_STAGE;
                UrlLibs.H5_HOST = UrlLibs.FORMAL_H5_HOST;
                UrlLibs.SHARE_BASE_URL_1 = UrlLibs.FORMAL_SHARE_BASE_URL_1;
                UrlLibs.SHARE_BASE_URL_2 = UrlLibs.FORMAL_SHARE_BASE_URL_2;
                UrlLibs.SHARE_BASE_URL_3 = UrlLibs.FORMAL_SHARE_BASE_URL_3;
                UrlLibs.SHARE_BASE_URL_4 = UrlLibs.FORMAL_SHARE_BASE_URL_4;
                UrlLibs.SHARE_APPID = UrlLibs.FORMAL_SHARE_APPID;
                return true;
            default:
                int currentEnvironmentType = 0;
                if (UrlLibs.DEV_H5_HOST.equals(BuildConfig.H5_HOST)) {
                    currentEnvironmentType = 1;
                } else if(UrlLibs.TEST_H5_HOST.equals(BuildConfig.H5_HOST)) {
                    currentEnvironmentType = 2;
                } else {
                    currentEnvironmentType = 3;
                }
                SharedPre.setInteger(DeveloperOptionsActivity.CURRENT_ENVIRONMENT, currentEnvironmentType);
                SharedPre.setInteger(DeveloperOptionsActivity.DEFULT_ENVIRONMENT, currentEnvironmentType);
                break;
        }
        HbcConfig.serverHost = UrlLibs.SERVER_IP_HOST_PUBLIC;
        return false;
    }

    public void initSensorsData() {
        // 神策 初始化 SDK
        SensorsDataAPI.sharedInstance(
                this,                               // 传入 Context
                SA_SERVER_URL,                      // 数据接收的 URL
                SA_CONFIGURE_URL,                   // 配置分发的 URL
                SA_DEBUG_MODE);                     // Debug 模式选项

        // 公共属性
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_plateform_type", "Android");        // 平台类型
            properties.put("hbc_version", BuildConfig.VERSION_NAME);// C端产品版本
            properties.put("hbc_source", BuildConfig.FLAVOR);  // 设置渠道名称属性
            properties.put("hbc_user_id", SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).getAnonymousId());
            SensorsDataAPI.sharedInstance(this).registerSuperProperties(properties);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

        //初始化用户属性
        LoginActivity.setSensorsUserEvent();
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
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
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

    public static void requestRemoteNimUserInfo(String account){
        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
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


    private static ContactProvider contactProvider = new ContactProvider() {
        @Override
        public List<UserInfoProvider.UserInfo> getUserInfoOfMyFriends() {
            List<NimUserInfo> nimUsers = NimUserInfoCache.getInstance().getAllUsersOfMyFriend();
            List<UserInfoProvider.UserInfo> users = new ArrayList<>(nimUsers.size());
            if (!nimUsers.isEmpty()) {
                users.addAll(nimUsers);
            }
            return users;
        }

        @Override
        public int getMyFriendsCount() {
            return FriendDataCache.getInstance().getMyFriendCounts();
        }
        @Override
        public String getUserDisplayName(String account) {
            return NimUserInfoCache.getInstance().getUserDisplayName(account);
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
        NimUIKit.init(context, infoProvider, contactProvider);
        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new GdMapProvider());
        // 会话窗口的定制初始化。
        //SessionHelper.init();
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                return message.getDirect() == MsgDirectionEnum.In
                        && (message.getAttachStatus() == AttachStatusEnum.transferring
                        || message.getAttachStatus() == AttachStatusEnum.fail);
            }
        });

        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
    }


}
