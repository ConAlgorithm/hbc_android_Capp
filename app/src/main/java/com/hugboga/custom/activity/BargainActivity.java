package com.hugboga.custom.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.BarginBean;
import com.hugboga.custom.data.bean.BarginWebchatList;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestBargin;
import com.hugboga.custom.data.request.RequestChangeUserInfo;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.BargainShareDialog;
import com.hugboga.custom.widget.CountDownLayout;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created on 16/9/6.
 * 砍价
 */

public class BargainActivity extends BaseActivity {

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;

    @Bind(R.id.countdown)
    CountDownLayout countdown;
    @Bind(R.id.cut_money)
    TextView cutMoney;
    @Bind(R.id.list_layout)
    LinearLayout listLayout;
    @Bind(R.id.cute_money_tv)
    TextView cuteMoneyTv;
    @Bind(R.id.rule)
    TextView rule;

    @Bind(R.id.cute_hint_tv1)
    TextView cuteHintTv1;
    @Bind(R.id.cute_hint_tv2)
    TextView cuteHintTv2;
    @Bind(R.id.cute_money_multiple_tv)
    TextView multipleTv;
    @Bind(R.id.countdown_parent_layout)
    RelativeLayout countdownParentLayout;
    @Bind(R.id.people_num_tv)
    TextView peopleNumTv;

    private BarginBean barginBean;
    private double orderBargainAmount;

    @Override
    public int getContentViewId() {
        return R.layout.activity_bargain;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initView();
        getIntentValue();
        getData();
        EventBus.getDefault().register(this);
        StatisticClickEvent.click(StatisticConstant.LAUNCH_KANJIA,"订单详情");
        setSensorsEvent();
    }

    private void getIntentValue(){
        orderNo = getIntent().getStringExtra("orderNo");
    }

