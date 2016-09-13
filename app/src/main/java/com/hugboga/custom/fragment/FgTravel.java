package com.hugboga.custom.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.adapter.ZBaseAdapter.OnItemClickListener;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.adapter.NewOrderAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.parser.ParserTravel;
import com.hugboga.custom.data.request.RequestTravel;
import com.hugboga.custom.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.fg_travel)
public class FgTravel extends BaseFragment implements View.OnClickListener, OnItemClickListener, ZListPageView.NoticeViewTask {

    public static final String FILTER_FLUSH = "com.hugboga.custom.travel.flush";
    public static final String JUMP_TYPE = "JUMP_TYPE";
    public static final String REFRESH_RUNNING = "REFRESH_RUNNING";
    public static final String REFRESH_FINISH = "REFRESH_FINISH";
    public static final String REFRESH_CANCEL = "REFRESH_CANCEL";
    public static final String REFRESH_EVALUATE = "REFRESH_EVALUATE";
    public static final int TYPE_ORDER_RUNNING = 0;
    public static final int TYPE_ORDER_FINISH = 1;
    public static final int TYPE_ORDER_CANCEL = 2;
    public static final int TYPE_ORDER_EVALUATE = 3;

    @ViewInject(R.id.travel_logout_layout)
    private View logoutLayout;
    @ViewInject(R.id.travel_content)
    LinearLayout contentLayout; //主题部分
    //Tab1
    @ViewInject(R.id.header_left_btn)
    private ImageView leftBtn;
    //Tab1
    @ViewInject(R.id.travel_tab1_layout)
    RelativeLayout tab1Layout;
    @ViewInject(R.id.travel_tab1_title)
    TextView tab1TextView;
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
    //Tab4
    @ViewInject(R.id.travel_tab4_layout)
    RelativeLayout tab4Layout;
    @ViewInject(R.id.travel_tab4_title)
    TextView tab4TextView;
    @ViewInject(R.id.travel_tab4_number)
    TextView tab4NumberTextView;
    @ViewInject(R.id.travel_tab4_line)
    View tab4LineView;

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

    RelativeLayout evaluateLayout;
    ZListPageView fgTravelEvaluate;
    ZSwipeRefreshLayout evaluateSwipeRefresh;
    RelativeLayout evaluateEmptyLayout;
    NewOrderAdapter evaluateAdapter;

    HashMap<Integer, Boolean> needRefreshMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public String getEventId() {
        return super.getEventId();
    }

    @Override
    public String getEventSource() {
        return "行程页";
    }

    @Override
    public Map getEventMap() {
        return super.getEventMap();
    }

