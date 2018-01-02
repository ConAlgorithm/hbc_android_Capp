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
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.collection.CollectionUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotLinesItemView extends LinearLayout implements HbcViewBehavior {

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
    @BindView(R.id.home_hot_search_city_layout)
    LinearLayout containerView;
    @BindView(R.id.save_guild_layout)
    LinearLayout save_guild_layout;
    @BindView(R.id.save_line)
    ImageView saveLine;
    Context context;

    public HotLinesItemView(Context context) {
        this(context, null);
    }

    public HotLinesItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = inflate(context, R.layout.home_hot_search_city_item, this);
        ButterKnife.bind(view);
    }

    public void setImageBound(int displayImgWidth, int displayImgHeight) {
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
                    String source = "首页";
                    if (v.getContext() instanceof CityActivity) {
                        CityActivity cityActivity = (CityActivity) v.getContext();
                        CityActivity.Params paramsData = cityActivity.paramsData;
                        if (paramsData != null) {
                            switch (paramsData.cityHomeType) {
                                case CITY:
                                    source = "城市";
                                    break;
                                case ROUTE:
                                    source = "线路圈";
                                    break;
                                case COUNTRY:
                                    source = "国家";
                                    break;
                            }
                        }
                    }
                    intent.putExtra(Constants.PARAMS_SOURCE, source);
                    if (skuItemBean.goodsClass == 1) {
                        StatisticClickEvent.click(StatisticConstant.CLICK_RG, source);
                    } else {
                        StatisticClickEvent.click(StatisticConstant.CLICK_RT, source);
                    }
                    v.getContext().startActivity(intent);


                }
            });

            if (!UserEntity.getUser().isLogin(context)) {
                saveLine.setSelected(false);
            }else{
                saveLine.setSelected(CollectionUtils.getIns(getContext()).isCollectionLine(skuItemBean.goodsNo));
            }
            save_guild_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String source = "";
                    if (getContext() instanceof CityActivity) {
                        source = ((CityActivity) getContext()).getEventSource();
                    }
                    if (CommonUtils.isLogin(context, source)) {
                        save_guild_layout.setEnabled(false);
                        ImageView saveLine = (ImageView) view.findViewById(R.id.save_line);
                        saveLine.setSelected(!saveLine.isSelected());
                        CollectionUtils.getIns(getContext()).changeCollectionLine(skuItemBean.goodsNo, saveLine.isSelected());
                        if (saveLine.isSelected()) {
                            if (skuItemBean != null) {
                                saveLine.setSelected(true);
                                CommonUtils.showToast("收藏成功");
                            }
                            setSensorsShareEvent(skuItemBean.goodsNo);
                        } else {
                            //data.isCollected= 1;
                            CommonUtils.showToast("已取消收藏");
                        }
                        save_guild_layout.setEnabled(true);
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
}
