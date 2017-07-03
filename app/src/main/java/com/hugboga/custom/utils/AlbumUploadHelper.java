package com.hugboga.custom.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hugboga.custom.data.bean.OssTokenBean;
import com.hugboga.custom.data.bean.OssTokenKeyBean;
import com.hugboga.custom.data.bean.Photo;
import com.hugboga.tools.HLog;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/7.
 */
public class AlbumUploadHelper {

    public static int IMAGE_SIZE = 1024 * 500;

    public static final int UPLOAD_SUCCESS = 1; //上传成功
    public static final int UPLOAD_WAITING = 2; //等待上传
    public static final int UPLOAD_PROGRESS = 3;//正在上传
    public static final int UPLOAD_FAIL = 4;  //上传失败

    private static String uploadUniqueStr = "hbc_"+System.currentTimeMillis();

    Context mContext;
    List<Photo> uploadQueue = Collections.synchronizedList(new LinkedList<Photo>());
    String sguideId;
    UploadListener uploadListener;

    static AlbumUploadHelper albumUploadHelper;

    private AlbumUploadHelper(Context context) {
        this.mContext = context;
    }

    synchronized public static AlbumUploadHelper with(Context context) {
        if (albumUploadHelper == null) {
            albumUploadHelper = new AlbumUploadHelper(context);
        }
        return albumUploadHelper;
    }


    public AlbumUploadHelper setData(List<Photo> photos) {
        this.uploadQueue.addAll(photos);
        return this;
    }

    public AlbumUploadHelper setGuideId(String id) {
        this.sguideId = id;
        return this;
    }

