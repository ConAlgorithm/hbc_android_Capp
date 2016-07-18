package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AppraisementBean;
import com.hugboga.custom.data.bean.EvaluateTagBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestEvaluateNew;
import com.hugboga.custom.data.request.RequestEvaluateTag;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.EvaluateTagGroup;
import com.hugboga.custom.widget.RatingView;

import net.grobas.view.PolygonImageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.view.annotation.Event;

import de.greenrobot.event.EventBus;

/**
 * Created by qingcha on 16/5/29.
 */
@ContentView(R.layout.fg_evaluate)
public class FgEvaluate extends BaseFragment implements RatingView.OnLevelChangedListener {

    @ViewInject(R.id.evaluate_avatar_iv)
    PolygonImageView avatarIV;
    @ViewInject(R.id.evaluate_name_tv)
    TextView nameTV;
    @ViewInject(R.id.evaluate_score_ratingview)
    RatingView scoreRatingview;
    @ViewInject(R.id.evaluate_describe_tv)
    TextView describeTV;
    @ViewInject(R.id.evaluate_plate_number_tv)
    TextView plateNumberTV;
    @ViewInject(R.id.evaluate_active_tv)
    TextView activeTV;
    @ViewInject(R.id.evaluate_score_tv)
    TextView scoreTV;
    @ViewInject(R.id.evaluate_ratingView)
    RatingView ratingview;
    @ViewInject(R.id.evaluate_taggroup)
    EvaluateTagGroup tagGroup;
    @ViewInject(R.id.evaluate_comment_et)
    EditText commentET;
    @ViewInject(R.id.evaluate_submit_tv)
    TextView submitTV;

    private OrderBean orderBean;
    private DialogUtil mDialogUtil;
    private boolean isFirstIn = true;
    private boolean isSubmitEvaluated = false;//是否提交过评价，提交了通知详情页更新。

