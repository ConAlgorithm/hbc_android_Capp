package com.huangbaoche.hbcframe.widget.monthpicker.monthswitchpager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.huangbaoche.hbcframe.R;
import com.huangbaoche.hbcframe.widget.monthpicker.model.CalendarDay;
import com.huangbaoche.hbcframe.widget.monthpicker.util.DayUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tudou on 15-5-18.
 */
public class MonthView extends View {
  private final static String TAG = "MonthView";
  private final static int DAY_IN_WEEK = 7;
  private final static float DAY_IN_MONTH_PADDING_VERTICAL = 6.0f;
  private final static int DEFAULT_HEIGHT = 32;
  protected static final int DEFAULT_NUM_ROWS = 7;

  private ArrayList<CalendarDay> mDays;
  private CalendarDay mFirstDay;
  private CalendarDay mSelectDay;
  private int mMonthPosition;

  private Paint mPaintNormal;
  private Paint mPaintSelect;
  private int mCircleColor;
  private int mTextNormalColor;
  private int mTextSundaySaturdayColor;
  private int mTextNotClickColor;
  private int mTextSelectColor;
  protected int mRowHeight = DEFAULT_HEIGHT;
  private int mNumRows = DEFAULT_NUM_ROWS;
  private int rowNum;

  private OnDayClickListener mOnDayClickListener;


  public MonthView(Context context) {
    this(context, null);
  }

