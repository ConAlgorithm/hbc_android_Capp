package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class SkuItemView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.home_hot_search_city_img)
    ImageView imageView;
    @Bind(R.id.home_hot_search_city_fillter_view)
    View filterView;
    @Bind(R.id.home_hot_search_city_bottom_text)
    TextView guideCountView;
    @Bind(R.id.home_hot_search_city_title)
    TextView bottomTitle;
    @Bind(R.id.home_hot_search_city_item_custom_count)
    TextView customCount;
    @Bind(R.id.home_hot_search_city_item_per_price)
    TextView perPrice;
    @Bind(R.id.home_hot_search_city_item_bottom_layout)
    RelativeLayout bottomLayout;

    public SkuItemView(Context context) {
        this(context, null);
    }

    public SkuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_item, this);
        ButterKnife.bind(view);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);

        int displayImgWidth = UIUtils.getScreenWidth() - UIUtils.dip2px(8) * 2;
        int displayImgHeight = (int)((400 / 650.0) * displayImgWidth);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayImgWidth, displayImgHeight);
        imageView.setLayoutParams(params);
        filterView.setLayoutParams(params);
    }

    @Override
    public void update(Object _data) {
        final SkuItemBean skuItemBean = (SkuItemBean) _data;
        Tools.showImage(imageView, skuItemBean.goodsPicture, R.mipmap.home_default_route_item);
        customCount.setText(skuItemBean.transactionVolumes + "人已体验");
        bottomTitle.setText(skuItemBean.goodsName);
        guideCountView.setText(skuItemBean.guideAmount + "位中文司导带你玩");

        String price = "￥" + skuItemBean.perPrice;
        String count = "/人起";
        SpannableString spannableString = new SpannableString(price + count);
        spannableString.setSpan(new AbsoluteSizeSpan(15, true), 0, price.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(12, true), price.length(), count.length() + price.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        perPrice.setText(spannableString);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SkuDetailActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, skuItemBean.skuDetailUrl);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                intent.putExtra(SkuDetailActivity.WEB_SKU, skuItemBean);
                intent.putExtra("goodtype",skuItemBean.goodsType);
                intent.putExtra(Constants.PARAMS_ID, skuItemBean.goodsNo);
                intent.putExtra("type", 1);
                intent.putExtra(Constants.PARAMS_SOURCE, "首页线路列表");
                v.getContext().startActivity(intent);
                StatisticClickEvent.click(StatisticConstant.CLICK_RG, "首页");
                StatisticClickEvent.click(StatisticConstant.LAUNCH_DETAIL_RG,"首页");
            }
        });
    }
}
