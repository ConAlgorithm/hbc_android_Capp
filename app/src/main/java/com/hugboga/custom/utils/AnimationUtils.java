package com.hugboga.custom.utils;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


public class AnimationUtils {


    public static void showAnimation(View v, int duration,Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY",  UIUtils.getScreenHeight(), 0);
        animator.setDuration(duration);
        animator.setStartDelay(0);
        if(null != listener) {
            animator.addListener(listener);
        }
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    public static void hideAnimation(View v, int duration,Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY", 0,UIUtils.getScreenHeight());
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        if(null != listener) {
            animator.addListener(listener);
        }
        animator.start();
    }

}
