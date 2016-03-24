package com.hugboga.custom.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.TravelAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.request.RequestTravel;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ContentView(R.layout.fg_travel)
public class FgTravel extends BaseFragment  implements AdapterView.OnItemClickListener, ZListView.OnRefreshListener, ZListView.OnLoadListener, View.OnClickListener {

    public static final String FILTER_FLUSH = "com.hugboga.custom.travel.flush";
    public static final String JUMP_TYPE = "JUMP_TYPE";
    public static final String REFRESH_RUNNING = "REFRESH_RUNNING";
    public static final String REFRESH_FINISH = "REFRESH_FINISH";
    public static final String REFRESH_CANCEL = "REFRESH_CANCEL";
    public static final int TYPE_ORDER_RUNNING=0;
    public static final int TYPE_ORDER_FINISH=1;
    public static final int TYPE_ORDER_CANCEL=2;
    private static final int PAGESIZE = 20;


    @ViewInject(R.id.header_left_btn)
    private ImageView leftBtn;
    //Tab1
    @ViewInject(R.id.travel_tab1_layout)
    RelativeLayout tab1Layout;
    @ViewInject(R.id.travel_tab1_title)
    TextView tab1TextView;
    @ViewInject(R.id.travel_tab1_number)
    TextView tab1NumberTextView;
    @ViewInject(R.id.travel_tab1_line)
    View tab1LineView;
    //Tab2
    @ViewInject(R.id.travel_tab2_layout)
    RelativeLayout tab2Layout;
    @ViewInject(R.id.travel_tab2_title)
    TextView tab2TextView;
    @ViewInject(R.id.travel_tab2_number)
    TextView tab2NumberTextView;
    @ViewInject(R.id.travel_tab2_line)
    View tab2LineView;
    //Tab3
    @ViewInject(R.id.travel_tab3_layout)
    RelativeLayout tab3Layout;
    @ViewInject(R.id.travel_tab3_title)
    TextView tab3TextView;
    @ViewInject(R.id.travel_tab3_number)
    TextView tab3NumberTextView;
    @ViewInject(R.id.travel_tab3_line)
    View tab3LineView;
    @ViewInject(R.id.travel_viewpager)
    ViewPager viewPager; //滑动页面

    RelativeLayout runninLayout;
    ZListView fgTravelRunning;
    TravelAdapter runningAdapter;
    RelativeLayout finishLayout;
    ZListView fgTravelFinish;
    TravelAdapter finishAdapter;
    RelativeLayout cancelLayout;
    ZListView fgTravelCancel;
    TravelAdapter cancelAdapter;

    View emptyViewRunning;
    View emptyViewFinish;
    View emptyViewCancel;

    HashMap<Integer,Boolean> needRefreshMap = new HashMap<>();

    //Request
    RequestTravel parserTravel;
    private int orderShowType = TYPE_ORDER_RUNNING;

    @Override
    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //注册刷新广播
        IntentFilter filter = new IntentFilter(FILTER_FLUSH);
        getActivity().registerReceiver(flushReceiver, filter);

        //进行中
        runninLayout = new RelativeLayout(getActivity());
        fgTravelRunning = new ZListView(getActivity());
        runningAdapter = new TravelAdapter(getActivity(),this);
        emptyViewRunning = inflater.inflate(R.layout.fg_travel_empty, null);
        initListView(runninLayout, fgTravelRunning, runningAdapter, emptyViewRunning);
        //已完成
        finishLayout = new RelativeLayout(getActivity());
        fgTravelFinish = new ZListView(getActivity());
        finishAdapter = new TravelAdapter(getActivity(),this);
        emptyViewFinish = inflater.inflate(R.layout.fg_travel_empty_finish, null);
        initListView(finishLayout, fgTravelFinish, finishAdapter, emptyViewFinish);
        //取消
        cancelLayout = new RelativeLayout(getActivity());
        fgTravelCancel = new ZListView(getActivity());
        cancelAdapter = new TravelAdapter(getActivity(),this);
        emptyViewCancel = inflater.inflate(R.layout.fg_travel_empty_cancel, null);
        initListView(cancelLayout, fgTravelCancel, cancelAdapter, emptyViewCancel);

        tab1TextView.setSelected(true);
        tab1NumberTextView.setSelected(true);
        tab1LineView.setVisibility(View.VISIBLE);
        needRefreshMap.put(TYPE_ORDER_RUNNING, true);
        needRefreshMap.put(TYPE_ORDER_FINISH, true);
        needRefreshMap.put(TYPE_ORDER_CANCEL,true);


