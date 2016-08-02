package com.hugboga.custom.activity.datepicker;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.request.RequestOrderDetail;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.OrderDetailFloatView;
import com.hugboga.custom.widget.OrderDetailGuideInfo;
import com.hugboga.custom.widget.OrderDetailItineraryView;
import com.hugboga.custom.widget.OrderDetailTitleBar;
import com.hugboga.custom.widget.TopTipsLayout;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/8/2.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener, HttpRequestListener {

    @Bind(R.id.top_tips_layout)
    TopTipsLayout topTipsLayout;

    @Bind(R.id.order_detail_title_layout)
    OrderDetailTitleBar titleBar;

    @Bind(R.id.order_detail_guideinfo_view)
    OrderDetailGuideInfo guideInfoView;

    @Bind(R.id.order_detail_itinerary_view)
    OrderDetailItineraryView itineraryView;

    @Bind(R.id.order_detail_float_view)
    OrderDetailFloatView floatView;

    @Bind(R.id.order_detail_group_layout)
    LinearLayout groupLayout;

    @Bind(R.id.order_detail_empty_tv)
    TextView emptyTV;

    @Bind(R.id.order_detail_explain_tv)
    TextView explainTV;

    private PopupWindow popup;
    private View menuLayout;
    private Params params;
    private OrderBean orderBean;
    private DialogUtil mDialogUtil;

    public static class Params implements Serializable {
        public String orderId;
        public String source;
        public int orderType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.fg_order_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mDialogUtil = DialogUtil.getInstance(this);
        titleBar.setTitle(params.orderType);
        emptyTV.setVisibility(View.VISIBLE);
//        titleBar.findViewById(R.id.header_detail_right_1_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupWindow();
//            }
//        });
//        titleBar.findViewById(R.id.header_detail_right_2_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialogUtil.showCallDialog();
//            }
//        });
//        itineraryView.setFragment(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void genTopTIps() {
        if (null != orderBean) {
            if (orderBean.orderStatus.code == 2) {//未付款
                topTipsLayout.setText(R.string.order_detail_top1_tips);
            } else if (orderBean.orderStatus.code == 1) {//已付款
                topTipsLayout.setText(R.string.order_detail_top2_tips);
            } else {
                topTipsLayout.hide();
            }
        } else {
            topTipsLayout.hide();
        }
    }

    @Override
    public void onClick(View v) {

    }

}
