package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CollectGuideListActivity;
import com.hugboga.custom.activity.GuideDetailActivity;
import com.hugboga.custom.activity.OrderSelectCityActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleNewActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.CircleImageView;
import com.hugboga.custom.widget.SimpleRatingBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by qingcha on 16/5/24.
 */
public class CollectGuideAdapter extends BaseAdapter<CollectGuideBean> {

    private Context context;
    public boolean isShowStatusLayout = true;
    public boolean isChartered = false;

    public CollectGuideAdapter(Context context, boolean isChartered) {
        super(context);
        this.context = context;
        this.isChartered = isChartered;
    }

    /**
     * 通过该标记位判断预约是否显示
     * params isShowAppointments true为显示
     * */
    public void setShowStatusLayout(boolean isShowStatusLayout) {
        this.isShowStatusLayout = isShowStatusLayout;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fg_collect_guide_item, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CollectGuideBean collectGuideBean = getItem(position);
        if (TextUtils.isEmpty(collectGuideBean.avatar)) {
            holder.avatarIV.setImageResource(R.mipmap.journey_head_portrait);
        } else {
            Tools.showImage(holder.avatarIV, collectGuideBean.avatar);
        }
        holder.nameTV.setText(collectGuideBean.name);
        holder.ratingView.setRating(collectGuideBean.stars);

        holder.score.setText(collectGuideBean.stars + "分");
        holder.city.setText(collectGuideBean.countryName+"-"+collectGuideBean.cityName);

        if(collectGuideBean.gender == 1){
            holder.nameTV.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.man_icon,0);
        }else{
            holder.nameTV.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.woman_icon,0);
        }

        if (isShowStatusLayout) {
            holder.appointmentTV.setVisibility(View.GONE);
            holder.describeTV.setText(context.getString(R.string.collect_guide_describe, collectGuideBean.carModel, "" + collectGuideBean.numOfPerson, "" + collectGuideBean.numOfLuggage));
            ArrayList<Integer> serviceTypes = collectGuideBean.serviceTypes;
            if (serviceTypes != null) {
                boolean isShowPlane = false;
                boolean isShowCar = false;
                boolean isShowSingle = false;
                final int arraySize = serviceTypes.size();
                for (int i = 0; i < arraySize; i++) {
                    switch (serviceTypes.get(i)) {
                        case 1://可以预约接送机
                            isShowPlane = true;
                            break;
                        case 3://可以预约包车
                            isShowCar = true;
                            break;
                        case 4://可以预约单次接送
                            isShowSingle = true;
                            break;
                    }
                }
                holder.planeLayout.setVisibility(isShowPlane ? View.VISIBLE : View.GONE);
                holder.carLayout.setVisibility(isShowCar ? View.VISIBLE : View.GONE);
                holder.singleLayout.setVisibility(isShowSingle ? View.VISIBLE : View.GONE);
                //控制分割线的隐藏
                holder.leftLine.setVisibility(isShowPlane && isShowCar ? View.VISIBLE : View.GONE);
                boolean isShowRightLine = (isShowSingle && isShowCar) || (isShowSingle && isShowPlane);
                holder.rightLine.setVisibility(isShowRightLine ? View.VISIBLE : View.GONE);

                holder.planeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PickSendActivity.class);
                        intent.putExtra("collectGuideBean", collectGuideBean);
                        intent.putExtra("source","收藏司导列表");
                        if (context instanceof GuideDetailActivity) {
                            intent.putExtra(Constants.PARAMS_SOURCE, ((GuideDetailActivity) context).getIntentSource());
                        }
                        context.startActivity(intent);
                    }
                });
                holder.carLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, OrderSelectCityActivity.class);
                        intent.putExtra("collectGuideBean", collectGuideBean);
                        intent.putExtra("source","收藏司导列表");
                        if (context instanceof GuideDetailActivity) {
                            intent.putExtra(Constants.PARAMS_SOURCE, ((GuideDetailActivity) context).getIntentSource());
                        }
                        context.startActivity(intent);
                    }
                });
                holder.singleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SingleNewActivity.class);
                        intent.putExtra("collectGuideBean", collectGuideBean);
                        intent.putExtra("source","收藏司导列表");
                        if (context instanceof GuideDetailActivity) {
                            intent.putExtra(Constants.PARAMS_SOURCE, ((GuideDetailActivity) context).getIntentSource());
                        }
                        context.startActivity(intent);
                    }
                });
            }
        } else {
            holder.statusLayout.setVisibility(View.GONE);
            holder.horizontalLine.setVisibility(View.GONE);
            holder.appointmentTV.setVisibility(View.VISIBLE);
            if (collectGuideBean.isAppointments()) {
                holder.appointmentTV.setTag(position);
                holder.appointmentTV.setText(context.getString(R.string.collect_guide_appointments));
                holder.appointmentTV.setBackgroundResource(R.drawable.shape_rounded_orange);
                holder.appointmentTV.setTextColor(context.getResources().getColor(R.color.basic_white));
                holder.appointmentTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new EventAction(EventType.CHOOSE_GUIDE, collectGuideBean));
                        if (context instanceof CollectGuideListActivity) {
                            ((CollectGuideListActivity) context).finish();
                        }
                    }
                });

            } else {
                holder.appointmentTV.setText(context.getString(R.string.collect_guide_unappointments));
                holder.appointmentTV.setBackgroundResource(R.drawable.shape_rounded_gray_light);
                holder.appointmentTV.setTextColor(context.getResources().getColor(R.color.basic_gray_light));
            }
            holder.describeTV.setText(context.getString(R.string.collect_guide_describe_filter, collectGuideBean.carDesc, collectGuideBean.carModel));
        }

        holder.topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChartered && !collectGuideBean.isAppointments()) {
                    return;
                }
                GuideDetailActivity.Params params = new GuideDetailActivity.Params();
                params.guideId = collectGuideBean.guideId;
                Intent intent = new Intent(context, GuideDetailActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                if (context instanceof CollectGuideListActivity) {
                    intent.putExtra(Constants.PARAMS_SOURCE, ((CollectGuideListActivity)context).getIntentSource());
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.collect_avatar_iv)
        CircleImageView avatarIV;
        @ViewInject(R.id.collect_name_tv)
        TextView nameTV;
        @ViewInject(R.id.collect_ratingView)
        SimpleRatingBar ratingView;
        @ViewInject(R.id.collect_describe_tv)
        TextView describeTV;
        @ViewInject(R.id.collect_appointment_tv)
        TextView appointmentTV;
        @ViewInject(R.id.collect_horizontal_line)
        View horizontalLine;
        @ViewInject(R.id.collect_status_layout)
        LinearLayout statusLayout;
        @ViewInject(R.id.collect_status_plane_layout)
        LinearLayout planeLayout;
        @ViewInject(R.id.collect_status_car_layout)
        LinearLayout carLayout;
        @ViewInject(R.id.collect_status_single_layout)
        LinearLayout singleLayout;
        @ViewInject(R.id.collect_vertical_line_left)
        View leftLine;
        @ViewInject(R.id.collect_vertical_line_right)
        View rightLine;
        @ViewInject(R.id.collect_guide_item_top_layout)
        RelativeLayout topLayout;
        @ViewInject(R.id.city)
        TextView city;
        @ViewInject(R.id.score)
        TextView score;

    }
}
