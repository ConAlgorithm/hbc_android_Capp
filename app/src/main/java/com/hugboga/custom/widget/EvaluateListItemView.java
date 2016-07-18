package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.EvaluateItemData;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.fragment.FgEvaluateList;
import com.hugboga.custom.fragment.FgGuideDetail;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/18.
 */
public class EvaluateListItemView extends LinearLayout{

    private PolygonImageView avatarIV;
    private TextView nameTV, stateTV, timeTV, contentTV;
    private RatingView ratingview;
    private EvaluateTagGroup tagGroup;
    private View bottomLineView, topLineView;
    private TextView moreComments;

    private FgGuideDetail guideDetailFragment;

    public EvaluateListItemView(Context context) {
        this(context, null);
    }

    public EvaluateListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        inflate(context, R.layout.evaluate_list_item, this);
        avatarIV = (PolygonImageView) findViewById(R.id.evaluate_list_item_avatar_iv);
        nameTV = (TextView) findViewById(R.id.evaluate_list_item_name_tv);
        stateTV = (TextView) findViewById(R.id.evaluate_list_item_state_tv);
        timeTV = (TextView) findViewById(R.id.evaluate_list_item_time_tv);
        ratingview = (RatingView) findViewById(R.id.evaluate_list_item_ratingview);
        tagGroup = (EvaluateTagGroup) findViewById(R.id.evaluate_list_item_taggroup);
        contentTV = (TextView) findViewById(R.id.evaluate_list_item_content_tv);
        bottomLineView = findViewById(R.id.evaluate_list_item_line_bottom);
        topLineView = findViewById(R.id.evaluate_list_item_line_top);
    }

    /**
     * 司导详情
     * */
    public void setGuideDetailData(FgGuideDetail fragment, final GuidesDetailData _data) {
        if (_data.getCommentNum() > 0 && _data.getComments() != null && _data.getComments().size() > 0) {
            this.guideDetailFragment = fragment;
            setVisibility(View.VISIBLE);
            topLineView.setVisibility(View.VISIBLE);
            bottomLineView.setVisibility(View.GONE);
            contentTV.setMaxLines(5);
            contentTV.setEllipsize(TextUtils.TruncateAt.END);
            setData(_data.getComments().get(0));
            if (moreComments == null) {
                moreComments = new TextView(getContext());
                moreComments.setTextColor(0xFF3A372E);
                moreComments.setTextSize(14);
                moreComments.setGravity(Gravity.CENTER);
                moreComments.setPadding(UIUtils.dip2px(50), 0, UIUtils.dip2px(50), 0);
                moreComments.setBackgroundResource(R.drawable.shape_evaluate_more_comments);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, UIUtils.dip2px(36));
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.topMargin = UIUtils.dip2px(5);
                addView(moreComments, params);
                moreComments.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (guideDetailFragment != null) {
                            guideDetailFragment.startFragment(FgEvaluateList.newInstance(_data.getGuideId(), ""+_data.getCommentNum()));
                        }
                    }
                });
            }
            moreComments.setText(getContext().getString(R.string.guide_detail_evaluate_all, _data.getCommentNum()));
        } else {
            setVisibility(View.GONE);
        }
    }

    /**
     * 评价列表
     * */
    public void setData(EvaluateItemData data) {
        if (data == null) {
            return;
        }
        if (TextUtils.isEmpty(data.getAvatar())) {
            avatarIV.setImageResource(R.mipmap.collection_icon_pic);
        } else {
            Tools.showImage(getContext(), avatarIV, data.getAvatar());
        }
        nameTV.setText(data.getName());
        stateTV.setText(data.getOrderTypeStr());
        timeTV.setText(data.getCreateTimeYMD());
        ratingview.setLevel(data.getTotalScore());
        contentTV.setText(data.getContent());

        if (data.getLabelNamesArr() != null && data.getLabelNamesArr().size() > 0) {
            tagGroup.setVisibility(View.VISIBLE);
            final int labelsSize = data.getLabelNamesArr().size();
            ArrayList<View> viewList = new ArrayList<View>(labelsSize);
            for (int i = 0; i < labelsSize; i++) {
                String label = data.getLabelNamesArr().get(i);
                if (TextUtils.isEmpty(label)) {
                    continue;
                }
                if (i < tagGroup.getChildCount()) {
                    TextView tagTV = (TextView)tagGroup.getChildAt(i);
                    tagTV.setVisibility(View.VISIBLE);
                    tagTV.setText(label);
                } else {
                    viewList.add(getNewTagView(label));
                }
            }
            for (int j = labelsSize; j < tagGroup.getChildCount(); j++) {
                tagGroup.getChildAt(j).setVisibility(View.GONE);
            }
            tagGroup.setTags(viewList, tagGroup.getChildCount() <= 0);
        } else {
            tagGroup.setVisibility(View.GONE);
        }
    }

    public TextView getNewTagView(String label) {
        TextView tagTV = new TextView(getContext());
        tagTV.setPadding(UIUtils.dip2px(24), UIUtils.dip2px(5), UIUtils.dip2px(24), UIUtils.dip2px(6));
        tagTV.setTextSize(14);
        tagTV.setBackgroundResource(R.drawable.shape_evaluate_tag);
        tagTV.setTextColor(0xFF3A3A32);
        tagTV.setSelected(true);
        tagTV.setEnabled(false);
        tagTV.setText(label);
        return tagTV;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

}
