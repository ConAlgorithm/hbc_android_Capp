package com.hugboga.custom;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.huangbaoche.hbcframe.HbcApplication;
import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.ServerCodeHandler;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestAccessKey;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.NotificationCheckUtils;
import com.hugboga.custom.utils.UmengADPlus;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.im.ImHelper;
import com.hugboga.im.entity.ImAnalysisEnitty;
import com.hugboga.tools.HLog;
import com.ishumei.smantifraud.SmAntiFraud;
import com.leon.channel.helper.ChannelReaderUtil;
import com.networkbench.agent.impl.NBSAppAgent;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.tongdun.android.shell.FMAgent;
import cn.tongdun.android.shell.exception.FMException;

/**
 * Created by admin on 2016/2/25.
 */
public class MyApplication extends HbcApplication implements Application.ActivityLifecycleCallbacks{

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
    static String channelNum = null;

    private final static List<Activity> activityList = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this.getApplicationContext();
        MobclickAgent.setDebugMode(HbcConfig.IS_DEBUG);
        x.Ext.setDebug(false); //设置xUtils的debug模式
        setHlog(); //设置日志配置
        getChannelNum();
        Log.e("hbcApplication", "debug " + BuildConfig.DEBUG);
        try {
            CrashReport.initCrashReport(this, "900024779", false);
            Reservoir.init(this, 4096);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final boolean inMainProcess = inMainProcess(mAppContext);
        if (inMainProcess) {
            initUrlHost();
            initConfig();

            UmengADPlus umengADPlus = new UmengADPlus();
            umengADPlus.sendMessage(this, "55ccb4cfe0f55ab500004a9d");

            UnicornUtils.initUnicorn(); // 七鱼
            initSensorsData();          // 初始化神策

            // android 大渠道（例如：官方渠道）接入数美， 其他小渠道接入同盾科技
            if (CommonUtils.isAgainstSM()) {
                initSmAntiFraud();          // 数美
            } else {
                initFMAgent();              // 同盾
            }
        }
        initNetworkbench();
        initNim();
    }

    /**
     * 设置日志配置
     */
    private void setHlog() {
        HLog.setIsDebug(BuildConfig.DEBUG);
        HLog.setLogTag("HBC");
        HLog.setMethodCount(0);
    }

    private void initNim() {
        ImHelper.setUserId(UserEntity.getUser().getUserId(this));
        ImHelper.initNim(this, R.mipmap.icon_avatar_user, imAnalysisHandler);
    }

