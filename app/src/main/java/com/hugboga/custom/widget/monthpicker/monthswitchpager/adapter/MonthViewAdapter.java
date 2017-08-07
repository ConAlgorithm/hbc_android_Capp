package com.hugboga.custom.widget.monthpicker.monthswitchpager.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hugboga.custom.data.bean.CalendarGoodsBean;
import com.hugboga.custom.widget.monthpicker.model.CalendarDay;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.view.MonthView;
import com.hugboga.custom.widget.monthpicker.util.DayUtils;

import java.util.ArrayList;

/**
 * Created by tudou on 15-4-30.
 */
public class MonthViewAdapter extends RecyclerView.Adapter<MonthViewAdapter.MonthViewHolder> implements
    MonthView.OnDayClickListener {

  private Context mContext;
  private CalendarDay mStartDay;
  private CalendarDay mEndDay;
  private CalendarDay mSelectCalendarDay;
  private ArrayList<CalendarDay> mAbleCalendayDays;
  private MonthView.OnDayClickListener mOnDayClickListener;
  private ArrayMap<String, CalendarGoodsBean> goodsCalendarMap;

  public MonthViewAdapter(Context context, MonthView.OnDayClickListener onDayClickListener) {
    mContext = context;
    mOnDayClickListener = onDayClickListener;
    mAbleCalendayDays = new ArrayList<>();
  }

  public void setData(CalendarDay startDay, CalendarDay endDay, ArrayList<CalendarDay> calendarDayArrayList) {
    mStartDay = startDay;
    mEndDay = endDay;
    if (calendarDayArrayList != null) {
      mAbleCalendayDays.clear();
      mAbleCalendayDays.addAll(calendarDayArrayList);
    }
    notifyDataSetChanged();
  }

  public CalendarDay getStartDay() {
    if (mStartDay == null) {
      try {
        throw new Exception("The StartDay must initial before the select Day!");
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      return mStartDay;
    }
    return null;
  }

  public void setSelectDay(CalendarDay calendarDay) {
    if (calendarDay == null) return;
    mSelectCalendarDay = calendarDay;
    notifyDataSetChanged();
  }

  public void setGoodsCalendarMap(ArrayMap<String, CalendarGoodsBean> goodsCalendarMap) {
    if (goodsCalendarMap == null && goodsCalendarMap.size() <= 0) return;
    this.goodsCalendarMap = goodsCalendarMap;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    if (mStartDay == null || mEndDay == null) {
      return 0;
    }
    int monthCount = DayUtils.calculateMonthCount(mStartDay, mEndDay);
    return monthCount;
  }

  @Override
  public MonthViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    MonthView monthView = new MonthView(mContext);
    monthView.setOnDayClickListener(this);
    int width = mContext.getResources().getDisplayMetrics().widthPixels;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
        ViewGroup.LayoutParams.MATCH_PARENT);
    monthView.setLayoutParams(params);
    MonthViewHolder viewHolder = new MonthViewHolder(monthView, mStartDay, mAbleCalendayDays);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(final MonthViewHolder viewHolder, final int position) {
    viewHolder.bind(position, mStartDay, mSelectCalendarDay, goodsCalendarMap);
  }

  @Override public void onDayClick(CalendarDay calendarDay) {
    mSelectCalendarDay = calendarDay;
    mOnDayClickListener.onDayClick(calendarDay);
    notifyDataSetChanged();
  }

  public static class MonthViewHolder extends RecyclerView.ViewHolder {

    MonthView monthView;

    public MonthViewHolder(View view,CalendarDay startDay, ArrayList<CalendarDay> mAbleCalendayDays) {
      super(view);
      monthView = (MonthView) view;
      monthView.setFirstDay(startDay);
    }

    public void bind(int position, CalendarDay startDay, CalendarDay calendarDay, ArrayMap<String, CalendarGoodsBean> goodsCalendarMap) {
      monthView.setFirstDay(startDay);
      monthView.setSelectDay(calendarDay);
      monthView.setMonthPosition(goodsCalendarMap, position);
    }

  }
}
