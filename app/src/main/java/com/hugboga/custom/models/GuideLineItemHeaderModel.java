package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.SearchGuideActivity;
import com.hugboga.custom.activity.SearchLineActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class GuideLineItemHeaderModel extends EpoxyModelWithHolder<GuideLineItemHeaderModel.GuideLineItemHeaderHolder> {

    int count;
    String title;
    GuideLineItemHeaderHolder guideLineItemHeaderHolder;
    String keyword;
    Context context;
    public GuideLineItemHeaderModel(Context context,int count, String title, String keyword){
        this.context = context;
        this.count = count;
        this.title = title;
        this.keyword = keyword;
    }
    @Override
    protected GuideLineItemHeaderHolder createNewHolder() {
        return new GuideLineItemHeaderHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.search_guide_line_header;
    }

    @Override
    public void bind(GuideLineItemHeaderHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        guideLineItemHeaderHolder =holder;
        init();
    }

    static class GuideLineItemHeaderHolder extends EpoxyHolder {
        View itemView;
        @Bind(R.id.more_count_line)
        TextView moreCountLine;
        @Bind(R.id.title_search)
        TextView title;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private void init(){
        if(guideLineItemHeaderHolder!= null){
            guideLineItemHeaderHolder.moreCountLine.setText("查看全部(" + count + ")");
            guideLineItemHeaderHolder.moreCountLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(title.equals("相关线路")){
                        Intent intent = new Intent(context,SearchLineActivity.class);
                        intent.putExtra("keyword",keyword);
                        context.startActivity(intent);
                    }else if(title.equals("相关司导")){
                        Intent intent = new Intent(context,SearchGuideActivity.class);
                        intent.putExtra("keyword",keyword);
                        context.startActivity(intent);
                    }


                }
            });
            guideLineItemHeaderHolder.title.setText(title);
        }
    }
}
