package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.SearchGuideActivity;
import com.hugboga.custom.activity.SearchLineActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hugboga.custom.constants.Constants.PARAMS_SEARCH_KEYWORD;

/**
 * Created by zhangqiang on 17/8/30.
 */

public class GuideLineItemHeaderView extends LinearLayout implements HbcViewBehavior{
    @Bind(R.id.more_count_line)
    TextView moreCountLine;
    @Bind(R.id.title_search)
    TextView titleText;
    String title;
    String keyword;
    int count;
    Context context;
    public GuideLineItemHeaderView(Context context) {
        this(context,null);
    }

    public GuideLineItemHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.search_guide_line_header, this);
        ButterKnife.bind(view);
    }


    @Override
    public void update(Object _data) {
        if(_data instanceof String ){
            keyword = (String) _data;
        }
            if(count <= 3){
                moreCountLine.setVisibility(View.GONE);
            }
            moreCountLine.setText("查看全部(" + count + ")");
            moreCountLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(title.equals("相关线路")){
                        Intent intent = new Intent(context,SearchLineActivity.class);
                        intent.putExtra(PARAMS_SEARCH_KEYWORD,keyword);
                        context.startActivity(intent);
                    }else if(title.equals("相关司导")){
                        Intent intent = new Intent(context,SearchGuideActivity.class);
                        intent.putExtra(PARAMS_SEARCH_KEYWORD,keyword);
                        context.startActivity(intent);
                    }

                }
            });
            titleText.setText(title);

    }

    public void setData(String title,int count){
        this.title = title;
        this.count = count;
    }
    public void setContext(Context context){
        this.context = context;
    }
}