    @Override
    public void initView() {
        logoutLayout.setVisibility(UserEntity.getUser().isLogin(getActivity()) ? View.GONE : View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //注册刷新广播
        IntentFilter filter = new IntentFilter(FILTER_FLUSH);
        getActivity().registerReceiver(flushReceiver, filter);

        runninLayout = (RelativeLayout) inflater.inflate(R.layout.travel_list_layout_running, null);
        fgTravelRunning = (ZListPageView) runninLayout.findViewById(R.id.listview);
        fgTravelRunning.removeItemDecoration(fgTravelRunning.divider);
        runningSwipeRefresh = (ZSwipeRefreshLayout) runninLayout.findViewById(R.id.swipe);
        runningEmptyLayout = (RelativeLayout) runninLayout.findViewById(R.id.list_empty);
        runningAdapter = new NewOrderAdapter(getContext());
        fgTravelRunning.setAdapter(runningAdapter);
        fgTravelRunning.setzSwipeRefreshLayout(runningSwipeRefresh);
        fgTravelRunning.setEmptyLayout(runningEmptyLayout);
        fgTravelRunning.setNoticeViewTask(this);
        fgTravelRunning.setRequestData(new RequestTravel(getActivity(), 0));
        fgTravelRunning.setOnItemClickListener(new TravelOnItemClickListener(fgTravelRunning));
        addFooterView(inflater, runningAdapter);
        ZDefaultDivider zDefaultDivider = fgTravelRunning.getItemDecoration();
        zDefaultDivider.setItemOffsets(0, 15, 0, 15);
        //设置开启我的行程事件
        setBtnClick(runninLayout.findViewById(R.id.travel_empty_btn));

        finishLayout = (RelativeLayout) inflater.inflate(R.layout.travel_list_layout_finish, null);
        fgTravelFinish = (ZListPageView) finishLayout.findViewById(R.id.listview);
        fgTravelFinish.removeItemDecoration(fgTravelFinish.divider);
        finishSwipeRefresh = (ZSwipeRefreshLayout) finishLayout.findViewById(R.id.swipe);
        finishEmptyLayout = (RelativeLayout) finishLayout.findViewById(R.id.list_empty);
        finishAdapter = new NewOrderAdapter(getContext());
        fgTravelFinish.setAdapter(finishAdapter);
        fgTravelFinish.setzSwipeRefreshLayout(finishSwipeRefresh);
        fgTravelFinish.setEmptyLayout(finishEmptyLayout);
        fgTravelFinish.setNoticeViewTask(this);
        fgTravelFinish.setRequestData(new RequestTravel(getActivity(), 4));
        fgTravelFinish.setOnItemClickListener(new TravelOnItemClickListener(fgTravelFinish));
        ZDefaultDivider zDefaultDivider2 = fgTravelFinish.getItemDecoration();
        zDefaultDivider2.setItemOffsets(0, 15, 0, 15);
        addFooterView(inflater, finishAdapter);

        cancelLayout = (RelativeLayout) inflater.inflate(R.layout.travel_list_layout_cancel, null);
        fgTravelCancel = (ZListPageView) cancelLayout.findViewById(R.id.listview);
        fgTravelCancel.removeItemDecoration(fgTravelCancel.divider);
        cancelSwipeRefresh = (ZSwipeRefreshLayout) cancelLayout.findViewById(R.id.swipe);
        cancelEmptyLayout = (RelativeLayout) cancelLayout.findViewById(R.id.list_empty);
        cancelAdapter = new NewOrderAdapter(getContext());
        fgTravelCancel.setAdapter(cancelAdapter);
        fgTravelCancel.setzSwipeRefreshLayout(cancelSwipeRefresh);
        fgTravelCancel.setEmptyLayout(cancelEmptyLayout);
        fgTravelCancel.setNoticeViewTask(this);
        fgTravelCancel.setRequestData(new RequestTravel(getActivity(), 5));
        fgTravelCancel.setOnItemClickListener(new TravelOnItemClickListener(fgTravelCancel));
        ZDefaultDivider zDefaultDivider3 = fgTravelCancel.getItemDecoration();
        zDefaultDivider3.setItemOffsets(0, 15, 0, 15);
        addFooterView(inflater, cancelAdapter);

        evaluateLayout = (RelativeLayout) inflater.inflate(R.layout.travel_list_layout_cancel, null);
        fgTravelEvaluate = (ZListPageView) evaluateLayout.findViewById(R.id.listview);
        fgTravelEvaluate.removeItemDecoration(fgTravelEvaluate.divider);
        evaluateSwipeRefresh = (ZSwipeRefreshLayout) evaluateLayout.findViewById(R.id.swipe);
        evaluateEmptyLayout = (RelativeLayout) evaluateLayout.findViewById(R.id.list_empty);
        evaluateAdapter = new NewOrderAdapter(getContext());
        fgTravelEvaluate.setAdapter(evaluateAdapter);
        fgTravelEvaluate.setzSwipeRefreshLayout(evaluateSwipeRefresh);
        fgTravelEvaluate.setEmptyLayout(evaluateEmptyLayout);
        fgTravelEvaluate.setNoticeViewTask(this);
        fgTravelEvaluate.setRequestData(new RequestTravel(getActivity(), 6));
        fgTravelEvaluate.setOnItemClickListener(new TravelOnItemClickListener(fgTravelEvaluate));
        ZDefaultDivider zDefaultDivider4 = fgTravelEvaluate.getItemDecoration();
        zDefaultDivider4.setItemOffsets(0, 15, 0, 15);
        addFooterView(inflater, evaluateAdapter);


        //Tab相关
        tab1TextView.setSelected(true);
        tab1LineView.setVisibility(View.VISIBLE);
        needRefreshMap.put(TYPE_ORDER_RUNNING, true);
        needRefreshMap.put(TYPE_ORDER_FINISH, true);
        needRefreshMap.put(TYPE_ORDER_CANCEL, true);
        needRefreshMap.put(TYPE_ORDER_EVALUATE, true);

        //加载数据片段页面
        List<RelativeLayout> listViews = new ArrayList<>();
        listViews.add(runninLayout);
        listViews.add(finishLayout);
        listViews.add(cancelLayout);
        listViews.add(evaluateLayout);
        OrderPageAdapter orderAdapter = new OrderPageAdapter(listViews);
        viewPager.setOffscreenPageLimit(4);
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

    private void addFooterView(LayoutInflater inflater, NewOrderAdapter adapter) {
        View footerView = inflater.inflate(R.layout.view_travel_footer, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(params);
        footerView.findViewById(R.id.travel_footer_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TravelFundActivity.class);
                getContext().startActivity(intent);
            }
        });
        adapter.addFooterView(footerView);
    }

