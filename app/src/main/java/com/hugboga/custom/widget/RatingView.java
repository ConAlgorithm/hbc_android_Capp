package com.hugboga.custom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 16/5/24.
 */
public class RatingView extends LinearLayout {

    private static final int NONE_VALUE = -1;
    private static final int DEFAULT_MAX_LEVELS = 5;

    private ImageView[] itemIV;
    private int level;
    private int maxLevels;
    private boolean touchable;
    private int gap;
    private int width, height;
    private int startX;
    private int[] distances;

    private OnLevelChangedListener listener;

    public RatingView(Context context) {
        this(context, null);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingView);
        int levelBg = typedArray.getResourceId(R.styleable.RatingView_rating_bg, NONE_VALUE);
        maxLevels = typedArray.getInteger(R.styleable.RatingView_numLevels, DEFAULT_MAX_LEVELS);
        level = typedArray.getInteger(R.styleable.RatingView_level, maxLevels);
        gap = typedArray.getDimensionPixelOffset(R.styleable.RatingView_gap, NONE_VALUE);
        touchable = typedArray.getBoolean(R.styleable.RatingView_touchable, true);
        typedArray.recycle();

        if (level > maxLevels) {
            level = maxLevels;
        }
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.setOrientation(LinearLayout.HORIZONTAL);

        itemIV = new ImageView[maxLevels];
        LayoutParams itemParams;
        if (gap != NONE_VALUE) {
            itemParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        } else {
            itemParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
        }
        for (int i = 0; i < maxLevels; i++) {
            ImageView imageView = new ImageView(context);
            if (levelBg != NONE_VALUE) {
                imageView.setImageResource(levelBg);
            }
            if (gap != NONE_VALUE) {
                itemParams.setMargins(0, 0, gap, 0);
            }
            imageView.setLayoutParams(itemParams);
            addView(imageView);
            itemIV[i] = imageView;
        }
        setLevel(level);
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RatingView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = RatingView.this.getHeight();
                if (gap != NONE_VALUE) {
                    for (int i = 0; i < maxLevels; i++){
                        width += itemIV[i].getWidth() + gap;
                    }
                } else {
                    width = RatingView.this.getWidth();
                }

                int s = width / maxLevels;
                if (distances == null) {
                    distances = new int[maxLevels];
                    for (int i = 0; i < maxLevels; i++) {
                        distances[i] = (i + 1) * s;
                    }
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchable) {
            onTouchEvent(ev);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!touchable) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                setLevelChanged(startX);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                if (moveX < 0 || moveX > width || moveY < 0 || moveY > height) {
                    return super.onTouchEvent(event);
                }
                setLevelChanged(moveX);
                return super.onTouchEvent(event);
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void setLevelChanged(int moveX) {
        if (level != getLevelFromX(moveX)) {
            level = getLevelFromX(moveX);
            if (level != NONE_VALUE) {
                setLevel(level);
                if (listener != null) {
                    listener.onLevelChanged(RatingView.this, level);
                }
            }
        }
    }

    private int getLevelFromX(int moveX) {
        for (int i = 0; i < maxLevels; i++) {
            if (moveX <= distances[i]) {
                return i + 1;
            }
        }
        return -1;
    }

    private void resetAll() {
        for (int i = 0; i < maxLevels; i++) {
            itemIV[i].setSelected(false);
        }
    }

    public interface OnLevelChangedListener {
        public void onLevelChanged(RatingView starView, int level);
    }

    public void setOnLevelChangedListener(OnLevelChangedListener listener) {
        this.listener = listener;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        resetAll();
        for (int i = 0; i < maxLevels; i++) {
            if (i <= level - 1) {
                itemIV[i].setSelected(true);
            } else {
                break;
            }
        }
    }
    public void setNumLevel(int level) {
        this.level = level;
        this.maxLevels =level;
        for (int i = 0; i < maxLevels; i++) {
            itemIV[i].setSelected(true);
        }
    }

    public boolean isTouchable() {
        return touchable;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

}

