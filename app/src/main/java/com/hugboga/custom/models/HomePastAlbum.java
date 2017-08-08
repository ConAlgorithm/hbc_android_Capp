package com.hugboga.custom.models;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.EvaluateNewActivity;
import com.hugboga.custom.adapter.HomePastAlbumAdapter;
import com.hugboga.custom.data.bean.HomeAlbumInfoVo;
import com.hugboga.custom.widget.SpaceItemDecoration;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomePastAlbum extends EpoxyModelWithHolder {
    HomePastAlbumHolder homePastAlbumHolder;
    HomePastAlbumAdapter homePastAlbumAdapter;
    public int displayImgWidth, displayImgHeight;
    Context context;
    ArrayList<HomeAlbumInfoVo> pastAlbumList;

    public HomePastAlbum(Context context) {
        this.context = context;
    }
    public HomePastAlbum(Context context, ArrayList<HomeAlbumInfoVo> pastAlbumList) {
        this.context = context;
        this.pastAlbumList = pastAlbumList;
    }

    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomePastAlbumHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_past_album;
    }

    class HomePastAlbumHolder extends EpoxyHolder {
        View itemView;
        @Bind(R.id.past_album_recyclerview)
        RecyclerView pastAlbumRecyclerview;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        homePastAlbumHolder = (HomePastAlbum.HomePastAlbumHolder) holder;

        //设置适配器
        if(homePastAlbumAdapter == null){
            //设置布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            homePastAlbumHolder.pastAlbumRecyclerview.setLayoutManager(linearLayoutManager);

            final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_past_album_space);
            displayImgWidth = context.getResources().getDimensionPixelOffset(R.dimen.home_past_album_hor);
            displayImgHeight = context.getResources().getDimensionPixelOffset(R.dimen.home_past_album_ver);
            homePastAlbumHolder.pastAlbumRecyclerview.getLayoutParams().height = displayImgHeight;
            homePastAlbumHolder.pastAlbumRecyclerview.setHorizontalScrollBarEnabled(false);
            SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
            itemDecoration.setItemOffsets(paddingLeft, 0, 0, 0, LinearLayout.HORIZONTAL);
            homePastAlbumHolder.pastAlbumRecyclerview.addItemDecoration(itemDecoration);
            homePastAlbumAdapter = new HomePastAlbumAdapter(context, displayImgWidth, displayImgHeight,pastAlbumList);
            homePastAlbumHolder.pastAlbumRecyclerview.setAdapter(homePastAlbumAdapter);
        }else{
            homePastAlbumAdapter.setData();
        }
    }

}
