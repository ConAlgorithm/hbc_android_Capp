package com.hugboga.custom.utils;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

    public static void showAnimationAlpha(View v, int duration,Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        animator.setDuration(duration);
        animator.setStartDelay(0);
        if(null != listener) {
            animator.addListener(listener);
        }
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    public static void showAnimationBig(View v, int duration,Animator.AnimatorListener listener) {

        PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.5f);
        PropertyValuesHolder valuesHolder2 = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.5f);
        PropertyValuesHolder valuesHolder3 = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.3f);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(v, valuesHolder1, valuesHolder2,valuesHolder3);

        objectAnimator.setDuration(duration);
        objectAnimator.setStartDelay(0);
        if(null != listener) {
            objectAnimator.addListener(listener);
        }
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

}
