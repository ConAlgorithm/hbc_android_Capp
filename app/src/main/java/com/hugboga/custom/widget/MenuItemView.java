package com.hugboga.custom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/9/27.
 */
public class MenuItemView extends RelativeLayout {

    @BindView(R.id.item_menu_title_tv)
    TextView titleTV;
    @BindView(R.id.item_menu_arrow_iv)
    ImageView arrowIV;
    @BindView(R.id.item_menu_bottom_line)
    View bottomLine;

    public MenuItemView(Context context) {
        this(context, null);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_item_menu, this);
        ButterKnife.bind(view);
        setClipChildren(false);
        setClipToPadding(false);
        setPadding(UIUtils.dip2px(17), 0, UIUtils.dip2px(17), 0);
        setBackgroundColor(0xFFFFFFFF);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemMenu, 0, 0);
        int titleId = typedArray.getResourceId(R.styleable.ItemMenu_itemTitle, 0);
        boolean isHideLine = typedArray.getBoolean(R.styleable.ItemMenu_isHideLine, false);
        boolean isHideArrow = typedArray.getBoolean(R.styleable.ItemMenu_isHideArrow, false);
        titleTV.setText(titleId);
        bottomLine.setVisibility(isHideLine ? View.GONE : View.VISIBLE);
        arrowIV.setVisibility(isHideArrow ? View.GONE : View.VISIBLE);
        typedArray.recycle();
    }
}
