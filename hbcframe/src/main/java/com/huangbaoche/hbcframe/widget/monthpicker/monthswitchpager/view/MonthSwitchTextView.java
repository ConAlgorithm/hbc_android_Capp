package com.huangbaoche.hbcframe.widget.monthpicker.monthswitchpager.view;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.widget.monthpicker.model.CalendarDay;
import com.huangbaoche.hbcframe.widget.monthpicker.util.DayUtils;

import com.huangbaoche.hbcframe.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by tudou on 15-5-18.
 */
public class MonthSwitchTextView extends RelativeLayout implements View.OnClickListener{

  private final static String TAG = "MonthSwitchTextView";

  ForegroundImageView mIconLeft;
  ForegroundImageView mIconRight;
  TextView mTextTitle;
    RelativeLayout mLeftLayout;
    RelativeLayout mRightLayout;
  private int mPosition;
  private CalendarDay mFirstDay;
  private int mCount;
  private MonthRecyclerView mMonthRecyclerView;
  private int mPrePosition;

  public MonthSwitchTextView(Context context) {
    this(context, null);
  }

  public MonthSwitchTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MonthSwitchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context, attrs, defStyleAttr);
  }

  private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
    LayoutInflater.from(context).inflate(R.layout.view_month_switch_text, this);
      mIconLeft = (ForegroundImageView) findViewById(android.R.id.icon1);
      mIconRight = (ForegroundImageView) findViewById(android.R.id.icon2);
      mTextTitle = (TextView) findViewById(android.R.id.text1);
      mLeftLayout = (RelativeLayout) findViewById(R.id.icon1_layout);
      mLeftLayout.setOnClickListener(this);
      mRightLayout = (RelativeLayout) findViewById(R.id.icon2_layout);
      mRightLayout.setOnClickListener(this);
  }

  private void updateView() {
    if (mPosition == 0) {
      //mIconLeft.setVisibility(View.GONE);
      mIconLeft.setImageResource(R.mipmap.calendar_arrow_left);
        mLeftLayout.setEnabled(false);
    } else {
      //mIconLeft.setVisibility(View.VISIBLE);
      mIconLeft.setImageResource(R.mipmap.calendar_click_arrow_left);
        mLeftLayout.setEnabled(true);
    }
    if (mPosition == mCount - 1) {
      //mIconRight.setVisibility(View.GONE);
      mIconRight.setImageResource(R.mipmap.calendar_arrow_right);
        mRightLayout.setEnabled(false);
    } else {
      //mIconRight.setVisibility(View.VISIBLE);
      mIconRight.setImageResource(R.mipmap.calendar_click_arrow_right);
        mRightLayout.setEnabled(true);
    }
    updateText();
  }

  private void update() {
    updateView();
  }

  public void onClick(View view) {
    //switch (view.getId()) {
      //case R.id.icon1_layout:
      if(view.getId() == R.id.icon1_layout){
        mPosition--;
        update();
        mMonthRecyclerView.scrollToPosition(mPosition);
      }else if(view.getId() == R.id.icon2_layout){
          mPosition++;
          update();
          mMonthRecyclerView.scrollToPosition(mPosition);
      }

        //break;
      //case R.id.icon2_layout:

        //break;
    //}
  }

  public void setPosition(int position) {
    mPosition = position;
    update();
  }

  public void setDay(CalendarDay startDay, CalendarDay endDay) {
    mFirstDay = startDay;
    mCount = DayUtils.calculateMonthCount(startDay, endDay);
    update();
  }

  public void setMonthRecyclerView(MonthRecyclerView recyclerView) {
    mMonthRecyclerView = recyclerView;
  }

  private void updateText() {
    if (mPrePosition == mPosition && mPrePosition != 0) {
      return;
    }
    mPrePosition = mPosition;

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(mFirstDay.getTime());
    int position = calendar.get(Calendar.DAY_OF_MONTH);
    calendar.add(Calendar.DAY_OF_MONTH, -(position - 1));
    calendar.add(Calendar.MONTH, mPosition);
    int flags =
        DateUtils.FORMAT_NO_MONTH_DAY + DateUtils.FORMAT_SHOW_DATE + DateUtils.FORMAT_SHOW_YEAR;
    Log.e(TAG, DateUtils.formatDateTime(getContext(), calendar.getTimeInMillis(), flags));
    mTextTitle.setText(new SimpleDateFormat("yyyy年M月").format(calendar.getTime()));
  }
}
