package com.hugboga.custom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.CollectGuideListActivity;
import com.hugboga.custom.activity.CouponActivity;
import com.hugboga.custom.activity.IMChatActivity;
import com.hugboga.custom.activity.InsureActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.PersonInfoActivity;
import com.hugboga.custom.activity.ServicerCenterActivity;
import com.hugboga.custom.activity.SettingActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.adapter.MenuItemAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.LvMenuItem;
import com.hugboga.custom.data.bean.PushMessage;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestPushClick;
import com.hugboga.custom.data.request.RequestPushToken;
import com.hugboga.custom.data.request.RequestUploadLocation;
import com.hugboga.custom.data.request.RequestUserInfo;
import com.hugboga.custom.fragment.FgChat;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.fragment.FgTravel;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.LocationUtils;
import com.hugboga.custom.utils.PermissionRes;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.PushUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.Tools;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.util.FileUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;


@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener, View.OnClickListener, HttpRequestListener {

    public static final String PUSH_BUNDLE_MSG = "pushMessage";
    public static final String FILTER_PUSH_DO = "com.hugboga.custom.pushdo";

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 12;

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer;

    @ViewInject(R.id.container)
    private ViewPager mViewPager;

    private PolygonImageView my_icon_head;//header的头像
    private TextView tv_nickname;//header的昵称
    private TextView couponTV, couponUnitTV;
    private TextView travelFundTV, travelFundUnitTV;
    private ImageView travelFundHintIV;

    private TextView tabMenu[] = new TextView[3];

    @ViewInject(R.id.bottom_point_2)
    private TextView bottomPoint2;
    @ViewInject(R.id.lv_slide_menu)
    private ListView mLvLeftMenu;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FgHome fgHome;
    private FgChat fgChat;
    private FgTravel fgTravel;
    private SharedPre sharedPre;

    private ActionBean actionBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            actionBean = (ActionBean) savedInstanceState.getSerializable(Constants.PARAMS_ACTION);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                actionBean = (ActionBean) bundle.getSerializable(Constants.PARAMS_ACTION);
            }
        }
//        setSupportActionBar(toolbar);
        sharedPre = new SharedPre(this);
        initBottomView();
        contentId = R.id.drawer_layout;
        initAdapterContent();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

//        navigationView.setNavigationItemSelectedListener(this);
        //为服务器授权
        grantPhone();
        initLocation();
        grantLocation();
