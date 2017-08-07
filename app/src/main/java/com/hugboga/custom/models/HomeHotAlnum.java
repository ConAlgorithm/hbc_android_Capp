package com.hugboga.custom.models;

import android.app.Activity;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeAlbumInfoVo;
import com.hugboga.custom.widget.HomeAlbumView;

/**
 * Created by zhangqiang on 17/8/2.
 */

public class HomeHotAlnum extends EpoxyModel<HomeAlbumView> {
    Activity activity;
    private HomeAlbumInfoVo homeAlbumInfoVo;
    private int position;
    @Override
    protected int getDefaultLayout() {
        return R.layout.model_album;
    }
    public HomeHotAlnum(Activity activity){
        this.activity = activity;
    }
    @Override
    public void bind(HomeAlbumView view) {
        super.bind(view);
        view.setActivity(activity);
        view.setAlbumList(homeAlbumInfoVo,position);
    }
    public void setAlbumData(HomeAlbumInfoVo homeAlbumInfoVo) {
        this.homeAlbumInfoVo = homeAlbumInfoVo;
    }
    public void setAlbumData(HomeAlbumInfoVo homeAlbumInfoVo, int position) {
        this.homeAlbumInfoVo = homeAlbumInfoVo;
        this.position = position;
    }
    public void update() {
//        if(homeServiceHolder != null){
//            homeServiceHolder
//        }
    }
    private void init(){
//        if(homeServiceHolder != null){
//
//        }
    }
}
