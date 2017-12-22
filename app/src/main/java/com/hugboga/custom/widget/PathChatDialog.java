package com.hugboga.custom.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.model.Text;
import com.hugboga.custom.R;

/**
 * Created by zhangqi on 2017/12/22.
 */

public class PathChatDialog extends Dialog implements View.OnClickListener {
    private TextView text_share, text_chat;

    public PathChatDialog(Context context) {
        this(context,R.style.AnimationDialog);
    }

    public PathChatDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_left_top);

        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
            lp.x = (int) (display.getWidth()*0.05f);
            lp.y = (int) (display.getHeight()*0.03f);
            lp.height = (int)(display.getHeight()*0.3f);
            lp.width = (int)(display.getWidth()*0.6f);
            getWindow().setAttributes(lp);

        }
        text_share = findViewById(R.id.dialog_text_share);
        text_chat = findViewById(R.id.dialog_text_chat);
        text_share.setOnClickListener(this);
        text_chat.setOnClickListener(this);
    }


    public void setMessageState(boolean state) {
     //   text_chat.setSelected(state);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_text_share:
                Toast.makeText(getContext(),"ok1",Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.dialog_text_chat:
                Toast.makeText(getContext(),"ok2",Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }

    }
}
