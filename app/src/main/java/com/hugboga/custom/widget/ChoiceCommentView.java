package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChoiceCommentsBean;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/8/1.
 */
public class ChoiceCommentView extends RelativeLayout implements HbcViewBehavior{

    @Bind(R.id.choice_comment_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.choice_comment_ratingview)
    SimpleRatingBar ratingView;
    @Bind(R.id.choice_comment_location_tv)
    TextView locationTV;
    @Bind(R.id.choice_comment_user_name_tv)
    TextView userNameTV;
    @Bind(R.id.choice_comment_date_tv)
    TextView dateTV;
    @Bind(R.id.choice_comment_description_tv)
    TextView descriptionTV;
    @Bind(R.id.choice_comment_imgs_layout)
    LinearLayout imgsLayout;
    @Bind(R.id.choice_comment_imgs_scrollview)
    HorizontalScrollView imgsScrollView;
    @Bind(R.id.choice_comment_guide_avatar_iv)
    PolygonImageView guideAvatarIV;
    @Bind(R.id.choice_comment_guide_name_tv)
    TextView guideNameTV;
    @Bind(R.id.choice_comment_guide_label_layout)
    LinearLayout guideLabelLayout;
    @Bind(R.id.choice_comment_guide_label_tv)
    TextView guideLabelTV;

    private int itemWidth = 0;
    private int itemHight = 0;

    private ChoiceCommentsBean.ChoiceCommentsItemBean itemBean;

    public ChoiceCommentView(Context context) {
        this(context, null);
    }

    public ChoiceCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_choice_comment, this);
        ButterKnife.bind(view);

        itemWidth = (int) ((UIUtils.getScreenWidth() - UIUtils.dip2px(103)) / 4.3f);
        itemHight = itemWidth;
        imgsScrollView.getLayoutParams().height = itemWidth;
    }

    @Override
    public void update(Object _data) {
        itemBean = (ChoiceCommentsBean.ChoiceCommentsItemBean) _data;
        Tools.showImage(avatarIV, itemBean.userAvatar, R.mipmap.icon_avatar_user);//用户头像
        userNameTV.setText(itemBean.userName);//用户名称
        ratingView.setRating(itemBean.totalScore);//评价星级
        dateTV.setText(String.format("%1$s %2$s", DateUtils.getDateFromSimpleStr2(itemBean.serviceTime), getOrderStateStr()));//时间及天数 2017年6月22日 x日包车游
        locationTV.setText(itemBean.serviceCityName);//服务城市

        //描述信息
        if (TextUtils.isEmpty(itemBean.comment)) {
            descriptionTV.setVisibility(View.GONE);
        } else {
            descriptionTV.setVisibility(View.VISIBLE);
            descriptionTV.setText(itemBean.comment);
        }

        //评价图片
        addCommentImageView(itemBean.commentPic);

        Tools.showImage(guideAvatarIV, itemBean.guideAvatar, R.mipmap.icon_avatar_guide);//司导头像
        guideNameTV.setText("服务司导 " + itemBean.guideName);//司导名称

        //司导标签
        String labels = getLabels(itemBean.guideLabels);
        if (TextUtils.isEmpty(labels)) {
            guideLabelLayout.setVisibility(View.GONE);
        } else {
            guideLabelLayout.setVisibility(View.VISIBLE);
            guideLabelTV.setText(labels);
        }
    }

    @OnClick(R.id.choice_comment_guide_layout)
    public void intentGuideDetail() {
        GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
        params.guideId = itemBean.guideId;
        Intent intent = new Intent(getContext(), GuideWebDetailActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, "游客说");
        intent.putExtra(Constants.PARAMS_DATA, params);
        getContext().startActivity(intent);
    }

    @OnClick({R.id.choice_comment_location_tv, R.id.choice_comment_location_iv})
    public void intentCityList() {
        int cityId = CommonUtils.getCountInteger("" + itemBean.serviceCityId);
        if (cityId == 0) {
            return;
        }
        CityListActivity.Params params = new CityListActivity.Params();
        params.id = cityId;
        params.titleName = itemBean.serviceCityName;
        params.cityHomeType = CityListActivity.CityHomeType.CITY;
        Intent intent = new Intent(getContext(), CityListActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, "游客说");
        getContext().startActivity(intent);
    }

    private String getOrderStateStr() {
        switch (itemBean.orderType) {
            case 1:
                return "接机服务";
            case 2:
                return "送机服务";
            case 4:
                return "单次接送服务";
            default:
                if (itemBean.totalDays != null) {
                    return String.format("%1$s日包车游", itemBean.totalDays);
                } else {
                    return "";
                }
        }
    }

    public void addCommentImageView(List<String> commentPictures) {
        imgsLayout.removeAllViews();
        if (commentPictures == null || commentPictures.size() <= 0) {
            imgsScrollView.setVisibility(View.GONE);
            return;
        }
        imgsScrollView.setVisibility(View.VISIBLE);
        int size = commentPictures.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = null;
            if (i < imgsLayout.getChildCount()) {
                imageView = (ImageView)imgsLayout.getChildAt(i);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView = getCommentImageView(i);
                imgsLayout.addView(imageView);
            }
            Tools.showImage(imageView, commentPictures.get(i), R.mipmap.order_car_dafault);
        }
        for (int j = size; j < imgsLayout.getChildCount(); j++) {
            imgsLayout.getChildAt(j).setVisibility(View.GONE);
        }
    }

    public ImageView getCommentImageView(int position) {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemHight);
        params.rightMargin = UIUtils.dip2px(10);
        imageView.setLayoutParams(params);
        imageView.setId(position);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = v.getId();
                if (itemBean == null || itemBean.commentPicL == null || position >= itemBean.commentPicL.size()) {
                    return;
                }
                CommonUtils.showLargerImages(getContext(), itemBean.commentPicL, position);
            }
        });
        return imageView;
    }

    public String getLabels(List<String> labelList) {
        if (labelList == null || labelList.size() == 0) {
            return "";
        }
        final int labelsSize = labelList.size();
        String labels = "";
        for (int i = 0; i < labelsSize; i++) {
            String tag = labelList.get(i);
            if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(tag.trim())) {
                continue;
            }
            tag = tag.trim();
            labels += tag;
            if (i + 1 < labelsSize) {
                labels += " · ";
            }
        }
        return labels;
    }
}
