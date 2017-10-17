package com.hugboga.custom.utils;

import android.content.Context;

import org.xutils.DbManager;

/**
 * Created by Administrator on 2016/2/29.
 */
public class DaoConfig {
    private static volatile DbManager.DaoConfig daoConfig = null;

    private DaoConfig() {
    }

    public static DbManager.DaoConfig getInstance(Context context) {
        if (daoConfig == null) {
            synchronized (DaoConfig.class) {
                if (daoConfig == null) {
                    daoConfig = new DbManager.DaoConfig()
                            .setDbName(DBHelper.DB_NAME);
//                            .setDbVersion(new SharedPre(context).getIntValue(SharedPre.RESOURCES_DB_VERSION, ResourcesConstants.RESOURCES_DB_VERSION_DEFAULT));
                }
            }
        }
        return daoConfig;
    }

    public static DbManager.DaoConfig getTmpConfig(Context context) {
        return      new DbManager.DaoConfig()
                            .setDbName(DBHelper.DB_NAME);
//                            .setDbVersion(new SharedPre(context).getIntValue(SharedPre.RESOURCES_DB_VERSION, ResourcesConstants.RESOURCES_DB_VERSION_DEFAULT));
    }

}
