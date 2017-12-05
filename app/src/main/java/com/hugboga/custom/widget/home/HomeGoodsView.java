package com.hugboga.custom.widget.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.adapter.HomeGoodsAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.models.home.HomeGoodsModel;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.SpaceItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/11/24.
 */

public class HomeGoodsView<T> extends LinearLayout {

    @BindView(R.id.home_album_title_tv)
    TextView titleTV;
    @BindView(R.id.home_album_title_arrow_iv)
    ImageView titleArrowIV;
    @BindView(R.id.home_album_recyclerview)
    RecyclerView recyclerView;

    private int displayImgWidth, displayImgHeight;
    private HomeGoodsAdapter adapter;
    private int type;

    public HomeGoodsView(Context context) {
        this(context, null);
    }

    public HomeGoodsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_home_album, this);
        ButterKnife.bind(this);

        setPadding(0, 0, 0, UIUtils.dip2px(44));
        displayImgWidth = (int) ((376 / 750.0f) * UIUtils.getScreenWidth() - UIUtils.dip2px(9) * 2);
        displayImgHeight = displayImgWidth;
        int viewHeight = displayImgHeight + UIUtils.dip2px(9 + 35 + 20 + 32 + 15);
        recyclerView.getLayoutParams().height = viewHeight;

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

    public void setDate(List<T> _itemList, int _type) {
        int titleId = _type == HomeGoodsModel.TYPE_TRANSFER ? R.string.home_goodes_title_transfer : R.string.home_goodes_title_chartered;
        titleTV.setText(getContext().getResources().getString(titleId));
        if (adapter == null) {
            adapter = new HomeGoodsAdapter(getContext(), _itemList, _type, displayImgWidth, displayImgHeight);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setData(_itemList);
        }
    }


    @OnClick({R.id.home_album_title_tv, R.id.home_album_title_arrow_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_album_title_tv:
            case R.id.home_album_title_arrow_iv:
                Class<?> cls;
                if (type == HomeGoodsModel.TYPE_TRANSFER) {//接送机
                    cls = CharterFirstStepActivity.class;
                } else {//包车
                    cls = CharterFirstStepActivity.class;
                }
                Intent intent = new Intent(getContext(), cls);
                intent.putExtra(Constants.PARAMS_SOURCE, "首页");
                getContext().startActivity(intent);
                break;
        }
    }
}