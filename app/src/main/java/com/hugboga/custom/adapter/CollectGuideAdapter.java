package com.hugboga.custom.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.fragment.FgCollectGuideList;
import com.hugboga.custom.fragment.FgGuideDetail;
import com.hugboga.custom.fragment.FgOrderSelectCity;
import com.hugboga.custom.fragment.FgPickSend;
import com.hugboga.custom.fragment.FgSingleNew;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.RatingView;

import net.grobas.view.PolygonImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

import static android.R.attr.fragment;


/**
 * Created by qingcha on 16/5/24.
 */
public class CollectGuideAdapter extends BaseAdapter<CollectGuideBean> {

    private Context context;
    public boolean isShowStatusLayout = true;

    public CollectGuideAdapter(Context context) {
        super(context);
        this.context = context;
    }

    private FgCollectGuideList fragment;

    public void setFragment(FgCollectGuideList fragment){
        this.fragment = fragment;
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
        final CollectGuideBean bean = getItem(position);
        if (TextUtils.isEmpty(bean.avatar)) {
            holder.avatarIV.setImageResource(R.mipmap.collection_icon_pic);
        } else {
            Tools.showImage(context, holder.avatarIV, bean.avatar);
        }
        holder.nameTV.setText(bean.name);
        holder.ratingView.setLevel(bean.stars);
        if (isShowStatusLayout) {
            holder.appointmentTV.setVisibility(View.GONE);
            holder.describeTV.setText(context.getString(R.string.collect_guide_describe, bean.carModel, bean.numOfPerson, bean.numOfLuggage));
            ArrayList<Integer> serviceTypes = bean.serviceTypes;
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

                holder.planeLayout.setTag(position);
                holder.planeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != fragment) {
                            fragment.finish();
                        }
                        EventBus.getDefault().post(new EventAction(EventType.PICK_SEND_TYPE,getItem(Integer.valueOf(v.getTag().toString()))));
                    }
                });
                holder.carLayout.setTag(position);
                holder.carLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != fragment) {
                            fragment.finish();
                        }
                        EventBus.getDefault().post(new EventAction(EventType.DAIRY_TYPE,getItem(Integer.valueOf(v.getTag().toString()))));
                    }
                });
                holder.singleLayout.setTag(position);
                holder.singleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != fragment) {
                            fragment.finish();
                        }
                        EventBus.getDefault().post(new EventAction(EventType.SINGLE_TYPE,getItem(Integer.valueOf(v.getTag().toString()))));
                    }
                });
            }
        } else {
            holder.statusLayout.setVisibility(View.GONE);
            holder.horizontalLine.setVisibility(View.GONE);
            holder.appointmentTV.setVisibility(View.VISIBLE);
            if (bean.isAppointments()) {
                holder.appointmentTV.setTag(position);
                holder.appointmentTV.setText(context.getString(R.string.collect_guide_appointments));
                holder.appointmentTV.setBackgroundResource(R.drawable.shape_rounded_orange);
                holder.appointmentTV.setTextColor(context.getResources().getColor(R.color.basic_white));
                holder.appointmentTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new EventAction(EventType.CHOOSE_GUIDE,getItem(Integer.valueOf(v.getTag().toString()))));
                        if(null != fragment) {
                            fragment.finish();
                        }
                    }
                });

            } else {
                holder.appointmentTV.setText(context.getString(R.string.collect_guide_unappointments));
                holder.appointmentTV.setBackgroundResource(R.drawable.shape_rounded_gray_light);
                holder.appointmentTV.setTextColor(context.getResources().getColor(R.color.basic_gray_light));
            }
            holder.describeTV.setText(context.getString(R.string.collect_guide_describe_filter, bean.carDesc, bean.carModel));
        }

        holder.topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment == null) {
                    return;
                }
                fragment.startFragment(FgGuideDetail.newInstance(bean.guideId));
            }
        });
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.collect_avatar_iv)
        PolygonImageView avatarIV;
        @ViewInject(R.id.collect_name_tv)
        TextView nameTV;
        @ViewInject(R.id.collect_ratingView)
        RatingView ratingView;
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

    }
}
