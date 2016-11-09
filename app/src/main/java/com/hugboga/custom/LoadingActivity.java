package com.hugboga.custom;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.ImageUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.PermissionRes;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.UpdateResources;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@ContentView(R.layout.activity_loading)
public class LoadingActivity extends BaseActivity implements HttpRequestListener {

    AlertDialog versionDialog; //版本更新弹窗
    HttpRequestUtils mHttpUtils;
    final Long aLong = 2000l;
    Long start = 0l;
    TextView timeSecond;
    private ErrorHandler errorHandler;

    TextView bottom_txt;
    ImageView show_ad;

    private ActionBean actionBean;

    int loading_time = 3;
    @Override
    protected void onStart() {
        super.onStart();
        grantPhone(); //先对手机授权
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        UIUtils.initDisplayConfiguration(LoadingActivity.this);
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, "55ccb4cfe0f55ab500004a9d", ChannelUtils.getChannel(this));
        MobclickAgent.startWithConfigure(config);

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
        if (getString(R.string.hbc_scheme).equals(scheme)) {

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
                }
            }
        }
    }


   public void initView() {
        show_ad = (ImageView) findViewById(R.id.show_ad);
        bottom_txt = (TextView) findViewById(R.id.bottom_txt);
        UpdateResources.checkLocalDB(this);
//        UpdateResources.checkLocalResource(this);
        if (PhoneInfo.isNewVersion(LoadingActivity.this)) {
            //新版本清空Accesskey，使请求重新获取
            UserEntity.getUser().setAccessKey(LoadingActivity.this, null);
        }
        getAD();
        timeSecond = (TextView) findViewById(R.id.time_second);
        timeSecond.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                MobClickUtils.onEvent(StatisticConstant.CLICK_SKIP_ACTIVITY);
                handler.sendEmptyMessage(200);
            }
        });
        timeSecond.setText(String.format(getString(R.string.loading_time),loading_time+""));
        handler.postDelayed(runnable,1000);
    }

//    Handler timeHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(loading_time > 0) {
                --loading_time;
                timeSecond.setText(String.format(getString(R.string.loading_time), loading_time + ""));
                if(loading_time > 0){
                    handler.postDelayed(runnable, 1000);
                }else{
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
        initView();
    }

    @PermissionDenied(PermissionRes.READ_PHONE_STATE)
    public void requestPhoneFailed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoadingActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.grant_fail_title);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
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



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(!adClick) {
                goNext();
            }
        }
    };


    private void goNext(){
        adClick = true;
        Intent intent = null;
        handler.removeMessages(200);
        if (PhoneInfo.isNewVersion(LoadingActivity.this)) {
            new SharedPre(this).setTravelFundHintIsShow(true);
            intent = new Intent(LoadingActivity.this, SplashActivity.class);
        } else {
            new SharedPre(this).setTravelFundHintIsShow(false);
            intent = new Intent(LoadingActivity.this, MainActivity.class);
        }
        if (actionBean != null) {
            intent.putExtra(Constants.PARAMS_ACTION, actionBean);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestADPicture) {
            RequestADPicture requestADPicture = (RequestADPicture) request;
            ADPictureBean adPictureBean = requestADPicture.getData();
            if (adPictureBean.displayFlag.equalsIgnoreCase("1")) {
                bottom_txt.setVisibility(View.GONE);
                showAd(adPictureBean);
            }
        }

    }

    boolean adClick = false;
    private void showAd(final ADPictureBean adPictureBean) {
        try {
            if (ImageUtils.getScreenWidth(this) <= 720) {
                String imgUrl = adPictureBean.picList.get(0).picture;
                Tools.showImage(getApplicationContext(), show_ad, imgUrl);
            } else if (ImageUtils.getScreenWidth(this) > 1080) {
                String imgUrl = adPictureBean.picList.get(2).picture;
                Tools.showImage(getApplicationContext(), show_ad, imgUrl);
            } else {
                String imgUrl = adPictureBean.picList.get(1).picture;
                Tools.showImage(getApplicationContext(), show_ad, imgUrl);
            }
            show_ad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(adPictureBean.urlAddress)) {
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
        if (errorHandler == null) {
            errorHandler = new ErrorHandler(this, this);
        }
        errorHandler.onDataRequestError(errorInfo, request);
        errorHandler = null;
        DialogUtil.getInstance(this).dismissLoadingDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
