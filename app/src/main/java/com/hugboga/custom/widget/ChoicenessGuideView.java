package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.parser.ParserUpLoadFile;
import com.hugboga.custom.data.request.FavoriteGuideSaved;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestUncollectGuidesId;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.GuideItemUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/14.
 */
public class ChoicenessGuideView extends LinearLayout implements HbcViewBehavior,HttpRequestListener{

    @Bind(R.id.choiceness_guide_bg_iv)
    ImageView bgIV;
    @Bind(R.id.choiceness_guide_description_tv)
    TextView descTV;
    @Bind(R.id.choiceness_guide_level_hint_tv)
    TextView levelTV;
    @Bind(R.id.choiceness_guide_level_tv)
    TextView levelTV2;
    @Bind(R.id.choiceness_guide_name_tv)
    TextView nameTV;
    @Bind(R.id.choiceness_guide_taggroup)
    TagGroup tagGroup;
    @Bind(R.id.choiceness_guide_service_type_tv)
    TextView serviceTypeTV;

    @Bind(R.id.choiceness_guide_city_iv)
    ImageView cityIV;
    @Bind(R.id.choiceness_guide_city_tv)
    TextView cityTV;

    @Bind(R.id.save_guild)
    ImageView saveGuild;
    public ChoicenessGuideView(Context context) {
        this(context, null);
    }

    public ChoicenessGuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_choiceness_guide, this);
        ButterKnife.bind(view);

        int padding = getContext().getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);
        setPadding(0, padding, 0, 0);

        int imageWidth = UIUtils.getScreenWidth() - padding * 2;
        int imageHeight = (int)((400/690.0f) * imageWidth);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageWidth, imageHeight);
        bgIV.setLayoutParams(params);
    }

    @Override
    public void update(Object _data) {
        final FilterGuideBean data = (FilterGuideBean) _data;
        Tools.showImage(bgIV, data.guideCover, R.drawable.home_guide_dafault);

        if(!UserEntity.getUser().isLogin(getContext())){
            saveGuild.setSelected(false);
        }else if(data.isCollected == 1){
            saveGuild.setSelected(true);

        }else if(data.isCollected == 0){
            saveGuild.setSelected(false);
        }
        saveGuild.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogin()) {
                    if(saveGuild.isSelected()){
                        data.isCollected = 0;
                        saveGuild.setSelected(false);
                        HttpRequestUtils.request(getContext(),new RequestUncollectGuidesId(getContext(), data.guideId),ChoicenessGuideView.this,false);
                    }else{
                        saveGuild.setSelected(true);
                        data.isCollected= 1;
                        HttpRequestUtils.request(getContext(),new RequestCollectGuidesId(getContext(), data.guideId),ChoicenessGuideView.this,false);
                    }
                }
            }
        });
        if (TextUtils.isEmpty(data.getGuideDesc())) {
            descTV.setVisibility(View.GONE);
        } else {
            descTV.setVisibility(View.VISIBLE);
            descTV.setText(data.getGuideDesc());
        }

        double serviceStar = data.getServiceStar();

        if (serviceStar <= 0) {
            levelTV.setText("暂无星级");
            levelTV.setTextSize(12);
            levelTV.setTextColor(0xFF929292);
        } else {
            String services = serviceStar+"星";
            SpannableString spannableString = new SpannableString(services);
            spannableString.setSpan(new AbsoluteSizeSpan(30), services.length()-1, services.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.guildsaved)), services.length()-1, services.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            levelTV.setTextColor(0xffffc100);
            levelTV.setTextSize(16);
            levelTV.setText(spannableString);
        }

        levelTV2.setText(data.commentNum + "评价");
        nameTV.setText(data.guideName);
        GuideItemUtils.setTag(tagGroup, data.skillLabelNames);

        boolean isShowCity = false;
        if (!TextUtils.isEmpty(data.cityName)) {
            if (getContext() instanceof MainActivity) {
                isShowCity = true;
            } else if (getContext() instanceof CityListActivity && ((CityListActivity)getContext()).isShowCity()) {
                isShowCity = true;
            }
        }

        if (isShowCity) {
            cityIV.setVisibility(View.VISIBLE);
            cityTV.setVisibility(View.VISIBLE);
            cityTV.setText(data.cityName);
        } else {
            cityIV.setVisibility(View.GONE);
            cityTV.setVisibility(View.GONE);
        }

        String serviceType = data.getServiceType();
        if (TextUtils.isEmpty(serviceType)) {
            serviceTypeTV.setVisibility(View.GONE);
        } else {
            serviceTypeTV.setVisibility(View.VISIBLE);
            serviceTypeTV.setText(serviceType);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
                params.guideId = data.guideId;
                Intent intent = new Intent(getContext(), GuideWebDetailActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                String source = "";
                if (getContext() instanceof MainActivity) {
                    source = "首页";
                } else if (getContext() instanceof CityListActivity) {
                    CityListActivity cityListActivity = (CityListActivity) getContext();
                    CityListActivity.Params paramsData = cityListActivity.paramsData;
                    if (paramsData != null) {
                        switch (paramsData.cityHomeType) {
                            case CITY:
                                source = "城市页";
                                break;
                            case ROUTE:
                                source = "国家页";
                                break;
                            case COUNTRY:
                                source = "线路圈页";
                                break;
                        }
                    }
                } else {
                    source = "精选司导页";
                }
                intent.putExtra(Constants.PARAMS_SOURCE, source);
                getContext().startActivity(intent);
            }
        });
    }

    /**
     * 判断是否登录
     */
    private boolean isLogin() {
        if (UserEntity.getUser().isLogin(getContext())) {
            return true;
        } else {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            //intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
            getContext().startActivity(intent);
            return false;
        }
    }

    public String getEventSource() {
        return "大图司导";
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof RequestCollectGuidesId){
            CommonUtils.showToast("收藏成功");
        }else if(request instanceof RequestUncollectGuidesId){
            CommonUtils.showToast("已取消收藏");
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }
}
