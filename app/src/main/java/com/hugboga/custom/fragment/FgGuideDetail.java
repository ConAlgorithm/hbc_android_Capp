package com.hugboga.custom.fragment;

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
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.ShareUrls;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestGuideDetail;
import com.hugboga.custom.data.request.RequestUncollectGuidesId;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.EvaluateListItemView;
import com.hugboga.custom.widget.RatingView;

import net.grobas.view.PolygonImageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * Created by qingcha on 16/5/28.
 */
@ContentView(R.layout.fg_guide_detail)
public class FgGuideDetail extends BaseFragment implements GuideCarPhotosAdapter.OnItemClickListener {

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
    @ViewInject(R.id.guide_detail_right_line)
    View rightLineView;
    @ViewInject(R.id.guide_detail_left_line)
    View leftLineView;
    @ViewInject(R.id.guide_detail_plane_layout)
    LinearLayout planeLayout;
    @ViewInject(R.id.guide_detail_car_layout)
    LinearLayout charteredCarLayout;
    @ViewInject(R.id.guide_detail_single_layout)
    LinearLayout singleLayout;
    @ViewInject(R.id.header_detail_title_tv)
    TextView titleTV;
    @ViewInject(R.id.header_detail_right_1_btn)
    ImageView collectIV;
    @ViewInject(R.id.guide_detail_evaluate_item)
    EvaluateListItemView evaluateItemView;
    @ViewInject(R.id.guide_detail_photo_recyclerview)
    RecyclerView carRecyclerView;
    @ViewInject(R.id.guide_detail_subtitle_photo_layout)
    FrameLayout carPhotosLayout;

    private String guideId;
    private GuidesDetailData data;
    private DialogUtil mDialogUtil;
    private CollectGuideBean collectBean;
    private GuideCarPhotosAdapter carPhotosAdapter;

    public static FgGuideDetail newInstance(String guideId) {
        FgGuideDetail fragment = new FgGuideDetail();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAMS_DATA, guideId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            guideId = savedInstanceState.getString(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                guideId = bundle.getString(Constants.PARAMS_DATA);
            }
        }
        mDialogUtil = DialogUtil.getInstance(getActivity());
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
        titleTV.setText(getString(R.string.guide_detail_subtitle_title));
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
            beanConversion();

            attestationIV.setVisibility(View.VISIBLE);
            locationIV.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(data.getAvatar())) {
                avatarIV.setImageResource(R.mipmap.collection_icon_pic);
            } else {
                Tools.showImage(getActivity(), avatarIV, data.getAvatar());
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

            platenumberTV.setText(data.getCarLicenceNo());
            ratingView.setLevel(data.getServiceStar());
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
            evaluateItemView.setGuideDetailData(FgGuideDetail.this, data);

            if (data.getCarPhotosS() != null && data.getCarPhotosS().size() > 0) {
                carRecyclerView.setVisibility(View.VISIBLE);
                carPhotosLayout.setVisibility(View.VISIBLE);
                if (carPhotosAdapter == null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    carRecyclerView.setLayoutManager(layoutManager);
                    carRecyclerView.setHorizontalScrollBarEnabled(false);
                    ZDefaultDivider zDefaultDivider = new ZDefaultDivider();
                    zDefaultDivider.setItemOffsets(UIUtils.dip2px(3), 0, UIUtils.dip2px(3), 0);
                    carRecyclerView.addItemDecoration(zDefaultDivider);
                    carPhotosAdapter = new GuideCarPhotosAdapter(getContext());
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
        } else if (_request instanceof RequestCollectGuidesId) {//收藏
            data.setIsFavored(1);
            collectIV.setSelected(true);
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_COLLECT, 1));
        }
    }


    @Event({R.id.guide_detail_plane_layout, R.id.guide_detail_car_layout,
            R.id.guide_detail_single_layout, R.id.header_detail_back_btn, R.id.header_detail_right_1_btn, R.id.header_detail_right_2_btn})
    private void onClickView(View view) {
        Bundle bundle = new Bundle();
        HashMap<String,String> map = new HashMap<String,String>();
        switch (view.getId()) {
            case R.id.guide_detail_plane_layout:
                finish();
                FgPickSend fgPickSend = new FgPickSend();
                bundle.putSerializable("collectGuideBean",beanConversion());
                fgPickSend.setArguments(bundle);
                startFragment(fgPickSend, bundle);
                break;
            case R.id.guide_detail_car_layout:
                finish();
                FgOrderSelectCity fgOrderSelectCity = new FgOrderSelectCity();
                bundle.putSerializable("collectGuideBean",beanConversion());
                fgOrderSelectCity.setArguments(bundle);
                startFragment(fgOrderSelectCity, bundle);
                break;
            case R.id.guide_detail_single_layout:
                finish();
                FgSingleNew fgSingleNew = new FgSingleNew();
                bundle.putSerializable("collectGuideBean",beanConversion());
                fgSingleNew.setArguments(bundle);
                startFragment(fgSingleNew);
                break;
//            case R.id.guide_detail_call_iv:
//                if (data == null) {
//                    break;
//                }
//                PhoneInfo.CallDial(getActivity(), data.getMobile());
//                break;
//            case R.id.ogi_evaluate_chat_iv:
//                ChatInfo chatInfo = new ChatInfo();
//                chatInfo.isChat = true;
//                chatInfo.userId = data.getGuideId();
//                chatInfo.userAvatar = data.getAvatar();
//                chatInfo.title = data.getGuideName();
//                chatInfo.targetType = "1";
//                RongIM.getInstance().startPrivateChat(getActivity(), "G" + data.getGuideId(), new ParserChatInfo().toJsonString(chatInfo));
//                break;
            case R.id.header_detail_back_btn:
                finish();
                break;
            case R.id.header_detail_right_1_btn://收藏
                if (data == null) {
                    return;
                }
                mDialogUtil.showLoadingDialog();
                BaseRequest baseRequest = null;
                if (data.isCollected()) {
                    baseRequest = new RequestUncollectGuidesId(getActivity(), data.getGuideId());
                } else {
                    baseRequest = new RequestCollectGuidesId(getActivity(), data.getGuideId());
                }
                requestData(baseRequest);
                break;
            case R.id.header_detail_right_2_btn://分享
                if (data == null) {
                    break;
                }
                CommonUtils.shareDialog(getActivity(), data.getAvatar(),
                        getString(R.string.guide_detail_share_title),
                        getString(R.string.guide_detail_share_content),
                        ShareUrls.getShareGuideUrl(data, UserEntity.getUser().getUserId(getActivity())));
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
            FgLargerImage.Params params = new FgLargerImage.Params();
            params.position = postion;
            params.imageUrlList = data.getCarPhotosL();
            startFragment(FgLargerImage.newInstance(params));
        }
    }
}
