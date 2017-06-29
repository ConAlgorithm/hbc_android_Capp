package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.DatePickerActivity;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.utils.DateUtils;

import java.text.ParseException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/16.
 */
public class SkuOrderChooseDateView extends LinearLayout{

    @Bind(R.id.sku_order_date_tv1)
    TextView dateTV1;
    @Bind(R.id.sku_order_date_tv2)
    TextView dateTV2;
    @Bind(R.id.sku_order_date_tv3)
    TextView dateTV3;
    @Bind(R.id.sku_order_date_other_tv)
    TextView otherTV;

    private String startDate;
    private TextView[] dateTVs = new TextView[3];
    private List<String> dateList;
    private String currentDate;
    private OnSelectedDateListener listener;

    public SkuOrderChooseDateView(Context context) {
        this(context, null);
    }

    public SkuOrderChooseDateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_order_choose_date, this);
        ButterKnife.bind(view);

        dateTVs[0] = dateTV1;
        dateTVs[1] = dateTV2;
        dateTVs[2] = dateTV3;
    }

    private void updateDateTV(String startDateStr, String chooseDateStr, String lastDateStr) {
        if (TextUtils.isEmpty(startDateStr) || TextUtils.isEmpty(chooseDateStr)) {
            return;
        }
        dateList = DateUtils.getOrderChooseDate(startDateStr, chooseDateStr, lastDateStr);
        int selectedIndex = 1;
        if (chooseDateStr.equalsIgnoreCase(lastDateStr)) {
            selectedIndex = 2;
        } else if (startDateStr.equalsIgnoreCase(chooseDateStr)) {
            selectedIndex = 0;
        }
        setDateTVSelected(selectedIndex);
    }

    private void setDateTVSelected(int selectedIndex) {
        if (dateList == null) {
            return;
        }
        final int dateListSize = dateList.size();
        for (int i = 0; i < dateListSize; i++) {
            if (i == selectedIndex) {
                if (listener != null && !dateList.get(i).equals(currentDate)) {
                    listener.onSelectedDate(dateList.get(i));
                }
                currentDate = dateList.get(i);
                dateTVs[i].setSelected(true);
            } else {
                dateTVs[i].setSelected(false);
            }
            dateTVs[i].setText(DateUtils.orderChooseDateTransform(dateList.get(i)));
        }
    }

    public void setStartDate(String data) {
        this.startDate = data;
        updateDateTV(startDate, startDate, null);
    }

    public String getServiceDate() {
        return currentDate;
    }

    public void setChooseDateBean(ChooseDateBean chooseDateBean) {
        if (chooseDateBean == null || TextUtils.isEmpty(chooseDateBean.halfDateStr)) {
            return;
        }
        updateDateTV(startDate, chooseDateBean.halfDateStr, chooseDateBean.maxDateStr);
    }

    @OnClick({R.id.sku_order_date_tv1, R.id.sku_order_date_tv2, R.id.sku_order_date_tv3, R.id.sku_order_date_other_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sku_order_date_tv1:
                setDateTVSelected(0);
                break;
            case R.id.sku_order_date_tv2:
                setDateTVSelected(1);
                break;
            case R.id.sku_order_date_tv3:
                setDateTVSelected(2);
                break;
            case R.id.sku_order_date_other_tv:
                Intent intent = new Intent(getContext(), DatePickerActivity.class);
                intent.putExtra(DatePickerActivity.PARAM_TYPE, DatePickerActivity.PARAM_TYPE_SINGLE_NOTEXT);
                intent.putExtra(DatePickerActivity.PARAM_TITLE, "请选择出发日期");
                if (!TextUtils.isEmpty(currentDate)) {
                    try {
                        ChooseDateBean chooseDateBean = new ChooseDateBean();
                        chooseDateBean.halfDateStr = currentDate;
                        chooseDateBean.halfDate = DateUtils.dateDateFormat.parse(currentDate);
                        chooseDateBean.type = DatePickerActivity.PARAM_TYPE_SINGLE_NOTEXT;
                        chooseDateBean.minDate = DateUtils.dateDateFormat.parse(startDate);
                        intent.putExtra("chooseDateBean", chooseDateBean);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                getContext().startActivity(intent);
                break;
        }
    }

    public interface OnSelectedDateListener {
        public void onSelectedDate(String date);
    }

    public void setOnSelectedDateListener(OnSelectedDateListener listener) {
        this.listener = listener;
    }
}
