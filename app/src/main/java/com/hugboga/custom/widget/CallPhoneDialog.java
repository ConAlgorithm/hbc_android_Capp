package com.hugboga.custom.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.PhoneInfo;

/**
 * Created by qingcha on 17/8/3.
 */
public class CallPhoneDialog extends Dialog implements View.OnClickListener{

    public String guideId;
    public String guidePhone;

    public CallPhoneDialog(Context context) {
        this(context, R.style.ShareDialog);
    }

    public CallPhoneDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.view_call_dialog);

        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.width = display.getWidth();
            getWindow().setAttributes(lp);
        }

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        findViewById(R.id.dialog_call_internet_layout).setOnClickListener(this);
        findViewById(R.id.dialog_call_ordinary_layout).setOnClickListener(this);
        findViewById(R.id.dialog_call_shadow_view).setOnClickListener(this);
    }

    public void setParams(String guideId, String guidePhone) {
        this.guideId = guideId;
        this.guidePhone = guidePhone;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_call_internet_layout://网络电话
                dismiss();
                break;
            case R.id.dialog_call_ordinary_layout://普通电话
                PhoneInfo.CallDial(getContext(), guidePhone);
                dismiss();
                break;
            case R.id.dialog_call_shadow_view:
                dismiss();
                break;
        }
    }
}
