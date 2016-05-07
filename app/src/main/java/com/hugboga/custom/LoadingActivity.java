package com.hugboga.custom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.data.bean.ADPictureBean;
import com.hugboga.custom.data.bean.CheckVersionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestADPicture;
import com.hugboga.custom.data.request.RequestCheckVersion;
import com.hugboga.custom.service.LogService;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.ImageUtils;
import com.hugboga.custom.utils.PermissionRes;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.PushUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UpdateResources;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.AnalyticsConfig;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.io.File;

@ContentView(R.layout.activity_loading)
public class LoadingActivity extends BaseActivity implements HttpRequestListener {

    AlertDialog versionDialog; //版本更新弹窗
    HttpRequestUtils mHttpUtils;
    final Long aLong = 2000l;
    Long start = 0l;
    private ErrorHandler errorHandler;

    ImageView show_ad;
    @Override
    protected void onStart() {
        super.onStart();
        grantPhone(); //先对手机授权
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        AnalyticsConfig.setAppkey(this,"55ccb4cfe0f55ab500004a9d");
        AnalyticsConfig.setChannel("channel"+ ChannelUtils.getChannel(this));
    }

    private void initView() {
        show_ad = (ImageView) findViewById(R.id.show_ad);
        UpdateResources.checkLocalDB(this);
//        UpdateResources.checkLocalResource(this);
        if (PhoneInfo.isNewVersion(LoadingActivity.this)) {
            //新版本清空Accesskey，使请求重新获取
            UserEntity.getUser().setAccessKey(LoadingActivity.this, null);
        }
        getAD();
        checkVersion();
    }

    private void getAD(){
        RequestADPicture requestADPicture = new RequestADPicture(this);
        HttpRequestUtils.request(this,requestADPicture,this,false);
    }

    /**
     * 授权获取手机信息权限
     */
    private void grantPhone() {
        MPermissions.requestPermissions(LoadingActivity.this, PermissionRes.READ_PHONE_STATE, android.Manifest.permission.READ_PHONE_STATE);
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

    private void checkVersion() {
//        DialogUtil.getInstance(this).showLoadingDialog();
        int resourcesVersion = new SharedPre(this).getIntValue(SharedPre.RESOURCES_H5_VERSION);
        RequestCheckVersion requestCheckVersion = new RequestCheckVersion(this, resourcesVersion);
        HttpRequestUtils.request(this, requestCheckVersion, this,false);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (PhoneInfo.isNewVersion(LoadingActivity.this)) {
                startActivity(new Intent(LoadingActivity.this, SplashActivity.class));
            } else {
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            }
            finish();
//            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_loading, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCheckVersion) {
            RequestCheckVersion requestCheckVersion = (RequestCheckVersion) request;
            final CheckVersionBean cvBean = requestCheckVersion.getData();
            UserEntity.getUser().setIsNewVersion(this, cvBean.hasAppUpdate);//是否有新版本

            DialogUtil.getInstance(this).showUpdateDialog(cvBean.hasAppUpdate,cvBean.force, cvBean.content, cvBean.url, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PushUtils.startDownloadApk(LoadingActivity.this, cvBean.url);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //在版本检测后 检测DB
                    UpdateResources.checkRemoteDB(LoadingActivity.this, cvBean.dbDownloadLink, cvBean.dbVersion, new CheckVersionCallBack() {
                        @Override
                        public void onFinished() {
                            //在检测DB后检测资源
                            UpdateResources.checkRemoteResources(LoadingActivity.this, cvBean, new CheckVersionCallBack() {
                                @Override
                                public void onFinished() {
                                    checkToNew();
                                }
                            });
                        }
                    });
                }
            });
            checkUploadLog(cvBean);
        }else if(request instanceof RequestADPicture){
            RequestADPicture requestADPicture = (RequestADPicture) request;
            ADPictureBean adPictureBean = requestADPicture.getData();
            if(adPictureBean.displayFlag.equalsIgnoreCase("1")){
                showAd(adPictureBean);
            }
        }

    }


    private void showAd(ADPictureBean adPictureBean){
        try {
            if (ImageUtils.getScreenWidth(this) <= 720) {
                String imgUrl = adPictureBean.picList.get(0).picture;
                Tools.showImage(getApplicationContext(), show_ad, imgUrl);
            } else if (ImageUtils.getScreenWidth(this) >= 1080) {
                String imgUrl = adPictureBean.picList.get(2).picture;
                Tools.showImage(getApplicationContext(), show_ad, imgUrl);
            } else {
                String imgUrl = adPictureBean.picList.get(1).picture;
                Tools.showImage(getApplicationContext(), show_ad, imgUrl);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 是否开启debug模式
     */
    private void checkUploadLog( CheckVersionBean cvBean){
        MLog.e("context="+this+",resource="+cvBean+" ,isDebugMod="+cvBean.debugMod);
        if(cvBean!=null&&cvBean.debugMod){
            Intent intent = new Intent(this, LogService.class);
            intent.putExtra(LogService.KEY_IS_RUNNING,true);
            startService(intent);
        }
    }

    private void checkToNew() {
//        DialogUtil.getInstance(this).dismissLoadingDialog();
        handler.sendEmptyMessageDelayed(200,3000);
//        Long time = System.currentTimeMillis() - start;
//        final Long cha = aLong - time;
//        if (cha <= 0) {
//            handler.sendEmptyMessage(0);
//        } else {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(cha);
//                        handler.sendEmptyMessage(0);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
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
