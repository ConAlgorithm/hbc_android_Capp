package com.hugboga.custom.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.huangbaoche.hbcframe.data.bean.UserEntity;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.request.RequestUploadLogs;
import com.hugboga.custom.utils.UploadLogs;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 日志统计
 * Created by admin on 2016/3/4.
 */
public class LogService extends Service {
    public static final  String KEY_IS_RUNNING="KEY_IS_RUNNING";

    CheckThread thread;
    @Override
    public void onCreate() {
        super.onCreate();
        thread = new CheckThread();
        thread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        thread.isRunning = intent.getBooleanExtra(KEY_IS_RUNNING,false);
        LogUtil.e("LogService onBind = "+thread.isRunning);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread.isRunning = intent.getBooleanExtra(KEY_IS_RUNNING,false);
        LogUtil.e("LogService onStartCommand = "+thread.isRunning);
        return super.onStartCommand(intent, flags, startId);
    }

    class CheckThread extends Thread{
        public   boolean isRunning = true;
        @Override
        public void run() {
            do{
                try {
                    //休息10秒
                    Thread.sleep(3000);
                    if(!isRunning){
                        MLog.i("DEBUG 调试模式停止");
                    }
                    LogUtil.e("logService upload log " + UserEntity.getUser().logBuilder.length() + " UserEntity =" + UserEntity.getUser());
                    String cmd ="logcat -d "+MLog.TAG+":i *:S";
                    String cmdCancel = "logcat -c";
                    MLog.e("cmd = " + cmd);
                    Process logcatProcess = Runtime.getRuntime().exec(cmd);
                    StringBuffer str =inputToStr(logcatProcess.getInputStream());
                    MLog.e("logcat e = " + str.length());
                    uploadLogs(str);
                    Runtime.getRuntime().exec(cmdCancel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }while (isRunning);
            LogService.this.stopSelf();
            MLog.e("MLogService exit");
    }

}

    private void uploadLogs(StringBuffer sb) {
        if(sb.length()==0)return;
        RequestUploadLogs request = new RequestUploadLogs(getApplication(),sb);
        HttpRequestUtils.request(getApplication(),request,httpListener,null);
    }

    HttpRequestListener httpListener = new HttpRequestListener() {
        @Override
        public void onDataRequestSucceed(BaseRequest request) {
           MLog.e(request.getData().toString());
            thread.isRunning = false;
        }

        @Override
        public void onDataRequestCancel(BaseRequest request) {

        }

        @Override
        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

        }
    };

    private StringBuffer inputToStr(InputStream is) throws IOException {
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i; (i = is.read(buf)) != -1;) {
            baos.write(buf, 0, i);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(baos.toString("UTF-8"));
        return sb;
    }


}
