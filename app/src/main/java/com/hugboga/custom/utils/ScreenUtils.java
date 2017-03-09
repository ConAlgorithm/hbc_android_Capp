package com.hugboga.custom.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created  on 16/4/15.
 */
public class ScreenUtils {
    public static float d2p(Context context, int px) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, r.getDisplayMetrics());
    }


    public static float sp2p(Context context, int px) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, r.getDisplayMetrics());
    }

}
