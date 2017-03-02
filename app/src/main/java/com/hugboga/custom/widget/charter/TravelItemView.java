package com.hugboga.custom.widget.charter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/28.
 */
public class TravelItemView extends LinearLayout {

    @Bind(R.id.travel_item_edit_iv)
    ImageView travelItemEditIv;
    @Bind(R.id.travel_item_edit_tv)
    TextView travelItemEditTv;
    @Bind(R.id.travel_item_edit_line_view)
    View travelItemEditLineView;
    @Bind(R.id.travel_item_del_tv)
    TextView travelItemDelTv;

    @Bind(R.id.travel_item_title_tv)
    TextView travelItemTitleTv;
    @Bind(R.id.travel_item_data_tv)
    TextView travelItemDataTv;

    @Bind(R.id.travel_item_pickup_no_iv)
    ImageView travelItemPickupNoIv;
    @Bind(R.id.travel_item_pickup_tv)
    TextView travelItemPickupTv;
    @Bind(R.id.travel_item_arrdate_tv)
    TextView travelItemArrdateTv;
    @Bind(R.id.travel_item_pickup_layout)
    RelativeLayout travelItemPickupLayout;

    @Bind(R.id.travel_item_charter_line_iv)
    ImageView travelItemCharterLineIv;
    @Bind(R.id.travel_item_line_tv)
    TextView travelItemLineTv;
    @Bind(R.id.travel_item_line_time_tv)
    TextView travelItemLineTimeTv;
    @Bind(R.id.travel_item_line_distance_tv)
    TextView travelItemLineDistanceTv;
    @Bind(R.id.travel_item_line_tag_layout)
    LinearLayout travelItemLineTagLayout;
    @Bind(R.id.travel_item_charter_line_layout)
    RelativeLayout travelItemCharterLineLayout;

    @Bind(R.id.travel_item_time_iv)
    ImageView travelItemTimeIv;
    @Bind(R.id.travel_item_time_tv)
    TextView travelItemTimeTv;
    @Bind(R.id.travel_item_time_layout)
    RelativeLayout travelItemTimeLayout;

    @Bind(R.id.travel_item_start_line_view)
    View travelItemStartLineIv;
    @Bind(R.id.travel_item_start_tv)
    TextView travelItemStartTv;
    @Bind(R.id.travel_item_start_des_tv)
    TextView travelItemStartDesTv;
    @Bind(R.id.travel_item_start_layout)
    RelativeLayout travelItemStartLayout;

    @Bind(R.id.travel_item_end_tv)
    TextView travelItemEndTv;
    @Bind(R.id.travel_item_end_des_tv)
    TextView travelItemEndDesTv;
    @Bind(R.id.travel_item_end_layout)
    RelativeLayout travelItemEndLayout;

    private CharterDataUtils charterDataUtils;
    private int position;
    private OnEditClickListener listener;

    public enum EditType {
        EDIT, DEL, ALL, VAIN
    }

    public TravelItemView(Context context) {
        this(context, null);
    }

