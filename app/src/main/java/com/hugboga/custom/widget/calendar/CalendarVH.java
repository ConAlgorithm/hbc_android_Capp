package com.hugboga.custom.widget.calendar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhangqiang on 17/5/16.
 */

public class CalendarVH extends ZBaseViewHolder {

    @ViewInject(R.id.calendar_cell_layout)
    public RelativeLayout mLayout;
    /*@ViewInject(R.id.calendar_cell_today)
    public ImageView mToday;*/
    @ViewInject(R.id.calendar_cell_text)
    public TextView mText;

    public CalendarVH(View itemView) {
        super(itemView);
        mText.setOnClickListener(null);
    }
}