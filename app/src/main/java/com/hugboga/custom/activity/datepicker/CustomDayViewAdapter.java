package com.hugboga.custom.activity.datepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.squareup.timessquare.CalendarCellView;
import com.squareup.timessquare.DayViewAdapter;

public class CustomDayViewAdapter implements DayViewAdapter {

    View dayView;
    @Override
    public void makeCellView(CalendarCellView parent) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_view_custom, null);
        parent.addView(layout);
        parent.setDayOfMonthTextView((TextView) layout.findViewById(R.id.day_view));
        parent.setBottomTextView((TextView) layout.findViewById(R.id.bottom_text));
        parent.setDay_layout((RelativeLayout)layout.findViewById(R.id.day_layout));
        parent.setDay_view_round((TextView)layout.findViewById(R.id.day_view_round));
        dayView = layout.findViewById(R.id.day_view);
    }

    @Override
    public View getDayView(){
        return dayView;
    }


}
