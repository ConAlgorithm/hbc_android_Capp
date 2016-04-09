package com.huangbaoche.hbcframe.widget.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Recycler间距
 * Created by ZONGFI on 2015/5/23.
 */
public class ZDefaultDivider extends RecyclerView.ItemDecoration {

    private int left = 16;
    private int top = 22;
    private int right = 16;
    private int bottom = 22;

    public ZDefaultDivider() {
    }

    public ZDefaultDivider(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(left, top, right, bottom);
    }

    public void setItemOffsets(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
