package com.hugboga.custom.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 16/9/8.
 */
public class DeliverLoadingView extends ImageView {

    private static final int DURATION = 2000;
    private static final int START_DELAY = 1000;//停留时间

    private ObjectAnimator inAnimator, outAnimator;

    private boolean isRun = true;
    private boolean isChangeBg = true;

    public DeliverLoadingView(Context context) {
        this(context, null);
    }

    public DeliverLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.mipmap.deliver_human);

        outAnimator = ObjectAnimator.ofFloat(this, "rotationY", 0, -90);
        outAnimator.setInterpolator(new AnticipateInterpolator());
        outAnimator.setDuration(DURATION / 2);
        outAnimator.setStartDelay(START_DELAY);
        outAnimator.start();
        outAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isChangeBg) {
                    isChangeBg = false;
                    setBackgroundResource(R.mipmap.deliver_car);
                } else {
                    isChangeBg = true;
                    setBackgroundResource(R.mipmap.deliver_human);
                }
                if (isRun) {
                    inAnimator.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        inAnimator = ObjectAnimator.ofFloat(this, "rotationY", 90, 0);
        inAnimator.setInterpolator(new OvershootInterpolator());
        inAnimator.setDuration(DURATION / 2);
        inAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isRun) {
                    outAnimator.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void onStop() {
        isRun = false;
    }

    public void onStart() {
        isRun = true;
        if (!outAnimator.isRunning() && !inAnimator.isRunning()) {
            outAnimator.start();
        }
    }

}