    public AlbumUploadHelper setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
        return this;
    }


    /**
     * 开始上传
     */
    public void startUpload() {
        uploadUniqueStr = "hbc_"+System.currentTimeMillis();
        startUpload(true);
    }

    /**
     * 从上传失败的队列的第一个图片开始上传
     */
    public void reStartUpload() {
        startUpload(true);
    }

    /**
     * 判断是否正在上传
     *
     * @return
     */
    public boolean isUploading() {
        return uploadQueue.size() > 0;
    }

    /**
     * 在上传队队尾直接添加新上传队列
     *
     * @param photos
     */
    public void appendUploadQueue(List<Photo> photos) {
        uploadQueue.addAll(photos);
        startUpload(true);
    }

    /**
     * 取消上传，清空上传队列
     */
    public void cancle() {
        if (uploadQueue != null) {
            uploadQueue.clear();
        }
        if (uploadAble != null) {
            uploadAble.cancel();

        }
    }

    /**
     * 如果是上传第一张，则直接上传，否则删除上一张已经上传的图片，继续上传下一张
     *
     * @param isFirst 是否是从队列第一张开始上传
     */
    private void startUpload(final boolean isFirst) {
        if (!isFirst) {
            if (uploadQueue != null && uploadQueue.size() > 0) {
                uploadQueue.remove(0);
            }
            if (uploadQueue.size() == 0) {
                if (uploadListener != null) {
                    uploadListener.onPostAllUploaded();
                }
                return;
            }
        }

        if (uploadQueue.size() == 0) {
            return;
        }

        getUploadKey();
    }


    private void getUploadKey() {

        TakeNumHelper.getInstance().getKey(mContext, new TakeNumHelper.KeyBackListener() {
            @Override
            public void onKeyBack(OssTokenBean ossTokenBean, OssTokenKeyBean ossTokenKeyBean) {
                uploadPre(ossTokenBean, ossTokenKeyBean);
            }

            @Override
            public void onFail(String message) {
                if (uploadListener != null) {
                    uploadListener.onPostUploadFail(uploadQueue.get(0).unquineId, message);
                }
                setFailStatus();
                //StatisticUtils.get().addBuried(StatisticUtils.EVENT_UPLOAD_PICTURE_RESULT,"os","ANDROID",StatisticUtils.UPLOAD_PICTURE_RESULT_KEY,StatisticUtils.RESULT_FAIL,"upload_id",uploadUniqueStr);

            }
        });
    }


    private void uploadPre(OssTokenBean ossTokenBean, OssTokenKeyBean ossTokenKeyBean) {
        Photo photo = uploadQueue.get(0);
        String filePath = photo.localFilePath;
        if (TextUtils.isEmpty(filePath)) {
            if (uploadListener != null) {
                uploadListener.onPostUploadFail(photo.unquineId, "文件路径为空");
            }
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            if (uploadListener != null) {
                uploadListener.onPostUploadFail(photo.unquineId, "文件不存在");
            }
            return;
        }

        boolean hasEditor = false;

        if (file.length() > IMAGE_SIZE) {
            file = ImageUtils.compressImageFile(mContext,file);
            hasEditor = true;
        }


        upload(file, photo.unquineId, hasEditor, ossTokenBean, ossTokenKeyBean);
    }

    Callback.Cancelable uploadAble;

    /**
     * 用xuitls 开始上传
     *
     * @param file
     * @param fid
     * @param hasEditor
     */
    private synchronized void upload(final File file, final int fid, final boolean hasEditor, final OssTokenBean ossTokenBean, final OssTokenKeyBean ossTokenKeyBean) {
        //StatisticUtils.get().addBuried(StatisticUtils.EVENT_UPLOAD_PICTURE,"os","ANDROID","upload_id",uploadUniqueStr);
        RequestParams params = new RequestParams(ossTokenBean.getAddress());
        initParams(params, file, ossTokenBean, ossTokenKeyBean);
        String string1 = ossTokenBean.toString();
        Log.d("zqossTokenBean",string1);
        String string2 = ossTokenKeyBean.toString();
        Log.d("zqossTokenKeyBean",string2);
        uploadAble = x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (hasEditor && file.exists()) {
                    file.delete();
                }
                HLog.i("upload success result:" + result);
                if (uploadListener != null) {
                    uploadListener.onPostUploadSuccess(fid, ossTokenKeyBean.getPath());
                }
                startUpload(false);
                //StatisticUtils.get().addBuried(StatisticUtils.EVENT_UPLOAD_PICTURE_RESULT,"os","ANDROID",StatisticUtils.UPLOAD_PICTURE_RESULT_KEY,StatisticUtils.RESULT_SUCCESS,"upload_id",uploadUniqueStr);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                HLog.i("upload onError");
                HLog.e("upload onError", ex);
                if (hasEditor && file.exists()) {
                    file.delete();
                }
                if (uploadListener != null) {
                    uploadListener.onPostUploadFail(fid, "上传服务器失败");
                }
                setFailStatus();
                //StatisticUtils.get().addBuried(StatisticUtils.EVENT_UPLOAD_PICTURE_RESULT,"os","ANDROID",StatisticUtils.UPLOAD_PICTURE_RESULT_KEY,StatisticUtils.RESULT_FAIL,"upload_id",uploadUniqueStr);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                HLog.i("upload onCancelled");
                if (hasEditor && file.exists()) {
                    file.delete();
                }
                if (uploadListener != null) {
                    uploadListener.onPostUploadCancleAll();
                }
            }

            @Override
            public void onFinished() {
                HLog.i("upload onFinished");
            }

            @Override
            public void onWaiting() {
                HLog.i("upload onWaiting");
            }

            @Override
            public void onStarted() {
                HLog.i("upload onStarted");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                HLog.i("upload onLoading total：" + total + " current:" + current);
                if (uploadListener != null) {
                    if (total != 0) {
                        double value = (current / (double) total) * 100;
                        uploadListener.onPostUploadProgress(fid, ((int) value) + "%");
                    }
                }
            }
        });
    }

    /**
     * 删除正在或等待上传的图片
     *
     * @param photos
     */
    public void deleteUploadFiles(List<String> photos) {
        if (uploadQueue == null || uploadQueue.size() == 0) {
            return;
        }
        for (Iterator<Photo> iterator = uploadQueue.iterator(); iterator.hasNext(); ) {
            Photo photo = iterator.next();
            for (String path : photos) {
                String localPath = photo.localFilePath;
                if (!TextUtils.isEmpty(localPath) && localPath.equals(path)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * 获取当前队列中剩余上传的图片个数
     *
     * @return
     */
    public int getUploadQueueSize() {
        return uploadQueue == null ? 0 : uploadQueue.size();
    }

    private void initParams(RequestParams params, File file, OssTokenBean ossTokenBean, OssTokenKeyBean ossTokenKeyBean) {
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("OSSAccessKeyId", ossTokenBean.getOssTokenParamBean().getOssAccessKeyId()));
        list.add(new KeyValue("policy", ossTokenBean.getOssTokenParamBean().getPolicy()));
        list.add(new KeyValue("Signature", ossTokenBean.getOssTokenParamBean().getSignature()));
        list.add(new KeyValue("key", ossTokenKeyBean.getKey()));
        list.add(new KeyValue("file", file));//文件流数据
        MultipartBody body = new MultipartBody(list, "utf-8");
        params.setRequestBody(body);
    }

    private void setFailStatus() {
        if(uploadQueue !=null && uploadQueue.size() >0){
            uploadQueue.get(0).uploadStatus = UPLOAD_FAIL;
        }
    }

    public interface UploadListener {
        void onPostUploadProgress(int fid, String percent);

        void onPostUploadSuccess(int fid, String uploadFileUrl);

        void onPostUploadFail(int fid, String message);

        void onPostAllUploaded();

        void onPostUploadCancleAll();
    }
}
