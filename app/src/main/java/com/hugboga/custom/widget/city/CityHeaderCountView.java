package com.hugboga.custom.widget.city;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;

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
    @BindView(R.id.city_header_count2_root)
    ConstraintLayout city_header_count2_root;
    @BindView(R.id.city_header_count_guide2)
    TextView city_header_count_guide2; //司导数
    @BindView(R.id.textView7)
    TextView textView7; //超过xxx司导的“超过”

    public CityHeaderCountView(@NonNull Context context) {
        this(context, null);
    }

    public CityHeaderCountView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_header_count, this);
        ButterKnife.bind(this, view);
    }

    public void init(Activity activity, Integer payNum, Integer guideNum, Integer servieGuideNum) {
        this.mActivity = activity;
        if (servieGuideNum == null || servieGuideNum == 0) {
            setVisibility(GONE);
            return;
        }
        if (payNum > 0) {
            city_header_count_pay.setText(payNum.toString());
            city_header_count_guide.setText(guideNum.toString());
            if (city_header_count2_root != null) {
                city_header_count2_root.setVisibility(View.GONE);
            }
        } else {
            //隐藏玩法相关
            if (city_header_count2_root != null) {
                city_header_count2_root.setVisibility(View.VISIBLE);
                city_header_count_guide2.setText(guideNum.toString());
            }
        }
    }

    @OnClick({R.id.city_header_click1, R.id.city_header_click2, R.id.city_header_count2_root})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_header_click1:
                //包车玩法事件
//                if (mActivity != null && (mActivity instanceof CityActivity)) {
//                    ((CityActivity) mActivity).clickMoreSku();
//                }
                break;
            case R.id.city_header_click2:
            case R.id.city_header_count2_root:
                //中文司导事件
                if (mActivity != null && (mActivity instanceof CityActivity)) {
                    ((CityActivity) mActivity).clickMoreGuide();
                }
                break;
        }
    }

    /**
     * 超过xx司导是否显示超过
     *
     * @param isShow
     */
    public void showMoreGuideTxt(boolean isShow) {
        textView7.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
