package com.hugboga.custom.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.IMChatActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.fragment.FgTravel;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.CircleImageView;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.util.HashMap;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by ZHZEPHI on 2015/7/21.
 */
public class TravelAdapter extends BaseAdapter<OrderBean>  {

    private final ImageOptions options;

    BaseFragment fragment;
    DialogUtil dialog;

    public TravelAdapter(Activity context, BaseFragment fragment) {
        super(context);
        this.fragment = fragment;
        dialog = DialogUtil.getInstance(context);
        options = new ImageOptions.Builder()
                .setFailureDrawableId(R.mipmap.chat_head)
                .setLoadingDrawableId(R.mipmap.chat_head)
                .setCircular(true)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fg_travel_item,null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderBean orderBean = getItem(position);
        //车辆类型
        holder.mCarType.setText(orderBean.carDesc);
        //当地城市时间
        holder.mServiceTimeLocl.setVisibility(View.VISIBLE);
        holder.mServiceTimeLocl.setText("("+orderBean.serviceCityName +"时间)");
        //订单状态
        holder.mStatus.setText(orderBean.orderStatus.name);
        holder.mCommendStartAddress.setVisibility(View.GONE);
        switch (orderBean.orderType){
            case  Constants.BUSINESS_TYPE_PICK:
                //接机
                holder.mLineView.setBackgroundResource(R.drawable.jour_blue_ttt);
                holder.mTypeStr.setText(R.string.title_pick);
                holder.mDays.setVisibility(View.GONE);
                holder.mFrom.setVisibility(View.VISIBLE);
                holder.mFrom.setText(orderBean.startAddress);
                holder.mTo.setVisibility(View.VISIBLE);
                holder.mTo.setText(orderBean.destAddress);
                holder.mCitys.setVisibility(View.GONE);
                try {
                    holder.mServiceTime.setVisibility(View.VISIBLE);
                    holder.mServiceTime.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case  Constants.BUSINESS_TYPE_SEND:
                //送机
                holder.mLineView.setBackgroundResource(R.drawable.jour_green_ttt);
                holder.mTypeStr.setText(R.string.title_send);
                holder.mDays.setVisibility(View.GONE);
                holder.mFrom.setVisibility(View.VISIBLE);
                holder.mFrom.setText(orderBean.startAddress);
                holder.mTo.setVisibility(View.VISIBLE);
                holder.mTo.setText(orderBean.destAddress);
                holder.mCitys.setVisibility(View.GONE);
                try {
                    holder.mServiceTime.setVisibility(View.VISIBLE);
                    holder.mServiceTime.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case  Constants.BUSINESS_TYPE_DAILY:
                //日租
                holder.mLineView.setBackgroundResource(R.drawable.jour_yellow_ttt);
                holder.mTypeStr.setText(R.string.title_daily);

                holder.mServiceTime.setVisibility(View.VISIBLE);
                if(orderBean.isHalfDaily==1){//半日包

                    holder.mDays.setVisibility(View.GONE);
                    holder.mServiceTime.setText(orderBean.serviceTime+"(半日包)");
                }else{
                    holder.mDays.setVisibility(View.VISIBLE);
                    holder.mDays.setText((orderBean.totalDays) + "天");
                    holder.mServiceTime.setText(orderBean.serviceTime+" 至 "+orderBean.serviceEndTime);
                }
                if(orderBean.orderGoodsType == Constants.BUSINESS_TYPE_DAILY){
                    holder.mTypeStr.setText("市内包车");
                }else{
                    holder.mTypeStr.setText("跨城市包车");
                }
                holder.mFrom.setVisibility(View.GONE);
                holder.mTo.setVisibility(View.GONE);
                holder.mCitys.setVisibility(View.VISIBLE);
                String dailyPlace = orderBean.serviceCityName;
                if(!TextUtils.isEmpty(orderBean.serviceEndCityName)){
                    dailyPlace+=" - " + orderBean.serviceEndCityName;
                }
                holder.mCitys.setText(dailyPlace);
                break;
            case  Constants.BUSINESS_TYPE_RENT:
                //次租
                holder.mLineView.setBackgroundResource(R.drawable.jour_red_ttt);
                holder.mTypeStr.setText(R.string.title_rent);
                holder.mDays.setVisibility(View.GONE);
                holder.mFrom.setVisibility(View.VISIBLE);
                holder.mFrom.setText(orderBean.startAddress);
                holder.mTo.setVisibility(View.VISIBLE);
                holder.mTo.setText(orderBean.destAddress);
                holder.mCitys.setVisibility(View.GONE);
                try {
                    holder.mServiceTime.setVisibility(View.VISIBLE);
                    holder.mServiceTime.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case  Constants.BUSINESS_TYPE_COMMEND:
                //线路包车
                holder.mLineView.setBackgroundResource(R.drawable.jour_purple_ttt);
                holder.mTypeStr.setText(R.string.title_commend);
                holder.mDays.setVisibility(View.GONE);
                holder.mServiceTime.setVisibility(View.VISIBLE);
                holder.mServiceTime.setText(splitDateStr(orderBean.serviceTime) + " 至 " + splitDateStr(splitDateStr(orderBean.serviceEndTime)));
                holder.mCommendStartAddress.setText("(出发地：" + orderBean.serviceCityName + ")");
                holder.mCommendStartAddress.setVisibility(View.VISIBLE);
                holder.mFrom.setVisibility(View.GONE);
                holder.mTo.setVisibility(View.GONE);
                holder.mServiceTimeLocl.setVisibility(View.GONE);
                holder.mCitys.setVisibility(View.VISIBLE);
                holder.mCitys.setText(orderBean.lineSubject);
                break;
        }
       /* if(orderBean.canChat && (orderBean.imToken!=null && !orderBean.imToken.isEmpty()) && (orderBean.orderGuideInfo!=null && orderBean.orderGuideInfo.guideID!=null)){
            holder.mBtnChat.setVisibility(View.VISIBLE);
            final ViewHolder finalHolder = holder;
            holder.mBtnChat.setTag(orderBean);
            holder.mBtnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderBean mOrderBean = (OrderBean)v.getTag();
                    MLog.e("进入聊天" + mOrderBean.orderNo);
                    gotoChatView(finalHolder.mBtnChatNum,mOrderBean);
                }
            });
        }else{*/
            holder.mBtnChat.setVisibility(View.GONE);
//        }
        setStatusView(holder, orderBean);
        return convertView;
    }

    private String splitDateStr(String dateStr){
        if(dateStr==null)return null;
        String[] dateArray = dateStr.split(" ");
        if(dateArray!=null&&dateArray.length>1)return dateArray[0];
        return dateStr;
    }

    /**
     * 设置聊一聊未读个数小红点
     * @param chatNumTextView
     */
    private void showMessageNum(final TextView chatNumTextView, Integer imcount){
        if(imcount>0){
            chatNumTextView.setVisibility(View.VISIBLE);
            chatNumTextView.setText(String.valueOf(imcount));
        }else{
            chatNumTextView.setVisibility(View.GONE);
        }
    }
    private int requestIMTokenCount = 0;
    private HashMap<String ,TextView> imMap = new HashMap<String ,TextView>();
    /**
     * 开始聊天
     */
    private void gotoChatView(final TextView chatNum, final OrderBean orderBean){
        imMap.put(orderBean.orderNo,chatNum);
        dialog.showLoadingDialog(true);
        RongIM.connect(orderBean.imToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                if (requestIMTokenCount < 3) {
                    requestIMTokenCount++;
                    requestIMTokenUpdate(orderBean);
                } else {
                    dialog.dismissLoadingDialog();
                }
            }

            @Override
            public void onSuccess(String s) {
                dialog.dismissLoadingDialog();
                //刷新IM头像
               /* RongIM.getInstance().refreshUserInfoCache(new UserInfo(s, UserEntity.getUser().getNickname(mContext), Uri.parse(UserEntity.getUser().getAvatar(mContext))));
                IMChatActivity.orderId = "G" + orderBean.orderGuideInfo.guideID;
                IMChatActivity.ids = s;
                chatNum.setVisibility(View.GONE);
                RongIM.getInstance().startPrivateChat(mContext, "G" + orderBean.orderGuideInfo.guideID, "title");
                Uri uri;
                if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                    uri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.mipmap.journey_head_portrait);
                } else {
                    uri = Uri.parse(orderBean.orderGuideInfo.guideAvatar);
                }
                UserInfo peerUser = new UserInfo(IMChatActivity.orderId, orderBean.orderGuideInfo.guideName, uri);
                RongContext.getInstance().getUserInfoCache().put(IMChatActivity.orderId, peerUser);*/
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                dialog.dismissLoadingDialog();
                if (requestIMTokenCount < 3) {
                    //您需要更换 Token
                    requestIMTokenCount++;
                    requestIMTokenUpdate(orderBean);
                } else {
                    dialog.dismissLoadingDialog();
                }
            }

        });
    }

    /**
     * 根据状态重置操作栏
     * @param holder
     * @param orderBean
     */
    private void setStatusView( ViewHolder holder,   OrderBean orderBean){
        holder.mAssessment.setOnClickListener(null);
        switch (orderBean.orderStatus){
            case INITSTATE:
                //等待支付 初始状态
                holder.mStatusLayout.setVisibility(View.VISIBLE);
                holder.mPrice.setVisibility(View.VISIBLE);
                holder.mPrice.setText("应付金额：" + orderBean.orderPriceInfo.shouldPay + "元");
                holder.mHeadLayout.setVisibility(View.GONE);
                holder.mBtnPay.setVisibility(View.VISIBLE);
                holder.mBtnPay.setTag(orderBean);
                holder.mBtnPay.setOnClickListener(new TravelOnClickListener(orderBean));
                holder.mBtnChat.setVisibility(View.GONE);
//                holder.mBtnCall.setVisibility(View.GONE);
                holder.mAssessment.setVisibility(View.GONE);
                holder.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case PAYSUCCESS:
                //预订成功
                holder.mStatusLayout.setVisibility(View.GONE);
                holder.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case AGREE:
                //导游已接单
                holder.mStatusLayout.setVisibility(View.VISIBLE);
                holder.mPrice.setVisibility(View.GONE);
                holder.mHeadLayout.setVisibility(View.VISIBLE);
                if(orderBean.orderGuideInfo!=null) {
                    holder.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    x.image().bind(holder.mHeadImg, orderBean.orderGuideInfo.guideAvatar,options);
               }
                holder.mBtnChat.setVisibility(View.VISIBLE);
                holder.mBtnPay.setVisibility(View.GONE);
                holder.mAssessment.setVisibility(View.GONE);
                holder.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case ARRIVED:
                //导游已到达
                holder.mStatusLayout.setVisibility(View.VISIBLE);
                holder.mPrice.setVisibility(View.GONE);
                holder.mHeadLayout.setVisibility(View.VISIBLE);
                holder.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                x.image().bind(holder.mHeadImg, orderBean.orderGuideInfo.guideAvatar,options);
                holder.mBtnPay.setVisibility(View.GONE);
                holder.mBtnChat.setVisibility(View.VISIBLE);
                holder.mAssessment.setVisibility(View.GONE);
                holder.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case SERVICING:
                //您已上车
                holder.mStatusLayout.setVisibility(View.VISIBLE);
                holder.mPrice.setVisibility(View.GONE);
                holder.mHeadLayout.setVisibility(View.VISIBLE);
                if(orderBean.orderGuideInfo!=null) {
                    holder.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    x.image().bind(holder.mHeadImg, orderBean.orderGuideInfo.guideAvatar,options);
                }
                holder.mBtnChat.setVisibility(View.VISIBLE);
                holder.mBtnPay.setVisibility(View.GONE);
                holder.mAssessment.setVisibility(View.GONE);
                holder.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case NOT_EVALUATED:
                //是否可以评价
                holder.mAssessment.setVisibility(View.VISIBLE);
                MLog.e("onclick " + orderBean.orderNo + " orderType = " + orderBean.orderType);
                MLog.e("onclick="+holder.mAssessment.hasOnClickListeners());
                holder.mAssessment.setOnClickListener(new TravelOnClickListener(orderBean));
                holder.mStatusLayout.setVisibility(View.VISIBLE);
                holder.mPrice.setVisibility(View.GONE);
                holder.mHeadLayout.setVisibility(View.VISIBLE);
                holder.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                x.image().bind(holder.mHeadImg, orderBean.orderGuideInfo.guideAvatar,options);
                holder.mBtnPay.setVisibility(View.GONE);
                holder.mBtnChat.setVisibility(View.VISIBLE);
                holder.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case COMPLAINT:
                //客诉处理中
                holder.mStatusLayout.setVisibility(View.GONE);
                holder.mStatus.setTextColor(Color.parseColor("#B3B3B3"));
                break;
            case COMPLETE:
                //已完成
                holder.mAssessment.setVisibility(View.GONE);
                holder.mStatusLayout.setVisibility(View.VISIBLE);
                holder.mHeadLayout.setVisibility(View.VISIBLE);
                holder.mBtnChat.setVisibility(View.VISIBLE);
                holder.mStatus.setTextColor(Color.parseColor("#BDBDBD"));
                holder.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                x.image().bind(holder.mHeadImg, orderBean.orderGuideInfo.guideAvatar,options);
                break;
            case CANCELLED:
            case REFUNDED:
                //已取消
                holder.mStatusLayout.setVisibility(View.GONE);
                holder.mStatus.setTextColor(Color.parseColor("#B3B3B3"));
                break;
            default:
                break;
        }
        //显示未读小红点个数
        showMessageNum(holder.mBtnChatNum, orderBean.imcount);
    }

    /**
     * update token
     */
    private void requestIMTokenUpdate(OrderBean orderBean) {
      /*  ParserIMTokenUpdate parser = new ParserIMTokenUpdate(orderBean.orderNo);
        parser.orderBean = orderBean;
        mHttpUtils = new HttpRequestUtils(mContext,parser,this);
        mHttpUtils.isShowLoading = false;
        mHttpUtils.execute();*/
    }

   /* @Override
    public void onDataRequestSucceed(InterfaceParser parser) {
        if(parser instanceof ParserIMTokenUpdate){
            ParserIMTokenUpdate mParser = (ParserIMTokenUpdate) parser;
            mParser.orderBean.imToken = mParser.token;
            TextView textView = imMap.get(mParser.orderBean.orderNo);
            gotoChatView(textView, mParser.orderBean);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, InterfaceParser parser) {
        if(mHttpUtils!=null)
            mHttpUtils.onDataRequestError(errorInfo);
    }

    @Override
    public void onDataRequestCancel(InterfaceParser parser) {

    }*/

    class ViewHolder {
        @ViewInject(R.id.travel_item_line)
        View mLineView; // 业务线
        @ViewInject(R.id.travel_item_typestr)
        TextView mTypeStr; //订单类型
        @ViewInject(R.id.travel_item_cartype)
        TextView mCarType; //车辆类型
        @ViewInject(R.id.travel_item_datetime)
        TextView mServiceTime; //服务时间
        @ViewInject(R.id.travel_item_commend_start_address)
        TextView mCommendStartAddress; //出发地
        @ViewInject(R.id.travel_item_datetime_type)
        TextView mServiceTimeLocl; //当地城市时间
        @ViewInject(R.id.travel_item_days)
        TextView mDays; //日租天数
        @ViewInject(R.id.travel_item_from)
        TextView mFrom; //出发地址
        @ViewInject(R.id.travel_item_to)
        TextView mTo; //到达地址
        @ViewInject(R.id.travel_item_citys)
        TextView mCitys; //包车城市指向
        @ViewInject(R.id.travel_item_status)
        TextView mStatus; //订单状态
        @ViewInject(R.id.travel_item_status_layout)
        RelativeLayout mStatusLayout; //状态行为操作栏
        @ViewInject(R.id.travel_item_price)
        TextView mPrice; //支付费用
        @ViewInject(R.id.travel_item_head_layout)
        LinearLayout mHeadLayout; //导游信息
        @ViewInject(R.id.travel_item_head_img)
        ImageView mHeadImg;//导游头像
        @ViewInject(R.id.travel_item_head_title)
        TextView mHeadTitle; //导游名称
        @ViewInject(R.id.travel_item_btn_pay)
        TextView mBtnPay; //立即支付
        @ViewInject(R.id.travel_item_btn_chat)
        ImageView mBtnChat; //聊一聊按钮
        @ViewInject(R.id.travel_item_btn_chat_num)
        TextView mBtnChatNum; //未读消息个数
        @ViewInject(R.id.travel_item_btn_assessment)
        TextView mAssessment; //评价车导
    }
    class TravelOnClickListener implements View.OnClickListener {

        private final OrderBean mOrderBean;
        public TravelOnClickListener(OrderBean orderBean){
            this.mOrderBean = orderBean;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.travel_item_btn_assessment:
                    MLog.e("评价车导2 " + mOrderBean.orderNo + " orderType = " + mOrderBean.orderType);
                    Bundle bundle = new Bundle();
                   /* bundle.putString(FgAssessment.GUIDE_ID, mOrderBean.orderGuideInfo.guideID);
                    bundle.putString(FgAssessment.ORDER_ID, mOrderBean.orderNo);
                    bundle.putInt(FgAssessment.ORDER_TYPE, mOrderBean.orderType);
                    bundle.putInt(BaseFragment.KEY_BUSINESS_TYPE, mOrderBean.orderType);
                    bundle.putString(FgAssessment.GUIDE_NAME, mOrderBean.orderGuideInfo.guideName);
                    fragment.startFragment(new FgAssessment(), bundle);*/
                    break;
                case R.id.travel_item_btn_pay:
                    OrderBean mOrderBean = (OrderBean)v.getTag();
                    MLog.e("立即支付 " + mOrderBean.orderNo);
                    //立即支付，进入订单详情
                    bundle = new Bundle();
                    bundle.putInt(FgTravel.KEY_BUSINESS_TYPE, mOrderBean.orderType);
                   /* bundle.putString(FgOrder.KEY_ORDER_ID, mOrderBean.orderNo);
                    fragment.startFragment(new FgOrder(), bundle);*/
                    break;
            }
        }
    }
}
