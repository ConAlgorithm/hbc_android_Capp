package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeChoicenessRouteAdapter;
import com.hugboga.custom.data.bean.HomeData;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeChoicenessRouteView extends LinearLayout {

    private RecyclerView recyclerView;
    private HomeChoicenessRouteAdapter adapter;

    public HomeChoicenessRouteView(Context context) {
        this(context, null);
    }

    public HomeChoicenessRouteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        inflate(getContext(), R.layout.view_home_choiceness_route, this);

        recyclerView = (RecyclerView) findViewById(R.id.home_choiceness_route_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHorizontalScrollBarEnabled(false);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        itemDecoration.setItemOffsets(getContext().getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left), 0, 0, 0);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new HomeChoicenessRouteAdapter(context);
        recyclerView.setAdapter(adapter);
    }

    public void setData(FgHome _frgment, ArrayList<HomeData.CityContentItem> _itemList) {
        if (adapter == null || _frgment == null || _itemList == null) {
            return;
        }
        adapter.setData(_frgment, _itemList);
    }
}
