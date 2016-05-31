package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.ShareUrls;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestGuideDetail;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.RatingView;

import net.grobas.view.PolygonImageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


import io.rong.imkit.RongIM;

/**
 * Created by qingcha on 16/5/28.
 */
@ContentView(R.layout.fg_guide_detail)
public class FgGuideDetail extends BaseFragment {

    @ViewInject(R.id.guide_detail_avatar_iv)
    PolygonImageView avatarIV;
    @ViewInject(R.id.guide_detail_name_tv)
    TextView nameTV;
    @ViewInject(R.id.guide_detail_attestation_iv)
    ImageView attestationIV;
    @ViewInject(R.id.guide_detail_location_iv)
    ImageView locationIV;
    @ViewInject(R.id.guide_detail_location_tv)
    TextView locationTV;
    @ViewInject(R.id.guide_detail_describe_tv)
    TextView describeTV;
    @ViewInject(R.id.guide_detail_platenumber_tv)
    TextView platenumberTV;
    @ViewInject(R.id.guide_detail_ratingView)
    RatingView ratingView;
    @ViewInject(R.id.guide_detail_score_tv)
    TextView scoreTV;
    @ViewInject(R.id.guide_detail_line)
    View lineView;
    @ViewInject(R.id.guide_detail_car_layout)
    LinearLayout charteredCarLayout;

    @ViewInject(R.id.header_detail_title_tv)
    TextView titleTV;

    private String guideId;
    private GuidesDetailData data;

    public static FgGuideDetail newInstance(String guideId) {
        FgGuideDetail fragment = new FgGuideDetail();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAMS_DATA, guideId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            guideId = savedInstanceState.getString(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                guideId = bundle.getString(Constants.PARAMS_DATA);
            }
        }
        titleTV.setText(getString(R.string.guide_detail_subtitle_title));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (guideId != null) {
            outState.putSerializable(Constants.PARAMS_DATA, guideId);
        }
    }

    @Override
    protected void initHeader() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return requestData(new RequestGuideDetail(getActivity(), guideId));
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestGuideDetail) {
            RequestGuideDetail request = (RequestGuideDetail) _request;
             data = request.getData();
            if (data == null) {
                return;
            }
            attestationIV.setVisibility(View.VISIBLE);
            locationIV.setVisibility(View.VISIBLE);
            Tools.showImage(getActivity(), avatarIV, data.getAvatar());
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

            platenumberTV.setText(data.getCarLicenceNo());
            ratingView.setLevel(data.getServiceStar());
            scoreTV.setText(String.valueOf(data.getServiceStar()));
            if (!data.isShowCharteredCar()) {
                lineView.setVisibility(View.GONE);
                charteredCarLayout.setVisibility(View.GONE);
            }
        }
    }


    @Event({R.id.guide_detail_plane_layout, R.id.guide_detail_car_layout,
            R.id.guide_detail_single_layout, R.id.guide_detail_call_iv, R.id.ogi_evaluate_chat_iv,
            R.id.header_detail_back_btn, R.id.header_detail_right_1_btn, R.id.header_detail_right_2_btn})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.guide_detail_plane_layout:
                break;
            case R.id.guide_detail_car_layout:
                break;
            case R.id.guide_detail_single_layout:
                break;
            case R.id.guide_detail_call_iv:
                if (data == null) {
                    break;
                }
                PhoneInfo.CallDial(getActivity(), data.getMobile());
                break;
            case R.id.ogi_evaluate_chat_iv:
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.isChat = true;
                chatInfo.userId = data.getGuideId();
                chatInfo.userAvatar = data.getAvatar();
                chatInfo.title = data.getGuideName();
                chatInfo.targetType = "1";
                RongIM.getInstance().startPrivateChat(getActivity(), "G" + data.getGuideId(), new ParserChatInfo().toJsonString(chatInfo));
                break;
            case R.id.header_detail_back_btn:
                finish();
                break;
            case R.id.header_detail_right_1_btn://收藏

                break;
            case R.id.header_detail_right_2_btn://分享
                if (data == null) {
                    break;
                }
                CommonUtils.shareDialog(getActivity(), data.getAvatar(),
                        getString(R.string.guide_detail_share_title),
                        getString(R.string.guide_detail_share_title),
                        ShareUrls.getShareGuideUrl(data, UserEntity.getUser().getUserId(getActivity())));
                break;
        }
    }
}
