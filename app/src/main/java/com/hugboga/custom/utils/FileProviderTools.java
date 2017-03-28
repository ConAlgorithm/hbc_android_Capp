package com.hugboga.custom.utils;

import android.content.Context;
import android.net.Uri;

import com.hugboga.tools.FileProviderUtils;

import java.io.File;

/**
 *  兼容android7.0以后的跨应用访问权限
 * Created by ZHZEPHI on 2017-03-10.
 */

public class FileProviderTools {

    private static final String PROVIDER_AUTHORITY = "com.hugboga.custom.fileprovider";

    /**
     * 获取文件存储Uri地址
     * 使用ContentProvider形式兼容android N版本文件权限问题
     *
     * @param file
     * @return
     */
    public static Uri getProviderUri(Context context, File file) {
        /*
        使用FileProvider步骤
        1. 在AndroidManifest.xml文件中注册provider
        2. 在资源文件xml中，创建path路径
        3. 在代码中用FileProviderTools.getProviderUri进行引用
         */
        return FileProviderUtils.getProviderUri(context, PROVIDER_AUTHORITY, file);
    }
}
