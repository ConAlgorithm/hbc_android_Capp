package com.hugboga.custom.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.ZSwipeRefreshLayout;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.NewOrderAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestTravel;
import com.hugboga.custom.widget.recycler.ZListPageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

@ContentView(R.layout.fg_travel)
public class FgTravel extends BaseFragment implements View.OnClickListener, ZBaseAdapter.OnItemClickListener {

    public static final String FILTER_FLUSH = "com.hugboga.custom.travel.flush";
    public static final String JUMP_TYPE = "JUMP_TYPE";
    public static final String REFRESH_RUNNING = "REFRESH_RUNNING";
    public static final String REFRESH_FINISH = "REFRESH_FINISH";
    public static final String REFRESH_CANCEL = "REFRESH_CANCEL";
    public static final int TYPE_ORDER_RUNNING = 0;
    public static final int TYPE_ORDER_FINISH = 1;
    public static final int TYPE_ORDER_CANCEL = 2;

    @ViewInject(R.id.travel_logout_layout)
    private View logoutLayout;
    //Tab1
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

    //进行中订单部分
    RelativeLayout runninLayout;
    ZListPageView fgTravelRunning;
    ZSwipeRefreshLayout runningSwipeRefresh;
    RelativeLayout runningEmptyLayout;
    NewOrderAdapter runningAdapter;
    //已完成订单部分
    RelativeLayout finishLayout;
    ZListPageView fgTravelFinish;
    ZSwipeRefreshLayout finishSwipeRefresh;
    RelativeLayout finishEmptyLayout;
    NewOrderAdapter finishAdapter;
    //已取消订单部分
    RelativeLayout cancelLayout;
    ZListPageView fgTravelCancel;
    ZSwipeRefreshLayout cancelSwipeRefresh;
    RelativeLayout cancelEmptyLayout;
    NewOrderAdapter cancelAdapter;

