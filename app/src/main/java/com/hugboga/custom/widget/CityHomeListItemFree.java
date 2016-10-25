package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.GoodsSec;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CityHomeListItemFree extends RelativeLayout implements HbcViewBehavior {
    private TextView goodsName,guidesAmount,headLable,routeName;
    private ImageView goodsImagefirst,goodsImageSec;
    private LinearLayout linearLayout;

    public CityHomeListItemFree(Context context) {
       this(context,null);
    }

    public CityHomeListItemFree(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context,R.layout.city_home_list_item_super_free,this);
        goodsName=(TextView)findViewById(R.id.city_home2_list_item_goods_name);
        guidesAmount=(TextView)findViewById(R.id.city_home_item_free_guides_count);
        headLable=(TextView)findViewById(R.id.city_home_item_free_head_lable);
        routeName=(TextView)findViewById(R.id.city_home_list_item_route_name_TV);

        goodsImagefirst=(ImageView)findViewById(R.id.city_home_list_item_free_image_first);
        goodsImageSec=(ImageView)findViewById(R.id.city_home_list_item_free_image_sec);

        linearLayout=(LinearLayout)findViewById(R.id.city_home_list_item_free_image_lay);
    }

    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof GoodsSec)) {
            return;
        }
        GoodsSec goodsSec = (GoodsSec)_data;

        if (!TextUtils.isEmpty(goodsSec.goodsPicture)) {
            Tools.showImage(goodsImagefirst,goodsSec.goodsPicture);
            if (goodsSec.goodsPics!= null &&goodsSec.goodsPics.size()>0){
                Tools.showImage(goodsImageSec,goodsSec.goodsPics.get(0));
            }
        }else {
            linearLayout.setBackgroundResource(R.color.default_img_bg);
        }


        goodsName.setText(goodsSec.goodsLable);
        guidesAmount.setText(goodsSec.guideAmount+"位当地中文司导");
        headLable.setText(goodsSec.headLable);
        routeName.setText(goodsSec.goodsName);
    }
}