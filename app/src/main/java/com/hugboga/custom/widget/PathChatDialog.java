package com.hugboga.custom.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by zhangqi on 2017/12/22.
 */

public class PathChatDialog extends Dialog implements View.OnClickListener {
    private TextView text_share, text_chat;
    private DialogClickListener listener;

    public interface DialogClickListener {
        void shareClick();

        void chatClict();
    }

    public PathChatDialog(Context context, DialogClickListener listener) {
        this(context, R.style.AnimationDialog, listener);
    }


    public PathChatDialog(Context context, int themeResId, DialogClickListener listener) {
        super(context, themeResId);
        setContentView(R.layout.dialog_left_top);

        this.listener = listener;
        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.gravity = Gravity.RIGHT | Gravity.TOP;
            lp.x = lp.x - UIUtils.getActionBarSize() / 2;
            lp.y = UIUtils.getActionBarSize() - 15;
            lp.height = (int) (display.getHeight() * 0.2f);
            lp.width = (int) (display.getWidth() * 0.43f);
            getWindow().setAttributes(lp);
        }
        text_share = findViewById(R.id.dialog_text_share);
        text_chat = findViewById(R.id.dialog_text_chat);
        text_share.setOnClickListener(this);
        text_chat.setOnClickListener(this);
    }


    public void setMessageState(boolean state) {
        text_chat.setSelected(state);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_text_share:
                listener.shareClick();
                dismiss();
                break;
            case R.id.dialog_text_chat:
                listener.chatClict();
                dismiss();
                break;
        }

    }
}