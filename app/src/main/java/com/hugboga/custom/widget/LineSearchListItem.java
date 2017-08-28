package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.SearchDestinationGuideLineActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SearchLineBean;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.SearchUtils;
import com.hugboga.custom.utils.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/21.
 */

public class LineSearchListItem extends LinearLayout implements HbcViewBehavior {
    @Bind(R.id.title_line_search)
    TextView title;
    @Bind(R.id.search_line_location)
    TextView location;
    @Bind(R.id.pic_line)
    ImageView picLine;
    @Bind(R.id.price_per)
    TextView pricePer;
    Context context;
    Activity activity;
    SearchLineBean.GoodsPublishStatusVo goodsPublishStatusVo;
    String keyword;
    public LineSearchListItem(Context context) {
        this(context,null);
        this.context = context;
    }

    public LineSearchListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.line_search_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {

        if(_data!= null && _data instanceof SearchLineBean.GoodsPublishStatusVo){
            goodsPublishStatusVo = (SearchLineBean.GoodsPublishStatusVo) _data;
            if(keyword!= null){
                goodsPublishStatusVo.keyword = keyword;
            }
        }
        final String allLocation = goodsPublishStatusVo.depCityName + "-" + goodsPublishStatusVo.depPlaceName;
        if(goodsPublishStatusVo.keyword != null && goodsPublishStatusVo.keyword.toString().length() >0){
            SpannableString temptitle = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),goodsPublishStatusVo.name,goodsPublishStatusVo.keyword);
            SpannableString templocation = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),allLocation,goodsPublishStatusVo.keyword);

            title.setText(temptitle);
            location.setText(templocation);
        }else{
            title.setText(goodsPublishStatusVo.name);
            location.setText(allLocation);
        }
        pricePer.setText("¥" + goodsPublishStatusVo.prePrice + "起/人");
        Tools.showImage(picLine,goodsPublishStatusVo.pics,R.mipmap.line_search_default);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity instanceof SearchDestinationGuideLineActivity) {
                    Intent intent = new Intent(activity, SkuDetailActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, goodsPublishStatusVo.goodsDetailUrl);
                    intent.putExtra(Constants.PARAMS_ID, goodsPublishStatusVo.no);
                    //intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    activity.startActivity(intent);
                }
            }
        });
    }

    public void setkeyWord(String keyword){
        this.keyword = keyword;
    }
    private void interClick(int position) {
        Intent intent = new Intent(context, SkuDetailActivity.class);
        //intent.putExtra(WebInfoActivity.WEB_URL, homeCityContentVo2.cityGoodsList.get(position).goodsDetailUrl);
        intent.putExtra(Constants.PARAMS_ID, goodsPublishStatusVo.no);
        //intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
        //SensorsUtils.onAppClick(getEventSource(), "推荐线路", "首页-推荐线路");
    }
    public void setActivity(Activity activity){
        this.activity = activity;
    }
}
