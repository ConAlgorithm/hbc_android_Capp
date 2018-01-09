package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.RecommendedGoodsBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/8/30.
 */
public class PayResultRecommendItemView extends RelativeLayout implements HbcViewBehavior{

    @BindView(R.id.recommend_item_parent_layout)
    RelativeLayout parentLayout;
    @BindView(R.id.recommend_item_bg_iv)
    ImageView bgIV;
    @BindView(R.id.recommend_item_fillter_view)
    View fillterView;
    @BindView(R.id.recommend_item_city_tv)
    TextView cityTV;
    @BindView(R.id.recommend_item_desc_tv)
    TextView descTV;
    @BindView(R.id.recommend_item_price_tv)
    TextView priceTV;

    public PayResultRecommendItemView(Context context) {
        this(context, null);
    }

    public PayResultRecommendItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_pay_result_recommend_item, this);
        ButterKnife.bind(view);
        int itemWidth = (UIUtils.getScreenWidth() - UIUtils.dip2px(10) * 2 - UIUtils.dip2px(14)) / 2;
        int imgHeight = (int)((236 / 328.0) * itemWidth);
        parentLayout.getLayoutParams().width = itemWidth;
        bgIV.getLayoutParams().height = imgHeight;
        fillterView.getLayoutParams().height = imgHeight;
    }

    @Override
    public void update(Object _data) {
        final RecommendedGoodsBean.RecommendedGoodsItemBean itemBean = (RecommendedGoodsBean.RecommendedGoodsItemBean) _data;

        Tools.showImage(bgIV, itemBean.pics, R.mipmap.line_goods_dafault);

        cityTV.setText(String.format("%1$s%2$s日", itemBean.depCityName, "" + itemBean.daysCount));

        descTV.setText(itemBean.getGoodsName());

        String priceStr = String.format("¥%1$s起/人", "" + itemBean.perPrice);
        SpannableStringBuilder ssb = new SpannableStringBuilder(priceStr);
        ssb.setSpan(new RelativeSizeSpan(1.4f), 1, String.valueOf(itemBean.perPrice).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        priceTV.setText(ssb);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SkuDetailActivity.class);
                intent.putExtra(Constants.PARAMS_ID, itemBean.goodsNo);
                intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
                v.getContext().startActivity(intent);
            }
        });
    }
}
