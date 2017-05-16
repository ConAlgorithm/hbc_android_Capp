package com.huangbaoche.hbcframe.widget.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.huangbaoche.hbcframe.R;


public class ZGridRecyclerView extends ZRecyclerView {

    private int column = 2;

    public ZGridRecyclerView(Context context) {
        super(context);
        initListView(context, null);
    }

    public ZGridRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initListView(context, attrs);
    }

    public ZGridRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initListView(context, attrs);
    }

    /**
     * 初始化列表组件，设置布局和增加Item空隙
     */
    private void initListView(Context context, AttributeSet attrs) {
        //初始化部分参数
        if (context != null) {
            TypedArray att = context.obtainStyledAttributes(attrs, R.styleable.ZGridRecyclerView);
            column = att.getInteger(R.styleable.ZGridRecyclerView_column_num, column);
            att.recycle();
        }
        setLayoutManager(); //设置布局形式
    }

    public void setLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), column);
        setLayoutManager(layoutManager);
        super.setLayoutManager(layoutManager);
    }

    public void setColumn(int column) {
        this.column = column;
        setLayoutManager();
    }

}
