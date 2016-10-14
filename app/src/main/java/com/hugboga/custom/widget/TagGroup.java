package com.hugboga.custom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 16/5/29.
 */
public class TagGroup extends ViewGroup {

    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private boolean isCenter = false;

    private ArrayList<Integer> rowWithList = new ArrayList<Integer>();

    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagGroup);
        mHorizontalSpacing = (int) a.getDimension(R.styleable.TagGroup_horizontalSpacing, UIUtils.dip2px(8.0f));
        mVerticalSpacing = (int) a.getDimension(R.styleable.TagGroup_verticalSpacing, UIUtils.dip2px(4.0f));
        isCenter = a.getBoolean(R.styleable.TagGroup_isCenter, false);
        a.recycle();
    }

    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
        requestLayout();
    }

    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        mVerticalSpacing = verticalSpacing;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int row = 0;
        int rowWidth = 0;
        int rowMaxHeight = 0;

        rowWithList.clear();

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                rowWidth += childWidth;
                if (rowWidth > widthSize) {
                    rowWithList.add(rowWidth - childWidth);
                    rowWidth = childWidth;
                    height += rowMaxHeight + mVerticalSpacing;
                    rowMaxHeight = childHeight;
                    row++;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
                rowWidth += mHorizontalSpacing;
            }
        }
        rowWithList.add(rowWidth);
        height += rowMaxHeight;
        height += getPaddingTop() + getPaddingBottom();
        if (row == 0) {
            width = rowWidth;
            width += getPaddingLeft() + getPaddingRight();
        } else {
            width = widthSize;
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int childLeft = parentLeft;
        if (isCenter && rowWithList.size() > 0) {
            childLeft = parentLeft + (r - l - rowWithList.get(0)) / 2 + mHorizontalSpacing / 2;
        }
        int childTop = parentTop;

        int rowMaxHeight = 0;
        int row = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                if (childLeft + width > parentRight) { // Next line
                    row++;
                    if (isCenter && row < rowWithList.size()) {
                        childLeft = parentLeft + (r - l - rowWithList.get(row)) / 2 + mHorizontalSpacing / 2;
                    } else {
                        childLeft = parentLeft;
                    }
                    childTop += rowMaxHeight + mVerticalSpacing;
                    rowMaxHeight = height;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, height);
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
                childLeft += width + mHorizontalSpacing;
            }
        }
    }

    public void setTags(List<View> views) {
        setTags(views, true);
    }

    public void setTags(List<View> views, boolean isRemoveViews) {
        if (isRemoveViews) {
            removeAllViews();
        }
        for (int i = 0; i < views.size(); i++) {
            View tag = views.get(i);
            addView(tag);
            tag.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        mListener.onTagClick(v, indexOfChild(v));
                    }
                }
            });
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    private OnTagItemClickListener mListener;

    public void setOnTagItemClickListener(OnTagItemClickListener listener) {
        mListener = listener;
    }

    public interface OnTagItemClickListener {
        public void onTagClick(View view, int position);
    }
}