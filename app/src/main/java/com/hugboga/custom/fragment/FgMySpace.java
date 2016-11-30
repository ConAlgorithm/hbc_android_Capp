package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CollectGuideListActivity;
import com.hugboga.custom.activity.CouponActivity;
import com.hugboga.custom.activity.InsureActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.PersonInfoActivity;
import com.hugboga.custom.activity.ServicerCenterActivity;
import com.hugboga.custom.activity.SettingActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.adapter.MenuItemAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.LvMenuItem;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestUserInfo;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by on 16/10/13.
 */
@ContentView(R.layout.fg_myspace)
public class FgMySpace extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener{

    @ViewInject(R.id.header_left_btn)
    private ImageView leftBtn;
    @Bind(R.id.fg_space_listview)
    ListView listView;

    private PolygonImageView my_icon_head;//header的头像
    private TextView tv_nickname;//header的昵称
    private TextView couponTV, couponUnitTV;
    private TextView travelFundTV, travelFundUnitTV;
    private ImageView travelFundHintIV;
    private ImageView headerBgIV;

    private MenuItemAdapter menuItemAdapter;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOOUT:
            case CLICK_USER_LOGIN:
                refreshContent();
                break;
        }
    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        fgTitle.setLayoutParams(titleParams);
        fgTitle.setText("我");
        leftBtn.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View header = inflater.inflate(R.layout.nav_header_main, null);
        RelativeLayout head_view = (RelativeLayout) header.findViewById(R.id.head_view);
        head_view.setOnClickListener(this);

        headerBgIV = (ImageView) header.findViewById(R.id.nav_header_bg_iv);//头像
        my_icon_head = (PolygonImageView) header.findViewById(R.id.my_icon_head);//头像
        my_icon_head.setOnClickListener(this);
        tv_nickname = (TextView) header.findViewById(R.id.tv_nickname);//昵称
        tv_nickname.setOnClickListener(this);
        couponTV = (TextView) header.findViewById(R.id.slidemenu_header_coupon_tv);//优惠券
        travelFundTV = (TextView) header.findViewById(R.id.slidemenu_header_travelfund_tv);//旅游基金
        couponUnitTV = (TextView) header.findViewById(R.id.slidemenu_header_coupon_unit_tv);
        travelFundUnitTV = (TextView) header.findViewById(R.id.slidemenu_header_travelfund_unit_tv);
        travelFundHintIV = (ImageView) header.findViewById(R.id.travel_fund_hint_iv);
        if (new SharedPre(getContext()).isShowTravelFundHint()) {
            travelFundHintIV.setVisibility(View.VISIBLE);
        } else {
            travelFundHintIV.setVisibility(View.GONE);
        }
        header.findViewById(R.id.slidemenu_header_coupon_layout).setOnClickListener(this);
        header.findViewById(R.id.slidemenu_header_travelfund_layout).setOnClickListener(this);
        tv_nickname.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CommonUtils.showToast("version=" + ChannelUtils.getVersion() + " versioncode=" + ChannelUtils.getVersionCode() + " channel =" + ChannelUtils.getChannel(getContext()) + "");
                return false;
            }
        });

        listView.addHeaderView(header);

        menuItemAdapter = new MenuItemAdapter(getContext(), mItems);
        listView.setAdapter(menuItemAdapter);
        listView.setOnItemClickListener(this);

        refreshContent();

        setSensorsDefaultEvent("个人中心", SensorsConstant.USERCENTER);
    }

    public void refreshUserInfo() {
        if (UserEntity.getUser().isLogin(getContext())) {
            HttpRequestUtils.request(getContext(), new RequestUserInfo(getContext()), this, false);
        }
    }

    /**
     * 刷新左边侧滑栏
     */
    private void refreshContent() {
        if (!UserEntity.getUser().isLogin(getContext())) {
            my_icon_head.setImageResource(R.mipmap.chat_head);
            headerBgIV.setImageResource(R.mipmap.personal_bg);
            tv_nickname.setText(this.getResources().getString(R.string.person_center_nickname));
            menuItemAdapter.notifyDataSetChanged();
            couponTV.setText("");
            travelFundTV.setText("");
            couponUnitTV.setText("--");
            travelFundUnitTV.setText("--");
            tv_nickname.setTextColor(0xFF999999);
        } else {
            String avatar = UserEntity.getUser().getAvatar(getContext());
            if (!TextUtils.isEmpty(avatar)) {
                Tools.showImage(my_icon_head, avatar, R.mipmap.chat_head);
//                Tools.showBlurryImage(headerBgIV, avatar, R.mipmap.personal_bg, 8, 3);
                headerBgIV.setImageResource(R.mipmap.personal_bg);
            } else {
                headerBgIV.setImageResource(R.mipmap.personal_bg);
                my_icon_head.setImageResource(R.mipmap.chat_head);
            }
            tv_nickname.setTextColor(0xFF3c3731);
            if (!TextUtils.isEmpty(UserEntity.getUser().getNickname(getContext()))) {
                tv_nickname.setText(UserEntity.getUser().getNickname(getContext()));
            } else {
                tv_nickname.setText(this.getResources().getString(R.string.person_center_no_nickname));
            }
            couponTV.setText("" + UserEntity.getUser().getCoupons(getContext()));
            travelFundTV.setText("" + UserEntity.getUser().getTravelFund(getContext()));
            couponUnitTV.setText("张");
            travelFundUnitTV.setText("元");

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map = new HashMap<String, String>();
        switch (position) {
            case Constants.PERSONAL_CENTER_BR://常用投保人
                if (isLogin("个人中心-常用投保人")) {
                    Intent intent = new Intent(getContext(), InsureActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                }
                break;
            case Constants.PERSONAL_CENTER_COLLECT://收藏司导
                if (isLogin("个人中心-收藏司导")) {
                    Intent intent = new Intent(getContext(), CollectGuideListActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                }
                break;
            case Constants.PERSONAL_CENTER_HD://活动
                MobClickUtils.onEvent(StatisticConstant.LAUNCH_ACTLIST);
                Intent intent = new Intent(getContext(), WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_ACTIVITY + UserEntity.getUser().getUserId(getContext()) + "&t=" + new Random().nextInt(100000));
                startActivity(intent);
                setSensorsDefaultEvent("活动列表", SensorsConstant.ACTLIST);
                break;
            case Constants.PERSONAL_CENTER_CUSTOMER_SERVICE://服务规则
                intent = new Intent(getContext(), ServicerCenterActivity.class);
                startActivity(intent);
                break;
            case Constants.PERSONAL_CENTER_INTERNAL_SERVICE://境内客服
                PhoneInfo.CallDial(getContext(), Constants.CALL_NUMBER_IN);
                break;
            case Constants.PERSONAL_CENTER_OVERSEAS_SERVICE://境外客服
                PhoneInfo.CallDial(getContext(), Constants.CALL_NUMBER_OUT);
                break;
            case Constants.PERSONAL_CENTER_SETTING://设置
                if (isLogin("个人中心-设置")) {
                    intent = new Intent(getContext(),SettingActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
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
                if (isLogin("个人中心-用户信息")) {
                    intent = new Intent(getContext(), PersonInfoActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                }
                break;
            case R.id.slidemenu_header_coupon_layout://我的优惠券
                if (isLogin("个人中心-优惠券")) {
                    intent = new Intent(getContext(), CouponActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                    UserEntity.getUser().setHasNewCoupon(false);
                }
                break;
            case R.id.slidemenu_header_travelfund_layout://旅游基金
                if (isLogin("个人中心-旅游基金")) {
                    SharedPre sharedPre= new SharedPre(getContext());
                    if (sharedPre.isShowTravelFundHint()) {
                        sharedPre.setTravelFundHintIsShow(false);
                        travelFundHintIV.setVisibility(View.GONE);
                    }
                    intent = new Intent(getContext(), TravelFundActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                    StatisticClickEvent.click(StatisticConstant.LAUNCH_TRAVELFOUND, "个人中心");
                    MobClickUtils.onEvent(StatisticConstant.CLICK_TRAVELFOUND_WD);
                }
                break;
        }
    }

    /**
     * 判断是否登录
     */
    private boolean isLogin(String source) {
        if (UserEntity.getUser().isLogin(getContext())) {
            return true;
        } else {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
            getContext().startActivity(intent);
            return false;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestUserInfo) {
            if (couponTV == null || travelFundTV == null) {
                return;
            }
            RequestUserInfo mRequest = (RequestUserInfo) request;
            UserBean user = mRequest.getData();
            UserEntity.getUser().setNickname(getContext(), user.nickname);
            UserEntity.getUser().setAvatar(getContext(), user.avatar);
            UserEntity.getUser().setUserName(getContext(), user.name);
            UserEntity.getUser().setTravelFund(getContext(), user.travelFund);
            UserEntity.getUser().setCoupons(getContext(), user.coupons);
            couponTV.setText("" + user.coupons);
            travelFundTV.setText("" + user.travelFund);
            couponUnitTV.setText("张");
            travelFundUnitTV.setText("元");
        }
    }

    @Override
    public String getEventSource() {
        return "个人中心";
    }
}
