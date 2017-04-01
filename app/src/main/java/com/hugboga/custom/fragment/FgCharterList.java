package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityRouteAdapter;

import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/27.
 */
@ContentView(R.layout.fg_charter_list)
public class FgCharterList extends BaseFragment {

    @Bind(R.id.fg_charter_list_recycler_view)
    RecyclerView recyclerView;

    private CityRouteAdapter adapter;
    private OnInitializedListener onInitializedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView() {
        adapter = new CityRouteAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        if (onInitializedListener != null) {
            onInitializedListener.onInitialized();
        }
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateSubtitleModel();
        adapter.updatePickupModelVisibility();
        adapter.updateNoCharterModelVisibility();
    }

    public CityRouteAdapter getCityRouteAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public interface OnInitializedListener {
        public void onInitialized();
    }

    public void setOnInitializedListener(OnInitializedListener onInitializedListener) {
        this.onInitializedListener = onInitializedListener;
    }
}
