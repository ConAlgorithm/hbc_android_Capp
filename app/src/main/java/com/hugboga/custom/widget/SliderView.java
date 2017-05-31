package com.hugboga.custom.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by qingcha on 17/2/22.
 * 根据https://github.com/navasmdc/MaterialDesignLibrary项目修改
 */
public class SliderView extends RelativeLayout {

    final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    final int disabledBackgroundColor = Color.parseColor("#dbdbdb");

    // Indicate if user touched this view the last time
    public boolean isLastTouch = false;

    private int backgroundColor = getResources().getColor(R.color.default_yellow);
    private Ball   ball;
    private Bitmap bitmap;
    private int max = 100;
    private int min = 0;
    private NumberIndicator        numberIndicator;
    private OnValueChangedListener onValueChangedListener;
    private boolean placedBall          = false;
    private boolean press               = false;
    private boolean showNumberIndicator = false;
    private int     value               = 30;

    boolean animation = false;
    private RectF rect;
    private Paint paint;
    private int sourceType;//用来区分来源

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            backgroundColor = getResources().getColor(R.color.default_yellow);
        } else {
            backgroundColor = disabledBackgroundColor;
        }
        if (ball != null) {
            ball.resetTextColor();
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public OnValueChangedListener getOnValueChangedListener() {
        return onValueChangedListener;
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        if (placedBall == false)
            post(new Runnable() {

                @Override
                public void run() {
                    setValue(value);
                }
            });
        else {
            this.value = value;
            float division = (ball.xFin - ball.xIni) / max;
            ViewHelper.setX(ball, value * division + getHeight() / 2 - ball.getWidth() / 2);
            ball.changeBackground(false);
        }

    }

    @Override
    public void invalidate() {
        ball.invalidate();
        super.invalidate();
    }

    public boolean isShowNumberIndicator() {
        return showNumberIndicator;
    }

    public void setShowNumberIndicator(boolean showNumberIndicator) {
        this.showNumberIndicator = showNumberIndicator;
        numberIndicator = (showNumberIndicator) ? new NumberIndicator(getContext()) : null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isLastTouch = true;
        if (isEnabled()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN
                    || event.getAction() == MotionEvent.ACTION_MOVE) {
                if (numberIndicator != null && numberIndicator.isShowing() == false)
                    numberIndicator.show();
                if ((event.getX() <= getWidth() && event.getX() >= 0)) {
                    press = true;
                    // calculate value
                    int newValue = 0;
                    float division = (ball.xFin - ball.xIni) / (max - min);
                    if (event.getX() > ball.xFin) {
                        newValue = max;
                    } else if (event.getX() < ball.xIni) {
                        newValue = min;
                    } else {
                        newValue = min + (int) ((event.getX() - ball.xIni) / division);
                    }
                    if (value != newValue) {
                        value = newValue;
                        if (onValueChangedListener != null)
                            onValueChangedListener.onSliderScrolled(newValue, sourceType);
                    }
                    // move ball indicator
                    float x = event.getX();
                    x = (x < ball.xIni) ? ball.xIni : x;
                    x = (x > ball.xFin) ? ball.xFin : x;
                    ViewHelper.setX(ball, x);
                    ball.changeBackground(true);

                    // If slider has number indicator
                    if (numberIndicator != null) {
                        // move number indicator
                        numberIndicator.indicator.x = x;
                        numberIndicator.indicator.finalY = getRelativeTop(this) + UIUtils.dip2px(10);
                        numberIndicator.indicator.finalSize = getHeight() / 4;
                        numberIndicator.numberIndicator.setText("");
                    }

                } else {
                    press = false;
                    isLastTouch = false;
                    if (numberIndicator != null)
                        numberIndicator.dismiss();

                }

            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (numberIndicator != null)
                    numberIndicator.dismiss();
                isLastTouch = false;
                press = false;
                if (ball != null) {
                    ball.changeBackground(false);
                }
                if (onValueChangedListener != null) {
                    onValueChangedListener.onValueChanged(value, sourceType);
                }
            }
        }
        return true;
    }

    @Override
    public void setBackgroundColor(int color) {
        backgroundColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(animation) {
            invalidate();
        }

        if (!placedBall) {
            placeBall();
        }


        paint.setColor(backgroundColor);

        if (rect == null) {
            rect = new RectF(getHeight() / 2, getHeight() / 2 - UIUtils.dip2px(5), getWidth() - getHeight() / 2, getHeight() / 2 + UIUtils.dip2px(5));
        }
        canvas.drawRoundRect(rect, UIUtils.dip2px(6), UIUtils.dip2px(6), paint);

        if (press && !showNumberIndicator) {
            paint.setAntiAlias(true);
            canvas.drawCircle(ViewHelper.getX(ball) + ball.getWidth() / 2, getHeight() / 2, getHeight() / 3, paint);
        }
        invalidate();
    }

    // Set atributtes of XML to View
    protected void setAttributes(AttributeSet attrs) {

        setBackgroundResource(R.drawable.background_transparent);

        // Set size of view
        setMinimumHeight(UIUtils.dip2px(48));
        setMinimumWidth(UIUtils.dip2px(80));

        // Set background Color
        // Color by resource
        int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML, "background", -1);
        if (bacgroundColor != -1) {
            setBackgroundColor(getResources().getColor(bacgroundColor));
        } else {
            // Color by hexadecimal
            int background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            if (background != -1)
                setBackgroundColor(background);
        }

        showNumberIndicator = attrs.getAttributeBooleanValue(MATERIALDESIGNXML, "showNumberIndicator", false);
        min = attrs.getAttributeIntValue(MATERIALDESIGNXML, "min", 0);
        max = attrs.getAttributeIntValue(MATERIALDESIGNXML, "max", 0);
        value = attrs.getAttributeIntValue(MATERIALDESIGNXML, "value", min);

        ball = new Ball(getContext());
        RelativeLayout.LayoutParams params = new LayoutParams(UIUtils.dip2px(23), UIUtils.dip2px(23));
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ball.setLayoutParams(params);
        addView(ball);

        if (showNumberIndicator) {
            numberIndicator = new NumberIndicator(getContext());
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void placeBall() {
        ViewHelper.setX(ball, getHeight() / 2 - ball.getWidth() / 2);
        ball.xIni = ViewHelper.getX(ball);
        ball.xFin = getWidth() - getHeight() / 2 - ball.getWidth() / 2;
        ball.xCen = getWidth() / 2 - ball.getWidth() / 2;
        placedBall = true;
    }

    // Event when slider change value
    public interface OnValueChangedListener {
        public void onSliderScrolled(int value, int type);//滑动中
        public void onValueChanged(int value, int type);//滑动结束
    }

    class Ball extends TextView {

        float xIni, xFin, xCen;

        public Ball(Context context) {
            super(context);
            setBackgroundResource(R.mipmap.seekbar_thumb);
            setGravity(Gravity.CENTER);
            resetTextColor();
            setPadding(0, 0, 0, UIUtils.dip2px(1));
            setTextSize(12);
            setText("" + value);
        }

        public void changeBackground(boolean isTouch) {
            resetTextColor();
            if (isTouch) {
                setBackgroundResource(R.mipmap.seekbar_thumb_touch);
                setText("");
            } else {
                setBackgroundResource(R.mipmap.seekbar_thumb);
                setText("" + value);
            }
        }

        public void resetTextColor() {
            if (SliderView.this.isEnabled()) {
                setTextColor(getResources().getColor(R.color.default_black));
            } else {
                setTextColor(0xFFcccccc);
            }
        }
    }

    // Slider Number Indicator
    class Indicator extends RelativeLayout {

        boolean animate               = true;
        // Final size after animation
        float   finalSize             = 0;
        // Final y position after animation
        float   finalY                = 0;
        boolean numberIndicatorResize = false;
        // Size of number indicator
        float   size                  = 0;
        // Position of number indicator
        float   x                     = 0;
        float   y                     = 0;

        public Indicator(Context context) {
            super(context);
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (numberIndicatorResize == false) {
                LayoutParams params = (LayoutParams) numberIndicator.numberIndicator.getLayoutParams();
                params.height = (int) finalSize * 2;
                params.width = (int) finalSize * 2;
                numberIndicator.numberIndicator.setLayoutParams(params);
            }

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(backgroundColor);
            if (animate) {
                if (y == 0) {
                    y = finalY + finalSize * 2;
                }
                y -= UIUtils.dip2px(6);
                size += UIUtils.dip2px(2);
            }
            canvas.drawCircle(ViewHelper.getX(ball) + getRelativeLeft((View) ball.getParent()) + ball.getWidth() / 2, y, size, paint);
            if (animate && size >= finalSize) {
                animate = false;
            }
            if (animate == false) {
                ViewHelper.setX(numberIndicator.numberIndicator, (ViewHelper.getX(ball) + getRelativeLeft((View) ball.getParent()) + ball.getWidth() / 2) - size);
                ViewHelper.setY(numberIndicator.numberIndicator, y - size);
                numberIndicator.numberIndicator.setText(value + "");
            }

            invalidate();
        }

    }

    class NumberIndicator extends Dialog {

        Indicator indicator;
        TextView  numberIndicator;

        public NumberIndicator(Context context) {
            super(context, android.R.style.Theme_Translucent);
        }

        @Override
        public void dismiss() {
            super.dismiss();
            indicator.y = 0;
            indicator.size = 0;
            indicator.animate = true;
        }

        @Override
        public void onBackPressed() {
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.number_indicator_spinner);
            setCanceledOnTouchOutside(false);

            RelativeLayout content = (RelativeLayout) this.findViewById(R.id.number_indicator_spinner_content);
            indicator = new Indicator(this.getContext());
            content.addView(indicator);

            numberIndicator = new TextView(getContext());
            numberIndicator.setTextColor(Color.WHITE);
            numberIndicator.setGravity(Gravity.CENTER);
            content.addView(numberIndicator);

            indicator.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

    }


    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        animation = true;
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        animation = false;
    }

    public int getRelativeTop(View myView) {
        if (myView.getId() == android.R.id.content) {
            return myView.getTop();
        } else {
            return myView.getTop() + getRelativeTop((View) myView.getParent());
        }
    }

    public int getRelativeLeft(View myView) {
        if (myView.getId() == android.R.id.content) {
            return myView.getLeft();
        } else {
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
        }
    }

    public void setType(int type) {
        this.sourceType = type;
    }

}
