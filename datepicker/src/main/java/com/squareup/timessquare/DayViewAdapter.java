package com.squareup.timessquare;

import android.view.View;

/** Adapter used to provide a layout for {@link CalendarCellView}.*/
public interface DayViewAdapter {
  void makeCellView(CalendarCellView parent);
  View getDayView();
}
