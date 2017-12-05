package com.hugboga.custom.widget.city;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 城市头部统计信息
 * Created by HONGBO on 2017/11/21 19:59.
 */

public class CityHeaderCountView extends FrameLayout {

    Activity mActivity;

    @BindView(R.id.city_header_count_pay)
    TextView city_header_count_pay; //包车玩法
    @BindView(R.id.city_header_count_guide)
    TextView city_header_count_guide; //司导数

    public CityHeaderCountView(@NonNull Context context) {
        this(context, null);
    }

    public CityHeaderCountView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_header_count, this);
        ButterKnife.bind(this, view);
    }

    public void init(Activity activity, Integer payNum, Integer guideNum) {
        this.mActivity = activity;
        city_header_count_pay.setText(payNum.toString());
        city_header_count_guide.setText(guideNum.toString());
    }

    @OnClick({R.id.city_header_click1, R.id.city_header_click2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_header_click1:
                //包车玩法事件
//                if (mActivity != null && (mActivity instanceof CityListActivity)) {
//                    ((CityListActivity) mActivity).clickMoreSku();
//                }
                break;
            case R.id.city_header_click2:
                //中文司导事件
                if (mActivity != null && (mActivity instanceof CityListActivity)) {
                    ((CityListActivity) mActivity).clickMoreGuide();
                }
                break;
        }
    }
}