        //加载数据片段页面
        List<RelativeLayout> listViews = new ArrayList<RelativeLayout>();
        listViews.add(runninLayout);
        listViews.add(finishLayout);
        listViews.add(cancelLayout);
        OrderPageAdapter orderAdapter = new OrderPageAdapter(listViews);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(orderAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                MLog.e("onPageSelected " + position);
                reSetTabView(position);
                runData(0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initListView(ViewGroup layout,ZListView listView,BaseAdapter adapter,View emptyView){
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(0));
        listView.setOnItemClickListener(this);
        listView.setonRefreshListener(this);
        listView.setonLoadListener(this);
        layout.addView(listView);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Button startBtn = (Button) emptyView.findViewById(R.id.travel_empty_btn);
        if(startBtn!=null)
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layout.addView(emptyView);
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
        leftBtn.setImageResource(R.mipmap.header_menu);
        leftBtn.setOnClickListener(this);
        fgTitle.setText("我的行程");

    }

    @Override
    protected void inflateContent() {

    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(flushReceiver);
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn:
                ((MainActivity) getActivity()).openDrawer();
                break;
            default:
                break;
        }
    }
    @Override
    protected Callback.Cancelable requestData() {
        return runData(0);
    }
    /**
     * 加载数据
     *
     */
    private Callback.Cancelable runData( int offset) {
        if(needRefreshMap.get(orderShowType)) {
            parserTravel = new RequestTravel(getActivity(),orderShowType, offset, PAGESIZE);
           return HttpRequestUtils.request(getActivity(),parserTravel,this,null);
        }else{
            onRequestComplete();
        }
        return null;
    }



    @Override
    public void onDataRequestSucceed(BaseRequest parser) {
        if (parser instanceof RequestTravel) {
            parserTravel = (RequestTravel) parser;
            ArrayList<OrderBean> listData = parserTravel.getData();
            switch (orderShowType){
                case TYPE_ORDER_RUNNING:
                    needRefreshMap.put(TYPE_ORDER_RUNNING, false);
                    inflateView(tab1NumberTextView, fgTravelRunning, runningAdapter,emptyViewRunning, listData);
                    break;
                case TYPE_ORDER_FINISH:
                    needRefreshMap.put(TYPE_ORDER_FINISH, false);
                    inflateView(tab2NumberTextView,fgTravelFinish,finishAdapter,emptyViewFinish, listData);
                    break;
                case TYPE_ORDER_CANCEL:
                    needRefreshMap.put(TYPE_ORDER_CANCEL,false);
                    inflateView(tab3NumberTextView,fgTravelCancel,cancelAdapter,emptyViewCancel, listData);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest parser) {
        super.onDataRequestError(errorInfo, parser);
        onRequestComplete();
    }

    private void onRequestComplete(){
        ZListView listView = fgTravelRunning;
        switch (orderShowType){
            case TYPE_ORDER_RUNNING:
                listView = fgTravelRunning;
                break;
            case TYPE_ORDER_FINISH:
                listView = fgTravelFinish;
                break;
            case TYPE_ORDER_CANCEL:
                listView = fgTravelCancel;
                break;
            default:
                break;
        }
        listView.onLoadComplete();
        listView.onRefreshComplete();
    }

    private void inflateView(TextView text,ZListView listView,TravelAdapter adapter,View emptyView, ArrayList listData) {
        listView.setEmptyView(emptyView);
        if(listView.state== ZListView.LOADING_MORE){
            adapter.addList(listData);
        }else{
            adapter.setList(listData);
        }
        if(listData.size()<PAGESIZE){
            listView.onLoadCompleteNone();
        }else{
            listView.onLoadComplete();
        }
        listView.onRefreshComplete();
        MLog.e("listView = "+listView);
    }


    @Event({R.id.travel_tab1_layout, R.id.travel_tab2_layout, R.id.travel_tab3_layout})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.travel_tab1_layout:
                //进行中
                reSetTabView(0);
                break;
            case R.id.travel_tab2_layout:
                //已完成
                reSetTabView(1);
                break;
            case R.id.travel_tab3_layout:
                //已取消
                reSetTabView(2);
                break;
            default:
                break;
        }
    }

