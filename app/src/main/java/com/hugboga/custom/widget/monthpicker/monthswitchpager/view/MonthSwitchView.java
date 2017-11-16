package com.hugboga.custom.widget.monthpicker.monthswitchpager.view;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CalendarGoodsBean;
import com.hugboga.custom.widget.monthpicker.model.CalendarDay;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.adapter.MonthViewAdapter;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.listener.MonthChangeListener;
import com.hugboga.custom.widget.monthpicker.util.DayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by tudou on 15-5-18.
 */
public class MonthSwitchView extends LinearLayout implements MonthView.OnDayClickListener {

  @BindView(android.R.id.text2) MonthSwitchTextView mSwitchText;
  @BindView(android.R.id.content) MonthRecyclerView mRecyclerView;

  private MonthView.OnDayClickListener mOnDayClickListener;

  private MonthViewAdapter mMonthAdapter;

  public MonthSwitchView(Context context) {
    this(context, null);
  }

  public MonthSwitchView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MonthSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    initialize(context, attrs, defStyleAttr);
  }

  private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
    LayoutInflater.from(context).inflate(R.layout.view_month_switch_container, this);
    ButterKnife.bind(this);

    mMonthAdapter = new MonthViewAdapter(context, this);
    mSwitchText.setMonthRecyclerView(mRecyclerView);
    mRecyclerView.setMonthSwitchTextView(mSwitchText);
    mRecyclerView.setAdapter(mMonthAdapter);
  }

  public void setData(CalendarDay startDay, CalendarDay endDay) {
    mMonthAdapter.setData(startDay, endDay, null);
    mSwitchText.setDay(startDay, endDay);
  }

  public void setSelectDay(CalendarDay calendarDay) {
    mRecyclerView.scrollToPosition(DayUtils.calculateMonthPosition(mMonthAdapter.getStartDay(), calendarDay));
    mMonthAdapter.setSelectDay(calendarDay);
  }

  public void setOnDayClickListener(MonthView.OnDayClickListener onDayClickListener) {
    mOnDayClickListener = onDayClickListener;
  }

  public void setGoodsCalendarMap(ArrayMap<String, CalendarGoodsBean> goodsCalendarMap) {
    mMonthAdapter.setGoodsCalendarMap(goodsCalendarMap);
  }

  @Override public void onDayClick(CalendarDay calendarDay) {
    mOnDayClickListener.onDayClick(calendarDay);
  }

  public void setMonthChangeListener(MonthChangeListener listener) {
    mSwitchText.setMonthChangeListener(listener);
  }
}
