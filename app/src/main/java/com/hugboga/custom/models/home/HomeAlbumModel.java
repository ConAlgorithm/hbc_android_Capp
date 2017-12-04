package com.hugboga.custom.models.home;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.widget.home.HomeAlbumView;

/**
 * Created by qingcha on 17/11/23.
 */

public class HomeAlbumModel extends EpoxyModel<HomeAlbumView> {

    public HomeBean.HotAlbumBean hotAlbumBean;

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_home_album;
    }

    public void setDate(HomeBean.HotAlbumBean hotAlbumBean) {
        this.hotAlbumBean = hotAlbumBean;
    }

    @Override
    public void bind(HomeAlbumView view) {
        super.bind(view);
        view.setDate(hotAlbumBean);
    }
}