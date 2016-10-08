package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.EvaluateListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.EvaluateItemData;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

/**
 * Created by qingcha on 16/6/18.
 */
public class EvaluateListItemView extends LinearLayout{

    private PolygonImageView avatarIV;
    private TextView nameTV, stateTV, timeTV, contentTV;
    private SimpleRatingBar ratingview;
    private View bottomLineView;
    private TextView moreComments;

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
        ratingview = (SimpleRatingBar) findViewById(R.id.evaluate_list_item_ratingview);
        contentTV = (TextView) findViewById(R.id.evaluate_list_item_content_tv);
        bottomLineView = findViewById(R.id.evaluate_list_item_line_bottom);
    }

    /**
     * 司导详情
     * */
    public void setGuideDetailData(final GuidesDetailData _data) {
        if (_data.commentNum > 0 && _data.comments != null && _data.comments.size() > 0) {
            setVisibility(View.VISIBLE);
            bottomLineView.setVisibility(View.GONE);
            contentTV.setMaxLines(5);
            contentTV.setEllipsize(TextUtils.TruncateAt.END);
            setData(_data.comments.get(0));
            if (moreComments == null) {
                moreComments = new TextView(getContext());
                moreComments.setTextColor(0xFF3A372E);
                moreComments.setTextSize(14);
                moreComments.setGravity(Gravity.CENTER);
                moreComments.setPadding(UIUtils.dip2px(40), 0, UIUtils.dip2px(40), 0);
                moreComments.setBackgroundResource(R.drawable.selector_evaluate_more_comments);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, UIUtils.dip2px(36));
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.topMargin = UIUtils.dip2px(5);
                addView(moreComments, params);
                moreComments.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), EvaluateListActivity.class);
                        intent.putExtra(Constants.PARAMS_ID, _data.guideId);
                        intent.putExtra(Constants.PARAMS_DATA, "" + _data.commentNum);
                        v.getContext().startActivity(intent);
                    }
                });
            }
            moreComments.setText(getContext().getString(R.string.guide_detail_evaluate_all, "" + _data.commentNum));
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
        ratingview.setRating(data.getTotalScore());
        contentTV.setText(data.getContent());
    }
}
