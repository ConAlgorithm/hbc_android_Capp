package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.Tools;
import com.hugboga.im.custom.CustomAttachment;
import com.hugboga.im.custom.attachment.MsgSkuAttachment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImSendMesView extends LinearLayout {

    @BindView(R.id.view_im_send_mes_iv)
    ImageView mesIV;
    @BindView(R.id.view_im_send_mes_title_tv)
    TextView titleTV;
    @BindView(R.id.view_im_send_mes_send_tv)
    TextView sendMesTV;
    @BindView(R.id.view_im_send_mes_close_layout)
    FrameLayout closeLayout;

    public ImSendMesView(Context context) {
        this(context, null);
    }

    public ImSendMesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_im_send_mes, this);
        ButterKnife.bind(this);
    }

    public void setData(CustomAttachment customAttachment) {
        if (customAttachment == null) {
            return;
        }
        switch (customAttachment.getType()) {
            case 1:
                MsgSkuAttachment msgSkuAttachment = (MsgSkuAttachment) customAttachment;
                Tools.showImage(mesIV, msgSkuAttachment.getFrontCover());
                titleTV.setText(msgSkuAttachment.getTitle());
                break;
        }

    }

    @OnClick(R.id.view_im_send_mes_layout)
    public void onFinish() {
        if (getContext() instanceof Activity) {
            ((Activity)getContext()).finish();
        }
    }

    public TextView getSendMesView() {
        return sendMesTV;
    }

    public FrameLayout getCloseLayout() {
        return closeLayout;
    }
}