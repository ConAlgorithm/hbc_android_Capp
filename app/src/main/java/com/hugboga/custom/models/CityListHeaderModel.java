package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.utils.CenterImageSpan;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 17/4/14.
 */
public class CityListHeaderModel extends EpoxyModel<RelativeLayout> {

    private CityListBean.CityContent cityContent;
    private int displayLayoutHeight;

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_city_list_header;
    }

    @Override
    public void bind(RelativeLayout view) {
        super.bind(view);
        ImageView bgIV = (ImageView) view.findViewById(R.id.city_list_header_bg_iv);
        TextView descriptionTV = (TextView) view.findViewById(R.id.city_list_header_description_tv);
        TextView cityTV = (TextView) view.findViewById(R.id.city_list_header_city_tv);
        displayLayoutHeight = (int)((500 / 750.0f) * UIUtils.getScreenWidth());
        view.getLayoutParams().height = displayLayoutHeight;

        Tools.showImage(bgIV, cityContent.cityHeadPicture, R.drawable.city_banner_default);
        if (cityContent.cityName != null) {
            String cityName = cityContent.cityName + " ";
            Drawable drawable = MyApplication.getAppContext().getResources().getDrawable(R.mipmap.city_arrow_down);
            drawable.setBounds(0, 0, UIUtils.dip2px(18), UIUtils.dip2px(10));
            SpannableString spannable = new SpannableString(cityName + "[icon]");
            CenterImageSpan span = new CenterImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            spannable.setSpan(span, cityName.length(), cityName.length() + "[icon]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            cityTV.setText(spannable);
        } else {
            cityTV.setText("");
        }

        if (TextUtils.isEmpty(cityContent.cityDesc)) {
            descriptionTV.setVisibility(View.GONE);
        } else {
            descriptionTV.setVisibility(View.VISIBLE);
            descriptionTV.setText(cityContent.cityDesc);
        }



        cityTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ChooseCityNewActivity.class);
                intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_RECOMMEND);
                intent.putExtra("isHomeIn", false);
                intent.putExtra("source", "城市");
                context.startActivity(intent);
            }
        });
    }

    public void setData(CityListBean.CityContent cityContent) {
        this.cityContent = cityContent;
    }

    public int getDisplayLayoutHeight() {
        return displayLayoutHeight;
    }

}
