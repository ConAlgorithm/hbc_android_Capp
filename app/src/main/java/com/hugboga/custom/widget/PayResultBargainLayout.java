package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BargainActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/8/30.
 */
public class PayResultBargainLayout extends RelativeLayout {

    @Bind(R.id.pay_result_bargain_parent_layout)
    RelativeLayout parentLayout;
    @Bind(R.id.pay_result_bargain_title_layout)
    RelativeLayout titleLayout;

    private String orderNo;

    public PayResultBargainLayout(Context context) {
        this(context, null);
    }

    public PayResultBargainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_pay_result_bargain_layout, this);
        ButterKnife.bind(view);

        int itemHeight = (int)((260 / 720.0) * UIUtils.getScreenWidth());
        RelativeLayout.LayoutParams parentParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, itemHeight);
        parentLayout.setLayoutParams(parentParams);

        int titleViewWidth = (int)(UIUtils.getScreenWidth() / 5.0 * 4);
        int titleViewHeight = (int)((76 / 576.0) * titleViewWidth);
        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(titleViewWidth, titleViewHeight);
        titleLayout.setLayoutParams(titleViewParams);
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @OnClick(R.id.pay_result_bargain_detail_view)
    public void intentBargainDetail() {
        if (orderNo == null) {
            return;
        }
        Intent intentBargain = new Intent(getContext(), BargainActivity.class);
        intentBargain.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
        intentBargain.putExtra("orderNo", orderNo);
        getContext().startActivity(intentBargain);
    }

}
