package com.hugboga.custom.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;

/**
 * Created by qingcha on 16/7/15.
 */
public class ShareDialog extends Dialog implements View.OnClickListener{

    private Params mParams;
    private OnShareListener listener;

    public ShareDialog(Context context) {
        this(context, R.style.ShareDialog);
    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.view_share_dialog);

        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.width = (int)(display.getWidth());
            getWindow().setAttributes(lp);
        }

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        findViewById(R.id.dialog_share_wechat_tv).setOnClickListener(this);
        findViewById(R.id.dialog_share_moments_tv).setOnClickListener(this);
        findViewById(R.id.dialog_share_cancel_tv).setOnClickListener(this);
        findViewById(R.id.dialog_share_shadow_view).setOnClickListener(this);
    }

    public void setParams(Params params) {
        this.mParams = params;
    }

    /**
     * 1:好友,2:朋友圈；
     * */
    private void setShare(int type) {
        if (mParams == null) {
            return;
        }
        if (listener != null) {
            listener.onShare(type);
        }
        WXShareUtils wxShareUtils = WXShareUtils.getInstance(getContext());
        wxShareUtils.source = mParams.source;
        if (TextUtils.isEmpty(mParams.picUrl)) {
            wxShareUtils.share(type, mParams.resID, mParams.title, mParams.content, mParams.shareUrl);
        } else {
            wxShareUtils.share(type, mParams.picUrl, mParams.title, mParams.content, mParams.shareUrl);
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_share_wechat_tv://微信好友
                setShare(1);
                break;
            case R.id.dialog_share_moments_tv://朋友圈
                setShare(2);
                break;
            case R.id.dialog_share_shadow_view:
            case R.id.dialog_share_cancel_tv:
                dismiss();
                break;
        }
    }

    public interface OnShareListener {
        public void onShare(int type);
    }

    public void setOnShareListener(OnShareListener listener) {
        this.listener = listener;
    }

    public static class Params {
        int resID;
        String picUrl;
        String title;
        String content;
        String shareUrl;
        String source;

        public Params(String picUrl, String title, String content, String shareUrl, String source) {
            this.picUrl = picUrl;
            this.title = title;
            this.content = content;
            this.shareUrl = shareUrl;
            this.source = source;
        }

        public Params(int resID, String title, String content, String shareUrl, String source) {
            this.resID = resID;
            this.title = title;
            this.content = content;
            this.shareUrl = shareUrl;
            this.source = source;
        }
    }
}
