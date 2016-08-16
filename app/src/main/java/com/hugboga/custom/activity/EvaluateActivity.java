package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AppraisementBean;
import com.hugboga.custom.data.bean.EvaluateData;
import com.hugboga.custom.data.bean.EvaluateTagBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestEvaluateNew;
import com.hugboga.custom.data.request.RequestEvaluateTag;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.EvaluateShareView;
import com.hugboga.custom.widget.EvaluateTagGroup;
import com.hugboga.custom.widget.RatingView;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/4.
 */
public class EvaluateActivity extends BaseActivity implements RatingView.OnLevelChangedListener {

    @Bind(R.id.evaluate_scrollview)
    ScrollView scrollview;
    @Bind(R.id.evaluate_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.evaluate_name_tv)
    TextView nameTV;
    @Bind(R.id.evaluate_score_ratingview)
    RatingView scoreRatingview;
    @Bind(R.id.evaluate_describe_tv)
    TextView describeTV;
    @Bind(R.id.evaluate_plate_number_tv)
    TextView plateNumberTV;
    @Bind(R.id.evaluate_active_tv)
    TextView activeTV;
    @Bind(R.id.evaluate_score_tv)
    TextView scoreTV;
    @Bind(R.id.evaluate_ratingView)
    RatingView ratingview;
    @Bind(R.id.evaluate_taggroup)
    EvaluateTagGroup tagGroup;
    @Bind(R.id.evaluate_comment_et)
    EditText commentET;
    @Bind(R.id.evaluate_comment_icon_iv)
    ImageView commentIconIV;
    @Bind(R.id.evaluate_submit_tv)
    TextView submitTV;
    @Bind(R.id.evaluate_share_view)
    EvaluateShareView shareView;

    private OrderBean orderBean;
    private DialogUtil mDialogUtil;
    private boolean isFirstIn = true;
    private boolean isSubmitEvaluated = false;//是否提交过评价，提交了通知详情页更新。

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            orderBean = (OrderBean) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                orderBean = (OrderBean) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        mDialogUtil = DialogUtil.getInstance(this);

        setContentView(R.layout.fg_evaluate);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (orderBean != null) {
            outState.putSerializable(Constants.PARAMS_DATA, orderBean);
        }
    }

    private void initView() {
        initDefaultTitleBar();
        fgTitle.setText(getString(R.string.evaluate_title));
        if (orderBean == null) {
            return;
        }
        final OrderGuideInfo guideInfo = orderBean.orderGuideInfo;
        if (guideInfo == null) {
            return;
        }
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubmitEvaluated) {
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_EVALUATION, orderBean.orderNo));
                }
                finish();
            }
        });
        if (TextUtils.isEmpty(guideInfo.guideAvatar)) {
            avatarIV.setImageResource(R.mipmap.journey_head_portrait);
        } else {
            Tools.showImage(avatarIV, guideInfo.guideAvatar);
        }
        nameTV.setText(guideInfo.guideName);
        scoreRatingview.setLevel((float)guideInfo.guideStarLevel);
        describeTV.setText(guideInfo.guideCar);
        plateNumberTV.setText(guideInfo.carNumber);

        if (isEvaluated()) {//已评价
            ratingview.setLevel(orderBean.appraisement.totalScore);
            if (TextUtils.isEmpty(orderBean.appraisement.content)) {
                commentIconIV.setVisibility(View.GONE);
                commentET.setVisibility(View.GONE);
            } else {
                commentIconIV.setVisibility(View.VISIBLE);
                commentET.setPadding(0, 0, 0, 0);
                commentET.setVisibility(View.VISIBLE);
                commentET.setText(orderBean.appraisement.content);
            }
            commentET.setEnabled(false);
            commentET.setBackgroundColor(0x00000000);
            submitTV.setVisibility(View.GONE);
            scoreTV.setVisibility(View.VISIBLE);
            if (orderBean.appraisement.totalScore >= 5) {
                scoreTV.setText(getString(R.string.evaluate_evaluated_satisfied));
            } else {
                scoreTV.setText(getString(R.string.evaluate_evaluated_ordinary));
            }
            ratingview.setOnLevelChangedListener(null);
            ratingview.setTouchable(false);
            if (orderBean.appraisement.guideLabels == null) {
                tagGroup.setVisibility(View.GONE);
            } else {
                tagGroup.setTagEnabled(false);
                tagGroup.setEvaluatedData(orderBean.appraisement.guideLabels);
            }
            shareView.update(orderBean);
            shareView.setVisibility(View.VISIBLE);
        } else {
            commentET.setEnabled(true);
            commentET.setBackgroundResource(R.drawable.border_evaluate_comment);
            requestData(new RequestEvaluateTag(this, orderBean.orderType));
            commentIconIV.setVisibility(View.GONE);
            shareView.setVisibility(View.GONE);
        }

        if (isActive()) {//活动
            String activeText = null;
            if (isEvaluated() && orderBean.appraisement.totalScore >= 5) {
                activeText = getString(R.string.evaluate_active_evaluated_get, orderBean.priceCommentReward);
            } else if (isEvaluated()) {
                activeText = getString(R.string.evaluate_active_evaluated);
            } else {//未评价
                activeText = getString(R.string.evaluate_active, orderBean.priceCommentReward);
            }
            activeTV.setText(activeText);
            activeTV.setVisibility(View.VISIBLE);
        } else {
            activeTV.setVisibility(View.GONE);
        }
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.requestFocus();
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

    @OnClick({R.id.evaluate_submit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.evaluate_submit_tv:
                mDialogUtil.showLoadingDialog();
                RequestEvaluateNew.RequestParams params = new RequestEvaluateNew.RequestParams();
                params.fromUname = UserEntity.getUser().getNickname(this);
                params.guideId = orderBean.orderGuideInfo.guideID;
                params.guideName = orderBean.orderGuideInfo.guideName;
                params.orderNo = orderBean.orderNo;
                params.orderType = orderBean.orderType;
                params.totalScore = Math.round(ratingview.getLevel());
                params.labels = tagGroup.getRequestTagIds();
                params.content = TextUtils.isEmpty(commentET.getText()) ? "" : commentET.getText().toString();
                RequestEvaluateNew request = new RequestEvaluateNew(this, params);
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
            initView();
            CommonUtils.showToast(R.string.evaluate_succeed);
            isSubmitEvaluated = true;
            EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));

            RequestEvaluateNew request = (RequestEvaluateNew) _request;
            EvaluateData evaluateData = request.getData();
            if (orderBean.appraisement.totalScore > 3 && evaluateData != null && orderBean.orderGuideInfo != null) {
                if (!orderBean.orderGuideInfo.isCollected()) {
                    requestData(new RequestCollectGuidesId(this, orderBean.orderGuideInfo.guideID));
                }
                ShareGuidesActivity.Params params = new ShareGuidesActivity.Params();
                params.evaluateData = evaluateData;
                params.orderNo = orderBean.orderNo;
                params.guideAvatar = orderBean.orderGuideInfo.guideAvatar;
                Intent intent = new Intent(EvaluateActivity.this, ShareGuidesActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                EvaluateActivity.this.startActivity(intent);
                finish();
            }
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
        return getString(resultStrId);
    }
}
