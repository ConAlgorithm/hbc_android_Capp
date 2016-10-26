package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.GoodsSec;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by wcj on 2016/10/18.
 */

public class CityHomeListItemWorry extends RelativeLayout implements HbcViewBehavior{

    private TextView headlable,guideAmount,places,goodsPrice,priceHint;
    private ImageView goodImagefirst;

    private int displayLayoutHeight;

    public CityHomeListItemWorry(Context context) {
        this(context,null);
    }

    public CityHomeListItemWorry(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.city_home_list_item_save_worry,this);
        headlable=(TextView)findViewById(R.id.city_home_item_head_lable);
        guideAmount=(TextView)findViewById(R.id.city_home_item_guides_count);
        places=(TextView)findViewById(R.id.city_home_list_item_route_name_TV);
        goodsPrice=(TextView)findViewById(R.id.city_home_list_item_price);
        priceHint=(TextView)findViewById(R.id.city_home_list_item_unit);
        goodImagefirst=(ImageView)findViewById(R.id.city_home_list_item_image_first);


        displayLayoutHeight=(int)((345 / 648.0) * UIUtils.getScreenWidth());
        goodImagefirst.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, displayLayoutHeight));
    }


    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof GoodsSec)) {
            return;
        }
        GoodsSec goodsSec = (GoodsSec)_data;

        if (!TextUtils.isEmpty(goodsSec.goodsPicture)) {
            Tools.showImage(goodImagefirst,goodsSec.goodsPicture);
        }else {
            goodImagefirst.setImageResource(R.color.default_img_bg);
        }
        guideAmount.setText(String.format("%1$s 位当地中文司导", goodsSec.guideAmount));
        goodsPrice.setText(String.valueOf(goodsSec.perPrice));

        String otherStr = "起/人·%1$s日";
        if (goodsSec.hotelStatus == 1) {// 是否含酒店
            otherStr += "·含酒店";
        }
        priceHint.setText(String.format(otherStr, goodsSec.daysCount));
        headlable.setText(goodsSec.headLable);
        if (!TextUtils.isEmpty(goodsSec.goodsName)){
            places.setText(goodsSec.places);
        }
    }
}
