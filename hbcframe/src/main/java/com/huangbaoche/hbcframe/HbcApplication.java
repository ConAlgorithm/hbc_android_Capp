package com.huangbaoche.hbcframe;

import android.app.Application;
import android.util.Log;

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
}
