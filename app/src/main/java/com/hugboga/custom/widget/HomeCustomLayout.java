package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.OrderSelectCityActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleNewActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.CommonUtils;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 16/6/19.
 */
public class HomeCustomLayout extends LinearLayout{

    public HomeCustomLayout(Context context) {
        this(context, null);
    }

    public HomeCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);

        View view = inflate(getContext(), R.layout.view_home_custom, this);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.home_custom_chartered_layout, R.id.home_custom_pickup_layout,
            R.id.home_custom_single_layout, R.id.home_custom_travelfund_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_custom_chartered_layout://包车
                goDairy();
                StatisticClickEvent.click(StatisticConstant.LAUNCH_DETAIL_R, "首页");
                break;
            case R.id.home_custom_pickup_layout://中文接送机
                goPickSend();
                StatisticClickEvent.click(StatisticConstant.LAUNCH_J, "首页");
                break;
            case R.id.home_custom_single_layout://单次接送
                goSingle();
                StatisticClickEvent.click(StatisticConstant.LAUNCH_C, "首页");
                break;
            case R.id.home_custom_travelfund_layout:
                Intent intent = null;
                if (!UserEntity.getUser().isLogin(getContext())) {
                    CommonUtils.showToast(R.string.login_hint);
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    break;
                }
                intent = new Intent(getContext(), TravelFundActivity.class);
                getContext().startActivity(intent);
                break;
        }
    }

    /**
     * 以下代码copy自旧版本首页
     * */
    private void goPickSend(){
        Intent intent = new Intent(getContext(), PickSendActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, "首页");
        getContext().startActivity(intent);
    }

    private void goDairy(){
        Intent intent = new Intent(getContext(), OrderSelectCityActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, "首页");
        getContext().startActivity(intent);
    }

    private void goSingle(){
        Intent intent = new Intent(getContext(),SingleNewActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, "首页");
        getContext().startActivity(intent);
    }
}
