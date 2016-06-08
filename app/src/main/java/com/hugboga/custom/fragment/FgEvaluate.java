package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.EvaluateTagBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestEvaluateNew;
import com.hugboga.custom.data.request.RequestEvaluateTag;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.RatingView;
import com.hugboga.custom.widget.TagGroup;

import net.grobas.view.PolygonImageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 16/5/29.
 */
@ContentView(R.layout.fg_evaluate)
public class FgEvaluate extends BaseFragment implements TagGroup.OnTagItemClickListener, RatingView.OnLevelChangedListener {

    //RequestEvaluateTag
    //RequestEvaluateNew
    //appraisement AppraisementBean

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
    TagGroup tagGroup;
    @ViewInject(R.id.evaluate_comment_et)
    EditText commentET;
    @ViewInject(R.id.evaluate_submit_tv)
    TextView submitTV;

    private final static int DEFAULT_TAG_COUNTS = 6;

    private OrderBean orderBean;
    private DialogUtil mDialogUtil;

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
        if (TextUtils.isEmpty(guideInfo.guideAvatar)) {
            avatarIV.setImageResource(R.mipmap.collection_icon_pic);
        } else {
            Tools.showImage(getContext(), avatarIV, guideInfo.guideAvatar);
        }
        nameTV.setText(guideInfo.guideName);
        scoreRatingview.setLevel((float)guideInfo.guideStarLevel);
        describeTV.setText(guideInfo.guideCarType);//TODO 折行
        plateNumberTV.setText(guideInfo.CarNumber);

        boolean isEvaluated = orderBean.userCommentStatus == 1 && orderBean.appraisement != null;//是否评价过
        if (isEvaluated) {//已评价
            ratingview.setLevel(orderBean.appraisement.totalScore);
            commentET.setText(orderBean.appraisement.content);
            commentET.setEnabled(false);
            commentET.setBackgroundColor(0x00000000);
            submitTV.setVisibility(View.GONE);
            if (orderBean.appraisement.totalScore >= 5) {
                scoreTV.setText(getContext().getString(R.string.evaluate_evaluated_satisfied));
            } else {
                scoreTV.setText(getContext().getString(R.string.evaluate_evaluated_ordinary));
            }
//                    addTag(listData, AddTagState.MORE_BTN);
            ratingview.setOnLevelChangedListener(null);
        } else {
            commentET.setEnabled(true);
            commentET.setBackgroundResource(R.drawable.border_evaluate_comment);
            submitTV.setVisibility(View.VISIBLE);
            requestData(new RequestEvaluateTag(getContext(), orderBean.orderType));
        }

        if (orderBean.priceCommentReward != 0 ) {//活动
            String activeText = null;
            if (isEvaluated && orderBean.appraisement.totalScore >= 5) {
                activeText = getContext().getString(R.string.evaluate_active_evaluated_get, orderBean.priceCommentReward);
            } else if (isEvaluated) {
                activeText = getContext().getString(R.string.evaluate_active_evaluated);
            } else {//未评价
                activeText = getContext().getString(R.string.evaluate_active, orderBean.priceCommentReward);
            }
            activeTV.setText(activeText);
            activeTV.setVisibility(View.VISIBLE);
        } else {
            activeTV.setVisibility(View.GONE);
        }

        tagGroup.setOnTagItemClickListener(this);
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
        scoreTV.setText(getScoreString(Math.round(level)));

    }

    @Override
    public void onTagClick(View _view, int position) {
        if (getString(R.string.more).equals(_view.getTag())) {
            TextView moreView = (TextView) _view;
            tagGroup.removeView(_view);
//            addTag(listData, AddTagState.SURPLUS);
            tagGroup.addView(moreView);
        } else {
            _view.setSelected(!_view.isSelected());
        }
    }

    /**
     * ADDALL 添加全部
     * SURPLUS 添加剩余的tag
     * MOREBTN 添加更多btn
     * */
    public enum AddTagState {
        ADDALL, SURPLUS, MORE_BTN
    }

    private void addTag(ArrayList<String> _listData, AddTagState state) {
        if (_listData == null) {
            return;
        }

        final int size = _listData.size();
        List<String> listData = _listData;

        if (state == AddTagState.SURPLUS && DEFAULT_TAG_COUNTS < size) {
            listData = _listData.subList(DEFAULT_TAG_COUNTS, size);
            if (listData == null) {
                return;
            }
        }

        ArrayList<View> viewList = new ArrayList<View>(size);
        for (int i = 0; i < size; i++) {
            TextView tagTV = new TextView(getActivity());
            tagTV.setPadding(UIUtils.dip2px(24), UIUtils.dip2px(5), UIUtils.dip2px(24), UIUtils.dip2px(6));
            if (state == AddTagState.MORE_BTN && i == DEFAULT_TAG_COUNTS) {
                tagTV.setText(getString(R.string.more));
                tagTV.setBackgroundResource(R.drawable.shape_evaluate_more);
                tagTV.setTag(getString(R.string.more));
                viewList.add(tagTV);
                tagGroup.setTags(viewList);
                return;
            }
            tagTV.setText(_listData.get(i));
            tagTV.setBackgroundResource(R.drawable.shape_evaluate_tag);
            viewList.add(tagTV);
        }
        tagGroup.setTags(viewList);
        return;
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
//                params.labels = ;
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
        } else if (_request instanceof RequestEvaluateTag) {
            RequestEvaluateTag request = (RequestEvaluateTag) _request;
            EvaluateTagBean tagBean = request.getData();
            //        addTag(listData, AddTagState.MORE_BTN);
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
