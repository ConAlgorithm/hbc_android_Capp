package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/10/20.
 */
public class HomeRouteFreeItemView extends RelativeLayout implements HbcViewBehavior{

    @Bind(R.id.route_free_item_title_tv)
    TextView titleTV;
    @Bind(R.id.route_free_item_left_iv)
    ImageView leftIV;
    @Bind(R.id.route_free_item_right_iv)
    ImageView rightIV;
    @Bind(R.id.route_free_item_guide_count_tv)
    TextView guideCountTV;
    @Bind(R.id.route_free_item_description_tv)
    TextView descriptionTV;

    public HomeRouteFreeItemView(Context context) {
        this(context, null);
    }

    public HomeRouteFreeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_route_free_item, this);
        ButterKnife.bind(this, view);

        double displayImgWidth = UIUtils.getScreenWidth() * (620 / 720.0) / 2;
        double displayImgHeight = (218 / 310.0) * displayImgWidth;
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams((int)displayImgWidth, (int)displayImgHeight);
        leftIV.setLayoutParams(ivParams);
        rightIV.setLayoutParams(ivParams);
    }

    @Override
    public void update(Object _data) {
        final SkuItemBean data = (SkuItemBean) _data;
        if (data == null) {
            return;
        }
        if (data.goodsPics != null && data.goodsPics.size() > 0) {
            Tools.showImage(leftIV, data.goodsPics.get(0));
            String rightImgUrl = data.goodsPicture;
            if (data.goodsPics.size() > 1) {
                rightImgUrl = data.goodsPics.get(1);
            }
            Tools.showImage(rightIV, rightImgUrl);
        }
        titleTV.setText(data.goodsLable);
        guideCountTV.setText(String.format("%1$s 位当地中文司导", data.guideAmount));
        descriptionTV.setText(data.goodsName);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSkuDetail(data);
            }
        });
    }

    private void intentSkuDetail(SkuItemBean skuItemBean) {
        if (skuItemBean == null) {
            return;
        }
        Intent intent = new Intent(getContext(), SkuDetailActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, skuItemBean.skuDetailUrl);
        intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
        intent.putExtra(SkuDetailActivity.WEB_SKU, skuItemBean);
        getContext().startActivity(intent);
        StatisticClickEvent.click(StatisticConstant.CLICK_RT, "首页");
    }
}
