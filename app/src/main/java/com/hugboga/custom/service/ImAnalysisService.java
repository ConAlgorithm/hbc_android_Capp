package com.hugboga.custom.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.UploadImAnalysisInfoRequest;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.im.ImAnalysisUtils;
import com.hugboga.im.entity.ImAnalysisEnitty;
import com.hugboga.tools.HLog;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import static com.hugboga.im.entity.ImAnalysisEnitty.IM_ANALYSIS_MSG_COUNT;
import static com.hugboga.im.entity.ImAnalysisEnitty.MAXCOUNT;

/**
 * Created by SPW on 2017/6/26.
 */

public class ImAnalysisService extends Service {


    public static final long DEFAULT_DURATION = 600*1000; //上传时间间隔10分钟
    public static boolean running = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(!imAnalysisThread.isAlive()){
            imAnalysisThread.start();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,new IntentFilter(ImAnalysisEnitty.ACTION_UPLOAD));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                if(msg.obj!=null){
                    String uploadContent = (String)msg.obj;
                    uploadImAnalyMsgs(uploadContent);
                }
            }catch (Exception e){

            }

        }
    };

    Thread imAnalysisThread = new Thread(){
        @Override
        public void run() {
            super.run();
            try {
              while (running){
                 validateUploadData(1);
                   Thread.sleep(DEFAULT_DURATION);
              }
            }catch (InterruptedException e){

            }catch (Exception e){

            }
        }
    };

    private void validateUploadData(int count){
        List<String> msgs = ImAnalysisUtils.readImAnalysisMsgToList(ImAnalysisService.this.getApplicationContext());
//        for(String string:msgs){
//            Log.e("im_ana",string);
//        }
        if(msgs.size()>=count){
            Message msg = new Message();
            msg.obj = ImAnalysisEnitty.generateJsonArray(msgs);
            mHandler.sendMessage(msg);
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            validateUploadData(MAXCOUNT);
        }
    };

    public void uploadImAnalyMsgs(final String content){
        IM_ANALYSIS_MSG_COUNT  = 0;
        ImAnalysisUtils.deleteFile(ImAnalysisService.this);
        UploadImAnalysisInfoRequest guideStoryAddRequest = new UploadImAnalysisInfoRequest(this,content);
        HttpRequestUtils.request(getApplication(), guideStoryAddRequest, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                HLog.d("Upload im analysis info successful!");
                uploadFailFiles();
            }
            @Override
            public void onDataRequestCancel(BaseRequest request) {
                HLog.e("Upload im analysis info fail!");
                ImAnalysisUtils.saveUploadFailInfoToFile(ImAnalysisService.this,content);
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                HLog.e("Upload im analysis info fail! Network error");
                ImAnalysisUtils.saveUploadFailInfoToFile(ImAnalysisService.this,content);
            }
        });

    }


    private void uploadFailFiles(){
        final List<File> failFiles = ImAnalysisUtils.getFailFileList(this,ImAnalysisUtils.IM_ANALYSIS_FAIL_FILE_NAME_PREFIX);
        if(failFiles.size()>0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(File file:failFiles){
                        String content = ImAnalysisUtils.readContentFromFile(file.getPath());
                        RequestParams requestParams = new RequestParams(HbcConfig.serverHost+UrlLibs.API_UPLOAD_IMANALYSIS_INFO);

                        requestParams.addHeader("appChannel", ChannelUtils.getChannel(ImAnalysisService.this));
                        requestParams.addHeader("deviceId", com.huangbaoche.hbcframe.util.PhoneInfo.getImei(MyApplication.getAppContext()));//PhoneInfo.getImei(((BaseRequest)params).getContext()));
                        requestParams.addHeader("os","android");
                        requestParams.addHeader("appVersion",ChannelUtils.getVersion());
                        requestParams.addHeader("idfa", PhoneInfo.getIMEI(ImAnalysisService.this));
                        requestParams.addHeader("ts", "" + System.currentTimeMillis());
                        requestParams.addHeader(HbcParamsBuilder.KEY_HEADER_AK, UserSession.getUser().getAccessKey(MyApplication.getAppContext()));
                        if(UserSession.getUser().getUserToken(MyApplication.getAppContext())!=null)
                            requestParams.setHeader(HbcParamsBuilder.KEY_HEADER_UT, UserSession.getUser().getUserToken(MyApplication.getAppContext()));
                        requestParams.setAsJsonContent(true);
                        requestParams.setBodyContent(content);
                        requestParams.setSslSocketFactory(DefaultSSLSocketFactory.getSocketFactory(ImAnalysisService.this));
                        try {
                            String reuslt = x.http().postSync(requestParams, String.class);
                            if (!TextUtils.isEmpty(reuslt)) {
                                JSONObject jsonObject = new JSONObject(reuslt);
                                int status = jsonObject.optInt("status");
                                if(status==200){
                                    HLog.i("Upload fail im analysis info successful");
                                    ImAnalysisUtils.deleteFile(ImAnalysisService.this,file.getPath());
                                }else{
                                    HLog.i("Upload fail im analysis info failer");
                                }
                            }

                        } catch (Throwable e) {
                            HLog.e("Upload fail im analysis info exception", e);
                        }
                    }
                }
            }).start();
        }
    }

}
