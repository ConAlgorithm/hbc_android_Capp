package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeAlbumInfoVo;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.Tools;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomePastAlbumAdapter extends RecyclerView.Adapter<HomePastAlbumAdapter.ViewHolder> {

    public int displayImgWidth, displayImgHeight;
    Context context;
    private LayoutInflater mInflater;
    ArrayList<HomeAlbumInfoVo> pastAlbumList;

    public HomePastAlbumAdapter(Context context,int displayImgWidth,int displayImgHeight,ArrayList<HomeAlbumInfoVo> pastAlbumList){
        this.context = context;
        this.displayImgHeight = displayImgHeight;
        this.displayImgWidth = displayImgWidth;
        this.pastAlbumList = pastAlbumList;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.past_album,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(displayImgWidth,displayImgHeight);
        viewHolder.mImg.setLayoutParams(lp);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tools.showImage(holder.mImg,pastAlbumList.get(position).albumImageUrl);
        holder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentActivity(context, WebInfoActivity.class,getEventSource());
                SensorsUtils.onAppClick(getEventSource(),"往期专辑","首页-往期专辑");
            }
        });
        //holder.desPast.setText("");
    }

    @Override
    public int getItemCount() {
        return pastAlbumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView)
        ImageView mImg;
        @Bind(R.id.des_past)
        TextView desPast;

        public ViewHolder(View arg0) {
            super(arg0);
            ButterKnife.bind(this, arg0);
        }
    }

    public void setData(){
        notifyDataSetChanged();
    }

    private void intentActivity(Context context, Class<?> cls, String eventId) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
        if (!TextUtils.isEmpty(eventId)) {
            MobClickUtils.onEvent(eventId);
        }
    }
    public String getEventSource() {
        return "首页";
    }
}