    @Override
    protected Callback.Cancelable requestData() {
        if (UserEntity.getUser().isLogin(getActivity())) {
            contentLayout.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.GONE);
            reSetTabView(0);
        } else {
            contentLayout.setVisibility(View.GONE);
            logoutLayout.setVisibility(View.VISIBLE);
        }
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

    public void loadDataEvaluate() {
        if (fgTravelEvaluate != null) {
            fgTravelEvaluate.showPageFirst();
            evaluateAdapter.notifyDataSetChanged();
        }
    }

    private void setBtnClick(View view) {
        Button startBtn = (Button) view;
        if (startBtn != null)
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //开启行程，跳转到首页
                    EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
                }
            });
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
        leftBtn.setImageResource(R.mipmap.header_menu);
        leftBtn.setOnClickListener(this);
        fgTitle.setText("行程");

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
                super.onClick(v);
                break;
        }
    }

    @Event({R.id.travel_tab1_layout, R.id.travel_tab2_layout, R.id.travel_tab3_layout,  R.id.travel_tab4_layout, R.id.travel_login_btn})
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
            case R.id.travel_tab4_layout:
                reSetTabView(3);
                break;
            case R.id.travel_login_btn:
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.putExtra("source",getEventSource());
                startActivity(intent);
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
        if (!UserEntity.getUser().isLogin(getActivity())) {
            return;
        }
        tab1TextView.setSelected(false);
        tab1LineView.setVisibility(View.GONE);
        tab2TextView.setSelected(false);
        tab2LineView.setVisibility(View.GONE);
        tab3TextView.setSelected(false);
        tab3LineView.setVisibility(View.GONE);
        tab4TextView.setSelected(false);
        tab4LineView.setVisibility(View.GONE);
        if (position == 0) {
            tab1TextView.setSelected(true);
            tab1LineView.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(0);
            loadDataRunning();
        } else if (position == 1) {
            tab2TextView.setSelected(true);
            tab2LineView.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(1);
            loadDataFinish();
        } else if (position == 2) {
            tab3TextView.setSelected(true);
            tab3LineView.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(2);
            loadDataCancel();
        } else if (position == 3) {
            tab4TextView.setSelected(true);
            tab4LineView.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(3);
            loadDataEvaluate();
        }
    }

    @Override
    public void notice(Object object) {
        if (object != null) {
            Object[] obj = (Object[]) object;
            int tab2Count = CommonUtils.getCountInteger("" + obj[2]);
            if (tab2Count > 0) {
                tab2NumberTextView.setVisibility(View.VISIBLE);
                tab2NumberTextView.setText(tab2Count > 100 ? "99+" : "" + tab2Count);
            } else {
                tab2NumberTextView.setVisibility(View.GONE);
            }
            int tab3Count = CommonUtils.getCountInteger("" + obj[3]);
            if (tab3Count > 0) {
                tab3NumberTextView.setVisibility(View.VISIBLE);
                tab3NumberTextView.setText(tab3Count > 100 ? "99+" : "" + tab3Count);
            } else {
                tab3NumberTextView.setVisibility(View.GONE);
            }
            int tab4Count = CommonUtils.getCountInteger("" + obj[4]);
            if (tab4Count > 0) {
                tab4NumberTextView.setVisibility(View.VISIBLE);
                tab4NumberTextView.setText(tab4Count > 100 ? "99+" : "" + tab4Count);
            } else {
                tab4NumberTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void error(ExceptionInfo errorInfo, BaseRequest request) {

    }

    class TravelOnItemClickListener implements ZBaseAdapter.OnItemClickListener {

        View view;

        public TravelOnItemClickListener(View view) {
            this.view = view;
        }

        @Override
        public void onItemClick(View v, int position) {
            MLog.e("view = " + view);
            OrderBean bean = null;
            if (view == fgTravelRunning) {
                bean = runningAdapter.getDatas().get(position);
            } else if (view == fgTravelFinish) {
                bean = finishAdapter.getDatas().get(position);
            } else if (view == fgTravelCancel) {
                bean = cancelAdapter.getDatas().get(position);
            } else if (view == fgTravelEvaluate) {
                bean = evaluateAdapter.getDatas().get(position);
            }
            OrderDetailActivity.Params params = new OrderDetailActivity.Params();
            params.orderType = bean.orderType;
            params.orderId = bean.orderNo;
            params.source = bean.orderType == 5 ? bean.serviceCityName : "首页";
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE,params.source);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

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
            needRefreshMap.put(TYPE_ORDER_EVALUATE, needRefreshMap.get(TYPE_ORDER_EVALUATE) || intent.getBooleanExtra(REFRESH_EVALUATE, false));
            MLog.e("onReceive jumpType=" + jumpType + " " + needRefreshMap.get(TYPE_ORDER_RUNNING) + " " + needRefreshMap.get(TYPE_ORDER_FINISH) + " " + TYPE_ORDER_CANCEL);
            reSetTabView(jumpType);
        }
    };

    @Subscribe
    public void onEventMainThread(EventAction action) {
        MLog.e(this + " onEventMainThread " + action.getType());
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
            case FGTRAVEL_UPDATE:
                contentLayout.setVisibility(View.VISIBLE);
                logoutLayout.setVisibility(View.GONE);
                requestData();
                break;
            case CLICK_USER_LOOUT:
                contentLayout.setVisibility(View.GONE);
                logoutLayout.setVisibility(View.VISIBLE);
                cleanListData();
                break;
            case TRAVEL_LIST_TYPE:
                int index = Integer.valueOf(action.data.toString());
                if (viewPager != null && index >= 0 && index < 4){
                    reSetTabView(index);
                }
                break;
            default:
                break;
        }
    }

    private void cleanListData() {
        try {
            runningAdapter.getDatas().clear();
            runningAdapter.notifyDataSetChanged();
            finishAdapter.getDatas().clear();
            finishAdapter.notifyDataSetChanged();
            cancelAdapter.getDatas().clear();
            cancelAdapter.notifyDataSetChanged();
            evaluateAdapter.getDatas().clear();
            evaluateAdapter.notifyDataSetChanged();
        }catch (Exception e) {

        }
    }
}
