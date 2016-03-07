package com.hugboga.custom.utils;

import android.content.Context;
import android.text.TextUtils;

import com.hugboga.custom.constants.ResourcesConstants;
//import com.hugboga.custom.data.bean.ResourcesBean;
//import com.hugboga.custom.data.parser.ParserCheckVersion;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 *
 * 更新资源，H5，DB
 * Created by admin on 2015/11/10.
 */
public class UpdateResources {


    /**
     * 检测服务器
     * @param context
     * @param parser
     * @param callBack
     */
//    public static void checkRemoteResources(Context context, ParserCheckVersion parser,RequestCallBack<File> callBack){
//
//        if(parser.resList!=null){
//            for(ResourcesBean bean:parser.resList){
//                if(ResourcesConstants.RESOURCES_H5_NAME.equals(bean.resName)){
//                    downloadResources(context, bean.resUrl,bean.resVersion,callBack);
//                }
//            }
//        }
//
//    }

    /**
     * 下载资源，解压，保存版本号
     * @param context
     * @param url
     * @param version
     * @param callBack
     */
//    public static void downloadResources(final Context context,String url, final int version,final RequestCallBack<File> callBack){
//        // data/data/com.hugboga.custom/cache/Resources/
//        final String outPath =context.getCacheDir()+ File.separator+ ResourcesConstants.RESOURCES_PATH+ File.separator;
//        // data/data/com.hugboga.custom/cache/Resources/hbc_h5
//        final String outFilePath =outPath+ ResourcesConstants.RESOURCES_LOCAL_NAME;
//        //  data/data/com.hugboga.custom/cache/Resources/hbc_h5.zip
//        final String outFile =outFilePath+ ResourcesConstants.RESOURCES_LOCAL_ZIP;
//        new HttpUtils().download(url, outFile, new RequestCallBack<File>() {
//            @Override
//            public void onSuccess(ResponseInfo<File> responseInfo) {
//                try {
//                    ZipUtils.UnZipFolder(outFile, outFilePath);
//                    File file = new File(outFile);
//                    file.delete();
//                    new SharedPre(context).saveIntValue(SharedPre.RESOURCES_H5_VERSION, version);
//                    MLog.e("onSuccess unZip " + responseInfo.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (callBack != null) callBack.onSuccess(responseInfo);
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                MLog.e("downloadResources onFailure " + msg);
//                if (callBack != null) callBack.onFailure(error, msg);
//            }
//
//            @Override
//            public void onLoading(long total, long current, boolean isUploading) {
//                MLog.e(current + "/" + total + " " + isUploading);
//                if (callBack != null) callBack.onLoading(total, current, isUploading);
//            }
//        });
//    }

    /**
     * 检测本地H5资源
     * 如果不存在，从项目资源 copy到指定目录
     * @param context
     */
  /*  public static void checkLocalResource(Context context){
        long time = System.currentTimeMillis();
        final String filePath =context.getCacheDir()+ File.separator+ ResourcesConstants.RESOURCES_PATH;
        String fileName = filePath+ File.separator+ ResourcesConstants.RESOURCES_LOCAL_NAME;
        try {
        final String resFile = ResourcesConstants.RESOURCES_LOCAL_NAME+ ResourcesConstants.RESOURCES_LOCAL_ZIP;
        File file = new File(fileName);
        if(!file.exists()){//如果不存在,从资源目录解压到项目目录
                ZipUtils.UnZipFolder(context.getResources().getAssets().open(resFile), fileName);
            new SharedPre(context).saveIntValue(SharedPre.RESOURCES_H5_VERSION, ResourcesConstants.RESOURCES_H5_VERSION_DEFAULT);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 检测本地数据库
     * @param context
     */
    public static void checkLocalDB(Context context){
        final DBHelper mDBHelper = new DBHelper(context);
        try {
            if (!mDBHelper.checkDataBase()) {//无数据库,从资源文件copy
                mDBHelper.deleteOldDb();
                mDBHelper.copyDataBase();
//                mDBHelper.getDbUtils();
                mDBHelper.getDbManager();
            }
        }catch (Exception e){
            MLog.e("checkLocalDB", e);
        }
    }

    /**
     * 检测 DB
     * @param context
     * @param url
     * @param version
     * @param callBack
     */
//    public static void checkRemoteDB(final Context context,String url, final int version,final RequestCallBack<File> callBack){
//        final DBHelper mDBHelper = new DBHelper(context);
//        int localVersion = new SharedPre(context).getIntValue(SharedPre.RESOURCES_DB_VERSION, ResourcesConstants.RESOURCES_DB_VERSION_DEFAULT);
//        MLog.e("localVersion=" + localVersion + " remoteVersion=" + version);
//        if(localVersion<version&&!TextUtils.isEmpty(url)){//从服务端更新
//            final String outFile = DBHelper.DB_PATH+ DBHelper.DB_NAME;
//            final String outFileTmp = outFile +".tmp";
//            new HttpUtils().download(url, outFileTmp, new RequestCallBack<File>() {
//                @Override
//                public void onSuccess(ResponseInfo<File> responseInfo) {
//                    if(callBack!=null)callBack.onSuccess(responseInfo);
//                    File fileTmp = new File(outFileTmp);
//                    File file = new File(outFile);
//                    mDBHelper.deleteOldDb();
//                    if(DBHelper.copyFile(fileTmp, file)){
//                        fileTmp.delete();
//                        new SharedPre(context).saveIntValue(SharedPre.RESOURCES_DB_VERSION, version);
//                        if(mDBHelper.checkDataBase()){//无数据库,从资源文件copy
//                            MLog.e("onSuccess copy DB ok ");
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(HttpException error, String msg) {
//                    MLog.e("onFailure " + msg);
//                    if(callBack!=null)callBack.onFailure(error,msg);
//                }
//
//                @Override
//                public void onLoading(long total, long current, boolean isUploading) {
//                    if(callBack!=null)callBack.onLoading(total, current,isUploading);
//                }
//            });
//        }
//    }


}
