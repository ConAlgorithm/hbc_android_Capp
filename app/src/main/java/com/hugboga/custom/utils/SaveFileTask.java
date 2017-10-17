package com.hugboga.custom.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SaveFileTask extends AsyncTask<String, Void, File> {

    private Context context;
    private File saveFile;
    private FileDownLoadCallBack callBack;
    private volatile String url;

    public SaveFileTask(Context context, FileDownLoadCallBack callBack) {
        this(context, null, callBack);
    }

    public SaveFileTask(Context context, File saveFile, FileDownLoadCallBack callBack) {
        this.context = context;
        this.saveFile = saveFile;
        this.callBack = callBack;
    }

    @Override
    protected File doInBackground(String... params) {
        url = params[0];
        try {
            File file = Glide.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            return file;
        } catch (Exception ex) {
            if (callBack != null) {
                callBack.onDownLoadFailed();
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(File file) {
        if (file == null) {
            if (callBack != null) {
                callBack.onDownLoadFailed();
            }
            return;
        }
        try {
            if (saveFile != null) {
                FileUtils.copyFile(file, saveFile) ;
            } else {
                saveImageToGallery(file);
            }
            if (callBack != null) {
                callBack.onDownLoadSuccess(file);
            }
        } catch (IOException e) {
            if (callBack != null) {
                callBack.onDownLoadFailed();
            }
            e.printStackTrace();
        }
    }

    public interface FileDownLoadCallBack {

        void onDownLoadSuccess(File file);

        void onDownLoadFailed();
    }

    public void saveImageToGallery(File file) {

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), url, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

}
