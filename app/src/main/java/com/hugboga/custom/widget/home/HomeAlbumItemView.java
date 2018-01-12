package com.hugboga.custom.widget.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.collection.CollectionHelper;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import net.grobas.view.PolygonImageView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/11/23.
 * <p>
 * 收藏逻辑保留自旧代码
 */

public class HomeAlbumItemView extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.home_album_item_layout)
    LinearLayout albumItemLayout;
    @BindView(R.id.home_album_desplay_iv)
    ImageView desplayIV;
    @BindView(R.id.home_album_collect_iv)
    ImageView collectIV;
    @BindView(R.id.home_album_price_tv)
    TextView priceTV;
    @BindView(R.id.home_album_desplay_layout)
    RelativeLayout desplayLayout;
    @BindView(R.id.home_album_item_title_tv)
    TextView titleTV;
    @BindView(R.id.home_banner_avatar_iv)
    PolygonImageView avatarIV;
    @BindView(R.id.home_banner_type_tv)
    TextView typeTV;
    @BindView(R.id.home_banner_guide_name_tv)
    TextView guideNameTV;
    @BindView(R.id.home_banner_collect_tv)
    TextView collectTV;
    @BindView(R.id.home_banner_desc_tv)
    TextView descTV;

    private HomeBean.AlbumBean albumBean;
    private ErrorHandler errorHandler;

    public HomeAlbumItemView(Context context) {
        this(context, null);
    }

    public HomeAlbumItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_home_album_item, this);
        ButterKnife.bind(this);
        typeTV.setText(getContext().getResources().getString(R.string.home_goodes_type1));
        typeTV.setBackgroundColor(0xFF4A4A4A);
    }

    public void setDesplayViewLayoutParams(int displayImgWidth, int displayImgHeight) {
        albumItemLayout.getLayoutParams().width = displayImgWidth;
        desplayLayout.getLayoutParams().height = displayImgHeight;
        desplayLayout.getLayoutParams().width = displayImgWidth;
    }

    @Override
    public void update(Object _data) {
        if (!(_data instanceof HomeBean.AlbumBean) || _data == null) {
            return;
        }
        albumBean = (HomeBean.AlbumBean) _data;
        Tools.showImage(desplayIV, albumBean.goodsPic);
        titleTV.setText(albumBean.getGoodsName());
        priceTV.setText(getContext().getResources().getString(R.string.home_goodes_price, "" + albumBean.perPrice));
        guideNameTV.setText(albumBean.getGuideName());
        collectTV.setText(getContext().getResources().getString(R.string.home_goodes_favorite_num, "" + albumBean.goodsFavoriteNum));
        descTV.setText(getContext().getResources().getString(R.string.home_goodes_service_day, "" + albumBean.goodsServiceDayNum, albumBean.routeCityDesc));
        Tools.showImage(avatarIV, albumBean.guideAvatar, R.mipmap.icon_avatar_guide);

        // 遗留代码
        if (!UserEntity.getUser().isLogin(getContext())) {
            collectIV.setSelected(false);
        } else {
            collectIV.setSelected(CollectionHelper.getIns(getContext()).getCollectionLine().isCollection(albumBean.goodsNo));
        }
    }

    @OnClick(R.id.home_album_item_layout)
    public void intentSkuDetailActivity() {
        Intent intent = new Intent(getContext(), SkuDetailActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, albumBean.goodsDetailUrl);
        intent.putExtra(Constants.PARAMS_ID, albumBean.goodsNo);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        getContext().startActivity(intent);
        SensorsUtils.onAppClick(getEventSource(), "热门专辑", "首页-热门专辑");
    }

    @OnClick(R.id.home_banner_avatar_iv)
    public void intentGuideWebDetailActivity() {
        if (TextUtils.isEmpty(albumBean.guideId)) {
            return;
        }
        GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
        params.guideId = albumBean.guideId;
        Intent intent = new Intent(getContext(), GuideWebDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        getContext().startActivity(intent);
    }

    private String getEventSource() {
        return "首页";
    }

    @OnClick(R.id.home_album_collect_iv)
    public void onClickCollect() {
        SensorsUtils.onAppClick("首页", "收藏", "");
        if (CommonUtils.isLogin(getContext(), getEventSource())) {
            collectIV.setEnabled(false);
            collectIV.setSelected(!collectIV.isSelected());
            CollectionHelper.getIns(getContext()).getCollectionLine().changeCollectionLine(albumBean.goodsNo, collectIV.isSelected());
            if (collectIV.isSelected()) {
                CommonUtils.showToast(getResources().getString(R.string.collect_succeed));
                setSensorsEvent(albumBean.guideId, albumBean.goodsNo);
            } else {
                CommonUtils.showToast(getResources().getString(R.string.collect_cancel));
            }
            collectIV.setEnabled(true);
        }
    }

    //收藏司导埋点
    public static void setSensorsEvent(String guideId, String goodsNo) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("guideId", guideId);
            properties.put("favoriteType", "司导");
            properties.put("goodsNo", goodsNo);
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("favorite", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
