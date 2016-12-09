package com.hugboga.custom.widget;

import android.app.Dialog;
import android.content.Context;
import com.hugboga.custom.R;

/**
 * Created by qingcha on 16/12/9.
 */
public class GiftDialog extends Dialog {

    public GiftDialog(Context context) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.view_dialog_gift);
        setCanceledOnTouchOutside(true);
    }
}
