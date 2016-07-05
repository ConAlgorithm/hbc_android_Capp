// Copyright 2013 Square, Inc.

package com.squareup.timessquare;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.timessquare.MonthCellDescriptor.RangeState;

public class CalendarCellView extends FrameLayout {
  private static final int[] STATE_SELECTABLE = {
      R.attr.tsquare_state_selectable
  };
  private static final int[] STATE_CURRENT_MONTH = {
      R.attr.tsquare_state_current_month
  };
  private static final int[] STATE_TODAY = {
      R.attr.tsquare_state_today
  };
  private static final int[] STATE_HIGHLIGHTED = {
      R.attr.tsquare_state_highlighted
  };
  private static final int[] STATE_RANGE_FIRST = {
      R.attr.tsquare_state_range_first
  };
  private static final int[] STATE_RANGE_MIDDLE = {
      R.attr.tsquare_state_range_middle
  };
  private static final int[] STATE_RANGE_LAST = {
      R.attr.tsquare_state_range_last
  };

  private boolean isSelectable = false;
  private boolean isCurrentMonth = false;
  private boolean isToday = false;
  private boolean isHighlighted = false;
  private RangeState rangeState = RangeState.NONE;

    public TextView getDay_view_round() {
        return day_view_round;
    }

    public void setDay_view_round(TextView day_view_round) {
        this.day_view_round = day_view_round;
    }

    private TextView day_view_round;

    private TextView dayOfMonthTextView;

    public RelativeLayout getDay_layout() {
        return day_layout;
    }

    public void setDay_layout(RelativeLayout day_layout) {
        this.day_layout = day_layout;
    }

    private RelativeLayout day_layout;

    public TextView getBottomTextView() {
        return bottomTextView;
    }

    public void setBottomTextView(TextView bottomTextView) {
        this.bottomTextView = bottomTextView;
    }

    private TextView bottomTextView;

  @SuppressWarnings("UnusedDeclaration") //
  public CalendarCellView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setSelectable(boolean isSelectable) {
    if (this.isSelectable != isSelectable) {
      this.isSelectable = isSelectable;
      refreshDrawableState();
    }
  }

  public void setCurrentMonth(boolean isCurrentMonth) {
    if (this.isCurrentMonth != isCurrentMonth) {
      this.isCurrentMonth = isCurrentMonth;
      refreshDrawableState();
    }
  }

  public void setToday(boolean isToday) {
    if (this.isToday != isToday) {
      this.isToday = isToday;
      refreshDrawableState();
    }
  }

  public void setRangeState(MonthCellDescriptor.RangeState rangeState) {
    if (this.rangeState != rangeState) {
      this.rangeState = rangeState;
      refreshDrawableState();
    }
  }

  public void setHighlighted(boolean isHighlighted) {
    if (this.isHighlighted != isHighlighted) {
      this.isHighlighted = isHighlighted;
      refreshDrawableState();
    }
  }

  public boolean isCurrentMonth() {
    return isCurrentMonth;
  }

  public boolean isToday() {
    return isToday;
  }

  public boolean isSelectable() {
    return isSelectable;
  }

  @Override protected int[] onCreateDrawableState(int extraSpace) {
    final int[] drawableState = super.onCreateDrawableState(extraSpace + 5);

    if (isSelectable) {
      mergeDrawableStates(drawableState, STATE_SELECTABLE);
    }

    if (isCurrentMonth) {
      mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
    }

    if (isToday) {
      mergeDrawableStates(drawableState, STATE_TODAY);
    }

    if (isHighlighted) {
      mergeDrawableStates(drawableState, STATE_HIGHLIGHTED);
    }

    if (rangeState == MonthCellDescriptor.RangeState.FIRST) {
        mergeDrawableStates(drawableState, STATE_RANGE_FIRST);
        day_view_round.setBackgroundResource(R.drawable.day_view_selector);
        bottomTextView.setText("开始");
    } else if (rangeState == MonthCellDescriptor.RangeState.MIDDLE) {
        dayOfMonthTextView.setBackgroundColor(Color.parseColor("#fcf0ac"));
        mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE);
    } else if (rangeState == RangeState.LAST) {
        mergeDrawableStates(drawableState, STATE_RANGE_LAST);
        day_view_round.setBackgroundResource(R.drawable.day_view_selector);
        bottomTextView.setText("结束");
    }

    return drawableState;
  }

  public void setDayOfMonthTextView(TextView textView) {
    dayOfMonthTextView = textView;
  }

  public TextView getDayOfMonthTextView() {
    if (dayOfMonthTextView == null) {
      throw new IllegalStateException(
              "You have to setDayOfMonthTextView in your custom DayViewAdapter."
      );
    }
    return dayOfMonthTextView;
  }
}
