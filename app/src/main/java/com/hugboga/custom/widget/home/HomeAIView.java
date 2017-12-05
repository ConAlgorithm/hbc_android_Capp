package com.hugboga.custom.widget.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by qingcha on 17/11/29.
 */

public class HomeAIView extends View {

    private static final int BG_COLOR = 0xFF000000;
    private static final int MIN_BG_RADIUS = UIUtils.dip2px(3);
    private static final int MAX_BG_RADIUS = UIUtils.dip2px(30);
    private static final int MARGIN_LEFT = MyApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.home_margin_left);
    private static final int MIN_WIDTH = UIUtils.dip2px(46);
    private static final int MAX_WIDTH = UIUtils.getScreenWidth() - MARGIN_LEFT * 2;
    private static final int HEIGHT = MIN_WIDTH;
    private static final int BITMAP_WIDTH = UIUtils.dip2px(18);
    private static final int BITMAP_HEIGHT = UIUtils.dip2px(18);
    private static final String TITLE_TEXT = MyApplication.getAppContext().getResources().getString(R.string.home_ai_where_to_go);
    private static final int TEXT_MARGIN_LEFT = UIUtils.dip2px(4);

    private int mWidth = 0;

    private int bgRectWidth;
    private int bgRectRadius;
    private Paint bgRectPaint;
    private RectF bgRectF = new RectF();

    private Bitmap bitmap;
    private Paint bitmapPaint;
    private RectF bitmapDestRect = new RectF();
    private float bitmapRectLeft;
    private float bitmapRectTop;
    private float bitmapMoveDistance;
    private float bitmapMaxMoveDistance;

    private Paint textPaint;
    private float textWidth;
    private float textHeight;

    private RecyclerView recyclerView;

    public HomeAIView(Context context) {
        this(context,null);
    }

    public HomeAIView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initValues();
    }

    private void initPaint() {
        bgRectPaint = new Paint();
        bgRectPaint.setColor(BG_COLOR);
        bgRectPaint.setAntiAlias(true);
        bgRectPaint.setStrokeWidth(5);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(UIUtils.sp2px(16));

        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
    }

    private void initValues() {
        bgRectWidth = MAX_WIDTH;
        bgRectRadius = MIN_BG_RADIUS;

        textWidth = textPaint.measureText(TITLE_TEXT);
        textHeight = UIUtils.getTextHeight(TITLE_TEXT, textPaint);

        bitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.mipmap.home_button_automaton_icon)).getBitmap();
        bitmapRectLeft = UIUtils.getScreenWidth() / 2 - (BITMAP_WIDTH + textWidth) / 2;
        bitmapRectTop = (MIN_WIDTH - BITMAP_HEIGHT) / 2;
        bitmapMaxMoveDistance = UIUtils.getScreenWidth() - MARGIN_LEFT - (MIN_WIDTH - BITMAP_WIDTH) / 2 - BITMAP_WIDTH - bitmapRectLeft;
    }

    public void reset() {
        bgRectWidth = MAX_WIDTH;
        bgRectRadius = MIN_BG_RADIUS;
        bitmapMoveDistance = 0;
        textPaint.setAlpha(255);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int rectLeft = mWidth - bgRectWidth - MARGIN_LEFT;
        int rectRight = mWidth - MARGIN_LEFT;
        bgRectF.set(rectLeft, 0, rectRight, HEIGHT);
        canvas.drawRoundRect(bgRectF, bgRectRadius, bgRectRadius, bgRectPaint);

        bitmapDestRect.set(bitmapRectLeft + bitmapMoveDistance, bitmapRectTop, bitmapRectLeft + bitmapMoveDistance + BITMAP_WIDTH, bitmapRectTop + BITMAP_HEIGHT);
        canvas.drawBitmap(bitmap, null, bitmapDestRect, bitmapPaint);

        canvas.drawText(TITLE_TEXT, bitmapRectLeft + BITMAP_WIDTH + bitmapMoveDistance + TEXT_MARGIN_LEFT, (HEIGHT + textHeight) / 2, textPaint);
    }

    public void startAnimation() {
        AnimatorSet backgroundAnimSet = new AnimatorSet();

        ValueAnimator widthAnim = ValueAnimator.ofInt(MAX_WIDTH, MIN_WIDTH);
        widthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bgRectWidth = (int) animation.getAnimatedValue();
            }
        });

        ValueAnimator roundRectRadiusAnim = ValueAnimator.ofInt(MIN_BG_RADIUS, MAX_BG_RADIUS);
        roundRectRadiusAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bgRectRadius = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator bitmapMoveAnim = ValueAnimator.ofFloat(0, bitmapMaxMoveDistance);
        bitmapMoveAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bitmapMoveDistance = (float) animation.getAnimatedValue();
            }
        });

        ValueAnimator textPaintAlphaAnim = ValueAnimator.ofInt(255, 0);
        textPaintAlphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int) animation.getAnimatedValue();
                textPaint.setAlpha(alpha);
            }
        });

        backgroundAnimSet.playTogether(widthAnim, bitmapMoveAnim, textPaintAlphaAnim, roundRectRadiusAnim);
        backgroundAnimSet.setDuration(1000);
        backgroundAnimSet.start();
    }

    public void setProgress(float progress) {//progress 0-1
        bgRectWidth = (int)((MAX_WIDTH - MIN_WIDTH) * (1 - progress)) + MIN_WIDTH;
        bgRectRadius = (int)((MAX_BG_RADIUS - MIN_BG_RADIUS) * progress) + MIN_BG_RADIUS;
        bitmapMoveDistance = (int)(bitmapMaxMoveDistance * progress);
        int alpha = (int)(255 * (1 - progress));
        textPaint.setAlpha(alpha);
        invalidate();
    }

}
