package com.hugboga.custom.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by qingcha on 16/6/19.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;

    public void setItemOffsets(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
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
        outRect.bottom = bottom;
        if (right == 0 && left > 0 && pos == (itemCount -1)) {
            outRect.right = left;
        } else {
            outRect.right = right;
        }
    }
}