    HashMap<Integer, Boolean> needRefreshMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        logoutLayout.setVisibility(UserEntity.getUser().isLogin(getActivity()) ? View.GONE : View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //注册刷新广播
        IntentFilter filter = new IntentFilter(FILTER_FLUSH);
        getActivity().registerReceiver(flushReceiver, filter);

        //进行中
        runninLayout = (RelativeLayout) inflater.inflate(R.layout.travel_list_layout_running, null);
        fgTravelRunning = (ZListPageView) runninLayout.findViewById(R.id.listview);
        runningSwipeRefresh = (ZSwipeRefreshLayout) runninLayout.findViewById(R.id.swipe);
        runningEmptyLayout = (RelativeLayout) runninLayout.findViewById(R.id.list_empty);
        runningAdapter = new NewOrderAdapter(getActivity());
        fgTravelRunning.setAdapter(runningAdapter);
        fgTravelRunning.setzSwipeRefreshLayout(runningSwipeRefresh);
        fgTravelRunning.setEmptyLayout(runningEmptyLayout);
        fgTravelRunning.setRequestData(new RequestTravel(getActivity(), 1));
        fgTravelRunning.setOnItemClickListener(this);
        //设置开启我的行程事件
        setBtnClick(runninLayout.findViewById(R.id.travel_empty_btn));
        //已完成
        finishLayout = (RelativeLayout) inflater.inflate(R.layout.travel_list_layout_finish, null);
        fgTravelFinish = (ZListPageView) finishLayout.findViewById(R.id.listview);
        finishSwipeRefresh = (ZSwipeRefreshLayout) finishLayout.findViewById(R.id.swipe);
        finishEmptyLayout = (RelativeLayout) finishLayout.findViewById(R.id.list_empty);
        finishAdapter = new NewOrderAdapter(getActivity());
        fgTravelFinish.setAdapter(finishAdapter);
        fgTravelFinish.setzSwipeRefreshLayout(finishSwipeRefresh);
        fgTravelFinish.setEmptyLayout(finishEmptyLayout);
        fgTravelFinish.setRequestData(new RequestTravel(getActivity(), 2));
        fgTravelFinish.setOnItemClickListener(this);
        //已取消
        cancelLayout = (RelativeLayout) inflater.inflate(R.layout.travel_list_layout_cancel, null);
        fgTravelCancel = (ZListPageView) cancelLayout.findViewById(R.id.listview);
        cancelSwipeRefresh = (ZSwipeRefreshLayout) cancelLayout.findViewById(R.id.swipe);
        cancelEmptyLayout = (RelativeLayout) cancelLayout.findViewById(R.id.list_empty);
        cancelAdapter = new NewOrderAdapter(getActivity());
        fgTravelCancel.setAdapter(cancelAdapter);
        fgTravelCancel.setzSwipeRefreshLayout(cancelSwipeRefresh);
        fgTravelCancel.setEmptyLayout(cancelEmptyLayout);
        fgTravelCancel.setRequestData(new RequestTravel(getActivity(), 3));
        fgTravelCancel.setOnItemClickListener(this);

        //Tab相关
        tab1TextView.setSelected(true);
        tab1NumberTextView.setSelected(true);
        tab1LineView.setVisibility(View.VISIBLE);
        needRefreshMap.put(TYPE_ORDER_RUNNING, true);
        needRefreshMap.put(TYPE_ORDER_FINISH, true);
        needRefreshMap.put(TYPE_ORDER_CANCEL, true);

        //加载数据片段页面
        List<RelativeLayout> listViews = new ArrayList<>();
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        reSetTabView(0); //刷新第一个标签页
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    public void loadDataRunning() {
        if (fgTravelRunning != null) {
            fgTravelRunning.showPageFirst();
            runningAdapter.notifyDataSetChanged();
        }
    }

    public void loadDataFinish() {
        if (fgTravelFinish != null) {
            fgTravelFinish.showPageFirst();
            finishAdapter.notifyDataSetChanged();
        }
    }

    public void loadDataCancel() {
        if (fgTravelCancel != null) {
            fgTravelCancel.showPageFirst();
            cancelAdapter.notifyDataSetChanged();
        }
    }

    private void setBtnClick(View view) {
        Button startBtn = (Button) view;
        if (startBtn != null)
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
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
        EventBus.getDefault().unregister(this);
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

    @Event({R.id.travel_tab1_layout, R.id.travel_tab2_layout, R.id.travel_tab3_layout, R.id.travel_login_btn})
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
            case R.id.travel_login_btn:
                startFragment(new FgLogin());
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
    }


    /**
     * 设置Tab切换效果
     *
     * @param position
     */
    private void reSetTabView(Integer position) {
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
            loadDataRunning();
        } else if (position == 1) {
            tab2TextView.setSelected(true);
            tab2NumberTextView.setSelected(true);
            tab2LineView.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(1);
            loadDataFinish();
        } else if (position == 2) {
            tab3TextView.setSelected(true);
            tab3NumberTextView.setSelected(true);
            tab3LineView.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(2);
            loadDataCancel();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view == fgTravelRunning) {
            OrderBean bean = (OrderBean) runningAdapter.getDatas().get(position);
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_BUSINESS_TYPE, bean.orderType);
            bundle.putInt(KEY_GOODS_TYPE, bean.orderGoodsType);
            bundle.putString(FgOrder.KEY_ORDER_ID, bean.orderNo);
            startFragment(new FgOrder(), bundle);
        } else if (view == fgTravelFinish) {
            OrderBean bean = (OrderBean) finishAdapter.getDatas().get(position - 1);
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_BUSINESS_TYPE, bean.orderType);
            bundle.putInt(KEY_GOODS_TYPE, bean.orderGoodsType);
            bundle.putString(FgOrder.KEY_ORDER_ID, bean.orderNo);
            startFragment(new FgOrder(), bundle);
        } else if (view == fgTravelCancel) {
            OrderBean bean = (OrderBean) cancelAdapter.getDatas().get(position - 1);
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_BUSINESS_TYPE, bean.orderType);
            bundle.putInt(KEY_GOODS_TYPE, bean.orderGoodsType);
            bundle.putString(FgOrder.KEY_ORDER_ID, bean.orderNo);
            startFragment(new FgOrder(), bundle);
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
            int jumpType = intent.getIntExtra(JUMP_TYPE, TYPE_ORDER_RUNNING);
            needRefreshMap.put(TYPE_ORDER_RUNNING, needRefreshMap.get(TYPE_ORDER_RUNNING) || intent.getBooleanExtra(REFRESH_RUNNING, false));
            needRefreshMap.put(TYPE_ORDER_FINISH, needRefreshMap.get(TYPE_ORDER_FINISH) || intent.getBooleanExtra(REFRESH_FINISH, false));
            needRefreshMap.put(TYPE_ORDER_CANCEL, needRefreshMap.get(TYPE_ORDER_CANCEL) || intent.getBooleanExtra(REFRESH_CANCEL, false));
            MLog.e("onReceive jumpType=" + jumpType + " " + needRefreshMap.get(TYPE_ORDER_RUNNING) + " " + needRefreshMap.get(TYPE_ORDER_FINISH) + " " + TYPE_ORDER_CANCEL);
            reSetTabView(jumpType);
        }
    };

    public void onEventMainThread(EventAction action) {
        MLog.e(this + " onEventMainThread " + action.getType());
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                requestData();
                logoutLayout.setVisibility(View.GONE);
                break;
            case CLICK_USER_LOOUT:
                cleanListData();
                logoutLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void cleanListData() {
        runningAdapter.getDatas().clear();
        runningAdapter.notifyDataSetChanged();
        finishAdapter.getDatas().clear();
        finishAdapter.notifyDataSetChanged();
        cancelAdapter.getDatas().clear();
        cancelAdapter.notifyDataSetChanged();
    }
}
