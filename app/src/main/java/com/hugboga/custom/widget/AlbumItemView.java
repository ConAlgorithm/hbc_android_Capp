package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeAlbumRelGoodsVo;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.Tools;

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
        final HomeAlbumRelGoodsVo homeAlbumRelGoodsVo = (HomeAlbumRelGoodsVo) data;
        if(homeAlbumRelGoodsVo != null){
            Tools.showImage(albumImgItem,homeAlbumRelGoodsVo.goodsPic);
            albumImgItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), WebInfoActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, homeAlbumRelGoodsVo.goodsDetailUrl);
                    getContext().startActivity(intent);
                }
            });
            albumPurchseItem.setText("¥" + homeAlbumRelGoodsVo.perPrice +"起/人");
            albumTitleItem.setText(homeAlbumRelGoodsVo.goodsName);
        }

    }

    public void setImageBound(int displayImgWidth, int displayImgHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(displayImgWidth, displayImgHeight);
        albumImgItem.setLayoutParams(params);
    }
}
