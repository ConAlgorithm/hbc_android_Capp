package com.hugboga.custom.widget.calendar;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import java.util.Calendar;

public class CalendarAdapter extends ZBaseAdapter<CalendarCell, CalendarVH> {


    private String chooseDateStr; //选择的日期
    Context context;
    public CalendarAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int initResource() {
        return R.layout.calendar_cell;
    }

    @Override
    protected ZBaseViewHolder getVH(View view) {
        return new CalendarVH(view);
    }

    @Override
    protected void getView(int position, CalendarVH vh) {
        CalendarCell cell = datas.get(position);
        if(cell.getType() == 0){
            vh.mText.setTextColor(context.getResources().getColor(R.color.reserve_calendar_title));
            vh.mText.setText(cell.getName());
        }else if(cell.getType() == 2){
            vh.mText.setText(cell.getName());
            if(cell.calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cell.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                vh.mText.setTextColor(context.getResources().getColor(R.color.reserve_calendar_week_color1));
            }else{
                vh.mText.setTextColor(context.getResources().getColor(R.color.reserve_calendar_week_color2));
            }
            //当日之前的日期颜色变灰
            cell.setIsenable(CalendarUtils.isEnable(cell.calendar));
            if(!cell.isenable()){
                vh.mText.setTextColor(context.getResources().getColor(R.color.text_hint_color));
            }
            if(CalendarUtils.isToday(cell.calendar)){
                vh.mText.setTextColor(context.getResources().getColor(R.color.reserve_calendar_week_color2));
            }
            Calendar endDate = Calendar.getInstance();
            endDate.add(Calendar.MONTH, 6);
            endDate.add(Calendar.DAY_OF_MONTH,-1);
            if(CalendarUtils.isAfterEndDate(cell.calendar,endDate)){
                vh.mText.setTextColor(context.getResources().getColor(R.color.text_hint_color));
            }
        }
        vh.mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(vh.mText.)
            }
        });
    }



    public void setChooseDateStr(String chooseDateStr) {
        this.chooseDateStr = chooseDateStr;
    }
}
