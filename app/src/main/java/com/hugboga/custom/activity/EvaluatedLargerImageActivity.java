package com.hugboga.custom.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.UIUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;

public class EvaluatedLargerImageActivity extends LargerImageActivity {

    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;

    private ArrayList<String> deleteList = new ArrayList<String>(1);

    @Override
    protected void initView() {
        super.initView();
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventAction(EventType.EVALUTE_PIC_DELETE, deleteList));
                finish();
            }
        });
        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(20), UIUtils.dip2px(20));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        headerRightBtn.setVisibility(View.GONE);
        headerRightBtn.setLayoutParams(headerRightImageParams);
        headerRightBtn.setPadding(0,0,0,0);
        headerRightBtn.setVisibility(View.VISIBLE);
        //headerRightBtn.setImageResource(R.mipmap.evaluate_photo_bin);
        headerRightBtn.setImageResource(R.mipmap.evaluate_photo_bin);
        headerRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete pic.
                //剩最后一张,删除后直接返回上一页
                int item = mViewPager.getCurrentItem();
                if(params.imageUrlList.size() == 1){
                    deleteList.add(params.imageUrlList.get(mViewPager.getCurrentItem()));
                    EventBus.getDefault().post(new EventAction(EventType.EVALUTE_PIC_DELETE,deleteList));
                    finish();
                    return;
                }
                if(item < params.imageUrlList.size()-1){
                    params.position = item;
                }else{
                    params.position = item-1;
                }
                deleteList.add(params.imageUrlList.get(mViewPager.getCurrentItem()));
                params.imageUrlList.remove(mViewPager.getCurrentItem());
                mViewPager.setAdapter(mAdapter);
                mViewPager.setCurrentItem(params.position);
                notifyDataSetChanged();

                int page = params.position +1;
                headerTitle.setText(page + "/" + params.imageUrlList.size());
            }
        });
    }
}
