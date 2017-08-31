package com.hugboga.custom.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.PersonInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.UnicornUtils;

/**
 * Created by zhangqiang on 17/8/31.
 */

public class UpPicDialog extends Dialog implements View.OnClickListener {
    Context context;
    public UpPicDialog(Context context) {
        this(context, R.style.ShareDialog);
    }

    public UpPicDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        setContentView(R.layout.view_uppic_dialog);

        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.width = display.getWidth();
            getWindow().setAttributes(lp);
        }

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        findViewById(R.id.take_photo).setOnClickListener(this);
        findViewById(R.id.choose_photo).setOnClickListener(this);
        findViewById(R.id.dissmis).setOnClickListener(this);
        findViewById(R.id.dialog_uppic_shadow_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_photo:
                takeOrChoosePhoto(context,1);
                break;
            case R.id.choose_photo:
                takeOrChoosePhoto(context,2);
                break;
            case R.id.dissmis:
            case R.id.dialog_uppic_shadow_view:
                dismiss();
                break;
        }
    }

    public void takeOrChoosePhoto(Context context, int position){
        if(context instanceof PersonInfoActivity){
            if(position == 1){
                ((PersonInfoActivity)context).grantCamera();
            }else if(position == 2){
                ((PersonInfoActivity)context).choosePhoto();
            }
        }

    }
}
