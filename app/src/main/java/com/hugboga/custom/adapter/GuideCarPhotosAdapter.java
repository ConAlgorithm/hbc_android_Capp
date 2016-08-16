package com.hugboga.custom.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/18.
 */
public class GuideCarPhotosAdapter extends RecyclerView.Adapter<GuideCarPhotosAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<String> urlList;
    private ViewGroup.LayoutParams params;
    private OnItemClickListener listener;

    public GuideCarPhotosAdapter(Context context) {
        this.mContext = context;
        params = new ViewGroup.LayoutParams(UIUtils.dip2px(100), UIUtils.dip2px(100));
    }

    public void setData(ArrayList<String> _urlList) {
        this.urlList = _urlList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(mContext);
        imageView.setBackgroundResource(R.mipmap.guide_car_default);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(params);
        return new MyViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Tools.showImage((ImageView) holder.itemView, urlList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return urlList != null ? urlList.size() : 0;
    }

    class MyViewHolder extends ViewHolder {
        public MyViewHolder(ImageView view) {
            super(view);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
