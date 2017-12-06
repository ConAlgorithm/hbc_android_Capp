package com.hugboga.custom.widget.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/11/24.
 */

public class HomeGoodsItemView extends LinearLayout implements HbcViewBehavior, View.OnClickListener{

    @BindView(R.id.home_goods_layout)
    LinearLayout goodsLayout;
    @BindView(R.id.home_goods_desplay_iv)
    ImageView desplayIV;
    @BindView(R.id.home_goods_title_tv)
    TextView titleTV;
    @BindView(R.id.home_goods_subtitle_tv)
    TextView subtitleTV;
    @BindView(R.id.home_goods_price_tv)
    TextView priceTV;

    private Object data;

    public HomeGoodsItemView(Context context) {
        this(context, null);
    }

    public HomeGoodsItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_home_goods_item, this);
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    @Override
    public void update(Object _data) {
        this.data = _data;
        if (_data instanceof HomeBean.TransferBean) {
            HomeBean.TransferBean transferBean = (HomeBean.TransferBean) _data;
            Tools.showImage(desplayIV, transferBean.airportPicture);
            titleTV.setText(transferBean.airportName);
            subtitleTV.setText(getContext().getResources().getString(R.string.home_goodes_item_subtitle, "" + transferBean.airportGuideNum, transferBean.airportUserNum));
            priceTV.setText(getContext().getResources().getString(R.string.home_goodes_price_car, "" + transferBean.airportPrice));
        } else if (_data instanceof HomeBean.CharteredBean) {
            HomeBean.CharteredBean charteredBean = (HomeBean.CharteredBean) _data;
            Tools.showImage(desplayIV, charteredBean.starCityPicture);
            titleTV.setText(charteredBean.starCityName);
            subtitleTV.setText(getContext().getResources().getString(R.string.home_goodes_item_subtitle, "" + charteredBean.charteredGuideNum, charteredBean.charteredUserNum));
            priceTV.setText(getContext().getResources().getString(R.string.home_goodes_price_car, "" + charteredBean.charteredPrice));
        }
    }

    public void setDesplayViewLayoutParams(int displayImgWidth, int displayImgHeight) {
        goodsLayout.getLayoutParams().width = displayImgWidth + UIUtils.dip2px(9) * 2;
        desplayIV.getLayoutParams().height = displayImgHeight;
        desplayIV.getLayoutParams().width = displayImgWidth;
    }

    @Override
    public void onClick(View v) {
        if (data instanceof HomeBean.TransferBean) {
            Intent intent = new Intent(getContext(), PickSendActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, "首页");
            getContext().startActivity(intent);
        } else if (data instanceof HomeBean.CharteredBean) {
            HomeBean.CharteredBean charteredBean = (HomeBean.CharteredBean) data;
            Intent intent = new Intent(getContext(), CharterFirstStepActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, "首页");
            if (charteredBean != null) {
                intent.putExtra(Constants.PARAMS_START_CITY_BEAN, DatabaseManager.getCityBean("" + charteredBean.starCityId));
            }
            getContext().startActivity(intent);
        }
    }
}
