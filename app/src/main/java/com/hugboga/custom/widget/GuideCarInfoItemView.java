package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.LargerImageActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.utils.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/10/8.
 */
public class GuideCarInfoItemView extends LinearLayout {

    @Bind(R.id.guide_carinfo_car_iv)
    ImageView carIV;
    @Bind(R.id.guide_carinfo_current_tv)
    TextView currentTV;
    @Bind(R.id.guide_carinfo_pic_count_tv)
    TextView picCountTV;

    @Bind(R.id.guide_carinfo_name_tv)
    TextView nameTV;
    @Bind(R.id.guide_carinfo_seat_num_tv)
    TextView seatNumTV;
    @Bind(R.id.guide_carinfo_luggage_num_tv)
    TextView luggageNumTV;
    @Bind(R.id.guide_carinfo_plate_num_tv)
    TextView plateNumTV;

    private GuideCarBean guideCarBean = null;

    public GuideCarInfoItemView(Context context) {
        this(context, null);
    }

    public GuideCarInfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_guide_car_info_item, this);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.guide_carinfo_car_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guide_carinfo_car_iv:
                if (guideCarBean.carPhotosL != null && guideCarBean.carPhotosL.size() > 0) {
                    Intent intent = new Intent(getContext(), LargerImageActivity.class);
                    LargerImageActivity.Params params = new LargerImageActivity.Params();
                    params.imageUrlList = guideCarBean.carPhotosL;
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    getContext().startActivity(intent);
                }
                break;
        }
    }

    public void update(GuideCarBean _data) {
        if (_data == null) {
            return;
        }
        guideCarBean = _data;
        if (TextUtils.isEmpty(guideCarBean.carPhoto)) {
//            carIV.setImageResource(R.mipmap.journey_head_portrait);
        } else {
            Tools.showImage(carIV, guideCarBean.carPhoto);
        }

        currentTV.setVisibility(guideCarBean.isInOrder == 1 ? View.VISIBLE : View.GONE);

        final int picCount = guideCarBean.carPhotosL != null ? guideCarBean.carPhotosL.size() : 0;
        picCountTV.setText(picCount + "张");

        nameTV.setText(guideCarBean.carInfo2);
        seatNumTV.setText(guideCarBean.carInfo1);
        luggageNumTV.setText(String.format("可载%1$s人, %2$s件行李", guideCarBean.modelGuestNum, guideCarBean.modelLuggageNum));
        if (TextUtils.isEmpty(guideCarBean.carLicenceNoCovered)) {
            plateNumTV.setText("");
        } else {
            plateNumTV.setText( "车牌: " + guideCarBean.carLicenceNoCovered);
        }
    }
}
