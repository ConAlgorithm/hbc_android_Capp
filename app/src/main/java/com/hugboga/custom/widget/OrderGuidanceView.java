package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.QueryCityGuideBean;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.Tools;

import net.grobas.view.PolygonImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/12/7.
 */

public class OrderGuidanceView extends LinearLayout {


    @BindView(R.id.order_guidance_title_tv)
    TextView titleTV;
    @BindView(R.id.order_guidance_subtitle_tv)
    TextView subtitleTV;
    @BindView(R.id.order_guidance_avatar_layout)
    LinearLayout avatarLayout;

    List<PolygonImageView> avatarViewList;

    public OrderGuidanceView(Context context) {
        this(context, null);
    }

    public OrderGuidanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_order_guidance, this);
        ButterKnife.bind(this);
        avatarViewList = new ArrayList<>();
        avatarViewList.add((PolygonImageView) findViewById(R.id.order_guidance_avatar_iv1));
        avatarViewList.add((PolygonImageView) findViewById(R.id.order_guidance_avatar_iv2));
        avatarViewList.add((PolygonImageView) findViewById(R.id.order_guidance_avatar_iv3));
        avatarViewList.add((PolygonImageView) findViewById(R.id.order_guidance_avatar_iv4));
        avatarViewList.add((PolygonImageView) findViewById(R.id.order_guidance_avatar_iv5));
        subtitleTV.setVisibility(View.GONE);
        avatarLayout.setVisibility(View.INVISIBLE);
    }

    public void setData(String cityId, String cityName) {
        if (getVisibility() == View.GONE) {
            return;
        }
        titleTV.setText(getContext().getResources().getString(R.string.guidance_view_title, cityName));
        subtitleTV.setVisibility(View.GONE);
        avatarLayout.setVisibility(View.INVISIBLE);
        RequestQueryCityGuide requestTravelPurposeForm = new RequestQueryCityGuide(getContext(), cityId);
        HttpRequestUtils.request(getContext(), requestTravelPurposeForm, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest _request) {
                ApiReportHelper.getInstance().addReport(_request);
                QueryCityGuideBean queryCityGuideBean = ((RequestQueryCityGuide) _request).getData();
                subtitleTV.setVisibility(View.VISIBLE);
                avatarLayout.setVisibility(View.VISIBLE);
                subtitleTV.setText(getContext().getResources().getString(R.string.guidance_view_subtitle, "" + queryCityGuideBean.guideAmount));

                if (queryCityGuideBean.guideAvatar != null) {
                    List<String> guideAvatars = queryCityGuideBean.guideAvatar;
                    int avatarViewSize = avatarViewList.size();
                    int avatarSize = guideAvatars.size();
                    for (int i = avatarViewSize - 1; i >= 0; i--) {
                        PolygonImageView avatarView = avatarViewList.get(i);
                        if (i < avatarSize) {
                            avatarView.setVisibility(View.VISIBLE);
                            Tools.showImage(avatarView, guideAvatars.get(i), R.mipmap.icon_avatar_guide);
                        } else {
                            avatarView.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                subtitleTV.setVisibility(View.GONE);
                avatarLayout.setVisibility(View.INVISIBLE);
            }
        }, false);
    }
}
