package com.hugboga.custom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ADPictureBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestADPicture;
import com.hugboga.custom.data.request.RequestAccessKey;
import com.hugboga.custom.data.request.RequestUpdateDeviceInfo;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.ImageUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.NotificationCheckUtils;
import com.hugboga.custom.utils.PermissionRes;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.UpdateResources;
import com.hugboga.custom.widget.DialogUtil;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.umeng.analytics.MobclickAgent;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

public class LoadingActivity extends BaseActivity implements HttpRequestListener {

    AlertDialog versionDialog; //版本更新弹窗
    HttpRequestUtils mHttpUtils;
    final Long aLong = 2000l;
    Long start = 0l;
    TextView timeSecond;
    private ErrorHandler errorHandler;

    ImageView bottom_txt;
    ImageView show_ad;

    private ActionBean actionBean;

    int loading_time = 3;

    @Override
    protected void onStart() {
        super.onStart();
        grantPhone(); //先对手机授权
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_loading;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        appLaunchCount();

        UIUtils.initDisplayConfiguration(LoadingActivity.this);
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, "55ccb4cfe0f55ab500004a9d", ChannelUtils.getChannel(this));
        MobclickAgent.startWithConfigure(config);

        schemeIntent(getIntent());

        setSensorsEvent();
    }

    private void appLaunchCount() {
        int count = SharedPre.getInteger(SharedPre.APP_LAUNCH_COUNT, 0);
        SharedPre.setInteger(SharedPre.APP_LAUNCH_COUNT, ++count);
    }

    private void setSensorsEvent() {
        try {
            //启动APP
            final int appLaunchCount = SharedPre.getInteger(SharedPre.APP_LAUNCH_COUNT, 0);
            JSONObject properties = new JSONObject();
            if (MyApplication.getChannelNum() != null) {
                properties.put("hbc_channelId", MyApplication.getChannelNum());
            } else {
                properties.put("hbc_channelId", BuildConfig.FLAVOR);
            }

            properties.put("hbc_is_first_time", appLaunchCount <= 1 ? true : false);
            properties.put("is_login_id", UserEntity.getUser().isLogin(this));
            properties.put("is_open_push", NotificationCheckUtils.notificationIsOpen(this));
            SensorsDataAPI.sharedInstance(this).track("wakeup_app", properties);

            // 公共属性
            JSONObject properties2 = new JSONObject();
            String language = "";
            Configuration conf = getResources().getConfiguration();
            if (TextUtils.equals(conf.locale.toString(), Locale.SIMPLIFIED_CHINESE.toString())) {//中文简体
                language = "简体中文";
            } else if (TextUtils.equals(conf.locale.toString(), Locale.TRADITIONAL_CHINESE.toString())) {//台湾
                language = "繁體中文";
            } else if (TextUtils.equals(conf.locale.getLanguage(), "zh") && TextUtils.equals(conf.locale.getCountry(), "HK")) {//香港
                language = "繁體中文";
            } else if (BuildConfig.FLAVOR == Constants.CHANNEL_GOOGLE_PLAY) {//google play默认繁体中文
                language = "繁體中文";
            } else {
                language = "简体中文";
            }
            properties2.put("language", language);
            SensorsDataAPI.sharedInstance(this).registerSuperProperties(properties2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        schemeIntent(intent);
    }

    private void schemeIntent(Intent _intent) {
        Intent intent = _intent;
        String scheme = intent.getScheme();
        if (Constants.SCHEMA_HBCC.equals(scheme)) {
            String data = null;

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                data = extras.getString("action");
                MLog.i("hbcc 短信跳转 intent" + data);
            }
            if (TextUtils.isEmpty(data)) {
                String dataString = intent.getDataString();
                if (!TextUtils.isEmpty(dataString)) {
                    try {
                        dataString = URLDecoder.decode(dataString, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    MLog.i("hbcc 短信跳转 all " + dataString);
                    scheme += "://?";
                    data = dataString.substring(scheme.length(), dataString.length());
                    MLog.i("hbcc 短信跳转 " + data);
                }
            }
            if (!TextUtils.isEmpty(data)) {
                try {
                    actionBean = (ActionBean) JsonUtils.fromJson(data, ActionBean.class);
                    actionBean.source = "外部调起";
                } catch (Exception e) {
                    //为空就直接进首页,没毛病
                    MLog.i("hbcc 短信跳转 解析出错或数据为空");
                }
            }
        }

    }


    public void initView() {
        show_ad = (ImageView) findViewById(R.id.show_ad);
        bottom_txt = (ImageView) findViewById(R.id.bottom_txt);
        UpdateResources.checkLocalDB(this);
//        UpdateResources.checkLocalResource(this);
        if (PhoneInfo.isNewVersion(LoadingActivity.this)) {
            //新版本清空Accesskey，使请求重新获取
            UserEntity.getUser().setAccessKey(LoadingActivity.this, null);
        }
        getAD();
        timeSecond = (TextView) findViewById(R.id.time_second);
        timeSecond.setVisibility(View.GONE);
        timeSecond.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                MobClickUtils.onEvent(StatisticConstant.CLICK_SKIP_ACTIVITY);
                handler.sendEmptyMessage(200);
            }
        });
        timeSecond.setText(String.format(getString(R.string.loading_time), loading_time + ""));
    }

    //    Handler timeHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (loading_time > 0) {
                --loading_time;
                timeSecond.setText(String.format(getString(R.string.loading_time), loading_time + ""));
                if (loading_time > 0) {
                    handler.postDelayed(runnable, 1000);
                } else {
                    handler.sendEmptyMessage(200);
                }
            }
        }
    };


    private void getAD() {
        RequestADPicture requestADPicture = new RequestADPicture(this);
        HttpRequestUtils.request(this, requestADPicture, this, false);
    }

    /**
     * 授权获取手机信息权限
     */
    private void grantPhone() {
        MPermissions.requestPermissions(LoadingActivity.this, PermissionRes.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);
    }

    @PermissionGrant(PermissionRes.READ_PHONE_STATE)
    public void requestPhoneSuccess() {
        if (!checkPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            requestPhoneFailed();
            return;
        }
        initView();
        try {
            requestKey(TextUtils.isEmpty(UserEntity.getUser().getAccessKey(this)));
        } catch (Exception e) {
        }
    }

    @PermissionDenied(PermissionRes.READ_PHONE_STATE)
    public void requestPhoneFailed() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            AlertDialogUtils.showAlertDialog(LoadingActivity.this, false
                    , getString(R.string.grant_fail_title), getString(R.string.grant_fail_phone1), getString(R.string.grant_fail_btn_exit)
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
        } else {
            AlertDialogUtils.showAlertDialog(LoadingActivity.this, false
                    , getString(R.string.grant_fail_title), getString(R.string.grant_fail_phone)
                    , getString(R.string.grant_fail_btn), getString(R.string.grant_fail_btn_exit)
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            grantPhone();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
        }
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

    /**
     * 请求获取AccessKey
     */
    private void requestKey(Boolean isEmpty) {
        BaseRequest baseRequest = isEmpty ? new RequestAccessKey(this) : new RequestUpdateDeviceInfo(this);
        HttpRequestUtils.request(this, baseRequest, new HttpRequestListener() {

            @Override
            public void onDataRequestSucceed(BaseRequest _request) {
                if (_request instanceof RequestAccessKey) {
                    UserSession.getUser().setAccessKey(LoadingActivity.this, (String) _request.getData());
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!adClick) {
                goNext();
            }
        }
    };


    private void goNext() {
        adClick = true;
        Intent intent = null;
        handler.removeMessages(200);
//         if (PhoneInfo.isNewVersion(LoadingActivity.this)) {
//            new SharedPre(this).setTravelFundHintIsShow(true);
//             UserEntity.getUser().setVersion(this, PhoneInfo.getSoftwareVersion(this));
//         } else {
//             new SharedPre(this).setTravelFundHintIsShow(false);
//         }

        if (PhoneInfo.isNewVersion(LoadingActivity.this)) {
            //new SharedPre(this).setTravelFundHintIsShow(true);
            intent = new Intent(LoadingActivity.this, SplashActivity.class);
        } else {
            //new SharedPre(this).setTravelFundHintIsShow(false);
            intent = new Intent(LoadingActivity.this, MainActivity.class);
        }
        if (actionBean != null) {
            intent.putExtra(Constants.PARAMS_ACTION, actionBean);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestADPicture) {
            RequestADPicture requestADPicture = (RequestADPicture) request;
            ADPictureBean adPictureBean = requestADPicture.getData();
            if (adPictureBean.displayFlag.equalsIgnoreCase("1")) {
                handler.postDelayed(runnable, 1000);
                timeSecond.setVisibility(View.VISIBLE);
                bottom_txt.setVisibility(View.GONE);
                showAd(adPictureBean);
            } else {
                handler.sendEmptyMessage(200);
            }
        }

    }

    boolean adClick = false;

    private void showAd(final ADPictureBean adPictureBean) {
        String imgUrl;
        //Animation animation= AnimationUtils.loadAnimation(this,R.anim.loading);
        try {
            if (ImageUtils.getScreenWidth(this) <= 720) {
                imgUrl = adPictureBean.picList.get(0).picture;
            } else if (ImageUtils.getScreenWidth(this) > 1080 && adPictureBean.picList.size() >= 3) {
                imgUrl = adPictureBean.picList.get(2).picture;
            } else {
                if (adPictureBean.picList.size() >= 2) {
                    imgUrl = adPictureBean.picList.get(1).picture;
                } else {
                    imgUrl = adPictureBean.picList.get(0).picture;
                }
            }
            if (!imgUrl.isEmpty()) {
                Tools.showAdImage(show_ad, imgUrl);
                show_ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(adPictureBean.urlAddress)) {
                            EventUtil.onDefaultEvent(StatisticConstant.CLICK_ACTIVITY, "启动页推广图");
                            EventUtil.onDefaultEvent(StatisticConstant.LAUNCH_ACTIVITY, "启动页推广图");
                            adClick = true;
                            handler.removeMessages(200);
                            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                            if (actionBean != null) {
                                intent.putExtra(Constants.PARAMS_ACTION, actionBean);
                            }
                            intent.putExtra("url", adPictureBean.urlAddress);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkToNew() {
//        DialogUtil.getInstance(this).dismissLoadingDialog();
        handler.sendEmptyMessageDelayed(200, 3000);
    }


    @Override
    public void onDataRequestCancel(BaseRequest request) {
        DialogUtil.getInstance(this).dismissLoadingDialog();
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        DialogUtil.getInstance(this).dismissLoadingDialog();
        handler.sendEmptyMessage(200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