  public MonthView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initData();
    initPaint();
  }

  private void initPaint() {
    mTextNotClickColor = getResources().getColor(R.color.color_CCCCCC);
    mTextSundaySaturdayColor = getResources().getColor(R.color.color_ee4000);
    mTextNormalColor = getResources().getColor(R.color.color_151515);
    mTextSelectColor = getResources().getColor(R.color.color_151515);
    mCircleColor = getResources().getColor(R.color.color_FFD602);

    mPaintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaintNormal.setColor(mTextNormalColor);
    mPaintNormal.setTextSize(getResources().getDimension(R.dimen.si_default_text_size));

    mPaintSelect = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaintSelect.setColor(mCircleColor);
    mPaintSelect.setStyle(Paint.Style.FILL);

  }

  private void initData() {
    mDays = new ArrayList<>();
    mRowHeight = getResources().getDimensionPixelSize(R.dimen.default_month_row_height);
  }

  public void setFirstDay(CalendarDay calendarDay) {
    mFirstDay = calendarDay;
  }

  public void setMonthPosition(int position) {
    mMonthPosition = position;
    createDays();
    invalidate();
  }

  public void setSelectDay(CalendarDay calendarDay) {
    mSelectDay = calendarDay;
  }

  private void createDays() {
    mDays.clear();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(mFirstDay.getTime());
    int position = calendar.get(Calendar.DAY_OF_MONTH);
    calendar.roll(Calendar.DAY_OF_MONTH, -(position - 1));
    calendar.add(Calendar.MONTH, mMonthPosition);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    Log.e(TAG, month + " yue " + year);
    int daysNum = DayUtils.getDaysInMonth(month, year);
    for (int i = 0; i < daysNum; i++) {
      mDays.add(new CalendarDay(calendar));
      calendar.roll(Calendar.DAY_OF_MONTH, 1);
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    if (mDays.size() < 28) {
      super.onDraw(canvas);
      return;
    }
    rowNum = 0;
    drawWeekLable(canvas);
    drawMonthNum(canvas);
  }

  private void drawWeekLable(Canvas canvas) {
    String[] weeks = {" ","日","一","二","三","四","五","六"};
    for (int i = 0; i < weeks.length; i++) {

      String content = weeks[i];
      Paint.FontMetrics fontMetrics = mPaintNormal.getFontMetrics();
      float fontHeight = fontMetrics.bottom - fontMetrics.top;
      float textWidth = mPaintNormal.measureText(content);
      float parentWidth = getWidth() - 2 * getResources().getDimension(R.dimen.activity_horizontal_margin);
      float y = mRowHeight  * rowNum + mRowHeight - (mRowHeight - fontHeight) / 2 - fontMetrics.bottom;
      float x = getResources().getDimension(R.dimen.activity_horizontal_margin)
          + parentWidth / DAY_IN_WEEK * (i - 1)
          + parentWidth / DAY_IN_WEEK / 2 - textWidth / 2;
      mPaintNormal.setColor(getResources().getColor(R.color.color_7f7f7f));
      canvas.drawText(content, x, y, mPaintNormal);
    }
    rowNum++;
  }

  private void drawMonthNum(Canvas canvas) {
    for (int i = 0; i < mDays.size(); i++) {
      CalendarDay calendarDay = mDays.get(i);
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(calendarDay.getTime());
      int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
      String content = String.valueOf(calendarDay.day);
      Paint.FontMetrics fontMetrics = mPaintNormal.getFontMetrics();
      float fontHeight = fontMetrics.bottom - fontMetrics.top;
      float textWidth;
      if(DayUtils.isToday(calendar)){
        textWidth = mPaintNormal.measureText("今天");
        content = "今天";
      }else {
        textWidth = mPaintNormal.measureText(content);
      }
      float parentWidth = getWidth() - 2 * getResources().getDimension(R.dimen.activity_horizontal_margin);
      float y = mRowHeight  * rowNum + mRowHeight - (mRowHeight - fontHeight) / 2 - fontMetrics.bottom;
      float x = getResources().getDimension(R.dimen.activity_horizontal_margin)
          + parentWidth / DAY_IN_WEEK * (weekDay - 1)
          + parentWidth / DAY_IN_WEEK / 2 - textWidth / 2;

      //Log.e(TAG, "i :  " + i + "   weekday: " + weekDay + "      rownum: " + rowNum + "   y: " + y);
      if (mSelectDay.getDayString().equals(calendarDay.getDayString())) {
        canvas.drawCircle(getResources().getDimension(R.dimen.activity_horizontal_margin)
                + parentWidth / DAY_IN_WEEK * (weekDay - 1)
                + parentWidth / DAY_IN_WEEK / 2, mRowHeight  * rowNum + mRowHeight / 2, mRowHeight * 2 / 4, mPaintSelect
        );
      }


      Calendar endDate = Calendar.getInstance();
      endDate.add(Calendar.MONTH, 6);
      endDate.add(Calendar.DAY_OF_MONTH,-1);
      Calendar firstDayCalendar = Calendar.getInstance();
      firstDayCalendar.setTimeInMillis(mFirstDay.getTime());
      if(!DayUtils.isBeforeStartEnable(calendar,firstDayCalendar) || DayUtils.isAfterEndDate(calendar,endDate)){
        mPaintNormal.setColor(mTextNotClickColor);
        canvas.drawText(content, x, y, mPaintNormal);
      }else if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
        if(!DayUtils.isBeforeStartEnable(calendar,firstDayCalendar) || DayUtils.isAfterEndDate(calendar,endDate)){
          //do nothing. Befor has drawed not click color.
        }else{
          mPaintNormal.setColor(mTextSundaySaturdayColor);
          canvas.drawText(content, x, y, mPaintNormal);
        }
      }else{
        mPaintNormal.setColor(mTextNormalColor);
        canvas.drawText(content, x, y, mPaintNormal);
      }

      if (weekDay == 7) rowNum++;
    }
  }

  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), mRowHeight * mNumRows + mRowHeight / 2);
  }

  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      CalendarDay calendarDay = getDayFromLocation(event.getX(), event.getY());
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(calendarDay.getTime());
      Calendar endDate = Calendar.getInstance();
      endDate.add(Calendar.MONTH, 6);
      endDate.add(Calendar.DAY_OF_MONTH,-1);
      if (calendarDay != null ) {
        Calendar firstDayCalendar = Calendar.getInstance();
        firstDayCalendar.setTimeInMillis(mFirstDay.getTime());
        if(!DayUtils.isBeforeStartEnable(calendar,firstDayCalendar) || DayUtils.isAfterEndDate(calendar,endDate)){
          //do bothing!
        }else{
          mOnDayClickListener.onDayClick(calendarDay);
        }
      }
    }
    return true;
  }

  public CalendarDay getDayFromLocation(float x, float y) {
    int padding = getContext().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
    if (x < padding) {
      return null;
    }

    if (x > getWidth() - padding) {
      return null;
    }

    if (y < mRowHeight || y > (rowNum + 1) * mRowHeight) {
      return null;
    }

    int yDay = (int) (y - mRowHeight) / mRowHeight;

    int xday = (int) ((x - padding) / ((getWidth() - padding * 2) / DAY_IN_WEEK));

    int position = yDay * DAY_IN_WEEK + xday;

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(mFirstDay.getTime());
    int monthPosition = calendar.get(Calendar.DAY_OF_MONTH);
    calendar.roll(Calendar.DAY_OF_MONTH, -(monthPosition - 1));
    calendar.add(Calendar.MONTH, mMonthPosition);

    position = position - calendar.get(Calendar.DAY_OF_WEEK) + 1;
    if (position < 0 || position > mDays.size() - 1) return null;
    return mDays.get(position);
  }

  public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
    mOnDayClickListener = onDayClickListener;
  }

  public interface OnDayClickListener {
    void onDayClick(CalendarDay calendarDay);
  }

}
