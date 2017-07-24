package com.hugboga.custom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CheckVersionBean;
import com.hugboga.custom.data.bean.PushMessage;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCheckVersion;
import com.hugboga.custom.data.request.RequestImAnalysisSwitch;
import com.hugboga.custom.data.request.RequestPushClick;
import com.hugboga.custom.data.request.RequestPushToken;
import com.hugboga.custom.data.request.RequestUploadLocation;
import com.hugboga.custom.fragment.FgDestination;
import com.hugboga.custom.fragment.FgHomePage;
import com.hugboga.custom.fragment.FgMySpace;
import com.hugboga.custom.fragment.FgNimChat;
import com.hugboga.custom.fragment.FgTravel;
import com.hugboga.custom.service.ImAnalysisService;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.FileUtil;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.LocationUtils;
import com.hugboga.custom.utils.PermissionRes;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.PushUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UpdateResources;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.GiftController;
import com.hugboga.custom.widget.NoScrollViewPager;
import com.hugboga.im.ImAnalysisUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, HttpRequestListener {

    public static final String PUSH_BUNDLE_MSG = "pushMessage";
    public static final String FILTER_PUSH_DO = "com.hugboga.custom.pushdo";
    public static final String PARAMS_PAGE_INDEX = "page_index";

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 12;

    public static final int REQUEST_EXTERNAL_STORAGE_UPDATE = 1;
    public static final int REQUEST_EXTERNAL_STORAGE_DB = 2;
    public static final int REQUEST_EXTERNAL_STORAGE = 3;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Bind(R.id.container)
    NoScrollViewPager mViewPager;

    @Bind(R.id.bottom_point_2)
    TextView bottomPoint2;
    @Bind(R.id.bottom_point_3)
    TextView qyServiceUnreadMsgCount;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TextView tabMenu[] = new TextView[5];
    private ActionBean actionBean;
    private int currentPosition = 0;
    private CheckVersionBean cvBean;
    private DialogUtil dialogUtil;
    public int lastPagerPosition = 0;

    //private FgHome fgHome;
    private FgHomePage fgHomePage;
    private FgDestination fgDestination;
    private FgNimChat fgChat;
    private FgTravel fgTravel;
    private FgMySpace fgMySpace;
    private SharedPre sharedPre;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pagePosition = -1;
        if (savedInstanceState != null) {
            actionBean = (ActionBean) savedInstanceState.getSerializable(Constants.PARAMS_ACTION);
            pagePosition = savedInstanceState.getInt(MainActivity.PARAMS_PAGE_INDEX, -1);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                actionBean = (ActionBean) bundle.getSerializable(Constants.PARAMS_ACTION);
                currentPosition = bundle.getInt(MainActivity.PARAMS_PAGE_INDEX, -1);
           }
        }
        MobClickUtils.onEvent(StatisticConstant.LAUNCH_DISCOVERY);
        checkVersion();
        sharedPre = new SharedPre(this);
        verifyStoragePermissions(this, REQUEST_EXTERNAL_STORAGE);
        initBottomView();
        initAdapterContent();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setScrollble(false);

        //为服务器授权
        grantPhone();
        try {
            initLocation();
            grantLocation();
        } catch (Exception e) {

        }
        connectIM();
        receivePushMessage(getIntent());
        new Thread(new CalaCacheThread()).start();//计算缓存图片大小
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MLog.e("umengLog" + getDeviceInfo(this));
        showAdWebView(getIntent().getStringExtra("url"));

        if (actionBean != null) {
            ActionController actionFactory = ActionController.getInstance();
            actionFactory.doAction(this, actionBean);
        }

        if (pagePosition != -1) {
            currentPosition = pagePosition;
            mViewPager.setCurrentItem(currentPosition);
        }

        requesetBattery();
        requestImAnalysisConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DefaultSSLSocketFactory.resetSSLSocketFactory(this);
        if (currentPosition == 0) {
//            final String versionName = SharedPre.getString(HomeCustomLayout.PARAMS_LAST_GUIDE_VERSION_NAME, "");
//            if (BuildConfig.VERSION_NAME.equals(versionName)) {
                GiftController.getInstance(this).showGiftDialog();
//            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GiftController.getInstance(this).abortion();
    }

    private void checkVersion() {
        int resourcesVersion = new SharedPre(this).getIntValue(SharedPre.RESOURCES_H5_VERSION);
        RequestCheckVersion requestCheckVersion = new RequestCheckVersion(this, resourcesVersion);
        HttpRequestUtils.request(this, requestCheckVersion, this, false);
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

    private void testPush() {
//        String teset  = "{\"action\":\"{\\\"t\\\":\\\"2\\\",\\\"v\\\":\\\"16\\\"}\",\"orderNo\":\"Z191195516914\",\"type\":\"G1\",\"orderType\":\"888\",\"sound\":\"newOrder.mp3\"}";
        String teset  = "{\"orderNo\":\"Z191195516914\",\"subOrderNo\":\"R1Z191195516914\",\"type\":\"G1\",\"orderType\":\"888\",\"sound\":\"newOrder.mp3\"}";
        PushMessage pushMessage = (PushMessage) JsonUtils.fromJson(teset, PushMessage.class);
        pushMessage.title = "";
        pushMessage.message = "您有1个新订单，能收到声音吗,请赶快登录皇包车-司导端APP去接单吧";
        PushUtils.showNotification(pushMessage);
    }

    private void showAdWebView(String url){
        if(null != url) {
            if (UserEntity.getUser().isLogin(activity)) {
                url = CommonUtils.getBaseUrl(url) + "userId=" + UserEntity.getUser().getUserId(activity) + "&t=" + new Random().nextInt(100000);
            }
            Intent intent = new Intent(activity,WebInfoActivity.class);
            intent.putExtra(WebInfoActivity.WEB_URL, url);
            startActivity(intent);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (actionBean != null) {
            outState.putSerializable(Constants.PARAMS_ACTION, actionBean);
            outState.putInt(MainActivity.PARAMS_PAGE_INDEX, currentPosition);
        }
    }


    Timer timer;
    TimerTask timerTask;

    public void uploadLocation() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                String lat = new SharedPre(MainActivity.this).getStringValue("lat");
                String lng = new SharedPre(MainActivity.this).getStringValue("lng");
                Log.e("========", "============lat=" + lat + "====lng=" + lng);

                if (!TextUtils.isEmpty(lat)) {
                    RequestUploadLocation requestUploadLocation = new RequestUploadLocation(MainActivity.this);
                    HttpRequestUtils.request(MainActivity.this, requestUploadLocation, MainActivity.this, false);

                }
            }
        };
        timer.schedule(timerTask, 0, 30000);
    }

    /**
     * 授权获取手机信息权限
     */
    private void grantPhone() {
        MPermissions.requestPermissions(MainActivity.this, PermissionRes.READ_PHONE_STATE, android.Manifest.permission.READ_PHONE_STATE);
    }

    @PermissionGrant(PermissionRes.READ_PHONE_STATE)
    public void requestPhoneSuccess() {
        try {
            JPushInterface.setAlias(MainActivity.this, PhoneInfo.getIMEI(this), new TagAliasCallback() {
                @Override
                public void gotResult(int code, String alias, Set<String> tags) {
                    PushUtils.uploadPushAlias(code, alias);
                }
            });
            MiPushClient.setAlias(getApplicationContext(), PhoneInfo.getIMEI(this), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PermissionDenied(PermissionRes.READ_PHONE_STATE)
    public void requestPhoneFailed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.grant_fail_title);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
            dialog.setMessage(R.string.grant_fail_phone1);
        } else {
            dialog.setMessage(R.string.grant_fail_phone);
            dialog.setPositiveButton(R.string.grant_fail_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    grantPhone();
                }
            });
        }
        dialog.setNegativeButton(R.string.grant_fail_btn_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        dialog.show();
    }

    private void uploadPushClick(String pushId) {
        RequestPushClick request = new RequestPushClick(this, pushId);
        HttpRequestUtils.request(this, request, this);
    }

    private void initAdapterContent() {
        //fgHome = new FgHome();
        fgHomePage = new FgHomePage();
        fgDestination = new FgDestination();
        fgTravel = new FgTravel();
        fgChat = new FgNimChat();
        fgMySpace = new FgMySpace();
        //addFragment(fgHome);
        addFragment(fgHomePage);
        addFragment(fgDestination);
        addFragment(fgChat);
        addFragment(fgTravel);
        addFragment(fgMySpace);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
            EventBus.getDefault().unregister(this);
            ApiReportHelper.getInstance().commitAllReport();
            ApiReportHelper.getInstance().abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestPushToken) {
            MLog.e(request.getData().toString());
        } else if (request instanceof RequestUploadLocation) {
            LocationUtils.cleanLocationInfo(MainActivity.this);
            String cityId = ((RequestUploadLocation) request).getData().cityId;
            String cityName = ((RequestUploadLocation) request).getData().cityName;
            String countryId = ((RequestUploadLocation) request).getData().countryId;
            String countryName = ((RequestUploadLocation) request).getData().countryName;
            LocationUtils.saveLocationCity(MainActivity.this, cityId, cityName, countryId, countryName);
//            MLog.e("Location: cityId:"+cityId + ",  cityName:"+cityName);
        } else if (request instanceof RequestCheckVersion) {
            RequestCheckVersion requestCheckVersion = (RequestCheckVersion) request;
            cvBean = requestCheckVersion.getData();
            UserEntity.getUser().setIsNewVersion(this, cvBean.hasAppUpdate);//是否有新版本
            dialogUtil = DialogUtil.getInstance(this);
            if (Constants.CHANNEL_GOOGLE_PLAY.equals(BuildConfig.FLAVOR)) {//google play
                dialogUtil.showUpdateDialog(cvBean.hasAppUpdate, cvBean.force, cvBean.content, cvBean.url);
            } else {
                dialogUtil.showUpdateDialog(cvBean.hasAppUpdate, cvBean.force, cvBean.content, cvBean.url, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            boolean isVerify = verifyStoragePermissions(activity, REQUEST_EXTERNAL_STORAGE_UPDATE);
                            if (!isVerify) {
                                downloadApk();
                            }
                        } else {
                            downloadApk();
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            boolean isVerify = verifyStoragePermissions(activity, REQUEST_EXTERNAL_STORAGE_DB);
                            if (!isVerify) {
                                updateDb();
                            }
                        } else {
                            updateDb();
                        }
                    }
                });
            }

        }
    }

    public void downloadApk() {
        if (cvBean == null || dialogUtil == null) {
            return;
        }
        if (cvBean.force && dialogUtil.getVersionDialog() != null) {
            try {
                Field field = dialogUtil.getVersionDialog().getClass().getSuperclass().getDeclaredField("mShowing");
                field.setAccessible(true);
                field.set(dialogUtil.getVersionDialog(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PushUtils.startDownloadApk(MainActivity.this, cvBean.url);
    }

    public void updateDb() {
        if (cvBean == null) {
            return;
        }
        //在版本检测后 检测DB
        UpdateResources.checkRemoteDB(MainActivity.this, cvBean.dbDownloadLink, cvBean.dbVersion, new CheckVersionCallBack() {
            @Override
            public void onFinished() {
            }
        });
    }

    public static boolean verifyStoragePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, requestCode);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
//        super.onDataRequestError(errorInfo, request);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                actionBean = (ActionBean) bundle.getSerializable(Constants.PARAMS_ACTION);
                int pagePosition = bundle.getInt(MainActivity.PARAMS_PAGE_INDEX, -1);
                if (pagePosition != -1) {
                    currentPosition = pagePosition;
                    mViewPager.setCurrentItem(currentPosition);
                }
            }
            if (actionBean != null) {
                ActionController actionFactory = ActionController.getInstance();
                actionFactory.doAction(this, actionBean);
            }
        }
        receivePushMessage(intent);
    }

    private void receivePushMessage(Intent intent) {
        if (intent == null) {
            return;
        }
        PushMessage message = (PushMessage) intent.getSerializableExtra(MainActivity.PUSH_BUNDLE_MSG);
        if (message != null) {
            uploadPushClick(message.messageID);
            ActionBean actionBean = message.getActionBean();
            if (actionBean != null) {
                actionBean.source = "push调起";
                ActionController actionFactory = ActionController.getInstance();
                actionFactory.doAction(this, actionBean);
                this.actionBean = actionBean;
            } else {
                if ("IM".equals(message.type)) {
                    gotoChatList();
//                } else if ("888".equals(message.orderType)) {
//                    if (getFragmentList().size() > 3) {
//                        for (int i = getFragmentList().size() - 1; i >= 3; i--) {
//                            getFragmentList().get(i).finish();
//                        }
//                    }
//                    if (mViewPager != null) {
//                        mViewPager.setCurrentItem(2);
//                    }
                } else {//其中之一 type = C13 提醒用户选司导
                    gotoOrder(message);
                }
            }
        }
    }

    private void gotoChatList() {
        //如果是收到消息推送 关了上层的页面
        if (getFragmentList().size() > 4) {
            for (int i = getFragmentList().size() - 1; i >= 4; i--) {
                getFragmentList().get(i).finish();
            }
        }
        //跳转到聊天列表
        mViewPager.setCurrentItem(2);
    }

    private void gotoOrder(PushMessage message) {
        OrderDetailActivity.Params params = new OrderDetailActivity.Params();
        params.orderType = CommonUtils.getCountInteger(message.orderType);
        params.orderId = message.orderNo;
        if ("888".equals(message.orderType) && !TextUtils.isEmpty(message.subOrderNo)) {
            params.subOrderId = message.subOrderNo;
        }
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, params.source);
        startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case SET_MAIN_PAGE_INDEX:
                lastPagerPosition = currentPosition;
                int index = Integer.valueOf(action.data.toString());
                if (index >= 0 && index < 4)
                    mViewPager.setCurrentItem(index);
                break;
            default:
                break;
        }
    }

    private void connectIM() {
        if (UserEntity.getUser().isLogin(this))
            IMUtil.getInstance().connect();
    }

    private void initBottomView() {
        tabMenu[0] = (TextView) findViewById(R.id.tab_text_1);
        tabMenu[1] = (TextView) findViewById(R.id.tab_text_2);
        tabMenu[2] = (TextView) findViewById(R.id.tab_text_3);
        tabMenu[3] = (TextView) findViewById(R.id.tab_text_4);
        tabMenu[4] = (TextView) findViewById(R.id.tab_text_5);
        tabMenu[0].setSelected(true);
    }

    private long exitTime;

    @Override
    public void onBackPressed() {
//        if (fgHome != null && fgHome.closeGuideView()) {//关掉引导遮罩
//            return;
//        }
//        if (fgHomePage != null && fgHomePage.closeGuideView()) {//关掉引导遮罩
//            return;
//        }
        if (getFragmentList().size() > mSectionsPagerAdapter.getCount()) {
            doFragmentBack();
        } else if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0);
        } else {
            long times = System.currentTimeMillis();
            if ((times - exitTime) > 2000) {
                CommonUtils.showToast("再次点击退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }


    @Override
    public int getContentId() {
        contentId = R.id.main_layout;
        return contentId;
    }


    @OnClick({R.id.tab_text_1, R.id.tab_text_2, R.id.tab_text_3, R.id.tab_text_4,R.id.tab_text_5})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_text_1:
                mViewPager.setCurrentItem(0);
                MobClickUtils.onEvent(StatisticConstant.LAUNCH_DISCOVERY);
                break;
            case R.id.tab_text_2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tab_text_3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tab_text_4:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.tab_text_5:
                mViewPager.setCurrentItem(4);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        MLog.e("onPageSelected = " + position);
        for (int i = 0; i < tabMenu.length; i++) {
            tabMenu[i].setSelected(position == i);
        }
        currentPosition = position;
        if (position == 0) {
            GiftController.getInstance(this).showGiftDialog();
            MobClickUtils.onEvent(StatisticConstant.LAUNCH_DISCOVERY);
        } else {
            GiftController.getInstance(this).abortion();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //通讯录
    private final int PICK_CONTACTS = 101;

    // 接收通讯录的选择号码事件
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            if (PICK_CONTACTS == requestCode) {
                Uri result = data.getData();
                String[] contact = PhoneInfo.getPhoneContacts(this, result);
                EventBus.getDefault().post(new EventAction(EventType.CONTACT, contact));
            }
        }
    }

    @Override
    public String getEventId() {
        return super.getEventId();
    }

    @Override
    public String getEventSource() {
        return "个人中心-用户信息";
    }

    @Override
    public Map getEventMap() {
        return super.getEventMap();
    }

    /**
     * 判断是否登录
     */
    private boolean isLogin(String source) {
        if (UserEntity.getUser().isLogin(this)) {
            return true;
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            startActivity(intent);
            return false;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return fgHomePage;
                }
                case 1: {
                    return fgDestination;
                }
                case 2: {
                    return fgChat;
                }
                case 3: {
                    return fgTravel;
                }
                case 4: {
                    return fgMySpace;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "发现";
                case 1:
                    return "目的地";
                case 2:
                    return "私聊";
                case 3:
                    return "行程";
                case 4:
                    return "我的";
            }
            return null;
        }
    }

    public void setIMCount(int count, int serviceMsgCount) {
        if (count > 0) {
            if (count > 99) {
                bottomPoint2.setText("99+");
            } else {
                bottomPoint2.setText("" + count);
            }
            bottomPoint2.setVisibility(View.VISIBLE);
            qyServiceUnreadMsgCount.setVisibility(View.GONE);
        } else if (serviceMsgCount > 0) {
            bottomPoint2.setVisibility(View.GONE);
            qyServiceUnreadMsgCount.setVisibility(View.VISIBLE);
        } else {
            bottomPoint2.setVisibility(View.GONE);
            bottomPoint2.setText("");
            qyServiceUnreadMsgCount.setVisibility(View.GONE);
        }

    }

    public void restartApp() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent restartIntent = PendingIntent.getActivity(
                this.getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //退出程序 重启应用
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); //  重启应用
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    MLog.e("==========PERMISSION_GRANTED=========");
                    requestLocation();
                } else {
                    // permission denied
                    MLog.e("==========denied=========");
                }
                break;
            case REQUEST_EXTERNAL_STORAGE_UPDATE:
                downloadApk();
                break;
            case REQUEST_EXTERNAL_STORAGE_DB:
                updateDb();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void exitApp() {
        restartApp();
        super.exitApp();
    }

    class CalaCacheThread implements Runnable {
        public void run() {
            long cacheSize = calculateCacheFileSize();
            sharedPre.saveLongValue(SharedPre.CACHE_SIZE, cacheSize);
        }
    }

    private long calculateCacheFileSize() {
        long length = 0L;
        try {
            String cachePath = Glide.getPhotoCacheDir(MyApplication.getAppContext()).getPath();
            File cacheDir1 = new File(cachePath);
            if (cacheDir1 != null) {
                length += FileUtil.getFileOrDirSize(cacheDir1);
            }
        } catch (Exception e) {

        }
        return length;
    }

    public void grantLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
        } else {
            requestLocation();
        }
    }


    public void requestLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListener);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    LocationManager locationManager;
    LocationListener locationListener;
    public void initLocation(){
        if(!LocationUtils.gpsIsOpen(this)){
            AlertDialog dialog = AlertDialogUtils.showAlertDialog(this, "没有开启GPS定位,请到设置里开启", "设置", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LocationUtils.openGPSSeting(MainActivity.this);
                    dialog.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String locStr =String.format("%s\n lat=%f \n lng=%f \n(%f meters)", location.getProvider(),
                                location.getLatitude(), location.getLongitude(), location.getAccuracy());

                LocationUtils.saveLocationInfo(MainActivity.this,location.getLatitude()+"",location.getLongitude()+"");


                // 神策 公共属性
                try {
                    JSONObject properties = new JSONObject();
                    properties.put("longitude", location.getLongitude());   // 经度
                    properties.put("latitude", location.getLatitude());     // 纬度
                    SensorsDataAPI.sharedInstance(MainActivity.this).registerSuperProperties(properties);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(timer == null) {
                    uploadLocation();
                }
//                MLog.e(locStr);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                LocationUtils.cleanLocationInfo(MainActivity.this);
            }
        };

    }



    @SuppressLint("NewApi")
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测是否有手机白名单设置，如果没有则弹框要求增加
     */
    private void requesetBattery() {
        try {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
                    //如果没有系统白名单设置，则弹框要求加入白名单
                    Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).setData(Uri.parse("package:" + getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
        }
    }

    //请求im统计开关
    private void requestImAnalysisConfig(){
        RequestImAnalysisSwitch requestImAnalysisSwitch = new RequestImAnalysisSwitch(MyApplication.getAppContext());
        HttpRequestUtils.request(this, requestImAnalysisSwitch, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                Object o  = request.getData();
                if(o!=null && o instanceof String){
                    try{
                        String value = (String)o;
                        JSONObject jsonObject = new JSONObject(value);
                        String open = jsonObject.optString("data");
                        if("1".equals(open)){// 1：打开IM统计 0:关闭IM统计
                            ImAnalysisUtils.setOpen(ImAnalysisUtils.SWITCHER_OPEN);
                            MyApplication.startImAnalysisService();
                        }else{
                            ImAnalysisUtils.setOpen(ImAnalysisUtils.SWITCHER_CLOSE);
                            Intent intent = new Intent(MyApplication.getAppContext(),ImAnalysisService.class);
                            MyApplication.getAppContext().stopService(intent);
                        }
                    }catch (Exception e){
                    }
                }
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            }
        }, false);
    }


}
