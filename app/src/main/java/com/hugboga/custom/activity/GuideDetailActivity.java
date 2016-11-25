package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.GuideCarPhotosAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.ShareUrls;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestGuideDetail;
import com.hugboga.custom.data.request.RequestUncollectGuidesId;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.EvaluateListItemView;
import com.hugboga.custom.widget.GuideDetailCarInfoView;
import com.hugboga.custom.widget.GuideDetailScrollView;
import com.hugboga.custom.widget.SimpleRatingBar;
import com.hugboga.custom.widget.TagGroup;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 16/8/4.
 */
public class GuideDetailActivity extends BaseActivity{

    @Bind(R.id.guide_detail_titlebar)
    RelativeLayout titlebar;
    @Bind(R.id.header_detail_right_2_btn)
    ImageView collectIV;
    @Bind(R.id.header_detail_right_1_btn)
    ImageView shareIV;
    @Bind(R.id.header_detail_title_tv)
    TextView titleTV;

    @Bind(R.id.guide_detail_scrollview)
    GuideDetailScrollView scrollView;
    @Bind(R.id.guide_detail_city_bg_layout)
    RelativeLayout cityBgLayout;
    @Bind(R.id.guide_detail_city_bg_iv)
    ImageView cityBgIV;
    @Bind(R.id.guide_detail_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.guide_detail_city_name_tv)
    TextView cityNameTV;
    @Bind(R.id.guide_detail_evaluate_item)
    EvaluateListItemView evaluateItemView;
    @Bind(R.id.guide_detail_carinfo_view)
    GuideDetailCarInfoView carinfoView;

    @Bind(R.id.guide_detail_taggroup)
    TagGroup tagGroup;
    @Bind(R.id.guide_detail_ratingView)
    SimpleRatingBar ratingView;
    @Bind(R.id.guide_detail_score_tv)
    TextView scoreTV;

    @Bind(R.id.guide_detail_name_tv)
    TextView nameTV;
    @Bind(R.id.guide_detail_gender_iv)
    ImageView genderIV;
    @Bind(R.id.guide_detail_driver_layout)
    LinearLayout driverLayout;
    @Bind(R.id.guide_detail_driver_avatar_iv)
    PolygonImageView driverAvatarIV;
    @Bind(R.id.guide_detail_driver_name_tv)
    TextView driverNameTV;
    @Bind(R.id.guide_detail_driver_gender_iv)
    ImageView driverGenderIV;

    @Bind(R.id.guide_detail_subtitle_appointment_line)
    View appointmentLine;
    @Bind(R.id.guide_detail_subtitle_appointment_tv)
    TextView appointmentTV;
    @Bind(R.id.guide_detail_plane_layout)
    RelativeLayout planeLayout;
    @Bind(R.id.guide_detail_car_layout)
    RelativeLayout charteredCarLayout;
    @Bind(R.id.guide_detail_single_layout)
    RelativeLayout singleLayout;


    private Params params;
    private GuidesDetailData data;

    private DialogUtil mDialogUtil;
    private CollectGuideBean collectBean;

    public static class Params implements Serializable {
        public String guideId;
        public String guideCarId;
        public String guideAgencyDriverId;
        public boolean isSelectedService = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_guide_detail);
        ButterKnife.bind(this);

        initUI();
        requestData();

