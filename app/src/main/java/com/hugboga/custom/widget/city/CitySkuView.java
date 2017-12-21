package com.hugboga.custom.widget.city;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.AiResultActivity;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.request.RequestCollectLineNo;
import com.hugboga.custom.data.request.RequestUncollectLinesNo;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PriceFormat;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
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

public class CitySkuView extends FrameLayout implements HttpRequestListener, HbcViewBehavior{

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
    public void init(DestinationGoodsVo destinationGoodsVo, boolean isFavious) {
        city_item_img.getLayoutParams().height = UIUtils.getScreenWidth() - UIUtils.dip2px(20);
        this.destinationGoodsVo = destinationGoodsVo;
        Tools.showImageNotCenterCrop(city_item_img, destinationGoodsVo.goodsImageUrl, R.mipmap.home_default_route_item);
        city_item_title.setText(destinationGoodsVo.goodsName);
        city_item_price.setText(String.format(getContext().getString(R.string.city_sku_item_price),
                String.valueOf(PriceFormat.priceNoPoint(destinationGoodsVo.perPrice))));
        NetImg.showCircleImage(getContext(), city_item_guide, destinationGoodsVo.guideHeadImageUrl);
        city_item_tip.setText(getItemTitle(destinationGoodsVo));
        city_item_tip2.setText(String.format(getContext().getString(R.string.city_sku_title2),
                String.valueOf(destinationGoodsVo.guideCount)));
        saveLineImg.setSelected(isFavious); //显示收藏线路信息
    }

    /**
     * 组合显示玩法标题统计
     *
     * @return
     */
    private String getItemTitle(DestinationGoodsVo destinationGoodsVo) {
        StringBuilder sb = new StringBuilder(String.format(getContext().getString(R.string.city_sku_title1),
                String.valueOf(destinationGoodsVo.userFavorCount), String.valueOf(destinationGoodsVo.dayCount)));
        if (!TextUtils.isEmpty(destinationGoodsVo.placeList)) {
            sb.append(String.format(getContext().getString(R.string.city_sku_title11), destinationGoodsVo.placeList));
        }
        return sb.toString();
    }

    @OnClick({R.id.city_item_hear, R.id.city_item_root_layout, R.id.city_item_guide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_item_hear:
                if (CommonUtils.isLogin(getContext(), getEventSource())) {
                    saveLineImg.setEnabled(false);
                    if (saveLineImg.isSelected()) {
                        HttpRequestUtils.request(getContext(), new RequestUncollectLinesNo(getContext(), destinationGoodsVo.goodsNo), CitySkuView.this, false);
                    } else {
                        HttpRequestUtils.request(getContext(), new RequestCollectLineNo(getContext(), destinationGoodsVo.goodsNo), CitySkuView.this, false);
                    }
                }
                break;
            case R.id.city_item_root_layout:
                Intent intent = new Intent(getContext(), SkuDetailActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, destinationGoodsVo.skuDetailUrl);
                intent.putExtra(Constants.PARAMS_ID, destinationGoodsVo.goodsNo);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                getContext().startActivity(intent);
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

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCollectLineNo) {
            saveLineImg.setSelected(true);
            if (onChangeFavious != null) {
                onChangeFavious.onChange(destinationGoodsVo, true);
            }
            CommonUtils.showToast(getResources().getString(R.string.collect_succeed));
            setSensorsShareEvent(destinationGoodsVo.goodsNo);
        } else if (request instanceof RequestUncollectLinesNo) {
            saveLineImg.setSelected(false);
            if (onChangeFavious != null) {
                onChangeFavious.onChange(destinationGoodsVo, false);
            }
            CommonUtils.showToast(getResources().getString(R.string.collect_cancel));
        }
        saveLineImg.setEnabled(true);
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    private ErrorHandler errorHandler;

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorHandler == null) {
            errorHandler = new ErrorHandler((Activity) getContext(), this);
        }
        errorHandler.onDataRequestError(errorInfo, request);
        saveLineImg.setEnabled(true);
        saveLineImg.setSelected(!saveLineImg.isSelected());
    }

    public String getEventSource() {
        if (getContext() instanceof AiResultActivity) {
            AiResultActivity aiResultActivity = (AiResultActivity) getContext();
            return aiResultActivity.getEventSource();
        } else if (getContext() instanceof CityActivity) {
            CityActivity cityActivity = (CityActivity) getContext();
            return cityActivity.getEventSource();
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

    private OnChangeFaviousListener onChangeFavious;

    public void setOnChangeFavious(OnChangeFaviousListener onChangeFavious) {
        this.onChangeFavious = onChangeFavious;
    }

    public interface OnChangeFaviousListener {
        void onChange(DestinationGoodsVo destinationGoodsVo, boolean isFavious);
    }

    @Override
    public void update(Object _data) {
        if (_data instanceof DestinationGoodsVo) {
            city_item_root_layout.getLayoutParams().width = UIUtils.getScreenWidth();
            city_item_img.getLayoutParams().height = UIUtils.getScreenWidth() - UIUtils.dip2px(20);
            DestinationGoodsVo bean = (DestinationGoodsVo) _data;
            init(bean, bean.isCollected == 1);
        }
    }
}
