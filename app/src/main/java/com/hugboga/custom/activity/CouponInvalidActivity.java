package com.hugboga.custom.activity;

import android.animation.Animator;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CouponAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.CouponTitleContent;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestInvaidableCoupon;
import com.hugboga.custom.data.request.RequestUsedCoupon;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.AnimationUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CouponItemView;
import com.hugboga.custom.widget.ZListView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;

import java.text.ParseException;
import java.util.List;

import butterknife.Bind;
/**
 * Created by zhangqiang on 17/6/24.
 */
public class CouponInvalidActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.coupon_listview)
    ZListView listView;
    @Bind(R.id.coupon_listview_empty)
    RelativeLayout emptyLayout;

    CouponAdapter adapter;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    private int mPageSize = 20;
    boolean isFromMyspace;
    private MostFitAvailableBean paramsData;

    private String idStr = null;

    @Override
    public int getContentViewId() {
        return R.layout.fg_coupon_invalid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (MostFitAvailableBean) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                paramsData = (MostFitAvailableBean) bundle.getSerializable(Constants.PARAMS_DATA);
                idStr = bundle.getString("idStr");
            }
        }

        initView();
        requestData();

        setSensorsDefaultEvent("优惠券", SensorsConstant.COUPON);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    private void initView() {
        isFromMyspace = getIntent().getBooleanExtra("isFromMyspace",false);
        if(!isFromMyspace){
            headerTitle.setText("不可用优惠券");
        }else{
            headerTitle.setText("失效优惠券");
        }
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setEmptyView(emptyLayout);
        listView.setOnItemClickListener(this);
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
    }

    ZListView.OnRefreshListener onRefreshListener = new ZListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter != null) {
                adapter = null;
            }
            runData(0);
        }
    };

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            if (adapter.getCount() > 0) {
                runData(adapter == null ? 0 : adapter.getCount());
            }
        }
    };


    private Callback.Cancelable runData(int pageIndex) {
        BaseRequest request = null;
        if (isFromMyspace) {
            request = new RequestUsedCoupon(this, pageIndex, mPageSize);
        } else if(!isFromMyspace && paramsData != null){
            request = new RequestInvaidableCoupon(this, paramsData, pageIndex);
        }
        return requestData(request);
    }

    private void requestData() {
        //默认加载所有优惠券
        if (adapter != null) {
            adapter = null;
        }
        runData(0);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestUsedCoupon) {
            RequestUsedCoupon mRequest = (RequestUsedCoupon) request;
            setData(mRequest.getData());
        } else if (request instanceof RequestInvaidableCoupon) {
            RequestInvaidableCoupon requestInvaidableCoupon = (RequestInvaidableCoupon) request;
            setData(requestInvaidableCoupon.getData());
        }
    }

    private void setData(List<CouponBean> list) {
        if (list != null) {
            if (adapter == null) {
                adapter = new CouponAdapter(this, idStr);
                listView.setAdapter(adapter);
                adapter.setList(list);
            } else {
                adapter.addList(list);
            }
        }
        if (list != null && list.size() < mPageSize) {
            listView.onLoadCompleteNone();
        } else {
            listView.onLoadComplete();
        }
        listView.onRefreshComplete();
    }

    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CouponBean bean = (CouponBean) adapter.getItem(position - 1);
        if (paramsData != null) {
            EventBus.getDefault().post(new EventAction(EventType.SELECT_COUPON_BACK, bean));
            finish();
        } else {
            //点击查看详情
            showCouponInfoByDialog(bean);
        }
    }

    private void showCouponInfoByDialog(CouponBean bean){
        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.coupon_info_layout, null);

        ((TextView) rl.findViewById(R.id.coupon_info_rule)).setText(bean.batchName);
        ((TextView) rl.findViewById(R.id.coupon_info_content)).setText(bean.applyRule);

        SpannableString spannableString = new SpannableString(bean.price);
        if(bean.price.endsWith("折")){
            spannableString.setSpan(new AbsoluteSizeSpan(50), bean.price.length() - 3, bean.price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(bean.price.endsWith("元")){
            spannableString.setSpan(new AbsoluteSizeSpan(50), bean.price.length() - 1, bean.price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        ((TextView) rl.findViewById(R.id.coupon_info_price)).setText(spannableString);
        if(bean.couponStatus.equals(2) || bean.couponStatus.equals(-1) || bean.couponStatus.equals(98)){
            ((TextView) rl.findViewById(R.id.coupon_info_price)).setTextColor(getResources().getColor(R.color.common_font_color_gray2));
        }
        setCouponStatus(bean,rl);

        if (bean.endDate.equals("0")) {
            ((TextView) rl.findViewById(R.id.coupon_info_limit_time)).setText("有效期：长期有效");
        } else {
            ((TextView) rl.findViewById(R.id.coupon_info_limit_time)).setText("有效期：" + bean.startDate + " 至 " + bean.endDate);
        }

        LinearLayout dataListLayout = (LinearLayout) rl.findViewById(R.id.data_list_layout);
        CouponItemView couponItemView;
        for(CouponTitleContent titleContent:bean.dataList){
            couponItemView = new CouponItemView(activity);
            if(bean.couponStatus.equals(2) || bean.couponStatus.equals(-1) || bean.couponStatus.equals(98)){
                couponItemView.setTitleType2(titleContent.title);
            }else if(bean.couponStatus.equals(1)){
                couponItemView.setTitleType1(titleContent.title);
            }
            couponItemView.setContent(titleContent.content);
            dataListLayout.addView(couponItemView);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(rl);
        final AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width= UIUtils.dip2px(330);
        lp.height=UIUtils.dip2px(380);
        dialogWindow.setAttributes(lp);
    }

    private void setCouponStatus(CouponBean bean,RelativeLayout rl){
        if(bean != null){
            if(bean.couponStatus.equals(2)){
                rl.findViewById(R.id.coupon_invalid).setVisibility(View.VISIBLE);
                ((TextView) rl.findViewById(R.id.coupon_invalid)).setText("已使用");

            }else if(bean.couponStatus.equals(-1)){
                rl.findViewById(R.id.coupon_invalid).setVisibility(View.VISIBLE);
                ((TextView) rl.findViewById(R.id.coupon_invalid)).setText("已过期");
            }else if(bean.couponStatus.equals(98)){
                rl.findViewById(R.id.coupon_invalid).setVisibility(View.VISIBLE);
                ((TextView) rl.findViewById(R.id.coupon_invalid)).setText("已冻结");
            }else if(bean.couponStatus.equals(1)){
                rl.findViewById(R.id.coupon_invalid).setVisibility(View.GONE);
            }
        }
    }
    /**
     * 查看订单详情
     *
     * @param bean
     */
    private void showCouponInfo(CouponBean bean) throws ParseException {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View popView = inflater.inflate(R.layout.coupon_info_layout, null);
        ImageView leftBtn = (ImageView) popView.findViewById(R.id.header_left_btn);
        leftBtn.setImageResource(R.mipmap.top_close);
        TextView header_title = (TextView) popView.findViewById(R.id.header_title);
        header_title.setText("我的优惠券");
        final PopupWindow pw = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //内容赋值
        ((TextView) popView.findViewById(R.id.coupon_info_price)).setText(bean.price);
        ((TextView) popView.findViewById(R.id.coupon_info_rule)).setText(bean.batchName);
        ((TextView) popView.findViewById(R.id.coupon_info_content)).setText(bean.applyRule);
        if (bean.endDate.equals("0")) {
            ((TextView) popView.findViewById(R.id.coupon_info_limit_time)).setText("有效期：长期有效");
        } else {
            ((TextView) popView.findViewById(R.id.coupon_info_limit_time)).setText("有效期：" + bean.startDate + " 至 " + bean.endDate);
        }
        LinearLayout dataListLayout = (LinearLayout) popView.findViewById(R.id.data_list_layout);
        CouponItemView couponItemView;
        for(CouponTitleContent titleContent:bean.dataList){
            couponItemView = new CouponItemView(activity);
            //couponItemView.setTitle(titleContent.title);
            couponItemView.setContent(titleContent.content);
            dataListLayout.addView(couponItemView);
        }
        //设置后进行展示
        pw.setBackgroundDrawable(new ColorDrawable(0));
        pw.setFocusable(true);
//        pw.setOutsideTouchable(true);
        pw.showAtLocation(emptyLayout, Gravity.CENTER, 0, 0);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.hideAnimation(popView, 500, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        pw.dismiss();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
//                pw.dismiss();
            }
        });

        AnimationUtils.showAnimation(popView, 500, null);
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_COUPON;
    }
}
