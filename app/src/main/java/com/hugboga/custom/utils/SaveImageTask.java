package com.hugboga.custom.utils;

import android.content.Context;
import android.os.AsyncTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SaveImageTask extends AsyncTask<String, Void, File> {

    private Context context;
    private File saveFile;
    private ImageDownLoadCallBack callBack;

    public SaveImageTask(Context context, File saveFile) {
        this(context, saveFile, null);
    }

    public SaveImageTask(Context context, File saveFile, ImageDownLoadCallBack callBack) {
        this.context = context;
        this.saveFile = saveFile;
        this.callBack = callBack;
    }

    @Override
    protected File doInBackground(String... params) {
        String url = params[0];
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
            FileUtils.copyFile(file, saveFile) ;
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

    public interface ImageDownLoadCallBack {

        void onDownLoadSuccess(File file);

        void onDownLoadFailed();
    }

}
