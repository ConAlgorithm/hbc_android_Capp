package com.hugboga.custom.utils;

import android.app.Activity;
import android.content.Context;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.data.bean.OssTokenBean;
import com.hugboga.custom.data.bean.OssTokenKeyBean;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.ref.WeakReference;

public class UploadPicUtils {

    private WeakReference<Context> weak;
    private AlbumUploadHelper.UploadListener uploadListener; //回调函数
    private DialogUtil mDialogUtil;
    private int fid = 0;

    /**
     * 上传图片
     */
    public UploadPicUtils(Context context, File file, AlbumUploadHelper.UploadListener uploadListener) {
        this.weak = new WeakReference<>(context);
        this.uploadListener = uploadListener;
        if (mDialogUtil == null && weak.get() != null) {
            mDialogUtil = DialogUtil.getInstance((Activity) weak.get());
        }
        if (file.exists()) {
            uploadPicture(file);
        } else {
            if (uploadListener != null) {
                uploadListener.onPostUploadFail(fid, "文件不存在");
            }
        }
    }

    private void uploadPicture(final File file) {
        if (weak.get() == null) {
            return;
        }
        mDialogUtil.showLoadingDialog();
        TakeNumHelper.getInstance().getKey(weak.get(), new TakeNumHelper.KeyBackListener() {
            @Override
            public void onKeyBack(OssTokenBean ossTokenBean, OssTokenKeyBean ossTokenKeyBean) {
                if (file.length()> AlbumUploadHelper.IMAGE_SIZE) {
                    File tmpFile = ImageUtils.compressImageFile(MyApplication.getAppContext(), file);
                    uploadPicTask(ossTokenBean, ossTokenKeyBean, tmpFile, true);
                } else {
                    uploadPicTask(ossTokenBean, ossTokenKeyBean, file, false);
                }
            }

            @Override
            public void onFail(String message) {
                if (uploadListener != null) {
                    uploadListener.onPostUploadFail(fid, message);
                }
                dismissDialog();
            }
        });
    }

    private void uploadPicTask(final OssTokenBean ossTokenBean, final OssTokenKeyBean ossTokenKeyBean, final File file,final boolean tmpFile) {
        RequestParams params = new RequestParams(ossTokenBean.getAddress());
        AlbumUploadHelper.initParams(params, file, ossTokenBean, ossTokenKeyBean);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(tmpFile && file.exists()){
                    file.delete();
                }
                if (uploadListener != null) {
                    uploadListener.onPostUploadSuccess(fid, ossTokenKeyBean.getPath());
                }
                dismissDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (uploadListener != null) {
                    uploadListener.onPostUploadFail(fid, "上传服务器失败");
                }
                dismissDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (uploadListener != null) {
                    uploadListener.onPostUploadCancleAll();
                }
                dismissDialog();
            }

            @Override
            public void onFinished() {
                dismissDialog();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (uploadListener != null) {
                    if (total != 0) {
                        double value = (current / (double) total) * 100;
                        uploadListener.onPostUploadProgress(fid, ((int) value) + "%");
                    }
                }
            }
        });
    }

    private void dismissDialog() {
        if (mDialogUtil != null) {
            mDialogUtil.dismissDialog();
        }
    }

}
