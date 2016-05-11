package com.hugboga.custom.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;

import net.grobas.view.PolygonImageView;

import org.xutils.view.annotation.ViewInject;

/**
 * 订单列表VH
 * Created by ZHZEPHI on 2015/11/7 11:44.
 */
public class NewOrderVH extends ZBaseViewHolder {

    @ViewInject(R.id.travel_item_line)
    public View mLineView; // 业务线
    @ViewInject(R.id.travel_item_typestr)
    public TextView mTypeStr; //订单类型
    @ViewInject(R.id.travel_item_cartype)
    public TextView mCarType; //车辆类型
    @ViewInject(R.id.travel_item_datetime)
    public TextView mServiceTime; //服务时间
    @ViewInject(R.id.travel_item_commend_start_address)
    public TextView mCommendStartAddress; //出发地
    @ViewInject(R.id.travel_item_datetime_type)
    public TextView mServiceTimeLocl; //当地城市时间
    @ViewInject(R.id.travel_item_days)
    public TextView mDays; //日租天数
    @ViewInject(R.id.travel_item_from)
    public TextView mFrom; //出发地址
    @ViewInject(R.id.travel_item_to)
    public TextView mTo; //到达地址
    @ViewInject(R.id.travel_item_citys)
    public TextView mCitys; //包车城市指向
    @ViewInject(R.id.travel_item_status)
    public TextView mStatus; //订单状态
    @ViewInject(R.id.travel_item_status_layout)
    public RelativeLayout mStatusLayout; //状态行为操作栏
    @ViewInject(R.id.travel_item_price)
    public TextView mPrice; //支付费用
    @ViewInject(R.id.travel_item_head_layout)
    public LinearLayout mHeadLayout; //导游信息
    @ViewInject(R.id.travel_item_head_img)
    public PolygonImageView mHeadImg;//导游头像
    @ViewInject(R.id.travel_item_head_title)
    public TextView mHeadTitle; //导游名称
    @ViewInject(R.id.travel_item_btn_pay)
    public TextView mBtnPay; //立即支付
    @ViewInject(R.id.travel_item_btn_chat)
    public ImageView mBtnChat; //聊一聊按钮
    @ViewInject(R.id.travel_item_btn_chat_num)
    public TextView mBtnChatNum; //未读消息个数
    @ViewInject(R.id.travel_item_btn_assessment)
    public TextView mAssessment; //评价车导

    @ViewInject(R.id.br_layout)
    public LinearLayout br_layout;
    @ViewInject(R.id.travel_item_btn_br)
    public TextView travel_item_btn_br;;
    @ViewInject(R.id.travel_item_btn_br_tips)
    public ImageView travel_item_btn_br_tips;

    public NewOrderVH(View itemView) {
        super(itemView);
    }
}
