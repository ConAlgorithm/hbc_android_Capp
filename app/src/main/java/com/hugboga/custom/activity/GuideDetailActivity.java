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
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.EvaluateListItemView;
import com.hugboga.custom.widget.SimpleRatingBar;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 16/8/4.
 */
public class GuideDetailActivity extends BaseActivity implements GuideCarPhotosAdapter.OnItemClickListener {

    @Bind(R.id.guide_detail_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.guide_detail_name_tv)
    TextView nameTV;
    @Bind(R.id.guide_detail_attestation_iv)
    ImageView attestationIV;
    @Bind(R.id.guide_detail_location_iv)
    ImageView locationIV;
    @Bind(R.id.guide_detail_location_tv)
    TextView locationTV;
    @Bind(R.id.guide_detail_describe_tv)
    TextView describeTV;
    @Bind(R.id.guide_detail_platenumber_tv)
    TextView platenumberTV;
    @Bind(R.id.guide_detail_ratingView)
    SimpleRatingBar ratingView;
    @Bind(R.id.guide_detail_score_tv)
    TextView scoreTV;
    @Bind(R.id.guide_detail_right_line)
    View rightLineView;
    @Bind(R.id.guide_detail_left_line)
    View leftLineView;
    @Bind(R.id.guide_detail_plane_layout)
    LinearLayout planeLayout;
    @Bind(R.id.guide_detail_car_layout)
    LinearLayout charteredCarLayout;
    @Bind(R.id.guide_detail_single_layout)
    LinearLayout singleLayout;
    @Bind(R.id.guide_detail_evaluate_item)
    EvaluateListItemView evaluateItemView;
    @Bind(R.id.guide_detail_photo_recyclerview)
    RecyclerView carRecyclerView;
    @Bind(R.id.guide_detail_subtitle_photo_layout)
    FrameLayout carPhotosLayout;
    @Bind(R.id.header_detail_title_tv)
    TextView titleTV;
    @Bind(R.id.header_detail_right_2_btn)
    ImageView shareIV;
    @Bind(R.id.header_detail_right_1_btn)
    ImageView collectIV;

