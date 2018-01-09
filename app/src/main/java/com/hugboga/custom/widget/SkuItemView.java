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

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.collection.CollectionHelper;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SkuItemView extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.home_hot_search_city_img)
    ImageView imageView;
    @BindView(R.id.home_hot_search_city_fillter_view)
    View filterView;
    @BindView(R.id.home_hot_search_city_bottom_text)
    TextView guideCountView;
    @BindView(R.id.home_hot_search_city_title)
    TextView bottomTitle;
    @BindView(R.id.home_hot_search_city_item_custom_count)
    TextView customCount;
    @BindView(R.id.home_hot_search_city_item_per_price)
    TextView perPrice;
    @BindView(R.id.home_hot_search_city_item_bottom_layout)
    RelativeLayout bottomLayout;
    @BindView(R.id.save_guild_layout)
    LinearLayout save_guild_layout;
    @BindView(R.id.save_line)
    ImageView saveLine;
    Context context;

    public SkuItemView(Context context) {
        this(context, null);
    }

    public SkuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = inflate(context, R.layout.view_sku_item, this);
        ButterKnife.bind(view);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);

        int displayImgWidth = UIUtils.getScreenWidth() - UIUtils.dip2px(8) * 2;
        int displayImgHeight = (int) ((400 / 650.0) * displayImgWidth);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayImgWidth, displayImgHeight);
        imageView.setLayoutParams(params);
        filterView.setLayoutParams(params);
    }

    SkuItemBean skuItemBean;

    @Override
    public void update(Object _data) {
        skuItemBean = (SkuItemBean) _data;
        if (skuItemBean != null) {
            Tools.showImage(imageView, skuItemBean.goodsPicture, R.mipmap.home_default_route_item);
            customCount.setText(skuItemBean.transactionVolumes + "人已体验");
            bottomTitle.setText(skuItemBean.getGoodsName());
            guideCountView.setText(skuItemBean.guideAmount + "位中文司导可服务");

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
                    intent.putExtra(SkuDetailActivity.WEB_SKU, skuItemBean);
                    intent.putExtra(Constants.PARAMS_ID, skuItemBean.goodsNo);
                    intent.putExtra(Constants.PARAMS_SOURCE, "包车线路");
                    v.getContext().startActivity(intent);
                    if (skuItemBean.goodsClass == 1) {
                        StatisticClickEvent.click(StatisticConstant.CLICK_RG, "商品列表页");
                    } else {
                        StatisticClickEvent.click(StatisticConstant.CLICK_RT, "商品列表页");
                    }
                }
            });
            if (!UserEntity.getUser().isLogin(context)) {
                saveLine.setSelected(false);
            } else {
                saveLine.setSelected(CollectionHelper.getIns(getContext()).getCollectionLine().isCollection(skuItemBean.goodsNo));
            }
            save_guild_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CommonUtils.isLogin(context, getEventSource())) {
                        save_guild_layout.setEnabled(false);
                        ImageView saveLine = (ImageView) view.findViewById(R.id.save_line);
                        saveLine.setSelected(!saveLine.isSelected());
                        CollectionHelper.getIns(getContext()).getCollectionLine().changeCollectionLine(skuItemBean.goodsNo, saveLine.isSelected());
                        CommonUtils.showToast(saveLine.isSelected() ? getResources().getString(R.string.collect_succeed)
                                : getResources().getString(R.string.collect_cancel));
                        save_guild_layout.setEnabled(true);
                        if (saveLine.isSelected()) {
                            setSensorsShareEvent(skuItemBean.goodsNo);
                        }
                    }
                }
            });
        }
    }

    //收藏商品埋点
    public static void setSensorsShareEvent(String goodsNo) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("goodsNo", goodsNo);
            properties.put("favoriteType", "商品");
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("favorite", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEventSource() {
        return "商品列表";
    }
}