//        addErrorProcess();
//        UpdateResources.checkLocalDB(this);
//        UpdateResources.checkLocalResource(this);
        setUpDrawer();
        connectIM();
        receivePushMessage(getIntent());
        new Thread(new CalaCacheThread()).start();//计算缓存图片大小
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LocationUtils.openGPSSeting(MainActivity.this);
        MLog.e("umengLog" + getDeviceInfo(this));
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (UserEntity.getUser().isLogin(MainActivity.this)) {
                    HttpRequestUtils.request(MainActivity.this, new RequestUserInfo(MainActivity.this), MainActivity.this);
                }
            }
        };
        drawer.addDrawerListener(mDrawerToggle);

        showAdWebView(getIntent().getStringExtra("url"));

        if (actionBean != null) {
            ActionController actionFactory = ActionController.getInstance(this);
            actionFactory.doAction(actionBean);
        }
    }

    private void testPush() {
        String teset  = "{\"action\":\"{\\\"t\\\":\\\"2\\\",\\\"v\\\":\\\"16\\\"}\",\"orderNo\":\"J100091049121\",\"type\":\"G1\",\"orderType\":\"1\",\"sound\":\"newOrder.mp3\"}";
        PushMessage pushMessage = (PushMessage) JsonUtils.fromJson(teset, PushMessage.class);
        pushMessage.title = "";
        pushMessage.message = "您有1个新订单，能收到声音吗,请赶快登录皇包车-司导端APP去接单吧";
        PushUtils.showNotification(pushMessage);
    }

    private void showAdWebView(String url){
        if(null != url) {
            Intent intent = new Intent(activity,WebInfoActivity.class);
            intent.putExtra(WebInfoActivity.WEB_URL, url);
            startActivity(intent);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (actionBean != null) {
            outState.putSerializable(Constants.PARAMS_ACTION, actionBean);
        }
    }


    Timer timer;
    TimerTask timerTask;

    public void uploadLocation() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                String lat = new SharedPre(MainActivity.this).getStringValue("lat");
                String lng = new SharedPre(MainActivity.this).getStringValue("lng");
                Log.e("========", "============lat=" + lat + "====lng=" + lng);

                if (!TextUtils.isEmpty(lat)) {
                    RequestUploadLocation requestUploadLocation = new RequestUploadLocation(MainActivity.this);
                    HttpRequestUtils.request(MainActivity.this, requestUploadLocation, MainActivity.this, false);

                }
            }
        };
        timer.schedule(timerTask, 0, 30000);
    }

    /**
     * 授权获取手机信息权限
     */
    private void grantPhone() {
        MPermissions.requestPermissions(MainActivity.this, PermissionRes.READ_PHONE_STATE, android.Manifest.permission.READ_PHONE_STATE);
    }

    @PermissionGrant(PermissionRes.READ_PHONE_STATE)
    public void requestPhoneSuccess() {
        JPushInterface.setAlias(MainActivity.this, PhoneInfo.getIMEI(this), null);
        uploadPushToken();
    }

    private void uploadPushToken() {
        String imei = PhoneInfo.getIMEI(this);
        RequestPushToken request = new RequestPushToken(this, imei, imei, BuildConfig.VERSION_NAME, imei, PhoneInfo.getSoftwareVersion(this));
        HttpRequestUtils.request(this, request, this);
    }

    @PermissionDenied(PermissionRes.READ_PHONE_STATE)
    public void requestPhoneFailed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.grant_fail_title);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
            dialog.setMessage(R.string.grant_fail_phone1);
        } else {
            dialog.setMessage(R.string.grant_fail_phone);
            dialog.setPositiveButton(R.string.grant_fail_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    grantPhone();
                }
            });
        }
        dialog.setNegativeButton(R.string.grant_fail_btn_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        dialog.show();
    }

    private void uploadPushClick(String pushId) {
        RequestPushClick request = new RequestPushClick(this, pushId);
        HttpRequestUtils.request(this, request, this);
    }

    private void initAdapterContent() {
        fgHome = new FgHome();
        fgChat = new FgChat();
        fgTravel = new FgTravel();
        addFragment(fgHome);
        addFragment(fgChat);
        addFragment(fgTravel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestPushToken) {
            MLog.e(request.getData().toString());
        } else if (request instanceof RequestUploadLocation) {
            LocationUtils.cleanLocationInfo(MainActivity.this);
            String cityId = ((RequestUploadLocation) request).getData().cityId;
            String cityName = ((RequestUploadLocation) request).getData().cityName;
            String countryId = ((RequestUploadLocation) request).getData().countryId;
            String countryName = ((RequestUploadLocation) request).getData().countryName;
            LocationUtils.saveLocationCity(MainActivity.this, cityId, cityName, countryId, countryName);
//            MLog.e("Location: cityId:"+cityId + ",  cityName:"+cityName);
        } else if (request instanceof RequestUserInfo) {
            if (couponTV == null || travelFundTV == null) {
                return;
            }
            RequestUserInfo mRequest = (RequestUserInfo) request;
            UserBean user = mRequest.getData();
            UserEntity.getUser().setNickname(this, user.nickname);
            UserEntity.getUser().setAvatar(this, user.avatar);
            UserEntity.getUser().setUserName(this, user.name);
            UserEntity.getUser().setTravelFund(this, user.travelFund);
            UserEntity.getUser().setCoupons(this, user.coupons);
            couponTV.setText("" + user.coupons);
            travelFundTV.setText("" + user.travelFund);
            couponUnitTV.setText("张");
            travelFundUnitTV.setText("元");
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        MLog.e(errorInfo == null ? "" : errorInfo.toString());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        receivePushMessage(intent);
    }

    private void receivePushMessage(Intent intent) {
        if (intent != null) {
            if (intent.getData() != null && "rong".equals(intent.getData().getScheme())) {
                Intent intentIm = new Intent(this, IMChatActivity.class);
                intentIm.putExtra(IMChatActivity.KEY_TITLE, intent.getData().toString());
                startActivity(intentIm);
            } else {
                PushMessage message = (PushMessage) intent.getSerializableExtra(MainActivity.PUSH_BUNDLE_MSG);
                if (message != null) {
                    uploadPushClick(message.messageID);
                    ActionBean actionBean = message.getActionBean();
                    if (actionBean != null) {
                        ActionController actionFactory = ActionController.getInstance(this);
                        actionFactory.doAction(actionBean);
                        this.actionBean = actionBean;
                    } else {
                        if ("IM".equals(message.type)) {
                            gotoChatList();
                        } else if ("888".equals(message.orderType)) {
                            if (getFragmentList().size() > 3) {
                                for (int i = getFragmentList().size() - 1; i >= 3; i--) {
                                    getFragmentList().get(i).finish();
                                }
                            }
                            if (mViewPager != null) {
                                mViewPager.setCurrentItem(2);
                            }
                        } else {
                            gotoOrder(message);
                        }
                    }
                }
            }
        }
    }

    private void gotoChatList() {
        //如果是收到消息推送 关了上层的页面
        if (getFragmentList().size() > 3) {
            for (int i = getFragmentList().size() - 1; i >= 3; i--) {
                getFragmentList().get(i).finish();
            }
        }
        //跳转到聊天列表
        mViewPager.setCurrentItem(1);
    }

    private void gotoOrder(PushMessage message) {
//        Bundle bundle = new Bundle();
//        bundle.putInt(BaseFragment.KEY_BUSINESS_TYPE, message.orderType);
//        bundle.putInt(BaseFragment.KEY_GOODS_TYPE, message.goodsType);
//        bundle.putString(FgOrder.KEY_ORDER_ID, message.orderID);
//        startFragment(new FgOrder(), bundle);
        OrderDetailActivity.Params params = new OrderDetailActivity.Params();
        params.orderType = CommonUtils.getCountInteger(message.orderType);
        params.orderId = message.orderNo;

        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
//                getUserCoupon();
                if (actionBean != null) {
                    ActionController actionFactory = ActionController.getInstance(this);
                    actionFactory.doAction(actionBean);
                    actionBean = null;
                }
            case CLICK_USER_LOOUT:
                refreshContent();
                break;
            case SET_MAIN_PAGE_INDEX:
                int index = Integer.valueOf(action.data.toString());
                if (index >= 0 && index < 3)
                    mViewPager.setCurrentItem(index);
                break;
            case ONBACKPRESS:
            case CLICK_HEADER_LEFT_BTN_BACK:
                if (getFragmentsSize() == mSectionsPagerAdapter.getCount()) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //打开
                }
                break;
            case START_NEW_FRAGMENT:
                if (getFragmentsSize() > mSectionsPagerAdapter.getCount()) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
                }
                break;
            default:
                break;
        }
    }

    private void connectIM() {
        if (UserEntity.getUser().isLogin(this))
            IMUtil.getInstance().connect();
    }

    private void initBottomView() {
        tabMenu[0] = (TextView) findViewById(R.id.tab_text_1);
        tabMenu[1] = (TextView) findViewById(R.id.tab_text_2);
        tabMenu[2] = (TextView) findViewById(R.id.tab_text_3);
        tabMenu[0].setSelected(true);
    }


