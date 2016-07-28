package com.hugboga.custom.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopTipsLayout extends RelativeLayout {

    View view;
    @Bind(R.id.img_close)
    ImageView imgClose;
    @Bind(R.id.top_text)
    TextView topText;
    @Bind(R.id.top_line)
    TextView topLine;

    public TopTipsLayout(Context context) {
        this(context,null);
    }

    public TopTipsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.order_detail_top_item, this);
        ButterKnife.bind(this, view);
    }

    public void setText(String text){
        topText.setText(text);
    }

    public void setText(int text){
        topText.setText(text);
    }

    public void hide(){
        view.setVisibility(GONE);
    }

    @OnClick(R.id.img_close)
    public void onClick() {
        view.setVisibility(GONE);
    }
}
