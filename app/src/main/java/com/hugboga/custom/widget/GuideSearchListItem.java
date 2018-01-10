package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.QueryCityActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.SearchDestinationGuideLineActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SearchGuideBean;
import com.hugboga.custom.utils.SearchUtils;
import com.hugboga.custom.utils.Tools;

import net.grobas.view.PolygonImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/21.
 */

public class GuideSearchListItem extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.search_guide_name)
    TextView name;
    @BindView(R.id.search_guide_location)
    TextView location;
    @BindView(R.id.label_search_guide)
    TextView label;
    @BindView(R.id.avatar_guide)
    PolygonImageView avatar_guide;
    @BindView(R.id.gender)
    PolygonImageView gender;

    Context context;
    Activity activity;
    String keyword;
    SearchGuideBean.GuideSearchItemBean guideSearchItemBean;
    public GuideSearchListItem(Context context) {
        this(context,null);
        this.context = context;
    }

    public GuideSearchListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.guide_search_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        if(_data!= null && _data instanceof SearchGuideBean.GuideSearchItemBean){
            guideSearchItemBean = (SearchGuideBean.GuideSearchItemBean) _data;
            if(keyword!= null){
                guideSearchItemBean.keyword = keyword;
            }
        }
        final String allLocation = guideSearchItemBean.cityName + "-" + guideSearchItemBean.countryName;
        StringBuilder allLabel = new StringBuilder();
        CharSequence finallabel=null;
        if(guideSearchItemBean.guideLabelList!= null){
            for(int i= 0;i<guideSearchItemBean.guideLabelList.size();i++){
                allLabel.append(guideSearchItemBean.guideLabelList.get(i)).append("·");
            }
            if(allLabel.charAt(allLabel.length()-1) == '·'){
                finallabel = allLabel.subSequence(0,allLabel.length()-1);
            }
        }

        if(guideSearchItemBean.keyword != null && guideSearchItemBean.keyword.toString().length() >0){
            if(guideSearchItemBean.guideLabelList!= null){
                SpannableString tempName = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),guideSearchItemBean.guideName,guideSearchItemBean.keyword);
                SpannableString tempLocation = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),allLocation,guideSearchItemBean.keyword);
                SpannableString tempLabel = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),finallabel.toString(),guideSearchItemBean.keyword);
                name.setText(tempName);
                location.setText(tempLocation);
                label.setText(tempLabel);
            }else{
                SpannableString tempName = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),guideSearchItemBean.guideName,guideSearchItemBean.keyword);
                SpannableString tempLocation = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),allLocation,guideSearchItemBean.keyword);
                name.setText(tempName);
                location.setText(tempLocation);
                label.setText("");
            }


        }else{
            if(guideSearchItemBean.guideLabelList!= null){
                name.setText(guideSearchItemBean.guideName);
                location.setText(allLocation);
                label.setText(allLabel);
            }else{
                name.setText(guideSearchItemBean.guideName);
                location.setText(allLocation);
                label.setText("");
            }
        }
        Tools.showImage(avatar_guide,guideSearchItemBean.avatar,R.mipmap.icon_avatar_guide);
        if(guideSearchItemBean.gender == 1){
           gender.setImageResource(R.mipmap.icon_man);
        }else if(guideSearchItemBean.gender == 2){
            gender.setImageResource(R.mipmap.icon_woman);
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if((activity instanceof SearchDestinationGuideLineActivity)
                        || (activity instanceof QueryCityActivity)){
                    GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
                    params.guideId = guideSearchItemBean.guideId;
                    Intent intent = new Intent(activity, GuideWebDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra("isFromHome",true);
                    activity.startActivity(intent);
                }

            }
        });
    }

    public void setkeyWord(String keyword){
        this.keyword = keyword;
    }
    public void setActivity(Activity activity){
        this.activity = activity;
    }
    public String getEventSource(){
        return "搜索";
    }
}
