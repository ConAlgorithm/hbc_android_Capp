package com.hugboga.custom.widget.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.action.data.ActionExam;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;

import net.grobas.view.PolygonImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/11/23.
 */

public class HomeBannerItemView extends LinearLayout implements HbcViewBehavior, View.OnClickListener {

    public static final float DESPLAY_IMG_RATIO = 700 / 670.0f;

    @BindView(R.id.home_banner_desplay_iv)
    ImageView desplayIV;
    @BindView(R.id.home_banner_title_tv)
    TextView titleTV;
    @BindView(R.id.home_banner_avatar_iv)
    PolygonImageView avatarIV;
    @BindView(R.id.home_banner_avatar_icon_iv)
    ImageView avatarIconIV;
    @BindView(R.id.home_banner_type_tv)
    TextView typeTV;
    @BindView(R.id.home_banner_guide_name_tv)
    TextView guideNameTV;
    @BindView(R.id.home_banner_collect_tv)
    TextView collectTV;
    @BindView(R.id.home_banner_desc_tv)
    TextView descTV;

    private HomeBean.BannerBean bannerBean;

    public HomeBannerItemView(Context context) {
        super(context);
        inflate(context, R.layout.view_home_banner_item, this);
        ButterKnife.bind(this);

        final int marginLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_margin_left);
        int bannerWidth = UIUtils.getScreenWidth() - marginLeft;
        int bannerHeight = (int) (DESPLAY_IMG_RATIO * bannerWidth);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, bannerHeight);
        desplayIV.setLayoutParams(params);
        this.setOnClickListener(this);
    }

    @Override
    public void update(Object _data) {
        if (!(_data instanceof HomeBean.BannerBean) || _data == null) {
            return;
        }
        Resources resources = getContext().getResources();
        bannerBean = (HomeBean.BannerBean) _data;
        Tools.showImage(desplayIV, bannerBean.bannerPicture);
        if (bannerBean.bannerType == 1 || bannerBean.bannerType == 3 || bannerBean.bannerType == 4) {//1活动、3广告、4精选
            titleTV.setText(bannerBean.bannerName);
            int typeStringId = 0;
            switch (bannerBean.bannerType) {
                case 1:
                    typeStringId = R.string.home_goodes_type2;
                    break;
                case 3:
                    typeStringId = R.string.home_goodes_type3;
                    break;
                case 4:
                    typeStringId = R.string.home_goodes_type4;
                    break;
            }
            typeTV.setText(resources.getString(typeStringId));
            typeTV.setBackgroundColor(getContext().getResources().getColor(R.color.default_yellow));
            guideNameTV.setVisibility(GONE);
            descTV.setVisibility(View.GONE);
            collectTV.setText(bannerBean.bannerDesc);
            avatarIV.setVisibility(GONE);
            avatarIconIV.setVisibility(GONE);
        } else if (bannerBean.bannerType == 2) {//商品
            titleTV.setText(bannerBean.getGoodsName());
            typeTV.setText(resources.getString(R.string.home_goodes_type1));
            typeTV.setBackgroundColor(0xFF4A4A4A);
            guideNameTV.setVisibility(VISIBLE);
            guideNameTV.setText(bannerBean.getGuideName());
            collectTV.setText(resources.getString(R.string.home_goodes_favorite_num, "" + bannerBean.goodsFavoriteNum));
            descTV.setText(resources.getString(R.string.home_goodes_service_day, "" + bannerBean.goodsServiceDay, bannerBean.routeCityDesc));
            descTV.setVisibility(View.VISIBLE);
            avatarIV.setVisibility(VISIBLE);
            avatarIconIV.setVisibility(VISIBLE);
            Tools.showImage(avatarIV, bannerBean.guideAvatar, R.mipmap.icon_avatar_guide);
        }
    }

    @Override
    public void onClick(View v) {
        if (bannerBean == null) {
            return;
        }
        SensorsUtils.setSensorsClickBanner(bannerBean.bannerAddress, bannerBean.sequence);
        if (bannerBean.needLogin == 1) {
            boolean isLogin = CommonUtils.isLogin(getContext(), getEventSource());
            if (!isLogin) return;
        }
        if (bannerBean.bannerType == 2) {
            Intent intent = new Intent(getContext(), SkuDetailActivity.class);
            intent.putExtra(WebInfoActivity.WEB_URL, bannerBean.bannerAddress);
            intent.putExtra(Constants.PARAMS_ID, bannerBean.bannerSettingId + "");
            intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
            getContext().startActivity(intent);
        } else {
            if (bannerBean.pushScheme == null) {
                if (!TextUtils.isEmpty(bannerBean.bannerAddress)) {
                    Intent intent = new Intent(v.getContext(), WebInfoActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    intent.putExtra(WebInfoActivity.WEB_URL, bannerBean.bannerAddress);

                    //增加分享查询参数
                    intent.putExtra(WebInfoActivity.WEB_SHARE_BTN, true);
                    intent.putExtra(WebInfoActivity.WEB_SHARE_NO, String.valueOf(bannerBean.bannerSettingId));

                    v.getContext().startActivity(intent);
                }
            } else {
                ActionController actionFactory = ActionController.getInstance();
                bannerBean.pushScheme.source = getEventSource();
                bannerBean.pushScheme.exam = getActionExam(bannerBean);//增加分享查询参数
                actionFactory.doAction(getContext(), bannerBean.pushScheme);
            }
        }
    }

    private ActionExam getActionExam(HomeBean.BannerBean bannerBean) {
        ActionExam exam = new ActionExam();
        exam.isShareBtn = true;
        exam.shareNo = String.valueOf(bannerBean.bannerSettingId);
        return exam;
    }

    @OnClick(R.id.home_banner_avatar_iv)
    public void intentGuideWebDetailActivity() {
        if (bannerBean == null || TextUtils.isEmpty(bannerBean.guideId)) {
            return;
        }
        GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
        params.guideId = bannerBean.guideId;
        Intent intent = new Intent(getContext(), GuideWebDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        getContext().startActivity(intent);
    }


    private String getEventSource() {
        return "BANNER";
    }

}