    private void initNetworkbench() {
        NBSAppAgent.setLicenseKey("34ac28c049574c4095b57fc0a591cd4b").withLocationServiceEnabled(true).
                startInApplication(this.getApplicationContext());
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    private void initUrlHost() {
        MLog.e("urlHost=" + BuildConfig.API_SERVER_URL);
        MLog.e("UrlLibs.H5_HOST=" + UrlLibs.H5_HOST);
        String channel = null;
        if (TextUtils.isEmpty(BuildConfig.API_SERVER_URL)) {
            if (getChannelNum() != null) {
                channel = getChannelNum();
            } else {
                channel = BuildConfig.FLAVOR;
            }

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
            if ("formal".equals(channel)) {
                scheme = UrlLibs.SERVER_HTTP_SCHEME_HTTPS;
            } else {
                scheme = UrlLibs.SERVER_HTTP_SCHEME_HTTP;
            }
            UrlLibs.SERVER_IP_HOST_PUBLIC = scheme + host;
        } else {
            UrlLibs.SERVER_IP_HOST_PUBLIC = BuildConfig.API_SERVER_URL;
        }
        UrlLibs.SHARE_BASE_URL_1 = BuildConfig.SHARE_BASE_URL_1;
        UrlLibs.SHARE_BASE_URL_2 = BuildConfig.SHARE_BASE_URL_2;
        UrlLibs.SHARE_BASE_URL_3 = BuildConfig.SHARE_BASE_URL_3;
        UrlLibs.SHARE_BASE_URL_4 = BuildConfig.SHARE_BASE_URL_4;
        UrlLibs.SHARE_APPID = BuildConfig.SHARE_APPID;
        UrlLibs.H5_HOST = BuildConfig.H5_HOST;

        HLog.e(UrlLibs.SHARE_BASE_URL_1 + "\n" + UrlLibs.SHARE_BASE_URL_2
                + "\n" + UrlLibs.SHARE_BASE_URL_3 + "\n" + UrlLibs.SHARE_BASE_URL_4
                + "\n" + UrlLibs.H5_HOST);
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
        HbcConfig.WX_APP_ID = BuildConfig.WX_APP_ID;
        if (getChannelNum() != null) {
            HbcConfig.FLAVOR = getChannelNum();
        } else {
            HbcConfig.FLAVOR = BuildConfig.FLAVOR;
        }
    }

    public void initSensorsData() {
        try {
            boolean isTest = false;
            if (getChannelNum() != null) {
                isTest = "developer".equals(getChannelNum()) || "examination".equals(getChannelNum());
            } else {
                isTest = "developer".equals(BuildConfig.FLAVOR) || "examination".equals(BuildConfig.FLAVOR);
            }

            // 神策 初始化 SDK
            SensorsDataAPI.sharedInstance(
                    this,                               // 传入 Context
                    SA_SERVER_URL,                      // 数据接收的 URL
                    SA_CONFIGURE_URL,                   // 配置分发的 URL
                    SA_DEBUG_MODE);                     // Debug 模式选项

            // 公共属性
            JSONObject properties = new JSONObject();
            properties.put("hbc_plateform_type", "Android");        // 平台类型
            properties.put("hbc_version", BuildConfig.VERSION_NAME);// C端产品版本
            if (getChannelNum() != null) {
                properties.put("hbc_source", getChannelNum());  // 设置渠道名称属性
            } else {
                properties.put("hbc_source", BuildConfig.FLAVOR);  // 设置渠道名称属性
            }
            properties.put("hbc_user_id", SensorsDataAPI.sharedInstance(this).getAnonymousId());
            properties.put("isTest", isTest);
            SensorsDataAPI.sharedInstance(this).registerSuperProperties(properties);

            setSensorsAutoTrack();
            addSensorsCustomAppInstall();
            //初始化用户属性
            LoginActivity.setSensorsUserEvent();
            if (UserEntity.getUser().isLogin(this)) {
                SensorsDataAPI.sharedInstance(this).login(UserEntity.getUser().getUserId(getApplicationContext()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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


    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getCurProcessName(context);
        if (TextUtils.isEmpty(processName)) {
            return true;
        }
        return packageName.equals(processName);
    }

    /**
     * 数美
     * 系统地址：https://www.fengkongcloud.com
     * 账号：yingbinmu@huangbaoche.com
     * 密码：10Yi100Yi
     * accessKey：WUUvcpMUan4hd4B5FaBC
     * organization：GqATrb95woTXTmiUQJrC
     */
    public void initSmAntiFraud() {
        try {
            SmAntiFraud.SmOption option = new SmAntiFraud.SmOption();
            option.setOrganization("GqATrb95woTXTmiUQJrC");
            if (getChannelNum() != null) {
                option.setChannel(getChannelNum());
            } else {
                option.setChannel(BuildConfig.FLAVOR);
            }

            SmAntiFraud.create(getApplicationContext(), option);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同盾
     * 系统地址：https://portaltest.tongdun.cn
     * 账号：yingbinmu@huangbaoche.com
     * 密码：fRqbbxpJ
     * FMAgent.ENV_SANDBOX表示测试环境，FMAgent.ENV_PRODUCTION表示生产环境
     */
    public void initFMAgent() {
        try {
            FMAgent.init(this, FMAgent.ENV_PRODUCTION);
        } catch (FMException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    打开神策的预制埋点
    * */
    private void setSensorsAutoTrack() {
        // 打开自动采集, 并指定追踪哪些 AutoTrack 事件
        List<SensorsDataAPI.AutoTrackEventType> eventTypeList = new ArrayList<>();
        // $AppStart
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START);
        // $AppEnd
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END);
        // $AppViewScreen
        //eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_VIEW_SCREEN);
        // $AppClick
        //eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_CLICK);
        SensorsDataAPI.sharedInstance(this).enableAutoTrack(eventTypeList);
    }

    /*
    添加自定义渠道追踪信息
    * */
    private void addSensorsCustomAppInstall() {
        try {
            JSONObject properties = new JSONObject();
            // 设置渠道名
            if (getChannelNum() != null) {
                properties.put("channelId", getChannelNum());
            } else {
                properties.put("channelId", BuildConfig.FLAVOR);
            }

            properties.put("is_open_push", NotificationCheckUtils.notificationIsOpen(this));
            // 追踪渠道效果
            SensorsDataAPI.sharedInstance(this).trackInstallation("AppInstall", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Handler imAnalysisHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                Bundle bundle = msg.getData();
                if (bundle != null) {
                    ImAnalysisEnitty imAnalysisEnitty = (ImAnalysisEnitty) bundle.getSerializable(ImAnalysisEnitty.KEY_IM_ANALYSIS_ENTITY);
                    if (imAnalysisEnitty != null) {
                        if (!TextUtils.isEmpty(imAnalysisEnitty.actionName)) {
                            SensorsUtils.setSensorsAppointImAnalysis(imAnalysisEnitty.actionName, imAnalysisEnitty.imKeyMap);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    };

    public static String getChannelNum() {
        channelNum = ChannelReaderUtil.getChannel(mAppContext);
        MLog.e("channelNum=" + channelNum);
        return channelNum;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activityList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 是否还有正在运行的activity
     * @return
     */
    public static boolean hasAliveActivity(){
        if(activityList!=null && activityList.size()!=0){
            return true;
        }
        return false;
    }
}
