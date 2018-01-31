package com.hugboga.custom.widget.city;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.AiResultActivity;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PriceFormat;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.collection.CollectionHelper;
import com.hugboga.tools.FormatUtil;
import com.hugboga.tools.NetImg;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 玩法Item布局展示
 * Created by HONGBO on 2017/12/6 12:18.
 */

public class CitySkuView extends FrameLayout {

    @BindView(R.id.city_item_root_layout)
    ConstraintLayout city_item_root_layout;
    @BindView(R.id.city_item_img)
    ImageView city_item_img;
    @BindView(R.id.city_item_img_price)
    TextView city_item_price; //玩法价格
    @BindView(R.id.city_item_title)
    TextView city_item_title; //标题
    @BindView(R.id.city_item_guide)
    ImageView city_item_guide; //司导头像
    @BindView(R.id.city_item_tip)
    TextView city_item_tip; //提示语1
    @BindView(R.id.city_item_tip2)
    TextView city_item_tip2; //提示语2
    @BindView(R.id.city_item_line)
    public ImageView line;
    @BindView(R.id.city_item_favor)
    TextView city_item_favor; //收藏数
    @BindView(R.id.city_item_hear)
    ImageView saveLineImg; //收藏线路

    DestinationGoodsVo destinationGoodsVo;

    public CitySkuView(@NonNull Context context) {
        this(context, null);
    }

    public CitySkuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_item, this);
        ButterKnife.bind(this, view);
    }

    /**
     * 显示数据
     */
    public void init(DestinationGoodsVo destinationGoodsVo) {
        this.destinationGoodsVo = destinationGoodsVo;
        Tools.showImageNotCenterCrop(city_item_img, destinationGoodsVo.goodsImageUrl, R.mipmap.home_default_route_item);
        city_item_favor.setText(String.valueOf(destinationGoodsVo.userFavorCount));
        city_item_title.setText(destinationGoodsVo.goodsName);
        city_item_price.setText(String.format(getContext().getString(R.string.city_sku_item_price),
                String.valueOf(PriceFormat.priceNoPoint(destinationGoodsVo.perPrice))));
        NetImg.showCircleImage(getContext(), city_item_guide, destinationGoodsVo.guideHeadImageUrl);
        city_item_tip.setText(getItemTitle(destinationGoodsVo));
        city_item_tip2.setText(String.format(getContext().getString(R.string.city_sku_title2),
                String.valueOf(destinationGoodsVo.guideCount)));
        saveLineImg.setSelected(CollectionHelper.getIns(getContext()).getCollectionLine().isCollection(destinationGoodsVo.goodsNo)); //显示收藏线路信息
    }

    /**
     * 组合显示玩法标题统计
     *
     * @return
     */
    private SpannableString getItemTitle(DestinationGoodsVo destinationGoodsVo) {
        String title1 = String.format(getContext().getString(R.string.city_sku_title1),
                String.valueOf(destinationGoodsVo.dayCount));
        String title2 = "";
        if (!TextUtils.isEmpty(destinationGoodsVo.placeList)) {
            title2 = String.format(getContext().getString(R.string.city_sku_title11), destinationGoodsVo.placeList);
        }
        SpannableString ssb = new SpannableString(title1 + title2);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#151515")), 0, title1.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    @OnClick({R.id.city_item_hear, R.id.city_item_root_layout, R.id.city_item_guide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_item_hear:
                if (CommonUtils.isLogin(getContext(), getEventSource())) {
                    saveLineImg.setEnabled(false);
                    saveLineImg.setSelected(!saveLineImg.isSelected());
                    CollectionHelper.getIns(getContext()).getCollectionLine().changeCollectionLine(destinationGoodsVo.goodsNo, saveLineImg.isSelected());
                    CommonUtils.showToast(getResources().getString(saveLineImg.isSelected() ? R.string.collect_succeed : R.string.collect_cancel));
                    if (saveLineImg.isSelected()) {
                        setSensorsShareEvent(destinationGoodsVo.goodsNo);
                    }
                    city_item_favor.setText(saveLineImg.isSelected() ?
                            String.valueOf((FormatUtil.getCountInteger(city_item_favor.getText().toString()) + 1))
                            : String.valueOf((FormatUtil.getCountInteger(city_item_favor.getText().toString()) - 1)));
                    saveLineImg.setEnabled(true);
                }
                break;
            case R.id.city_item_root_layout:
                Intent intent = new Intent(getContext(), SkuDetailActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, destinationGoodsVo.skuDetailUrl);
                intent.putExtra(Constants.PARAMS_ID, destinationGoodsVo.goodsNo);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                getContext().startActivity(intent);
                if (getContext() instanceof MainActivity) {
                    SensorsUtils.onAppClick(getEventSource(), "热门专辑", "首页-热门专辑");
                }
                break;
            case R.id.city_item_guide:
                if (TextUtils.isEmpty(destinationGoodsVo.guideId)) {
                    return;
                }
                GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
                params.guideId = destinationGoodsVo.guideId;
                Intent intent2 = new Intent(getContext(), GuideWebDetailActivity.class);
                intent2.putExtra(Constants.PARAMS_DATA, params);
                intent2.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                getContext().startActivity(intent2);
                break;
        }
    }

    public String getEventSource() {
        if (getContext() instanceof AiResultActivity) {
            AiResultActivity aiResultActivity = (AiResultActivity) getContext();
            return aiResultActivity.getEventSource();
        } else if (getContext() instanceof CityActivity) {
            CityActivity cityActivity = (CityActivity) getContext();
            return cityActivity.getEventSource();
        } else if (getContext() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getContext();
            return mainActivity.getEventSource();
        }
        return null;
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

    public void setCityView(int size) {
        city_item_root_layout.getLayoutParams().width = size;
        city_item_img.getLayoutParams().height = size;
        city_item_img.getLayoutParams().width = size;
    }

    public void setDesplayViewLayoutParams(int displayImgWidth, int displayImgHeight) {
        setCityView(displayImgWidth);
    }
}
