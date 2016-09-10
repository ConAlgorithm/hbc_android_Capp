package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestBargainShare;
import com.hugboga.custom.data.request.RequestBargin;
import com.hugboga.custom.data.request.RequestChangeUserInfo;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CountDownLayout;
import com.hugboga.custom.widget.ShareDialog;
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    ImageView cutMoney;
    @Bind(R.id.list_layout)
    LinearLayout listLayout;
    @Bind(R.id.cute_money_tv)
    TextView cuteMoneyTv;
    @Bind(R.id.rule)
    TextView rule;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_bargain);
        ButterKnife.bind(this);
        initView();
        getIntentValue();
        getData();
    }

    private void getIntentValue(){
        orderNo = getIntent().getStringExtra("orderNo");
    }

    private String orderNo = "J190706533863";
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
                BarginBean barginBean = ((RequestBargin) request).getData();
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
            cuteMoneyTv.setText(barginBean.bargainAmount);
            second = barginBean.seconds;
            if(barginBean.bargainWechatRspList.size() > 0) {
                if (0 != second) {
                    countDownTimer.start();
                } else {
                    countdown.changeTime(0);
                    cutMoney.setImageResource(R.mipmap.cut_end);
                    cutMoney.setOnClickListener(null);
                }
            }

            offset += limit;
            if(loadMore){
                addMoreListView(barginBean);
            }else {
                genListView(barginBean);
            }
            if(offset >=  bargainTotal){
                bottom.setText(R.string.no_more);
                bottom.setOnClickListener(null);
            }
        }

    }

    //显示分享界面
    private void barginShare(int picture, final String title, final String content, final String shareUrl) {
        CommonUtils.shareDialog(activity, picture, title, content, shareUrl, getClass().getSimpleName()
                , new ShareDialog.OnShareListener() {
                    @Override
                    public void onShare(int type) {
                        countDownTimer.start();
                    }
                });
    }

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
        ImageView head;
        List<BarginWebchatList> bargainWechatRspList = barginBean.bargainWechatRspList;
        for (BarginWebchatList barginWebchat : bargainWechatRspList) {
            view = inflater.inflate(R.layout.bargin_list_item, null);
            head = (ImageView) view.findViewById(R.id.head);
            name = (TextView) view.findViewById(R.id.name);
            time = (TextView) view.findViewById(R.id.time);
            money = (TextView) view.findViewById(R.id.money);
            Tools.showCircleImage(activity, head, barginWebchat.wechatPic);
            name.setText(barginWebchat.wechatNickname);
            time.setText(barginWebchat.bargTime);
            money.setText("-" + barginWebchat.bargAmount + "元");
            listLayout.addView(view);
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

    int second = 48* 60 * 60;
    CountDownTimer countDownTimer;

    private void initView() {
        initDefaultTitleBar();
        fgTitle.setText("邀请好友来砍价");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        countDownTimer = new CountDownTimer(second * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.changeTime((int) millisUntilFinished / 1000);
                LogUtil.e("===", "" + (int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                LogUtil.e("===", "done");
                countdown.changeTime(0);
                cutMoney.setImageResource(R.mipmap.cut_end);
                cutMoney.setOnClickListener(null);
            }
        };

        rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_RULES);
                startActivity(intent);
            }
        });


    }


    private View popupView;
    PopupWindow popupWindow;
    EditText nameEdit;
    private void showAddName(){
        popupView = LayoutInflater.from(activity).inflate(R.layout.bargain_add_name_layout,null);
        popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        nameEdit = (EditText)popupView.findViewById(R.id.real_name);
        popupView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameEdit.getText())){
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
                //真实姓名
                RequestChangeUserInfo request = new RequestChangeUserInfo(activity, null, null, null, null, null, name);
                HttpRequestUtils.request(activity, request, new HttpRequestListener() {
                    @Override
                    public void onDataRequestSucceed(BaseRequest request) {
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onDataRequestCancel(BaseRequest request) {

                    }

                    @Override
                    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

                    }
                });
            }
        });
        popupWindow.showAsDropDown(headerLeftBtn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        countDownTimer.cancel();
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
        return super.getEventSource();
    }

    //是否显示过填写姓名popup
    boolean isShowAddNamePopup = false;

    @OnClick(R.id.cut_money)
    public void onClick() {
        if(TextUtils.isEmpty(userName)) {
            if(!isShowAddNamePopup){
                isShowAddNamePopup = true;
                showAddName();
            }else{
                getShareUrl();
            }
        }else{
            getShareUrl();
        }
    }


    private void getShareUrl(){
        RequestBargainShare requestBargainShare = new RequestBargainShare(activity,orderNo);
        HttpRequestUtils.request(activity, requestBargainShare, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                String h5Url = ((RequestBargainShare)request).getData();
                barginShare(R.mipmap.bargain_share,shareTitle,getString(R.string.share_bargin_100),
                        h5Url);
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

            }
        });
    }


//    http://act.dev.huangbaoche.com/h5/cactivity/shareGui/index.html?orderNo=J190348171529&userId=191442516911
//    //  分享的链接di'zh地址
//      需要 传递的参数 orderNo // 订单号；sign // 签名； activityId // 活动Id； packBatchNo // 领券批次


}
