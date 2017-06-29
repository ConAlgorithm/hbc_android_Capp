package com.squareup.timessquare;

import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

public class DefaultDayViewAdapter implements DayViewAdapter {
  @Override
  public void makeCellView(CalendarCellView parent) {
      ScoreTextView textView = new ScoreTextView(
              new ContextThemeWrapper(parent.getContext(), R.style.CalendarCell_CalendarDate));
      textView.setDuplicateParentStateEnabled(true);
      parent.addView(textView);
      parent.setDayOfMonthTextView(textView);
  }

    @Override
    public View getDayView() {
        return null;
    }
}
