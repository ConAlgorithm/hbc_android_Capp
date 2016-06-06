package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.OrderStatus;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by qingcha on 16/5/29.
 */
@ContentView(R.layout.fg_evaluate)
public class FgEvaluate extends BaseFragment implements TagGroup.OnTagItemClickListener {

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

    private final static int DEFAULT_TAG_COUNTS = 6;

    private OrderBean orderBean;
    private DialogUtil mDialogUtil;
    ArrayList<String> listData;

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

        if (TextUtils.isEmpty(guideInfo.guideAvatar)) {
            avatarIV.setImageResource(R.mipmap.collection_icon_pic);
        } else {
            Tools.showImage(getContext(), avatarIV, guideInfo.guideAvatar);
        }
        nameTV.setText(guideInfo.guideName);
        scoreRatingview.setLevel((float)guideInfo.guideStarLevel);
        describeTV.setText(guideInfo.guideCarType);//TODO 折行
        plateNumberTV.setText(guideInfo.CarNumber);

//        if (orderBean.userCommentStatus == 0 && orderBean.appraisement != null) {//已评价
//            scoreRatingview.setLevel(orderBean.appraisement.totalScore);
//            commentET.setText(orderBean.appraisement.content);
//        }

        listData = new ArrayList<String>(7);
        listData.add("车很干净");
        listData.add("漂移 你懂吗？");
        listData.add("司机很帅的啊 哈哈");
        listData.add("这SB不认识路");
        listData.add("车很干净");
        listData.add("好快");
        listData.add("车很干净7");
        listData.add("车很干净8");
        listData.add("好快9");
        listData.add("车很干净0");

        tagGroup.setOnTagItemClickListener(this);
        addTag(listData, AddTagState.MORE_BTN);
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
    public void onTagClick(View view, int position) {
        if (getString(R.string.more).equals(view.getTag())) {
            tagGroup.removeView(view);
            addTag(listData, AddTagState.SURPLUS);
        } else {
            view.setSelected(!view.isSelected());
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

    @OnClick({R.id.evaluate_submit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.evaluate_submit_tv:
                RequestEvaluateNew.RequestParams params = new RequestEvaluateNew.RequestParams();
//                mDialogUtil.showLoadingDialog();
//                RequestEvaluateNew request = new RequestEvaluateNew(getActivity(), pageIndex);
//                requestData(request);
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestEvaluateNew) {
            RequestEvaluateNew request = (RequestEvaluateNew) _request;
        } else if (_request instanceof RequestEvaluateTag) {

        }
    }
}
