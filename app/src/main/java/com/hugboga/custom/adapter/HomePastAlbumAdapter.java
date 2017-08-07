package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomePastAlbumAdapter extends RecyclerView.Adapter<HomePastAlbumAdapter.ViewHolder> {

    public int displayImgWidth, displayImgHeight;
    Context context;
    private LayoutInflater mInflater;

    final private static int[] PIC_RES = new int[] {
            R.mipmap.pic1, R.mipmap.pic2, R.mipmap.pic3, R.mipmap.pic4, R.mipmap.pic5
    };
    public HomePastAlbumAdapter(Context context,int displayImgWidth,int displayImgHeight){
        this.context = context;
        this.displayImgHeight = displayImgHeight;
        this.displayImgWidth = displayImgWidth;
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
        holder.mImg.setImageResource(PIC_RES[position]);
        //holder.desPast.setText("");
    }

    @Override
    public int getItemCount() {
        return PIC_RES.length;
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
}
