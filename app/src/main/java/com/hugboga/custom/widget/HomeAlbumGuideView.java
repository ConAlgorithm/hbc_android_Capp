package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.adapter.HomeAlbumAdapter;
import com.hugboga.custom.adapter.HomeAlbumGuideAdapter;
import com.hugboga.custom.data.bean.HomeAlbumInfoVo;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/9/15.
 */

public class HomeAlbumGuideView extends LinearLayout{
    Activity activity;
    public int displayImgWidth, displayImgHeight;
    @Bind(R.id.viewtop)
    View viewtop;
    @Bind(R.id.album_recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.title_ablum)
    LinearLayout title;
    @Bind(R.id.album_img_layout)
    FrameLayout albumImgLayout;

    @Bind(R.id.img_album)
    ImageView imgAlum;
    @Bind(R.id.color_on_img)
    ImageView colorOnImg;
    @Bind(R.id.name_album)
    TextView nameAlbum;
    @Bind(R.id.service_num)
    TextView servicesNum;
    HomeAlbumInfoVo homeAlbumInfoVo;
    HomeAlbumGuideAdapter homeAlbumAdapter;
    public HomeAlbumGuideView(Context context) {
        this(context,null);
    }

    public HomeAlbumGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_album_guide, this);
        ButterKnife.bind(view);

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_album_space);
        displayImgWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.home_filter_avr_hor);
        displayImgHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.home_filter_avr_ver);
        int viewHeight = displayImgHeight + UIUtils.dip2px(165);
        recyclerView.getLayoutParams().height = viewHeight;
        imgAlum.getLayoutParams().width = UIUtils.getScreenWidth();
        imgAlum.getLayoutParams().height = (int)(imgAlum.getLayoutParams().width *175.0/375.0);
        colorOnImg.getLayoutParams().width = UIUtils.getScreenWidth();
        colorOnImg.getLayoutParams().height = (int)(colorOnImg.getLayoutParams().width *175.0/375.0);

        recyclerView.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHorizontalScrollBarEnabled(false);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        itemDecoration.setItemOffsets(paddingLeft, 0, 0, 0, LinearLayout.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecoration);

    }

    public void setAlbumList(final HomeAlbumInfoVo homeAlbumInfoVo, int position) {
        this.homeAlbumInfoVo = homeAlbumInfoVo;
        if (homeAlbumAdapter == null) {
            homeAlbumAdapter = new HomeAlbumGuideAdapter(getContext(),  displayImgWidth, displayImgHeight, homeAlbumInfoVo);
            recyclerView.setAdapter(homeAlbumAdapter);
        } else {
            homeAlbumAdapter.setData(homeAlbumInfoVo);
        }
        if(position == 0){
            title.setVisibility(VISIBLE);
            viewtop.setVisibility(VISIBLE);
        }else {
            title.setVisibility(GONE);
            viewtop.setVisibility(GONE);
        }

        if(homeAlbumInfoVo.albumImageUrl == null || homeAlbumInfoVo.albumImageUrl.isEmpty()){
            colorOnImg.setVisibility(GONE);
        }else{
            Tools.showImageForHomePage(imgAlum,homeAlbumInfoVo.albumImageUrl,R.mipmap.hotalbum);
            colorOnImg.setVisibility(VISIBLE);
        }
        nameAlbum.setText(homeAlbumInfoVo.albumName);
        servicesNum.setText(activity.getResources().getString(R.string.home_hot_album_guide_orders,homeAlbumInfoVo.albumOrders+""));
        albumImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,WebInfoActivity.class);
                intent.putExtra("web_url", homeAlbumInfoVo.albumLinkUrl);
                activity.startActivity(intent);
                SensorsUtils.onAppClick(getEventSource(),"热门专辑","首页-热门专辑");
            }
        });

    }
    public void setActivity(Activity activity){
        this.activity = activity;
    }
    private String getEventSource(){
        return "首页";
    }
}