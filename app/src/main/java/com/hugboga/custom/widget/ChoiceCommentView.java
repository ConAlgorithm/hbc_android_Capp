package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
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
    @Bind(R.id.choice_comment_guide_taggroup)
    TagGroup guideTagGroup;

    public ChoiceCommentView(Context context) {
        this(context, null);
    }

    public ChoiceCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_choice_comment, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
//        Tools.showImage(avatarIV, data.avatar, R.mipmap.icon_avatar_user);//用户头像
//        userNameTV.setText();//用户名称
//        ratingView.setRating();//评价星级
//        dateTV.setText();//时间及天数 2017年6月22日 7日包车游
//        locationTV.setText();//服务城市
//
//        //描述信息
//        if (TextUtils.isEmpty(XX)) {
//            descriptionTV.setVisibility(View.GONE);
//        } else {
//            descriptionTV.setVisibility(View.VISIBLE);
//            descriptionTV.setText(XX);
//        }
//
//        Tools.showImage(guideAvatarIV, data.avatar, R.mipmap.icon_avatar_guide);//司导头像
//        guideNameTV.setText("服务司导 " + "xxx");//司导名称
//        GuideItemUtils.setTag(guideTagGroup, data.skillLabelNames);//司导标签
    }

    @OnClick(R.id.choice_comment_guide_layout)
    public void intentGuideDetail() {
    }

    public void addCommentImageView(LinearLayout picLayout, List<String> carPictures) {
//        picLayout.removeAllViews();
//        if (carPictures == null || carPictures.size() <= 0) {
//            return;
//        }
//        int size = carPictures.size();
//        for (int i = 0; i < size; i++) {
//            ImageView imageView = getCarImageView(carPictures.get(i), i);
//            picLayout.addView(imageView);
//        }
    }

    public ImageView getCommentImageView(String url, int position) {
        ImageView imageView = new ImageView(getContext());
        Tools.showImage(imageView, url, R.mipmap.order_car_dafault);
        int itemWidth = (UIUtils.getScreenWidth() - UIUtils.dip2px(80)) / 2;
        int itemHight = (int) ((2 / 3.0) * itemWidth);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemHight);
        params.rightMargin = UIUtils.dip2px(10);
        imageView.setLayoutParams(params);
        imageView.setId(position);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = v.getId();
//                if (selectedCarBean == null || selectedCarBean.carPicturesL == null || position >= selectedCarBean.carPicturesL.size()) {
//                    return;
//                }
//                CommonUtils.showLargerImages(getContext(), selectedCarBean.carPicturesL, position);
            }
        });
        return imageView;
    }
}