    private void setSensorsEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_web_title", "砍价页");
            properties.put("hbc_web_url", SensorsConstant.KANJIA + "?order_id=" + orderNo);
            properties.put("hbc_refer", getIntentSource());
            SensorsDataAPI.sharedInstance(this).track("page_view", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String orderNo = "R122621569604";
    private String sign = "C9B525F3D472BD7094CEF8EA5028500F";
    private int limit = 5;
    private int offset = 0;
    private int bargainTotal = 0;
    private String userName = null;
    private void getData() {
        final RequestBargin requestBargin = new RequestBargin(activity, orderNo, limit, offset);
        HttpRequestUtils.request(activity, requestBargin, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                barginBean = ((RequestBargin) request).getData();
                genView(barginBean);
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                LogUtil.e("===", errorInfo.toString());
            }
        });
    }

    //是否是加载更多
    boolean loadMore = false;
    private String shareTitle = "";


    private void genView(BarginBean barginBean) {
        if (null != barginBean) {
            userName = barginBean.userName;
            shareTitle = String.format(getString(R.string.share_bargin_title),barginBean.cnstr);
            bargainTotal = barginBean.bargainTotal;
            cuteMoneyTv.setText("" + barginBean.bargainVirtualAmount);
            multipleTv.setText(String.format("(%1$s倍)", barginBean.multiple <= 1 ? "X1" : "已上浮" + barginBean.multiple));

            if(null != barginBean.bargainWechatRspList && barginBean.bargainWechatRspList.size() > 0) {
                offset += limit;
                if(loadMore){
                    addMoreListView(barginBean);
                }else {
                    genListView(barginBean);
                }
                if(offset >=  bargainTotal){
                    if(null != bottom) {
                        bottom.setText(R.string.no_more);
                        bottom.setOnClickListener(null);
                    }
                }
                peopleNumTv.setText(String.format("（共%1$s人）", bargainTotal));
            } else {
                peopleNumTv.setText("");
            }
            setTimerData(barginBean, barginBean.isStart);
        }

    }

    int isStart = -1;
    private void setTimerData(BarginBean barginBean, int _isStart) {
        if (_isStart == isStart) {
            return;
        }
        isStart = _isStart;
        second = barginBean.seconds;

        if (isStart == 2 || 0 == second) {//已结束
            countdown.changeTime(0);
            cutMoney.setBackgroundResource(R.drawable.shape_rounded_gray_all);
            cutMoney.setText("砍价已完成");
            cutMoney.setOnClickListener(null);
        } else {
            countdown.changeTime(second);
            initimer();
            if (isStart == 1) {
                countDownTimer.start();
            }
        }
    }


    private void initimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(second * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (countdown != null) {
                    countdown.changeTime((int) millisUntilFinished / 1000);
//                    LogUtil.e("===", "" + (int) millisUntilFinished / 1000);
                }
            }

            @Override
            public void onFinish() {
                if (countdown != null && cutMoney != null) {
//                    LogUtil.e("===", "done");
                    countdown.changeTime(0);
                    cutMoney.setBackgroundResource(R.drawable.shape_rounded_gray_all);
                    cutMoney.setText("砍价已完成");
                    cutMoney.setOnClickListener(null);
                }
            }
        };
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case WECHAT_SHARE_SUCCEED:
                getData();
                StatisticClickEvent.clickShare(StatisticConstant.SHAREKJ_BACK,shareType == 1?"微信好友":"朋友圈");
                break;
        }
    }

    int shareType = 1;
    LayoutInflater inflater;
    TextView bottom;
    private void addBottom(){
        bottom = new TextView(activity);
        bottom.setText(R.string.show_more);
        bottom.setTextSize(15);
        bottom.setHeight(UIUtils.dip2px(60));
        bottom.setGravity(Gravity.CENTER);
        bottom.setTextColor(ContextCompat.getColor(activity, R.color.basic_black));
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offset < bargainTotal) {
                    loadMore = true;
                    getData();
                }
            }
        });
        listLayout.addView(bottom);
    }

    private void genWebchatListView(BarginBean barginBean){
        View view;
        TextView name, time, money;
        PolygonImageView head;
        List<BarginWebchatList> bargainWechatRspList = barginBean.bargainWechatRspList;
        if(null != barginBean.bargainWechatRspList) {
            for (BarginWebchatList barginWebchat : bargainWechatRspList) {
                view = inflater.inflate(R.layout.bargin_list_item, null);
                head = (PolygonImageView) view.findViewById(R.id.head);
                name = (TextView) view.findViewById(R.id.name);
                time = (TextView) view.findViewById(R.id.time);
                money = (TextView) view.findViewById(R.id.money);
                Tools.showImage(head, barginWebchat.wechatPic, R.mipmap.icon_avatar_user);
                name.setText(barginWebchat.wechatNickname);
                time.setText(barginWebchat.bargTime);
                money.setText("-" + barginWebchat.bargAmount + "元");
                listLayout.addView(view);
            }
        }
    }

    private void genListView(BarginBean barginBean) {
        listLayout.removeAllViews();
        inflater = LayoutInflater.from(activity);
        genWebchatListView(barginBean);
        addBottom();
    }

    private void addMoreListView(BarginBean barginBean){
        listLayout.removeView(bottom);
        genWebchatListView(barginBean);
        addBottom();
    }

    int second = 0;
    CountDownTimer countDownTimer;

    private void initView() {
        initDefaultTitleBar();
        fgTitle.setText(R.string.bargain_title);
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefreshOrder();
                finish();
            }
        });
        countdown.changeTime(second);

        rule.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        rule.getPaint().setAntiAlias(true);
        rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_RULES);
                startActivity(intent);
            }
        });

        int countdownParentLayoutHeight = (int) (787 / 750.0f * UIUtils.getScreenWidth());
        int paddingButtom = (int) (18 / 750.0f * UIUtils.getScreenWidth());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, countdownParentLayoutHeight);
        countdownParentLayout.setLayoutParams(params);
        countdownParentLayout.setPadding(0, 0, 0, paddingButtom);

        setHintText(cuteHintTv1, "砍价人数达到15人以上，总金额X1.5");
        setHintText(cuteHintTv2, "砍价人数达到30人以上，总金额X2.0");

        orderBargainAmount = getIntent().getDoubleExtra("bargainAmount", 0);

        userName = UserEntity.getUser().getUserName(this);
    }

    private void setHintText(TextView textView, String hint) {
        SpannableString hintSpan = new SpannableString(hint);
        hintSpan.setSpan(new ForegroundColorSpan(0xFFCC0001), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        hintSpan.setSpan(new ForegroundColorSpan(0xFFCC0001), 16, hint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(hintSpan);
    }


    private View popupView;
    PopupWindow popupWindow;
    EditText nameEdit;
    private void showAddName(){
        popupView = LayoutInflater.from(activity).inflate(R.layout.bargain_add_name_layout,null);
        popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        nameEdit = (EditText)popupView.findViewById(R.id.real_name);
        popupView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.hideSoftInputMethod(nameEdit);
                popupWindow.dismiss();
            }
        });
        popupView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameEdit.getText()) || TextUtils.isEmpty(nameEdit.getText().toString().trim())){
                    CommonUtils.showToast(R.string.real_name);
                    return;
                }
                String name = nameEdit.getText().toString();
                for(int i = 0;i< name.length();i++) {
                    if (!Tools.isEmojiCharacter(name.charAt(i))) {
                        CommonUtils.showToast("真实姓名不能包含表情符号");
                        return;
                    }
                }
                name = name.replaceAll(" ", "");
                final String requestName = name;
                //真实姓名
                RequestChangeUserInfo request = new RequestChangeUserInfo(activity, null, null, null, null, null, name);
                HttpRequestUtils.request(activity, request, new HttpRequestListener() {
                    @Override
                    public void onDataRequestSucceed(BaseRequest request) {
                        ApiReportHelper.getInstance().addReport(request);
                        UserEntity.getUser().setUserName(activity, requestName);
                        CommonUtils.hideSoftInputMethod(nameEdit);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onDataRequestCancel(BaseRequest request) {

                    }

                    @Override
                    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                        if (popupWindow != null) {
                            CommonUtils.hideSoftInputMethod(nameEdit);
                            popupWindow.dismiss();
                        }
                    }
                });
            }
        });
        popupWindow.showAsDropDown(headerLeftBtn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(null != countDownTimer) {
            countDownTimer.cancel();
        }
    }

    @Override
    public String getIntentSource() {
        return super.getIntentSource();
    }

    @Override
    protected void initDefaultTitleBar() {
        super.initDefaultTitleBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public String getEventId() {
        return super.getEventId();
    }

    @Override
    public String getEventSource() {
       return  "砍价";
    }

    //是否显示过填写姓名popup
    boolean isShowAddNamePopup = false;

    @OnClick(R.id.cut_money)
    public void onClick() {
        StatisticClickEvent.click(StatisticConstant.CLICK_KANJIA,"订单详情");
        if(TextUtils.isEmpty(userName)) {
            if(!isShowAddNamePopup){
                isShowAddNamePopup = true;
                showAddName();
            } else {
                showShareDialog();
            }
        } else {
            showShareDialog();
        }
    }

    private void showShareDialog() {
        BargainShareDialog shareDialog = new BargainShareDialog(activity);
        shareDialog.setData(shareTitle, orderNo, getEventSource());
        shareDialog.setOnStartBargainListener(new BargainShareDialog.OnStartBargainListener() {
            @Override
            public void onStartBargain() {
                if (barginBean.isStart == 0) {
                    barginBean.isStart = 1;
                    genView(barginBean);
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE, orderNo));
                }
            }
        });
        shareDialog.show();
    }

    public void onRefreshOrder() {
        if (barginBean != null && barginBean.bargainVirtualAmount != orderBargainAmount) {
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE, orderNo));
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            onRefreshOrder();
        }
        return super.onKeyUp(keyCode, event);
    }
}
