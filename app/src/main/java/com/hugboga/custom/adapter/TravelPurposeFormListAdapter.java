package com.hugboga.custom.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.TravelPurposeFormBean;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;

/**
 * Created by Administrator on 2017/3/2.
 */

public class TravelPurposeFormListAdapter extends HbcRecyclerTypeBaseAdpater<TravelPurposeFormBean.ListData> {

    public TravelPurposeFormListAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getItemView(int position) {
        return new TravelPurposeFormListItemView(getContext());
    }

    @Override
    protected int getChildItemViewType(int position) {
        return 0;
    }


    public class TravelPurposeFormListItemView extends LinearLayout implements HbcViewBehavior{

        private ImageView addBtn;
        private TextView title,date;
        public TravelPurposeFormListItemView(Context context) {
            this(context,null);
        }

        public TravelPurposeFormListItemView(Context context, AttributeSet attrs) {
            this(context, attrs,0);
        }

        public TravelPurposeFormListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            View view = inflate(context, R.layout.item_travel_purpose_form,this);
            ViewGroup.LayoutParams paramsRoot = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(paramsRoot);

            float displayWidth = UIUtils.screenWidth * 260f/750f;
            int displayHeight = (int) (displayWidth*(360f/260f));
            LayoutParams paramsLay = new LayoutParams((int)displayWidth,displayHeight);
            LinearLayout layoutbg = (LinearLayout)findViewById(R.id.travel_form_item_bg_layout);
            layoutbg.setOrientation(VERTICAL);
            layoutbg.setLayoutParams(paramsLay);

            addBtn = (ImageView)findViewById(R.id.add_btn);
            title = (TextView)findViewById(R.id.travel_purpose_form_title);
            date = (TextView)findViewById(R.id.travel_purpose_form_date);
            title.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.GONE);

        }

        @Override
        public void update(Object _data) {
            TravelPurposeFormBean.ListData listData = null;
            if (null != _data && _data instanceof TravelPurposeFormBean.ListData){
                listData = (TravelPurposeFormBean.ListData) _data;
            }
            title.setText(listData.toCity.toString());
            if ("待定".equals(listData.tripTimeStr.toString().trim())){
                date.setText("日期待定");
            }else {
                date.setText(listData.tripTimeStr.toString());
            }
        }
    }


}