//    private List<LvMenuItem> mItems = new ArrayList<LvMenuItem>(
//            Arrays.asList(
//                    new LvMenuItem(R.mipmap.personal_center_coupon, "优惠券", ""),
//                    new LvMenuItem(R.mipmap.personal_icon_br, "常用投保人", ""),
//                    new LvMenuItem(R.mipmap.personal_icon_hd, "活动", ""),
//                    new LvMenuItem(R.mipmap.personal_center_setting, "设置", ""),
//                    new LvMenuItem(R.mipmap.personal_center_customer_service, "客服中心", "我们的服务介绍和保障"),
//                    new LvMenuItem(R.mipmap.personal_center_internal, "境内客服", "仅限国内使用"),
//                    new LvMenuItem(R.mipmap.personal_center_overseas, "境外客服", "仅限国外使用")
//            ));

    private List<LvMenuItem> mItems = new ArrayList<LvMenuItem>(
            Arrays.asList(
                    new LvMenuItem(R.mipmap.personal_icon_safe, "常用投保人"),
                    new LvMenuItem(R.mipmap.personal_icon_collection, "我收藏的司导"),
                    new LvMenuItem(MenuItemAdapter.ItemType.SPACE),
                    new LvMenuItem(R.mipmap.personal_icon_activity, "活动"),
                    new LvMenuItem(MenuItemAdapter.ItemType.SPACE),
                    new LvMenuItem(R.mipmap.personal_icon_service, "服务规则"),
                    new LvMenuItem(R.mipmap.personal_icon_call, "联系境内客服", MenuItemAdapter.ItemType.SERVICE),
                    new LvMenuItem(R.mipmap.personal_icon_call, "联系境外客服", MenuItemAdapter.ItemType.SERVICE),
                    new LvMenuItem(MenuItemAdapter.ItemType.SPACE),
                    new LvMenuItem(R.mipmap.personal_icon_install, "设置")
            ));

    MenuItemAdapter menuItemAdapter;

    private void setUpDrawer() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View header = inflater.inflate(R.layout.nav_header_main, null);
        RelativeLayout head_view = (RelativeLayout) header.findViewById(R.id.head_view);
        head_view.setOnClickListener(this);
        my_icon_head = (PolygonImageView) header.findViewById(R.id.my_icon_head);//头像
        my_icon_head.setOnClickListener(this);
        tv_nickname = (TextView) header.findViewById(R.id.tv_nickname);//昵称
        tv_nickname.setOnClickListener(this);
        couponTV = (TextView) header.findViewById(R.id.slidemenu_header_coupon_tv);//优惠券
        travelFundTV = (TextView) header.findViewById(R.id.slidemenu_header_travelfund_tv);//旅游基金
        couponUnitTV = (TextView) header.findViewById(R.id.slidemenu_header_coupon_unit_tv);
        travelFundUnitTV = (TextView) header.findViewById(R.id.slidemenu_header_travelfund_unit_tv);
        travelFundHintIV = (ImageView) header.findViewById(R.id.travel_fund_hint_iv);
        if (new SharedPre(this).isShowTravelFundHint()) {
            travelFundHintIV.setVisibility(View.VISIBLE);
        } else {
            travelFundHintIV.setVisibility(View.GONE);
        }

        header.findViewById(R.id.slidemenu_header_coupon_layout).setOnClickListener(this);
        header.findViewById(R.id.slidemenu_header_travelfund_layout).setOnClickListener(this);
        tv_nickname.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CommonUtils.showToast("version=" + ChannelUtils.getVersion() + " versioncode=" + ChannelUtils.getVersionCode() + " channel =" + ChannelUtils.getChannel(MainActivity.this) + "");
                return false;
            }
        });

        mLvLeftMenu.addHeaderView(header);
        menuItemAdapter = new MenuItemAdapter(this, mItems);
        mLvLeftMenu.setAdapter(menuItemAdapter);
        mLvLeftMenu.setOnItemClickListener(this);
        refreshContent();
    }

    /**
     * 刷新左边侧滑栏
     */
    private void refreshContent() {
        if (!UserEntity.getUser().isLogin(this)) {
            my_icon_head.setImageResource(R.mipmap.chat_head);
            tv_nickname.setText(this.getResources().getString(R.string.person_center_nickname));
            menuItemAdapter.notifyDataSetChanged();
            couponTV.setText("");
            travelFundTV.setText("");
            couponUnitTV.setText("--");
            travelFundUnitTV.setText("--");
            tv_nickname.setTextColor(0xFF999999);
        } else {
            if (!TextUtils.isEmpty(UserEntity.getUser().getAvatar(this))) {
                Tools.showImage(this, my_icon_head, UserEntity.getUser().getAvatar(this));
//                x.image().bind(my_icon_head, UserEntity.getUser().getAvatar(this));
            } else {
                my_icon_head.setImageResource(R.mipmap.chat_head);
            }
            tv_nickname.setTextColor(0xFF3c3731);
            if (!TextUtils.isEmpty(UserEntity.getUser().getNickname(this))) {
                tv_nickname.setText(UserEntity.getUser().getNickname(this));
            } else {
                tv_nickname.setText(this.getResources().getString(R.string.person_center_no_nickname));
            }
            couponTV.setText("" + UserEntity.getUser().getCoupons(this));
            travelFundTV.setText("" + UserEntity.getUser().getTravelFund(this));
            couponUnitTV.setText("张");
            travelFundUnitTV.setText("元");

        }
    }

    /**
     * 打开左侧菜单
     */
    public void openDrawer() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        }
        MLog.e(" openDrawer ");
    }

    private long exitTime;

    @Override
    public void onBackPressed() {
        if (getFragmentList().size() > mSectionsPagerAdapter.getCount()) {
            doFragmentBack();
        } else if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0);
        } else {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                long times = System.currentTimeMillis();
                if ((times - exitTime) > 2000) {
                    CommonUtils.showToast("再次点击退出");
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
        }
    }


    @Override
    public int getContentId() {
        return contentId;
    }


    @Event({R.id.tab_text_1, R.id.tab_text_2, R.id.tab_text_3})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tab_text_1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tab_text_2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tab_text_3:
                mViewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        MLog.e("onPageSelected = " + position);
        for (int i = 0; i < tabMenu.length; i++) {
            tabMenu[i].setSelected(position == i);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map = new HashMap<String, String>();
        switch (position) {
            case Constants.PERSONAL_CENTER_BR://常用投保人
                if (isLogin()) {
                    Intent intent = new Intent(activity, InsureActivity.class);
                    startActivity(intent);
                }
                break;
            case Constants.PERSONAL_CENTER_COLLECT://收藏司导
                if (isLogin()) {
                    startActivity(new Intent(MainActivity.this, CollectGuideListActivity.class));
                }
                break;
            case Constants.PERSONAL_CENTER_HD://活动
                    Intent intent = new Intent(MainActivity.this, WebInfoActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_ACTIVITY + UserEntity.getUser().getUserId(this.getApplicationContext()) + "&t=" + new Random().nextInt(100000));
                    startActivity(intent);
                break;
            case Constants.PERSONAL_CENTER_CUSTOMER_SERVICE://服务规则
                intent = new Intent(activity, ServicerCenterActivity.class);
                startActivity(intent);
                break;
            case Constants.PERSONAL_CENTER_INTERNAL_SERVICE://境内客服
                PhoneInfo.CallDial(MainActivity.this, Constants.CALL_NUMBER_IN);
                break;
            case Constants.PERSONAL_CENTER_OVERSEAS_SERVICE://境外客服
                PhoneInfo.CallDial(MainActivity.this, Constants.CALL_NUMBER_OUT);
                break;
            case Constants.PERSONAL_CENTER_SETTING://设置
                if (isLogin()) {
                    intent = new Intent(activity,SettingActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);


    }

    //通讯录
    private final int PICK_CONTACTS = 101;

    // 接收通讯录的选择号码事件
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            if (PICK_CONTACTS == requestCode) {
                Uri result = data.getData();
                String[] contact = PhoneInfo.getPhoneContacts(this, result);
                EventBus.getDefault().post(new EventAction(EventType.CONTACT, contact));
            }
        }
    }


    /**
     * 判断是否登录
     */
    private boolean isLogin() {
        if (UserEntity.getUser().isLogin(this)) {
            return true;
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            return false;
        }
    }
    Intent intent;
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.head_view:
            case R.id.my_icon_head:
            case R.id.tv_nickname:
                if (isLogin()) {
                    intent = new Intent(this, PersonInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.slidemenu_header_coupon_layout://我的优惠券
                if (isLogin()) {
                    intent = new Intent(activity, CouponActivity.class);
                    startActivity(intent);
                    UserEntity.getUser().setHasNewCoupon(false);
                }
                break;
            case R.id.slidemenu_header_travelfund_layout://旅游基金
                if (isLogin()) {
                    SharedPre sharedPre= new SharedPre(this);
                    if (sharedPre.isShowTravelFundHint()) {
                        sharedPre.setTravelFundHintIsShow(false);
                        travelFundHintIV.setVisibility(View.GONE);
                    }
                    intent = new Intent(activity, TravelFundActivity.class);
                    startActivity(intent);
                }
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return fgHome;
                }
                case 1: {
                    return fgChat;
                }
                case 2: {
                    return fgTravel;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "发现";
                case 1:
                    return "私聊";
                case 2:
                    return "行程";
            }
            return null;
        }
    }

    public void setIMCount(int count) {
        if (count > 0) {
            if (count > 99) {
                bottomPoint2.setText("99+");
            } else {
                bottomPoint2.setText("" + count);
            }
            bottomPoint2.setVisibility(View.VISIBLE);

        } else {
            bottomPoint2.setVisibility(View.GONE);
            bottomPoint2.setText("");
        }

    }

    public void restartApp() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent restartIntent = PendingIntent.getActivity(
                this.getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //退出程序 重启应用
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); //  重启应用
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_ACCESS_COARSE_LOCATION:
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    MLog.e("==========PERMISSION_GRANTED=========");
                    requestLocation();
                } else {
                    // permission denied
                    MLog.e("==========denied=========");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void exitApp() {
        restartApp();
        super.exitApp();
    }

    class CalaCacheThread implements Runnable {
        public void run() {
            long cacheSize = calculateCacheFileSize();
            sharedPre.saveLongValue(SharedPre.CACHE_SIZE, cacheSize);
        }
    }

    private long calculateCacheFileSize() {
        long length = 0L;
        String DISK_CACHE_DIR_NAME = "xUtils_img"; //1
        String CACHE_DIR_NAME = "xUtils_cache";    //2

        File cacheDir1 = FileUtil.getCacheDir(DISK_CACHE_DIR_NAME);
        File cacheDir2 = FileUtil.getCacheDir(CACHE_DIR_NAME);
        if (cacheDir1 != null) {
            length += FileUtil.getFileOrDirSize(cacheDir1);
        }
        if (cacheDir2 != null) {
            length += FileUtil.getFileOrDirSize(cacheDir2);
        }
        return length;
    }

    public void grantLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        } else {
            requestLocation();
        }
    }


    public void requestLocation() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListener);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    LocationManager locationManager;
    LocationListener locationListener;
    public void initLocation(){
        if(!LocationUtils.gpsIsOpen(this)){
            AlertDialog dialog = AlertDialogUtils.showAlertDialog(this, "没有开启GPS定位,请到设置里开启", "设置", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LocationUtils.openGPSSeting(MainActivity.this);
                    dialog.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String locStr =String.format("%s\n lat=%f \n lng=%f \n(%f meters)", location.getProvider(),
                                location.getLatitude(), location.getLongitude(), location.getAccuracy());

                LocationUtils.saveLocationInfo(MainActivity.this,location.getLatitude()+"",location.getLongitude()+"");
                if(timer == null) {
                    uploadLocation();
                }
//                MLog.e(locStr);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                LocationUtils.cleanLocationInfo(MainActivity.this);
            }
        };

    }



    @SuppressLint("NewApi")
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
