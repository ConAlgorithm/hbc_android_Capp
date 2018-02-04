package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

public class ChatLogoutView extends LinearLayout {

    @BindView(R.id.chat_container)
    LinearLayout containerLayout;
    @BindView(R.id.chat_scrollview)
    ScrollView scrollview;
    @BindView(R.id.chat_root_layout)
    LinearLayout rootLayout;

    @BindView(R.id.space_view)
    View spaceView;
    @BindView(R.id.space_view1)
    View spaceView1;
    @BindView(R.id.mes1_view)
    ImageView mes1View;
    @BindView(R.id.mes2_view)
    ImageView mes2View;
    @BindView(R.id.gif_view)
    GifImageView gifView;
    @BindView(R.id.mes3_view)
    ImageView mes3View;

    private boolean isLooper;

    public ChatLogoutView(Context context) {
        this(context, null);
    }

    public ChatLogoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_chat_logout, this);
        ButterKnife.bind(view);
        int width = (int) (UIUtils.getScreenWidth() / 5.0 * 3.3);
        int height = (int) (388 / 496.0f * width);
        rootLayout.getLayoutParams().width = width;
        rootLayout.getLayoutParams().height = height;

        int containerHeight = (int) (297 / 388.0f * height);
        int containerMarginTop = height - containerHeight;
        LayoutParams containerParams = new LayoutParams(width, containerHeight);
        containerParams.topMargin = containerMarginTop;
        scrollview.setLayoutParams(containerParams);

        int itemWidth = width - UIUtils.dip2px(15) * 2;
        int mes1Height = (int) (123 / 645f * itemWidth);
        int spaceHeight = containerHeight - mes1Height - UIUtils.dip2px(12);
        spaceView.getLayoutParams().height = mes1Height + UIUtils.dip2px(12);
        spaceView1.getLayoutParams().height = spaceHeight;
        mes1View.getLayoutParams().height = mes1Height;
        mes2View.getLayoutParams().height = (int) (168 / 645f * itemWidth);
        mes3View.getLayoutParams().height = (int) (168 / 645f * itemWidth);

        scrollview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isLooper) {
                return;
            }
            switch (msg.what) {
                case 0:
                    scrollview.scrollTo(0, 0);
                    mes2View.setVisibility(View.GONE);
                    mes3View.setVisibility(View.GONE);
                    gifView.setVisibility(View.GONE);
                    gifView.setBackgroundResource(0);
                    break;
                case 1:
                    scrollview.fullScroll(View.FOCUS_DOWN);
                    mes2View.setVisibility(View.VISIBLE);
                    gifView.setVisibility(View.INVISIBLE);
                    gifView.setBackgroundResource(0);
                    break;
                case 2:
                    scrollview.fullScroll(View.FOCUS_DOWN);
                    mes3View.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    gifView.setVisibility(View.VISIBLE);
                    gifView.setBackgroundResource(R.mipmap.chat_logout_loading);
                    break;
                case 4:
                    scrollview.fullScroll(View.FOCUS_DOWN);
                    break;
            }
            int nextFrame = msg.what == 4 ? 0 : msg.what + 1;
            long delayedTime = msg.what == 0 ? 1000 : 2000;
            handler.sendEmptyMessageDelayed(nextFrame, delayedTime);
        }
    };

    public void setLooper(boolean isLooper) {
        this.isLooper = isLooper;
        if (isLooper && scrollview != null) {
            handler.sendEmptyMessage(0);
        }
    }
}
