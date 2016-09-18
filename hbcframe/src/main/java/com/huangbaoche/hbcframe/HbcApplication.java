package com.huangbaoche.hbcframe;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.huangbaoche.hbcframe.util.Common;
import com.huangbaoche.hbcframe.util.MLog;

import org.xutils.x;

/**
 * Created by wyouflf on 15/10/28.
 */
public class HbcApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        Log.e("hbcApplication", "debug " + BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
