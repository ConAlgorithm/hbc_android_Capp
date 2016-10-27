package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by qingcha on 16/6/19.
 */
public class HomeRouteItemView extends RelativeLayout implements HbcViewBehavior{

    @Bind(R.id.home_route_item_bg_iv)
    ImageView bgIV;
    @Bind(R.id.home_route_item_guide_count_tv)
    TextView guideCountTV;
    @Bind(R.id.home_route_item_title_tv)
    TextView titleTV;
    @Bind(R.id.home_route_item_price_tv)
    TextView priceTV;
    @Bind(R.id.home_route_item_other_tv)
    TextView otherTV;

    public HomeRouteItemView(Context context) {
        this(context, null);
    }

    public HomeRouteItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xFFFFFFFF);
        View view = inflate(context, R.layout.view_home_route_item, this);
        ButterKnife.bind(this, view);

        setPadding(0, UIUtils.dip2px(8), 0, UIUtils.dip2px(6));
        double displayImgWidth = UIUtils.getScreenWidth() * (620 / 720.0);
        double displayImgHeight = (330 / 620.0) * displayImgWidth;
        bgIV.setLayoutParams(new RelativeLayout.LayoutParams((int)displayImgWidth, (int)displayImgHeight));
    }

    @Override
    public void update(Object _data) {
        final SkuItemBean data = (SkuItemBean) _data;
        if (data == null) {
            return;
        }

        if (TextUtils.isEmpty(data.goodsPicture)) {
            bgIV.setImageResource(R.mipmap.home_default_route_item);
        } else {
            Tools.showImage(bgIV, data.goodsPicture, R.mipmap.home_default_route_item);
        }
        guideCountTV.setText(String.format("%1$s 位当地中文司导", data.guideAmount));
        titleTV.setText(data.goodsName);
        priceTV.setText("" + data.perPrice);

        String otherStr = "起/人·%1$s日";
        if (data.hotelStatus == 1) {// 是否含酒店
            otherStr += "·含酒店";
        }
        otherTV.setText(String.format(otherStr, data.daysCount));
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
        intent.putExtra("goodtype",skuItemBean.goodsType);
        intent.putExtra("type","1");
        getContext().startActivity(intent);
        StatisticClickEvent.click(StatisticConstant.CLICK_RG, "首页");
    }
}
