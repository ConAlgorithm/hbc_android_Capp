package com.hugboga.custom.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by qingcha on 16/6/19.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;
    private int orientation = -1;

    public void setItemOffsets(int left, int top, int right, int bottom) {
        setItemOffsets(left, top, right, bottom, -1);
    }

    public void setItemOffsets(int left, int top, int right, int bottom, int orientation) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = 0;
        if (parent.getAdapter() != null) {
            itemCount = parent.getAdapter().getItemCount();
        }
        int pos = parent.getChildAdapterPosition(view);


        outRect.left = left;
        outRect.top = top;
        outRect.right = right;
        outRect.bottom = bottom;

        if (orientation == LinearLayout.HORIZONTAL) {
            if (right == 0 && left > 0 && pos == (itemCount -1)) {
                outRect.right = left;
            } else {
                outRect.right = right;
            }
        } else if (orientation == LinearLayout.VERTICAL) {
            if (top == 0 && bottom > 0 && pos == 0) {
                outRect.top = bottom;
            } else {
                outRect.top = top;
            }
        }
    }
}