    public TravelItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_travel_list_item, this);
        ButterKnife.bind(view);
        this.charterDataUtils = CharterDataUtils.getInstance();
    }

    public void update(int _position) {
        this.position = _position;
        ArrayList<CityRouteBean.CityRouteScope> travelList = charterDataUtils.travelList;

        ChooseDateBean chooseDateBean = charterDataUtils.params.chooseDateBean;//start
        travelItemDataTv.setText(DateUtils.getNextDay(chooseDateBean.start_date, _position + 1));

        final int travelListSize = travelList.size();
        if (_position < travelListSize) {
            CityRouteBean.CityRouteScope cityRouteScope = travelList.get(_position);
            CityBean startCityBean = charterDataUtils.getCityBean(_position + 1);
            travelItemTitleTv.setTextColor(getResources().getColor(R.color.default_black));
            travelItemTitleTv.setText(String.format("Day%1$s：%2$s", _position + 1, startCityBean.name));//标题
            if (_position == 0) {//第一天（只有接机，只有"编辑"状态）
                setEditLayout(EditType.EDIT);
                if (cityRouteScope.routeType == CityRouteBean.RouteType.PICKUP) {//只接机
                    travelItemLineTagLayout.setVisibility(View.GONE);
                    travelItemCharterLineLayout.setVisibility(View.GONE);
                    travelItemTimeLayout.setVisibility(View.GONE);
                    updatePickupLayout();
                    updateOnlyPickupLayout();
                } else if (charterDataUtils.flightBean != null && charterDataUtils.isSelectedPickUp) {//包车加接机
                    travelItemTimeLayout.setVisibility(View.GONE);
                    travelItemStartLayout.setVisibility(View.GONE);
                    travelItemEndLayout.setVisibility(View.GONE);
                    updatePickupLayout();
                    updateLineLayout(cityRouteScope, _position);
                } else {//只包车
                    travelItemPickupLayout.setVisibility(View.GONE);
                    travelItemTimeLayout.setVisibility(View.GONE);
                    travelItemStartLayout.setVisibility(View.GONE);
                    travelItemEndLayout.setVisibility(View.GONE);
                    updateLineLayout(cityRouteScope, _position);
                }
            } else if (_position == chooseDateBean.dayNums - 1) {//最后一天（有送机，"编辑 | 删除"）
                setEditLayout(EditType.ALL);
                if (cityRouteScope.routeType == CityRouteBean.RouteType.SEND) {//只送机
                    travelItemTimeLayout.setVisibility(View.VISIBLE);
                    travelItemTimeIv.setBackgroundResource(R.mipmap.trip_icon_time);
                    travelItemTimeTv.setText(charterDataUtils.params.chooseDateBean.endDate + charterDataUtils.sendServerTime);
                    updateSendLayout();
                } else if (charterDataUtils.airPortBean != null && charterDataUtils.isSelectedSend) {//包车加送机
                    travelItemTimeLayout.setVisibility(View.VISIBLE);
                    travelItemTimeIv.setBackgroundResource(R.mipmap.trip_icon_go);
                    travelItemTimeTv.setText("游玩结束送机：" + charterDataUtils.airPortBean.airportName);
                    updateLineLayout(cityRouteScope, _position);
                } else {//只包车
                    travelItemPickupLayout.setVisibility(View.GONE);
                    travelItemTimeLayout.setVisibility(View.GONE);
                    travelItemStartLayout.setVisibility(View.GONE);
                    travelItemEndLayout.setVisibility(View.GONE);
                    updateLineLayout(cityRouteScope, _position);
                }
            } else if (cityRouteScope.routeType == CityRouteBean.RouteType.AT_WILL) {//随便转转（只有"编辑"）
                setEditLayout(EditType.EDIT);
                travelItemCharterLineLayout.setVisibility(View.GONE);
                travelItemLineTagLayout.setVisibility(View.GONE);
                travelItemPickupLayout.setVisibility(View.GONE);
                travelItemTimeLayout.setVisibility(View.GONE);
                travelItemStartLayout.setVisibility(View.GONE);
                travelItemEndLayout.setVisibility(View.GONE);
                travelItemTitleTv.setText(String.format("Day%1$s：%2$s", _position + 1, cityRouteScope.routeTitle));
            } else {//包车（只有"编辑"）
                setEditLayout(EditType.EDIT);
                travelItemPickupLayout.setVisibility(View.GONE);
                travelItemTimeLayout.setVisibility(View.GONE);
                travelItemStartLayout.setVisibility(View.GONE);
                travelItemEndLayout.setVisibility(View.GONE);
                updateLineLayout(cityRouteScope, _position);
            }
        } else {
            travelItemCharterLineLayout.setVisibility(View.GONE);
            travelItemLineTagLayout.setVisibility(View.GONE);
            travelItemPickupLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.GONE);
            travelItemStartLayout.setVisibility(View.GONE);
            travelItemEndLayout.setVisibility(View.GONE);
            travelItemTitleTv.setText(String.format("Day%1$s", _position + 1));//标题
            travelItemTitleTv.setTextColor(0xFFA8A8A8);

            if (chooseDateBean.dayNums > 1 && _position == chooseDateBean.dayNums - 1) {//最后一天（删除）
                setEditLayout(EditType.DEL);
            } else {//编辑
                setEditLayout(EditType.EDIT);
            }
        }
    }

    public void updateSendLayout() {
        travelItemStartLayout.setVisibility(View.VISIBLE);
        travelItemStartDesTv.setVisibility(View.VISIBLE);
        //先的高度改变
        travelItemStartTv.setText(charterDataUtils.sendPoiBean.placeName);//出发地点
        travelItemStartDesTv.setText(charterDataUtils.sendPoiBean.placeDetail);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(1), UIUtils.dip2px(20));
        params.topMargin = UIUtils.dip2px(20);
        params.leftMargin = UIUtils.dip2px(7.5f);
        travelItemStartLineIv.setLayoutParams(params);

        travelItemEndLayout.setVisibility(View.VISIBLE);
        travelItemEndDesTv.setVisibility(View.GONE);
        travelItemEndTv.setText(charterDataUtils.airPortBean.airportName);//送达机场
    }

    public void updateOnlyPickupLayout() {
        if (charterDataUtils.flightBean != null) {
            travelItemStartLayout.setVisibility(View.VISIBLE);
            travelItemStartDesTv.setVisibility(View.GONE);
            travelItemStartTv.setText(charterDataUtils.flightBean.arrAirportName);//到达机场
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(1), UIUtils.dip2px(6));
            params.topMargin = UIUtils.dip2px(20);
            params.leftMargin = UIUtils.dip2px(7.5f);
            travelItemStartLineIv.setLayoutParams(params);
        } else {
            travelItemStartLayout.setVisibility(View.GONE);
        }

        travelItemEndLayout.setVisibility(View.VISIBLE);
        if (charterDataUtils.pickUpPoiBean != null) {
            travelItemEndTv.setTextColor(getResources().getColor(R.color.default_black));
            travelItemEndTv.setText(charterDataUtils.pickUpPoiBean.placeName);//到达地
            travelItemEndDesTv.setText(charterDataUtils.pickUpPoiBean.placeName);//到达地描述
            travelItemEndDesTv.setVisibility(View.VISIBLE);
        } else {
            travelItemEndTv.setTextColor(0xFFCCCCCC);
            travelItemEndTv.setText("未添加接机送达地");
            travelItemEndDesTv.setVisibility(View.GONE);
        }
    }

    public void updatePickupLayout() {
        FlightBean flightBean = charterDataUtils.flightBean;
        if (flightBean == null) {
            travelItemPickupLayout.setVisibility(View.GONE);
        } else {
            travelItemPickupLayout.setVisibility(View.VISIBLE);
            travelItemPickupTv.setText("只接机:" + flightBean.flightNo);//只接机：NH956
            travelItemArrdateTv.setText(String.format("当地时间%1$s %2$s降落", DateUtils.getStrWeekFormat3(flightBean.arrDate), flightBean.arrivalTime));//计划到达时间（当地时间：2017年02月18日 周五 12:40降落）
        }
    }

    public void updateLineLayout(CityRouteBean.CityRouteScope cityRouteScope, int _position) {
        travelItemLineTagLayout.setVisibility(View.VISIBLE);
        travelItemCharterLineLayout.setVisibility(View.VISIBLE);
        travelItemLineTv.setTextColor(getResources().getColor(R.color.default_black));
        if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
            CityBean startCityBean = charterDataUtils.getCityBean(_position + 1);
            CityBean endCityBean = charterDataUtils.getCityBean(_position + 2);
            if (startCityBean != null && endCityBean != null && startCityBean != endCityBean) {
                travelItemTitleTv.setText(String.format("Day%1$s：%2$s-%3$s", _position + 1, startCityBean.name, endCityBean.name));
                travelItemLineTv.setText(String.format("%1$s出发，%2$s结束", startCityBean.name, endCityBean.name));
            } else if(startCityBean != null && startCityBean == endCityBean) {
                travelItemTitleTv.setText(String.format("Day%1$s：%2$s", _position + 1, "跨城市游玩"));
                travelItemLineTv.setTextColor(0xFFCCCCCC);
                travelItemLineTv.setText("未选择送达城市");
            } else {
                travelItemLineTv.setText(cityRouteScope.routeTitle);
            }
        } else {
            travelItemLineTv.setText(cityRouteScope.routeTitle);
        }
        travelItemLineTimeTv.setText(String.format("%1$s小时", "" + cityRouteScope.routeLength));
        travelItemLineDistanceTv.setText(String.format("%1$s公里", "" + cityRouteScope.routeKms));
    }

    public void setEditLayout(EditType editType) {
        switch (editType) {
            case EDIT:
                travelItemEditIv.setVisibility(View.VISIBLE);
                travelItemEditTv.setVisibility(View.VISIBLE);
                travelItemEditLineView.setVisibility(View.GONE);
                travelItemDelTv.setVisibility(View.GONE);
                break;
            case DEL:
                travelItemEditIv.setVisibility(View.GONE);
                travelItemEditTv.setVisibility(View.GONE);
                travelItemEditLineView.setVisibility(View.GONE);
                travelItemDelTv.setVisibility(View.VISIBLE);
                break;
            case ALL:
                travelItemEditIv.setVisibility(View.VISIBLE);
                travelItemEditTv.setVisibility(View.VISIBLE);
                travelItemEditLineView.setVisibility(View.VISIBLE);
                travelItemDelTv.setVisibility(View.VISIBLE);
                break;
            case VAIN:
                travelItemEditIv.setVisibility(View.GONE);
                travelItemEditTv.setVisibility(View.GONE);
                travelItemEditLineView.setVisibility(View.GONE);
                travelItemDelTv.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.travel_item_edit_iv, R.id.travel_item_edit_tv})
    public void onEditClick() {
        if (listener != null) {
            listener.onEditClick(position);
        }
    }

    @OnClick({R.id.travel_item_del_tv})
    public void onDelClick() {
        if (listener != null) {
            listener.onDelClick(position);
        }
    }

    public interface OnEditClickListener {
        public void onEditClick(int position);
        public void onDelClick(int position);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.listener = listener;
    }
}
