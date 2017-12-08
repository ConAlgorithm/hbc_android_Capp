package com.hugboga.custom.widget.city;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Tab的Item布局
 * Created by HONGBO on 2017/11/24 17:12.
 */

public class TabView extends FrameLayout {

    @BindView(R.id.tab_view_title)
    TextView tab_view_title;
    @BindView(R.id.tab_view_arrow)
    ImageView tab_view_arrow;
    @BindView(R.id.tab_view_bottom)
    ImageView tab_view_bottom;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.tab_view_layout, this);
        ButterKnife.bind(this, view);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        String text = ta.getString(R.styleable.TabView_text);
        boolean isSelect = ta.getBoolean(R.styleable.TabView_selected, false);
        tab_view_title.setText(text);
        tab_view_arrow.setImageResource(isSelect ? R.mipmap.destination_screening_an_icon : R.mipmap.destination_screening_up_icon);
        tab_view_bottom.setVisibility(isSelect ? View.VISIBLE : View.GONE);
        tab_view_title.setSelected(isSelect);
        ta.recycle();
    }

    public boolean isSelected() {
        return tab_view_title.isSelected();
    }

    public void setSelected(boolean isSelect) {
        tab_view_arrow.setImageResource(isSelect ? R.mipmap.destination_screening_an_icon : R.mipmap.destination_screening_up_icon);
        tab_view_bottom.setVisibility(isSelect ? View.VISIBLE : View.GONE);
        tab_view_title.setSelected(isSelect);
    }

    public void setText(String text) {
        tab_view_title.setText(text);
    }
}