    @Override
    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
      /*  if (FgPaySuccess.class.getSimpleName().equals(from)) {
            requestDate();
        } else if (FgAssessment.class.getSimpleName().equals(from)) {
            requestDate();
        }*/
    }



    /**
     * 设置Tab切换效果
     *
     * @param position
     */
    private void reSetTabView(Integer position) {
        if(position==orderShowType){
            requestData();
        }else {
            orderShowType = position;
            tab1TextView.setSelected(false);
            tab1NumberTextView.setSelected(false);
            tab1LineView.setVisibility(View.GONE);
            tab2TextView.setSelected(false);
            tab2NumberTextView.setSelected(false);
            tab2LineView.setVisibility(View.GONE);
            tab3TextView.setSelected(false);
            tab3NumberTextView.setSelected(false);
            tab3LineView.setVisibility(View.GONE);
            if (position == 0) {
                tab1TextView.setSelected(true);
                tab1NumberTextView.setSelected(true);
                tab1LineView.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(0);
            } else if (position == 1) {
                tab2TextView.setSelected(true);
                tab2NumberTextView.setSelected(true);
                tab2LineView.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(1);
            } else if (position == 2) {
                tab3TextView.setSelected(true);
                tab3NumberTextView.setSelected(true);
                tab3LineView.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(2);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == fgTravelRunning) {
            OrderBean bean = (OrderBean) runningAdapter.getItem(position - 1);
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_BUSINESS_TYPE, bean.orderType);
            bundle.putInt(KEY_GOODS_TYPE, bean.orderGoodsType);
            bundle.putString(FgOrder.KEY_ORDER_ID, bean.orderNo);
            startFragment(new FgOrder(), bundle);
        } else if (parent == fgTravelFinish) {
            OrderBean bean = (OrderBean) finishAdapter.getItem(position - 1);
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_BUSINESS_TYPE, bean.orderType);
            bundle.putInt(KEY_GOODS_TYPE, bean.orderGoodsType);
            bundle.putString(FgOrder.KEY_ORDER_ID, bean.orderNo);
            startFragment(new FgOrder(), bundle);
        } else if (parent == fgTravelCancel) {
            OrderBean bean = (OrderBean) cancelAdapter.getItem(position - 1);
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_BUSINESS_TYPE, bean.orderType);
            bundle.putInt(KEY_GOODS_TYPE, bean.orderGoodsType);
            bundle.putString(FgOrder.KEY_ORDER_ID, bean.orderNo);
            startFragment(new FgOrder(), bundle);
        }
    }

    @Override
    public void onRefresh() {
        needRefreshMap.put(orderShowType,true);
        runData( 0);
    }

    @Override
    public void onLoad() {
        needRefreshMap.put(orderShowType,true);
        switch (orderShowType){
            case TYPE_ORDER_RUNNING:
                runData( runningAdapter.getCount());
                break;
            case TYPE_ORDER_FINISH:
                runData( finishAdapter.getCount());
                break;
            case TYPE_ORDER_CANCEL:
                runData( cancelAdapter.getCount());
                break;
            default:
                break;
        }
    }
    class OrderPageAdapter extends PagerAdapter {

        private List<RelativeLayout> listViews;

        public OrderPageAdapter(List<RelativeLayout> listViews) {
            this.listViews = listViews;
        }

        @Override
        public int getCount() {
            return listViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(listViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(listViews.get(position));
            return listViews.get(position);
        }
    }

    /**
     * 刷新订单列表
     */
    BroadcastReceiver flushReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MLog.e("onReceive  刷新订单列表");
            int jumpType = intent.getIntExtra(JUMP_TYPE,TYPE_ORDER_RUNNING);
            needRefreshMap.put(TYPE_ORDER_RUNNING,needRefreshMap.get(TYPE_ORDER_RUNNING)||intent.getBooleanExtra(REFRESH_RUNNING,false));
            needRefreshMap.put(TYPE_ORDER_FINISH, needRefreshMap.get(TYPE_ORDER_FINISH)||intent.getBooleanExtra(REFRESH_FINISH,false));
            needRefreshMap.put(TYPE_ORDER_CANCEL, needRefreshMap.get(TYPE_ORDER_CANCEL)||intent.getBooleanExtra(REFRESH_CANCEL, false));
            MLog.e("onReceive jumpType=" + jumpType + " " + needRefreshMap.get(TYPE_ORDER_RUNNING) + " " + needRefreshMap.get(TYPE_ORDER_FINISH)+" "+TYPE_ORDER_CANCEL);
            reSetTabView(jumpType);
        }
    };
}