    private String guideId;
    private GuidesDetailData data;
    private DialogUtil mDialogUtil;
    private CollectGuideBean collectBean;
    private GuideCarPhotosAdapter carPhotosAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            guideId = savedInstanceState.getString(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                guideId = bundle.getString(Constants.PARAMS_DATA);
            }
        }

        setContentView(R.layout.fg_guide_detail);
        ButterKnife.bind(this);

        mDialogUtil = DialogUtil.getInstance(this);
        titleTV.setText(getString(R.string.guide_detail_subtitle_title));
        shareIV.setImageResource(R.mipmap.sddate_share);
        collectIV.setImageResource(R.drawable.selector_guide_detail_collect);

        requestData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (guideId != null) {
            outState.putSerializable(Constants.PARAMS_DATA, guideId);
        }
    }

    private void requestData() {
        requestData(new RequestGuideDetail(this, guideId));
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestGuideDetail) {
            RequestGuideDetail request = (RequestGuideDetail) _request;
            data = request.getData();
            if (data == null) {
                return;
            }
            beanConversion();

            attestationIV.setVisibility(View.VISIBLE);
            locationIV.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(data.getAvatar())) {
                avatarIV.setImageResource(R.mipmap.journey_head_portrait);
            } else {
                Tools.showImage(this, avatarIV, data.getAvatar());
            }
            nameTV.setText(data.getGuideName());
            locationTV.setText(data.getCityName());

            String carName = data.getCarBrandName() + data.getCarName();
            String carType = data.getCarTypeName() + data.getCarClassName();
            String describe  = getString(R.string.solidus, carName, carType);
            int describeWidth = UIUtils.getStringWidth(describeTV, describe);
            if (describeWidth > UIUtils.getScreenWidth() - UIUtils.dip2px(160)) {
                describeTV.setText(carName + "\n" + carType);
            } else {
                describeTV.setText(describe);
            }

            if (!TextUtils.isEmpty(data.getCarLicenceNo())) {
                platenumberTV.setText(getString(R.string.platenumber) + data.getCarLicenceNo());
            }
            ratingView.setRating(data.getServiceStar());
            scoreTV.setText(String.valueOf(data.getServiceStar()));
            collectIV.setSelected(data.isCollected());

            ArrayList<Integer> serviceTypes = data.getServiceTypes();
            if (serviceTypes != null) {
                boolean isShowPlane = false;
                boolean isShowCar = false;
                boolean isShowSingle = false;
                final int arraySize = serviceTypes.size();
                for (int i = 0; i < arraySize; i++) {
                    switch (serviceTypes.get(i)) {
                        case 1://可以预约接送机
                            isShowPlane = true;
                            break;
                        case 3://可以预约包车
                            isShowCar = true;
                            break;
                        case 4://可以预约单次接送
                            isShowSingle = true;
                            break;
                    }
                }
                planeLayout.setVisibility(isShowPlane ? View.VISIBLE : View.GONE);
                charteredCarLayout.setVisibility(isShowCar ? View.VISIBLE : View.GONE);
                singleLayout.setVisibility(isShowSingle ? View.VISIBLE : View.GONE);

                //控制分割线的隐藏
                leftLineView.setVisibility(isShowPlane && isShowCar ? View.VISIBLE : View.GONE);
                boolean isShowRightLine = (isShowSingle && isShowCar) || (isShowSingle && isShowPlane);
                rightLineView.setVisibility(isShowRightLine ? View.VISIBLE : View.GONE);
            }
            evaluateItemView.setGuideDetailData(data);

            if (data.getCarPhotosS() != null && data.getCarPhotosS().size() > 0) {
                carRecyclerView.setVisibility(View.VISIBLE);
                carPhotosLayout.setVisibility(View.VISIBLE);
                if (carPhotosAdapter == null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    carRecyclerView.setLayoutManager(layoutManager);
                    carRecyclerView.setHorizontalScrollBarEnabled(false);
                    ZDefaultDivider zDefaultDivider = new ZDefaultDivider();
                    zDefaultDivider.setItemOffsets(UIUtils.dip2px(3), 0, UIUtils.dip2px(3), 0);
                    carRecyclerView.addItemDecoration(zDefaultDivider);
                    carPhotosAdapter = new GuideCarPhotosAdapter(this);
                    carRecyclerView.setAdapter(carPhotosAdapter);
                    carPhotosAdapter.setItemClickListener(this);
                }
                carPhotosAdapter.setData(data.getCarPhotosS());
            } else {
                carRecyclerView.setVisibility(View.GONE);
                carPhotosLayout.setVisibility(View.GONE);
            }
        } else if (_request instanceof RequestUncollectGuidesId) {//取消收藏
            data.setIsFavored(0);
            collectIV.setSelected(false);
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_COLLECT, 0));
            CommonUtils.showToast(getString(R.string.collect_cancel));
        } else if (_request instanceof RequestCollectGuidesId) {//收藏
            data.setIsFavored(1);
            collectIV.setSelected(true);
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_COLLECT, 1));
            CommonUtils.showToast(getString(R.string.collect_succeed));
        }
    }

    @OnClick({R.id.guide_detail_plane_layout, R.id.guide_detail_car_layout,
            R.id.guide_detail_single_layout, R.id.header_detail_back_btn, R.id.header_detail_right_1_btn, R.id.header_detail_right_2_btn})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.guide_detail_plane_layout:
                intent = new Intent(this, PickSendActivity.class);
                intent.putExtra("collectGuideBean", beanConversion());
                intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                startActivity(intent);
                break;
            case R.id.guide_detail_car_layout:
                intent = new Intent(this, OrderSelectCityActivity.class);
                intent.putExtra("collectGuideBean", beanConversion());
                intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                intent.putExtra(Constants.PARAMS_SOURCE_DETAIL, getIntentSource());
                startActivity(intent);
                break;
            case R.id.guide_detail_single_layout:
                intent = new Intent(this, SingleNewActivity.class);
                intent.putExtra("collectGuideBean", beanConversion());
                intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                startActivity(intent);
                break;
            case R.id.header_detail_back_btn:
                finish();
                break;
            case R.id.header_detail_right_1_btn://收藏
                if (data == null) {
                    return;
                }
                EventUtil.onDefaultEvent(StatisticConstant.COLLECTG, getEventSource());
                mDialogUtil.showLoadingDialog();
                BaseRequest baseRequest = null;
                if (data.isCollected()) {
                    baseRequest = new RequestUncollectGuidesId(this, data.getGuideId());
                } else {
                    baseRequest = new RequestCollectGuidesId(this, data.getGuideId());
                }
                requestData(baseRequest);
                break;
            case R.id.header_detail_right_2_btn://分享
                if (data == null) {
                    break;
                }
                CommonUtils.shareDialog(this, data.getAvatar(),
                        getString(R.string.guide_detail_share_title),
                        getString(R.string.guide_detail_share_content),
                        ShareUrls.getShareGuideUrl(data, UserEntity.getUser().getUserId(this)));
                break;
        }
    }

    private CollectGuideBean beanConversion() {
        if (collectBean == null) {
            collectBean = new CollectGuideBean();
            collectBean.guideId = data.getGuideId();
            collectBean.name = data.getGuideName();
            collectBean.stars = data.getServiceStar();
            collectBean.carClass = data.getCarClass();
            collectBean.carType = data.getCarType();
            collectBean.numOfLuggage = data.getCarLuggageNum();
            collectBean.numOfPerson = data.getCarPersonNum();
            collectBean.avatar = data.getAvatar();
            collectBean.carDesc = data.getCarDesc();
            collectBean.carModel = data.getCarDesc();;
//          status;//可预约状态 1.可预约、0.不可预约
        }
        return collectBean;
    }

    @Override
    public void onItemClick(View view, int postion) {
        if (data.getCarPhotosL() != null && data.getCarPhotosL().size() > 0) {
            Intent intent = new Intent(GuideDetailActivity.this, LargerImageActivity.class);
            LargerImageActivity.Params params = new LargerImageActivity.Params();
            params.position = postion;
            params.imageUrlList = data.getCarPhotosL();
            intent.putExtra(Constants.PARAMS_DATA, params);
            startActivity(intent);
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
