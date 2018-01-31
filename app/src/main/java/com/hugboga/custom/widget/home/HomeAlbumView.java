package com.hugboga.custom.widget.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.adapter.HomeAlbumItemAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.SpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/11/23.
 */

public class HomeAlbumView extends LinearLayout {

    @BindView(R.id.home_album_title_tv)
    TextView titleTV;
    @BindView(R.id.home_album_recyclerview)
    RecyclerView recyclerView;

    private int displayImgWidth, displayImgHeight;
    private HomeAlbumItemAdapter adapter;
    private HomeBean.HotAlbumBean hotAlbumBean;

    public HomeAlbumView(Context context) {
        this(context, null);
    }

    public HomeAlbumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_home_album, this);
        ButterKnife.bind(this);

        titleTV.setMaxWidth(UIUtils.getScreenWidth() - UIUtils.dip2px(70));

        displayImgWidth = (int) ((560 / 750f) * UIUtils.getScreenWidth());
        displayImgHeight = displayImgWidth;
//        int viewHeight = displayImgHeight + UIUtils.dip2px(70) + UIUtils.dip2px(90);
//        recyclerView.getLayoutParams().height = viewHeight;

        recyclerView.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHorizontalScrollBarEnabled(false);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        final int space = getContext().getResources().getDimensionPixelOffset(R.dimen.home_item_horizontal_space);
        itemDecoration.setItemOffsets(0, 0, space, 0, LinearLayout.HORIZONTAL);
        final int marginLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_margin_left);
        itemDecoration.setFirstItemAdditionalOffsets(marginLeft);
        recyclerView.addItemDecoration(itemDecoration);
    }

    @OnClick(R.id.home_album_title_layout)
    public void intentWebInfoActivity() {
        Intent intent = new Intent(getContext(), WebInfoActivity.class);
        intent.putExtra("web_url", hotAlbumBean.albumLinkUrl);
        intent.putExtra(WebInfoActivity.WEB_SHARE_BTN, true);
        intent.putExtra(WebInfoActivity.IS_SHOW_TITLE_NAME, false);
        intent.putExtra(Constants.PARAMS_SOURCE, "专辑");
        getContext().startActivity(intent);
        SensorsUtils.onAppClick("首页", "查看更多", "专辑");
    }

    public void setDate(HomeBean.HotAlbumBean _hotAlbumBean) {
        if (_hotAlbumBean == null) {
            return;
        }
        hotAlbumBean = _hotAlbumBean;
        if (adapter == null) {
            adapter = new HomeAlbumItemAdapter(getContext(), hotAlbumBean, displayImgWidth, displayImgHeight);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setData(hotAlbumBean.albumRelItemList);
        }
        titleTV.setText(hotAlbumBean.albumName);
    }
}
