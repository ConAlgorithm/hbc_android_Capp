package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hugboga.custom.R;


public class DateTimePopupPiker extends PopupWindow {

    View contentView;
    TextView cancle_btn,ok_btn;
    DatePicker datePicker;
    TimePicker timePicker;
    public DateTimePopupPiker(final Activity context,final OnDateTimeSelectListener onDateTimeSelectListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.date_time_layout, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(contentView);
        this.setWidth(w);
        this.setHeight(h);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
//        this.setAnimationStyle(R.style.AnimationPreview);

        cancle_btn = (TextView)contentView.findViewById(R.id.cancle_btn);
        ok_btn = (TextView)contentView.findViewById(R.id.cancle_btn);
        datePicker = (DatePicker)contentView.findViewById(R.id.date_piker);
        timePicker = (TimePicker)contentView.findViewById(R.id.time_picker);
        cancle_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                onDateTimeSelectListener.onDateTimeSelect("aaa123123");
            }
        });

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }

    public interface OnDateTimeSelectListener{
        void onDateTimeSelect(String value);
    }


}