        setSensorsDefaultEvent("司导个人页", SensorsConstant.GPROFILE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void initUI() {

        mDialogUtil = DialogUtil.getInstance(this);
        collectIV.setImageResource(R.drawable.selector_guide_detail_collect);
        shareIV.setImageResource(R.mipmap.guide_detail_share);

        int cityBgHeight = (int)(UIUtils.getScreenWidth() * (400 / 750.0f));
        cityBgLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, cityBgHeight + UIUtils.dip2px(40)));
        cityBgIV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, cityBgHeight));
        scrollView.setTitlebar(titlebar, cityBgHeight);

        carinfoView.setGuideCarId(params.guideCarId);

        if (isOnlyShow()) {
            collectIV.setVisibility(View.GONE);
            shareIV.setVisibility(View.GONE);
        }
    }

    private boolean isOnlyShow() {
        if (params.isSelectedService) {
            return true;
        } else {
            return false;
        }
    }

    private void requestData() {
        if (params != null) {
            requestData(new RequestGuideDetail(this, params.guideId, params.guideCarId, params.guideAgencyDriverId));
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestGuideDetail) {
            RequestGuideDetail request = (RequestGuideDetail) _request;
            data = request.getData();
            if (data == null) {
                return;
            }

            //收藏状态
            collectIV.setSelected(data.isCollected());

            //城市背景图
            Tools.showImage(cityBgIV, data.cityBackGroundPicSrc, R.drawable.guides_detail_city_bg);

            //城市-国家
            cityNameTV.setText(data.cityName + "-" + data.countryName);

            //地接社或司导头像
            Tools.showImage(avatarIV, data.avatar, R.mipmap.journey_head_portrait);

            //地接社或司导名称
            nameTV.setText(data.guideName);
            titleTV.setText(data.guideName);
            if (data.agencyType == 0) { //个人司导(显示性别)
                driverLayout.setVisibility(View.GONE);
                if (data.gender == 1 || data.gender == 2) {
                    genderIV.setVisibility(View.VISIBLE);
                    genderIV.setBackgroundResource(data.gender == 1 ? R.mipmap.man_icon : R.mipmap.woman_icon);
                } else {
                    genderIV.setVisibility(View.GONE);
                }
            } else if (!TextUtils.isEmpty(data.agencyDriverName)) { //显示服务司导
                genderIV.setVisibility(View.GONE);
                driverLayout.setVisibility(View.VISIBLE);
                Tools.showImage(driverAvatarIV, data.agencyDriverAvatar, R.mipmap.journey_head_portrait);
                driverNameTV.setText("服务司导:" + data.agencyDriverName);
                if (data.agencyDriverGender == 1 || data.agencyDriverGender == 2) {
                    driverGenderIV.setVisibility(View.VISIBLE);
                    driverGenderIV.setBackgroundResource(data.agencyDriverGender == 1 ? R.mipmap.man_icon : R.mipmap.woman_icon);
                } else {
                    driverGenderIV.setVisibility(View.GONE);
                }
            } else {
                genderIV.setVisibility(View.GONE);
                driverLayout.setVisibility(View.GONE);
            }

            //服务星级
            ratingView.setRating(data.serviceStar);
            scoreTV.setText(String.valueOf(data.serviceStar));

            if (isOnlyShow() || data.agencyType == 3 || (data.serviceDaily != 1 && data.serviceJsc != 1)) {
                appointmentLine.setVisibility(View.GONE);
                appointmentTV.setVisibility(View.GONE);

                collectIV.setVisibility(View.GONE);
                shareIV.setVisibility(View.GONE);

                charteredCarLayout.setVisibility(View.GONE);
                planeLayout.setVisibility(View.GONE);
                singleLayout.setVisibility(View.GONE);
            } else {
                appointmentLine.setVisibility(View.VISIBLE);
                appointmentTV.setVisibility(View.VISIBLE);
                //是否可服务包车，0否，1是
                charteredCarLayout.setVisibility(data.serviceDaily == 1 ? View.VISIBLE : View.GONE);
                //是否可服务接送机、单次接送，0否，1是
                planeLayout.setVisibility(data.serviceJsc == 1 ? View.VISIBLE : View.GONE);
                singleLayout.setVisibility(data.serviceJsc == 1 ? View.VISIBLE : View.GONE);
            }

            //评价
            evaluateItemView.setGuideDetailData(data);
            carinfoView.update(data);

            //评价标签
            if (data.commentLabels != null && data.commentLabels.size() > 0) {
                tagGroup.setVisibility(View.VISIBLE);
                final int labelsSize = data.commentLabels.size();
                ArrayList<View> viewList = new ArrayList<View>(labelsSize);
                for (int i = 0; i < labelsSize; i++) {
                    GuidesDetailData.CommentLabel label = data.commentLabels.get(i);
                    if (label == null || TextUtils.isEmpty(label.labelName)) {
                        continue;
                    }
                    if (i < tagGroup.getChildCount()) {
                        TextView tagTV = (TextView)tagGroup.getChildAt(i);
                        tagTV.setVisibility(View.VISIBLE);
                        tagTV.setText(label.labelName + "  " + label.labelCount);
                    } else {
                        viewList.add(getNewTagView(label.labelName + "  " + label.labelCount));
                    }
                }
                for (int j = labelsSize; j < tagGroup.getChildCount(); j++) {
                    tagGroup.getChildAt(j).setVisibility(View.GONE);
                }
                tagGroup.setTags(viewList, tagGroup.getChildCount() <= 0);
            } else {
                tagGroup.setVisibility(View.GONE);
            }
        }else if (_request instanceof RequestUncollectGuidesId) {//取消收藏
            data.isFavored = 0;
            collectIV.setSelected(false);
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_COLLECT, 0));
            CommonUtils.showToast(getString(R.string.collect_cancel));
        } else if (_request instanceof RequestCollectGuidesId) {//收藏
            data.isFavored = 1;
            collectIV.setSelected(true);
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_COLLECT, 1));
            CommonUtils.showToast(getString(R.string.collect_succeed));
        }
    }

    private CollectGuideBean getCollectBean() {
        if (collectBean == null) {
            collectBean = new CollectGuideBean();
            collectBean.guideId = data.guideId;
            collectBean.name = data.guideName;
        }
        return collectBean;
    }

    public TextView getNewTagView(String label) {
        TextView tagTV = new TextView(this);
        tagTV.setPadding(UIUtils.dip2px(22), UIUtils.dip2px(4), UIUtils.dip2px(22), UIUtils.dip2px(4));
        tagTV.setTextSize(14);
        tagTV.setBackgroundResource(R.drawable.shape_evaluate_tag);
        tagTV.setTextColor(0xFF111111);
        tagTV.setEnabled(false);
        tagTV.setText(label);
        return tagTV;
    }

    @OnClick({R.id.guide_detail_plane_layout, R.id.guide_detail_car_layout,
            R.id.guide_detail_single_layout, R.id.header_detail_back_btn, R.id.header_detail_right_1_btn, R.id.header_detail_right_2_btn})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.guide_detail_plane_layout:
                intent = new Intent(this, PickSendActivity.class);
                intent.putExtra("collectGuideBean", getCollectBean());
                intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                startActivity(intent);
                break;
            case R.id.guide_detail_car_layout:
                intent = new Intent(this, OrderSelectCityActivity.class);
                intent.putExtra("collectGuideBean", getCollectBean());
                intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                startActivity(intent);
                break;
            case R.id.guide_detail_single_layout:
                intent = new Intent(this, SingleNewActivity.class);
                intent.putExtra("collectGuideBean", getCollectBean());
                intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                startActivity(intent);
                break;
            case R.id.header_detail_back_btn:
                finish();
                break;
            case R.id.header_detail_right_2_btn://收藏
                if (data == null) {
                    return;
                }
                EventUtil.onDefaultEvent(StatisticConstant.COLLECTG, getEventSource());
                mDialogUtil.showLoadingDialog();
                BaseRequest baseRequest = null;
                if (data.isCollected()) {
                    baseRequest = new RequestUncollectGuidesId(this, data.guideId);
                } else {
                    baseRequest = new RequestCollectGuidesId(this, data.guideId);
                }
                requestData(baseRequest);
                break;
            case R.id.header_detail_right_1_btn://分享
                if (data == null) {
                    break;
                }
                CommonUtils.shareDialog(this, data.avatar,
                        getString(R.string.guide_detail_share_title),
                        getString(R.string.guide_detail_share_content),
                        ShareUrls.getShareGuideUrl(data, UserEntity.getUser().getUserId(this)));
                break;
        }
    }

    @Override
    public String getEventSource() {
        return "司导个人页";
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_GPROFILE;
    }
}
