package com.hugboga.custom.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter.OnItemClickListener;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.adapter.NewOrderAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.TravelListAllBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.parser.ParserTravel;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CsDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class FgTravel extends BaseFragment implements OnItemClickListener {

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

    @BindView(R.id.travel_logout_layout)
    View logoutLayout;
    @BindView(R.id.travel_login_hint_tv)
    TextView loginHintTV;

    @BindView(R.id.travel_content)
    LinearLayout contentLayout; //主题部分
    //Tab1
    @BindView(R.id.header_left_btn)
    ImageView leftBtn;
    //Tab1
    @BindView(R.id.travel_tab1_layout)
    RelativeLayout tab1Layout;
    @BindView(R.id.travel_tab1_title)
    TextView tab1TextView;
    @BindView(R.id.travel_tab1_line)
    View tab1LineView;
    //Tab2
    @BindView(R.id.travel_tab2_layout)
    RelativeLayout tab2Layout;
    @BindView(R.id.travel_tab2_title)
    TextView tab2TextView;
    @BindView(R.id.travel_tab2_number)
    TextView tab2NumberTextView;
    @BindView(R.id.travel_tab2_line)
    View tab2LineView;
    //Tab3
    @BindView(R.id.travel_tab3_layout)
    RelativeLayout tab3Layout;
    @BindView(R.id.travel_tab3_title)
    TextView tab3TextView;
    @BindView(R.id.travel_tab3_number)
    TextView tab3NumberTextView;
    @BindView(R.id.travel_tab3_line)
    View tab3LineView;
    //Tab4
    @BindView(R.id.travel_tab4_layout)
    RelativeLayout tab4Layout;
    @BindView(R.id.travel_tab4_title)
    TextView tab4TextView;
    @BindView(R.id.travel_tab4_number)
    TextView tab4NumberTextView;
    @BindView(R.id.travel_tab4_line)
    View tab4LineView;

    @BindView(R.id.travel_viewpager)
    ViewPager viewPager; //滑动页面

    TravelListAll travelListAll;
    TravelListUnpay travelListUnpay;
    TravelListDoing travelListDoing;
    TravelListUnevaludate travelListUnevaludate;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    HashMap<Integer, Boolean> needRefreshMap = new HashMap<>();

    private int pagerPosition = 0;
    CsDialog csDialog;

    @Override
    public int getContentViewId() {
        return R.layout.fg_travel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setSensorsDefaultEvent("行程", SensorsConstant.ORDERLIST);
    }

    @Override
    public String getEventId() {
        return super.getEventId();
    }

    @Override
    public String getEventSource() {
        return "行程";
    }

    @Override
    public Map getEventMap() {
        return super.getEventMap();
    }

    private void initAdapterContent() {
        //fgHome = new FgHome();
        travelListAll = new TravelListAll();
        travelListUnpay = new TravelListUnpay();
        travelListDoing = new TravelListDoing();
        travelListUnevaludate = new TravelListUnevaludate();
        //addFragment(fgHome);
        //构造适配器
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        fragments.add(travelListAll);
        fragments.add(travelListUnpay);
        fragments.add(travelListDoing);
        fragments.add(travelListUnevaludate);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), (ArrayList<BaseFragment>) fragments);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagerPosition = position;
                reSetTabView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initView() {
        logoutLayout.setVisibility(UserEntity.getUser().isLogin(getActivity()) ? View.GONE : View.VISIBLE);
        String hintStr = CommonUtils.getString(R.string.travel_login_hint);
        SpannableString msp = new SpannableString(hintStr);
        msp.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                gotoLogin("管理你的行程");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.basic_black));
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        }, 6, hintStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginHintTV.setMovementMethod(LinkMovementMethod.getInstance()); //不设置没有点击事件
        loginHintTV.setText(msp);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //注册刷新广播
        IntentFilter filter = new IntentFilter(FILTER_FLUSH);
        getActivity().registerReceiver(flushReceiver, filter);

        initAdapterContent();

        //Tab相关
        tab1TextView.setSelected(true);
        tab1LineView.setVisibility(View.VISIBLE);
        needRefreshMap.put(TYPE_ORDER_RUNNING, true);
        needRefreshMap.put(TYPE_ORDER_FINISH, true);
        needRefreshMap.put(TYPE_ORDER_CANCEL, true);
        needRefreshMap.put(TYPE_ORDER_EVALUATE, true);

        reSetTabView(0); //刷新第一个标签页
    }

    private void addFooterView(LayoutInflater inflater, NewOrderAdapter adapter) {
        View footerView = inflater.inflate(R.layout.view_travel_footer, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(params);
        intentTravelFundActivity(footerView.findViewById(R.id.travel_footer_get_layout));
        adapter.addFooterView(footerView);
    }

    @Override
    protected Callback.Cancelable requestData() {
        if (UserEntity.getUser().isLogin(getActivity())) {
            contentLayout.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.GONE);
            reSetTabView(pagerPosition, true);
        } else {
            contentLayout.setVisibility(View.GONE);
            logoutLayout.setVisibility(View.VISIBLE);
        }
        return null;
    }

    private void intentTravelFundActivity(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TravelFundActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                getContext().startActivity(intent);
                MobClickUtils.onEvent(StatisticConstant.CLICK_TRAVELFOUND_XC);
                SensorsUtils.onAppClick(getEventSource(), "旅游基金-点击领取", "");
            }
        });
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        fgTitle.setLayoutParams(titleParams);
        fgTitle.setText(R.string.travel_title);
        leftBtn.setVisibility(View.GONE);

        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(47), UIUtils.dip2px(16));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        fgRightBtn.setLayoutParams(headerRightImageParams);
        fgRightBtn.setPadding(0, 0, 0, 0);
        fgRightBtn.setImageResource(R.mipmap.topbar_cs2);
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsUtils.onAppClick(getEventSource(), "客服", "");
                csDialog = CommonUtils.csDialog(getContext(), null, null, null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, "行程-客服", new CsDialog.OnCsListener() {
                    @Override
                    public void onCs() {
                        if (csDialog != null && csDialog.isShowing()) {
                            csDialog.dismiss();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void inflateContent() {
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        try {
            getActivity().unregisterReceiver(flushReceiver);
        } catch (Exception e) {
            //Receiver not registered
        }
        super.onDestroy();
    }

    @OnClick({R.id.travel_tab1_layout, R.id.travel_tab2_layout, R.id.travel_tab3_layout, R.id.travel_tab4_layout, R.id.travel_login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.travel_tab1_layout:
                //进行中
                viewPager.setCurrentItem(0);
                break;
            case R.id.travel_tab2_layout:
                //已完成
                viewPager.setCurrentItem(1);
                break;
            case R.id.travel_tab3_layout:
                //已取消
                viewPager.setCurrentItem(2);
                break;
            case R.id.travel_tab4_layout:
                viewPager.setCurrentItem(3);
                break;
            case R.id.travel_login_btn:
                gotoLogin("登录");
                break;
            default:
                break;
        }
    }

    private void gotoLogin(String elementContent) {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("source", getEventSource());
        startActivity(intent);
        SensorsUtils.onAppClick(getEventSource(), elementContent, "");
    }

    @Override
    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
    }


    private void reSetTabView(Integer position) {
        reSetTabView(position, false);
    }

    /**
     * 设置Tab切换效果
     *
     * @param position
     */
    private void reSetTabView(Integer position, boolean isSetCurrentItem) {
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
            if (isSetCurrentItem) {
                viewPager.setCurrentItem(0);
            }
        } else if (position == 1) {
            tab2TextView.setSelected(true);
            tab2LineView.setVisibility(View.VISIBLE);
            if (isSetCurrentItem) {
                viewPager.setCurrentItem(1);
            }
        } else if (position == 2) {
            tab3TextView.setSelected(true);
            tab3LineView.setVisibility(View.VISIBLE);
            if (isSetCurrentItem) {
                viewPager.setCurrentItem(2);
            }
        } else if (position == 3) {
            tab4TextView.setSelected(true);
            tab4LineView.setVisibility(View.VISIBLE);
            if (isSetCurrentItem) {
                viewPager.setCurrentItem(3);
            }
        }

    }

    public void setTravelCount(int requestType, TravelListAllBean travelListAllBean) {

        if (requestType == ParserTravel.AllLISTT) {
            setListCount(tab2NumberTextView, travelListAllBean.unpayTotalSize);
            setListCount(tab3NumberTextView, travelListAllBean.ingTotalSize);
            setListCount(tab4NumberTextView, travelListAllBean.evaluationTotalSize);
        } else if (requestType == ParserTravel.UNPAYISTT) {
            setListCount(tab2NumberTextView, travelListAllBean.totalSize);
        } else if (requestType == ParserTravel.INGISTT) {
            setListCount(tab3NumberTextView, travelListAllBean.totalSize);
        } else if (requestType == ParserTravel.UNEVALUATEIONLISTT) {
            setListCount(tab4NumberTextView, travelListAllBean.totalSize);
        }
    }

    public void clearTravelCount() {
        tab2NumberTextView.setVisibility(View.GONE);
        tab2NumberTextView.setText("");
        tab3NumberTextView.setVisibility(View.GONE);
        tab3NumberTextView.setText("");
        tab4NumberTextView.setVisibility(View.GONE);
        tab4NumberTextView.setText("");
    }

    private void setListCount(TextView textView, Object _count) {
        int count = CommonUtils.getCountInteger("" + _count);
        if (count > 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(count > 100 ? "..." : "" + count);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

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
            reSetTabView(jumpType, true);
        }
    };

    @Subscribe
    public void onEventMainThread(EventAction action) {
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
                clearTravelCount();
                break;
            case TRAVEL_LIST_TYPE:
                if (getContext() instanceof MainActivity) {
                    MainActivity activity = ((MainActivity) getContext());
                    if (activity.lastPagerPosition == 2) {
                        break;
                    }
                }
                int index = Integer.valueOf(action.data.toString());
                if (viewPager != null && index >= 0 && index < 4) {
                    viewPager.setCurrentItem(index);
                    reSetTabView(index, true);
                }
                break;
            case TRAVEL_LIST_NUMBER:
                Bundle bundle = (Bundle) action.getData();
                int requestType = bundle.getInt("requestType");
                TravelListAllBean travelListAllBean = (TravelListAllBean) bundle.getSerializable("travelListAllBean");
                if (travelListAllBean != null) {
                    setTravelCount(requestType, travelListAllBean);
                }
                break;
            case REFRESH_TRAVEL_DATA:
                viewPager.setCurrentItem(3);
                EventBus.getDefault().post(new EventAction(EventType.REFRESH_TRAVEL_DATA_UNEVALUDATE));
                break;
            default:
                break;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return travelListAll;
                }
                case 1: {
                    return travelListUnpay;
                }
                case 2: {
                    return travelListDoing;
                }
                case 3: {
                    return travelListUnevaludate;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }
}
