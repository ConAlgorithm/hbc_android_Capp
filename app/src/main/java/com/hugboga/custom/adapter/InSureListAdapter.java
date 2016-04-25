package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.InsureResultBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by dyt on 16/4/23.
 */
public class InSureListAdapter extends BaseAdapter {


    List<InsureResultBean> list;
    Context context;

    public InSureListAdapter(List<InsureResultBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public InsureResultBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.insure_item,null);
        }
        CheckBox  checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView cardId = (TextView)convertView.findViewById(R.id.cardId);
        ImageView edit = (ImageView)convertView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventAction(EventType.EDIT_INSURE,getItem(position)));
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getItem(position).isCheck = 1;
                }else{
                    getItem(position).isCheck = 0;
                }
                EventBus.getDefault().post(new EventAction(EventType.CHECK_INSURE,getItem(position)));
            }
        });

        if(1 == getItem(position).isCheck){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }


        if(1 == getItem(position).isDel){
            checkBox.setVisibility(View.VISIBLE);
        }else{
            checkBox.setVisibility(View.GONE);
        }
        name.setText(getItem(position).name);
        cardId.setText(getItem(position).passportNo);


        return convertView;
    }
}
