package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.GuideCarListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.GuidesDetailData;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/10/8.
 */
public class GuideDetailCarInfoView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.guide_detail_car_info_item_view1)
    GuideCarInfoItemView itemView1;
    @Bind(R.id.guide_detail_car_info_item_view2)
    GuideCarInfoItemView itemView2;

    @Bind(R.id.guide_detail_car_info_more_tv)
    TextView moreTV;

    private GuidesDetailData data;
    public String guideCarId;

    public GuideDetailCarInfoView(Context context) {
        this(context, null);
    }

    public GuideDetailCarInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        View view = inflate(context, R.layout.view_guidedetail_carinfo, this);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.guide_detail_car_info_more_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guide_detail_car_info_more_tv:
                if (data == null) {
                    return;
                }
                Intent intent = new Intent(getContext(), GuideCarListActivity.class);
                intent.putExtra(Constants.PARAMS_ID, data.guideId);
                if (!TextUtils.isEmpty(guideCarId)) {
                    intent.putExtra(GuideCarListActivity.PARAMS_GUIDE_CAR_ID, guideCarId);
                }
                getContext().startActivity(intent);
                break;
        }
    }

    @Override
    public void update(Object _data) {
        data = (GuidesDetailData)_data;
        if (data == null || data.guideCars == null || data.guideCars.size() <= 0) {
            setVisibility(View.GONE);
            return;
        }
        final int guideCarSize = data.guideCars.size();
        setVisibility(View.VISIBLE);
        itemView1.update(data.guideCars.get(0));
        if (guideCarSize > 1) {
            itemView2.setVisibility(View.VISIBLE);
            itemView2.update(data.guideCars.get(1));
        } else {
            itemView2.setVisibility(View.GONE);
        }
        moreTV.setText(String.format("查看全部%1$s条车辆信息", data.guideCarCount));
    }

    public void setGuideCarId(String guideCarId) {
        this.guideCarId = guideCarId;
    }

}
