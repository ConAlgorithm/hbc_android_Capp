package com.hugboga.custom.fragment;

import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SkuItemBean;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 *
 * SKu 下单
 * Created by admin on 2016/3/17.
 */
@ContentView(R.layout.fg_sku_submit)
public class FgSkuSubmit extends BaseFragment {

    @ViewInject(R.id.sku_title)
    private TextView skuTitle;//标题
    @ViewInject(R.id.sku_label)
    private TextView skuLabel;//标签描述
    @ViewInject(R.id.sku_days)
    private TextView skuDays;//天数
    @ViewInject(R.id.sku_start_day_edit)
    private TextView skuStartDayEdit;//起始地点
    @ViewInject(R.id.sku_car_type_edit)
    private TextView skuCarTypeEdit;//车类
    @ViewInject(R.id.sku_start_time_edit)
    private TextView skuStartTime;//起始时间
    @ViewInject(R.id.submit_adult)
    private TextView skuAdult;//成人数
    @ViewInject(R.id.submit_child)
    private TextView skuChild;//儿童数
    @ViewInject(R.id.sku_start_address_edit)
    private TextView skuStartAddress;//地址
    @ViewInject(R.id.sku_user_name_edit)
    private TextView skuUserName;//用户名
    @ViewInject(R.id.sku_user_phone_edit)
    private TextView skuPhone;//用户手机

    private SkuItemBean skuBean;


    @Override
    protected void initHeader() {
        fgTitle.setText("填写订单");
    }

    @Override
    protected void initView() {
        skuBean = (SkuItemBean) getArguments().getSerializable(FgSkuDetail.WEB_CITY);
        MLog.e("skuBean= "+skuBean);
        if(skuBean ==null)return;
        skuTitle.setText(skuBean.goodsName);
        skuLabel.setText(skuBean.salePoints);
        skuDays.setText(getString(R.string.sku_days,skuBean.daysCount));
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Event({R.id.sku_start_day_edit})
    private void onClickView(View view){


    }
}
