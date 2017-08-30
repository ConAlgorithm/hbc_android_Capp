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
import com.hugboga.custom.data.bean.CollectLineBean;
import com.hugboga.custom.data.bean.SearchLineBean;
import com.hugboga.custom.utils.SearchUtils;
import com.hugboga.custom.utils.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/28.
 */

public class CollectLinelistItem extends LinearLayout implements HbcViewBehavior{
    @Bind(R.id.title_line_search)
    TextView title;
    @Bind(R.id.search_line_location)
    TextView location;
    @Bind(R.id.pic_line)
    ImageView picLine;
    @Bind(R.id.price_per)
    TextView pricePer;
    @Bind(R.id.offline_view)
    View offline_view;
    @Bind(R.id.offline_icon)
    TextView offline_icon;
    CollectLineBean.CollectLineItemBean collectLineItemBean;

    public CollectLinelistItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.line_search_item, this);
        ButterKnife.bind(view);
    }


    public CollectLinelistItem(Context context) {
        this(context, null);
    }

    @Override
    public void update(Object _data) {

        if(_data!= null && _data instanceof CollectLineBean.CollectLineItemBean){
            collectLineItemBean = (CollectLineBean.CollectLineItemBean) _data;
        }
        final String allLocation = collectLineItemBean.depCityName + "-" + collectLineItemBean.depPlaceName;
        title.setText(collectLineItemBean.name);
        location.setText(allLocation);
        pricePer.setText("¥" + collectLineItemBean.prePrice + "起/人");
        Tools.showImage(picLine,collectLineItemBean.pics,R.mipmap.line_search_default);
        if(collectLineItemBean.publishStatus == -1){
            offline_icon.setVisibility(VISIBLE);
            offline_view.setVisibility(VISIBLE);
        }
        if (collectLineItemBean.publishStatus == 1) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SkuDetailActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, collectLineItemBean.goodsDetailUrl);
                    intent.putExtra(Constants.PARAMS_ID, collectLineItemBean.no);
                    //intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    view.getContext().startActivity(intent);
                }
            });
        }

    }

}
