package com.hugboga.custom.widget.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestCollectLineNo;
import com.hugboga.custom.data.request.RequestUncollectGuidesId;
import com.hugboga.custom.data.request.RequestUncollectLinesNo;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/11/23.
 *
 * 收藏逻辑保留自旧代码
 */

public class HomeAlbumItemView extends LinearLayout implements HbcViewBehavior, HttpRequestListener {

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
        titleTV.setText(albumBean.goodsName);
        priceTV.setText(getContext().getResources().getString(R.string.home_goodes_price, "" + albumBean.perPrice));
        guideNameTV.setText(albumBean.guideName);
        collectTV.setText(getContext().getResources().getString(R.string.home_goodes_favorite_num, "" + albumBean.goodsFavoriteNum));
        descTV.setText(getContext().getResources().getString(R.string.home_goodes_service_day, "" + albumBean.goodsServiceDayNum, albumBean.routeCityDesc));
        Tools.showImage(avatarIV, albumBean.guideAvatar, R.mipmap.icon_avatar_guide);

        // 遗留代码
        if (!UserEntity.getUser().isLogin(getContext())){
            collectIV.setSelected(false);
        } else {
            collectIV.setSelected(albumBean.isCollected == 1);
        }
    }

    @OnClick(R.id.home_album_item_layout)
    public void intentSkuDetailActivity() {
        Intent intent = new Intent(getContext(), SkuDetailActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, albumBean.goodsDetailUrl);
        intent.putExtra(Constants.PARAMS_ID, albumBean.goodsNo);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        getContext().startActivity(intent);
        SensorsUtils.onAppClick(getEventSource(),"热门专辑","首页-热门专辑");
    }

    @OnClick(R.id.home_banner_avatar_iv)
    public void intentGuideWebDetailActivity() {
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
        if (CommonUtils.isLogin(getContext(), getEventSource())) {
            collectIV.setEnabled(false);
            if (collectIV.isSelected()) {
                HttpRequestUtils.request(getContext(),new RequestUncollectLinesNo(getContext(), albumBean.goodsNo), HomeAlbumItemView.this, false);
            } else {
                HttpRequestUtils.request(getContext(),new RequestCollectLineNo(getContext(), albumBean.goodsNo), HomeAlbumItemView.this, false);
            }
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCollectLineNo) {
            albumBean.isCollected = 1;
            collectIV.setSelected(true);
            CommonUtils.showToast(getResources().getString(R.string.collect_succeed));
            setSensorsEvent(albumBean.guideId);
        } else if(request instanceof RequestUncollectLinesNo) {
            albumBean.isCollected = 0;
            collectIV.setSelected(false);
            CommonUtils.showToast(getResources().getString(R.string.collect_cancel));
        }
        collectIV.setEnabled(true);
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorHandler == null) {
            errorHandler = new ErrorHandler((Activity) getContext(), this);
        }
        errorHandler.onDataRequestError(errorInfo, request);
        collectIV.setEnabled(true);
        collectIV.setSelected(!collectIV.isSelected());
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    //收藏司导埋点
    public static void setSensorsEvent(String guideId) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("guideId", guideId);
            properties.put("favoriteType", "司导");
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("favorite", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}