package com.hugboga.custom.widget;

import android.graphics.Paint;
import android.graphics.Path;
import android.widget.LinearLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 17/3/14.
 */
public class OrderDetailTabStripView extends LinearLayout {

    private final Paint mSelectedIndicatorPaint;
    private final Path trigonalPath;

    private int mSelectedPosition;
    private float mSelectionOffset;

    OrderDetailTabStripView(Context context) {
        this(context, null);
    }

    OrderDetailTabStripView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mSelectedIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trigonalPath = new Path();
    }

    void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();
        final int childCount = getChildCount();
        if (childCount > 0) {
            View selectedTitle = getChildAt(mSelectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();

            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {
                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() + (1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() + (1.0f - mSelectionOffset) * right);
            }
//            mSelectedIndicatorPaint.setColor(0x33FF317A);
//            canvas.drawRect(left, 0, right, height, mSelectedIndicatorPaint);
            int middle = (right - left)/2 + left;
            trigonalPath.reset();
            trigonalPath.moveTo(middle - UIUtils.dip2px(6), height);
            trigonalPath.lineTo(middle + UIUtils.dip2px(6), height);
            trigonalPath.lineTo(middle, height - UIUtils.dip2px(5.5f));
            trigonalPath.close();
            mSelectedIndicatorPaint.setColor(getContext().getResources().getColor(R.color.default_yellow));
            canvas.drawPath(trigonalPath, mSelectedIndicatorPaint);
        }
    }
}