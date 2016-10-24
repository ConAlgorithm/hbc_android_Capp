package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.widget.CityHomeFilter;
import com.hugboga.custom.widget.CityHomeListItemFree;
import com.hugboga.custom.widget.CityHomeListItemWorry;

import java.util.List;

import static com.netease.nim.uikit.NimUIKit.getContext;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CityHomeFilterAdapter extends RecyclerView.Adapter<CityHomeFilterAdapter.MyHolder> {

    private Context context;
    private List<String> list;
    public static final int HEAD_LABLE_WORRY_TYPE=1;
    public static final int HEAD_LABLE_FREE_TYPE=2;

    public CityHomeFilterAdapter(Context context,List<String> list) {
        this.context=context;
        this.list=list;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.city_home_filter_list_item,null);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textView.setText(list.get(position));
        if (list.get(position)!=null){
            holder.textView.setBackgroundResource(R.drawable.bg_city_home_guides_count);
            holder.textView.setTextColor(getContext().getResources().getColor(R.color.color_yellow_b39729));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public MyHolder(View itemView) {
           super(itemView);
//            textView=(TextView)itemView.findViewById(R.id.city_home_filter_list_item);
        }
    }
}
