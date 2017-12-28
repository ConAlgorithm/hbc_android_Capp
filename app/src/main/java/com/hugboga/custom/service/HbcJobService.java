package com.hugboga.custom.service;
import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PushUtils;
import com.hugboga.custom.utils.rom.Rom;
import com.hugboga.tools.HLog;

/**
 * Created by SPW on 2017/12/14.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class HbcJobService extends JobService{


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //Toast.makeText(HbcJobService.this, "HbcJobService", Toast.LENGTH_SHORT).show();
            HLog.i("HbcJobService is running");
            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, true);
            if(!MyApplication.hasAliveActivity()){
                PushUtils.initJPush();
                if (!Rom.isEmui() && !CommonUtils.isSupportGoogleService()) {
                    HLog.i("xiaomi init");
                    PushUtils.initXMpush();
                }
            }
            return true;
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Message m = Message.obtain();
        m.obj = params;
        handler.sendMessage(m);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        handler.removeCallbacksAndMessages(null);
        return false;
    }

}