package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeAlbumRelItemVo;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.Tools;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/4.
 */

public class AlbumItemView extends LinearLayout implements HbcViewBehavior {
    @Bind(R.id.album_img_item)
    ImageView albumImgItem;
    @Bind(R.id.album_purchse_item)
    TextView albumPurchseItem;
    @Bind(R.id.album_title_item)
    TextView albumTitleItem;
    public AlbumItemView(Context context) {
        this(context,null);
    }

    public AlbumItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.home_album_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object data) {
        final HomeAlbumRelItemVo homeAlbumRelGoodsVo = (HomeAlbumRelItemVo) data;
        if(homeAlbumRelGoodsVo != null){
            Tools.showImageForHomePage(albumImgItem,homeAlbumRelGoodsVo.goodsPic,R.mipmap.hotalbumitem);
            albumImgItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), SkuDetailActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, homeAlbumRelGoodsVo.goodsDetailUrl);
                    intent.putExtra(Constants.PARAMS_ID, homeAlbumRelGoodsVo.goodsNo);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    getContext().startActivity(intent);
                    SensorsUtils.onAppClick(getEventSource(),"热门专辑","首页-热门专辑");
                }
            });
            setPrice(homeAlbumRelGoodsVo);
            albumTitleItem.setText(homeAlbumRelGoodsVo.goodsName);
        }

    }

    public void setImageBound(int displayImgWidth, int displayImgHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(displayImgWidth, displayImgHeight);
        albumImgItem.setLayoutParams(params);
    }
    private String getEventSource(){
        return "首页-专辑推荐线路";
    }
    private void setPrice(HomeAlbumRelItemVo homeAlbumRelGoodsVo){
        if(homeAlbumRelGoodsVo!= null){
            String perPrice = "";
            try {
                DecimalFormat df = new DecimalFormat("#,###");
                perPrice = df.format(homeAlbumRelGoodsVo.perPrice);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }

            String finalPerPrice = "¥" + perPrice + getContext().getResources().getString(R.string.home_album_purchse);
            SpannableString spannableString = new SpannableString(finalPerPrice);
            spannableString.setSpan(new AbsoluteSizeSpan(12, true), finalPerPrice.length()-3, finalPerPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            albumPurchseItem.setText(spannableString);
        }
    }
}
