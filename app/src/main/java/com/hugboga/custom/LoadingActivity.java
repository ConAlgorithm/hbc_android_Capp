package com.hugboga.custom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CheckVersionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestCheckVersion;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.PushUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UpdateResources;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_loading)
public class LoadingActivity extends BaseFragmentActivity implements HttpRequestListener {

    AlertDialog versionDialog; //版本更新弹窗
    HttpRequestUtils mHttpUtils;
    final Long aLong = 2000l;
    Long start = 0l;
    private ErrorHandler errorHandler;

    @Override
    protected void onStart() {
        super.onStart();

        UpdateResources.checkLocalDB(this);
        UpdateResources.checkLocalResource(this);
        if(PhoneInfo.isNewVersion(LoadingActivity.this)){
            //新版本清空Accesskey，使请求重新获取
            UserEntity.getUser().setAccessKey(LoadingActivity.this,null);
        }
        checkVersion();
    }

    private void checkVersion(){
        String version = PhoneInfo.getSoftwareVersion(LoadingActivity.this);
        int resourcesVersion = new SharedPre(this).getIntValue(SharedPre.RESOURCES_H5_VERSION);
        RequestCheckVersion requestCheckVersion = new RequestCheckVersion(this,version,resourcesVersion);
        HttpRequestUtils.request(this, requestCheckVersion, this);

    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(PhoneInfo.isNewVersion(LoadingActivity.this)){
                startActivity(new Intent(LoadingActivity.this,SplashActivity.class));
            }else{
                startActivity(new Intent(LoadingActivity.this,MainActivity.class));
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
            UserEntity.getUser().setIsNewVersion(this, !TextUtils.isEmpty(cvBean.url));//是否有新版本
            DialogUtil.getInstance(this).showUpdateDialog(cvBean.force, cvBean.content, cvBean.url, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PushUtils.startDownloadApk(LoadingActivity.this, cvBean.url);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkToNew();
                }
            });
            UpdateResources.checkRemoteResources(this, cvBean, null);
            UpdateResources.checkRemoteDB(this, cvBean.dbDownloadLink, cvBean.dbVersion, null);
        }
    }


    private void checkToNew(){
        Long time = System.currentTimeMillis()-start;
        final Long cha = aLong-time;
        if(cha<=0){
            handler.sendEmptyMessage(0);
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(cha);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    @Override
    public void onDataRequestCancel(BaseRequest request) {
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if(errorHandler==null){
            errorHandler = new ErrorHandler(this,this);
        }
        errorHandler.onDataRequestError(errorInfo, request);
        errorHandler = null;
    }

}