    public static FgEvaluate newInstance(OrderBean orderBean) {
        FgEvaluate fragment = new FgEvaluate();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.PARAMS_DATA, orderBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            orderBean = savedInstanceState.getParcelable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                orderBean = bundle.getParcelable(Constants.PARAMS_DATA);
            }
        }
        mDialogUtil = DialogUtil.getInstance(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (orderBean != null) {
            outState.putParcelable(Constants.PARAMS_DATA, orderBean);
        }
    }

    @Override
    protected void initHeader() {
        fgTitle.setText(getActivity().getString(R.string.evaluate_title));
        final OrderGuideInfo guideInfo = orderBean.orderGuideInfo;
        if (guideInfo == null) {
            return;
        }
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubmitEvaluated) {
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_EVALUATION));
                }
                finish();
            }
        });
        if (TextUtils.isEmpty(guideInfo.guideAvatar)) {
            avatarIV.setImageResource(R.mipmap.collection_icon_pic);
        } else {
            Tools.showImage(getContext(), avatarIV, guideInfo.guideAvatar);
        }
        nameTV.setText(guideInfo.guideName);
        scoreRatingview.setLevel((float)guideInfo.guideStarLevel);
        describeTV.setText(guideInfo.guideCar);//TODO 折行
        plateNumberTV.setText(guideInfo.carNumber);

        if (isEvaluated()) {//已评价
            ratingview.setLevel(orderBean.appraisement.totalScore);
            if (TextUtils.isEmpty(orderBean.appraisement.content)) {
                commentET.setVisibility(View.GONE);
            } else {
                commentET.setVisibility(View.VISIBLE);
                commentET.setText(orderBean.appraisement.content);
            }
            commentET.setEnabled(false);
            commentET.setBackgroundColor(0x00000000);
            submitTV.setVisibility(View.GONE);
            scoreTV.setVisibility(View.VISIBLE);
            if (orderBean.appraisement.totalScore >= 5) {
                scoreTV.setText(getContext().getString(R.string.evaluate_evaluated_satisfied));
            } else {
                scoreTV.setText(getContext().getString(R.string.evaluate_evaluated_ordinary));
            }
            ratingview.setOnLevelChangedListener(null);
            ratingview.setTouchable(false);
            tagGroup.setTagEnabled(false);
            tagGroup.setEvaluatedData(orderBean.appraisement.guideLabels);
        } else {
            commentET.setEnabled(true);
            commentET.setBackgroundResource(R.drawable.border_evaluate_comment);
            requestData(new RequestEvaluateTag(getContext(), orderBean.orderType));
        }

        if (isActive()) {//活动
            String activeText = null;
            if (isEvaluated() && orderBean.appraisement.totalScore >= 5) {
                activeText = getContext().getString(R.string.evaluate_active_evaluated_get, orderBean.priceCommentReward);
            } else if (isEvaluated()) {
                activeText = getContext().getString(R.string.evaluate_active_evaluated);
            } else {//未评价
                activeText = getContext().getString(R.string.evaluate_active, orderBean.priceCommentReward);
            }
            activeTV.setText(activeText);
            activeTV.setVisibility(View.VISIBLE);
        } else {
            activeTV.setVisibility(View.GONE);
        }
    }

    /**
     * 是否评价过
     * */
    private boolean isEvaluated() {
        return orderBean.userCommentStatus == 1 && orderBean.appraisement != null;
    }

    /**
     * 是否有活动
     * */
    private boolean isActive() {
        return orderBean.priceCommentReward != 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onLevelChanged(RatingView starView, float level) {
        if (isFirstIn) {
            isFirstIn = false;
            submitTV.setVisibility(View.VISIBLE);
            commentET.setVisibility(View.VISIBLE);
            scoreTV.setVisibility(View.VISIBLE);
        }
        scoreTV.setText(getScoreString(Math.round(level)));
        tagGroup.setLevelChanged((int)level);
        if (!isEvaluated() && isActive()) {
            if ((int)level == 5) {
                ratingview.setAllItemBg(R.drawable.selector_evaluate_ratingbar_full);
            } else {
                ratingview.setAllItemBg(R.drawable.selector_evaluate_ratingbar);
            }
        }
    }

    @Event({R.id.evaluate_submit_tv})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.evaluate_submit_tv:
                mDialogUtil.showLoadingDialog();
                RequestEvaluateNew.RequestParams params = new RequestEvaluateNew.RequestParams();
                params.fromUname = UserEntity.getUser().getNickname(getActivity());
                params.guideId = orderBean.orderGuideInfo.guideID;
                params.guideName = orderBean.orderGuideInfo.guideName;
                params.orderNo = orderBean.orderNo;
                params.orderType = orderBean.orderType;
                params.totalScore = Math.round(ratingview.getLevel());
                params.labels = tagGroup.getRequestTagIds();
                params.content = TextUtils.isEmpty(commentET.getText()) ? "" : commentET.getText().toString();
                RequestEvaluateNew request = new RequestEvaluateNew(getActivity(), params);
                requestData(request);
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestEvaluateNew) {
            orderBean.userCommentStatus = 1;
            if (orderBean.appraisement == null) {
                orderBean.appraisement = new AppraisementBean();
            }
            orderBean.appraisement.totalScore = Math.round(ratingview.getLevel());
            orderBean.appraisement.content = TextUtils.isEmpty(commentET.getText()) ? "" : commentET.getText().toString();
            initHeader();
            CommonUtils.showToast(R.string.evaluate_succeed);
            isSubmitEvaluated = true;
            EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
        } else if (_request instanceof RequestEvaluateTag) {
            RequestEvaluateTag request = (RequestEvaluateTag) _request;
            EvaluateTagBean tagBean = request.getData();
            tagGroup.initTagView(tagBean);
            ratingview.setOnLevelChangedListener(this);
        }
    }

    private String getScoreString(int totalScore) {
        int resultStrId = 0;
        switch (totalScore) {
            case 1:
                resultStrId = R.string.evaluate_star_very_unsatisfactory;
                break;
            case 2:
                resultStrId = R.string.evaluate_star_unsatisfactory;
                break;
            case 3:
                resultStrId = R.string.evaluate_star_ordinary;
                break;
            case 4:
                resultStrId = R.string.evaluate_star_satisfied;
                break;
            case 5:
                resultStrId = R.string.evaluate_star_very_satisfied;
                break;

        }
        return getContext().getString(resultStrId);
    }
}
