package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomePageAdapter;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/7.
 */
@ContentView(R.layout.fg_homepage)
public class FgHomePage extends BaseFragment {

    @Bind(R.id.home_list_view)
    RecyclerView homeListView;

    HomePageAdapter homePageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        setSensorsDefaultEvent(getEventSource(), SensorsConstant.DISCOVERY);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_DISCOVERY;
    }

    @Override
    protected void initView() {
        homePageAdapter = new HomePageAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        homeListView.setLayoutManager(layoutManager);
        homeListView.setHasFixedSize(true);
        homeListView.setAdapter(homePageAdapter);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return requestData(new RequestHome(getActivity()));
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if(_request instanceof RequestHome){
            HomeBeanV2 homeBeanV2 = ((RequestHome)_request).getData();
            addHeader(homeBeanV2.headAggVo);
            addHotExplorations(homeBeanV2.hotExplorationAggVo.hotExplorations);
        }
    }


    private void addHeader(HomeBeanV2.HomeHeaderInfo homeHeaderInfo){
        homePageAdapter.showHeader(homeHeaderInfo);
    }

    private void addHotExplorations(List<HomeBeanV2.HotExploration> hotExplorations){
        homePageAdapter.addHotExploations(hotExplorations);
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

//    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn1:
//                requestData();
//            case R.id.btn2:
//                requestData();
//            case R.id.btn3:
//                requestData();
//                break;
//        }
//    }

    @Override
    public String getEventSource() {
        return "首页";
    }


//    class ModelH extends EpoxyModel<LinearLayout>{
//
//        @Override
//        protected int getDefaultLayout() {
//            return R.layout.actheader;
//        }
//
//        @Override
//        public void bind(LinearLayout view) {
//            super.bind(view);
//            view.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    adapter.removeModel();
//                    adapter.addModelAs();
//                }
//            });
//
//            view.findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    adapter.removeModel();
//                    adapter.addModelBs();
//                }
//            });
//
//            view.findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    adapter.removeModel();
//                    adapter.addModelCs();
//                }
//            });
//        }
//    }

//    class ModelA extends EpoxyModel<LinearLayout> {
//        private String title;
//        private String content;
//
//        @Override
//        protected int getDefaultLayout() {
//            return R.layout.act1;
//        }
//    }
//
//
//    class ModelB extends EpoxyModel<LinearLayout>{
//        private String title;
//        private String content;
//
//        @Override
//        protected int getDefaultLayout() {
//            return R.layout.act2;
//        }
//    }
//
//    class ModelC extends EpoxyModel<LinearLayout>{
//        private String title;
//        private String content;
//
//        @Override
//        protected int getDefaultLayout() {
//            return R.layout.act3;
//        }
//    }

//    class MyAdapter extends EpoxyAdapter{
//
//        ModelH modelH;
//
//        public MyAdapter(ModelH modelH) {
//            this.modelH = modelH;
//            addModel(modelH);
//        }
//
//
//
//        public void addModelAs(){
//            List<ModelA> modelAs = new ArrayList<>();
//            for (int i=0;i<20;i++){
//                ModelA modelA = new ModelA();
//                modelAs.add(modelA);
//            }
//            addModels(modelAs);
//        };
//
//        public void addModelBs(){
//            List<ModelB> modelAs = new ArrayList<>();
//            for (int i=0;i<20;i++){
//                ModelB modelB = new ModelB();
//                modelAs.add(modelB);
//            }
//            addModels(modelAs);
//        };
//
//        public void addModelCs(){
//            List<ModelC> modelAs = new ArrayList<>();
//            for (int i=0;i<20;i++){
//                ModelC modelC = new ModelC();
//                modelAs.add(modelC);
//            }
//            addModels(modelAs);
//        };
//
//        public void removeModel(){
//            removeAllAfterModel(modelH);
//        }
//
//        @Override
//        protected void showModel(EpoxyModel<?> model) {
//            if(model==null){
//                return;
//            }
//            super.showModel(model);
//
//        }
//    }
}
