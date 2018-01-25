package com.hugboga.custom.widget.recyclerviewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

/**
 * Created by qingcha on 17/11/27.
 */

public class AutoLoopRecyclerViewPager extends LoopRecyclerViewPager{

    private static final int DEFAULT_INTERVAL = 5000;
    private static final int SCROLL_WHAT = 0;

    private MyHandler handler;
    private boolean isAutoScroll = false;

    public AutoLoopRecyclerViewPager(Context context) {
        this(context,null,0);
    }

    public AutoLoopRecyclerViewPager(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AutoLoopRecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handler = new MyHandler(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);

        if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
            stopAutoScroll();
        } else if (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_CANCEL || !isAutoScroll) {
            startAutoScroll();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage();
    }

    private void sendScrollMessage() {
        handler.removeMessages(SCROLL_WHAT);
        if (isAutoScroll) {
            handler.sendEmptyMessageDelayed(SCROLL_WHAT, DEFAULT_INTERVAL);
        }
    }

    private static class MyHandler extends Handler {

        private final WeakReference<AutoLoopRecyclerViewPager> autoScrollViewPager;

        public MyHandler(AutoLoopRecyclerViewPager autoScrollViewPager) {
            this.autoScrollViewPager = new WeakReference<>(autoScrollViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AutoLoopRecyclerViewPager pager = this.autoScrollViewPager.get();
            if (pager != null) {
                pager.smoothScrollToPosition(pager.getCurrentPosition() + 1);
                pager.sendScrollMessage();
            }
        }
    }
}
