package com.hugboga.custom.widget.ai;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ai.AccompanyReq;
import com.hugboga.custom.data.bean.ai.DurationReq;
import com.hugboga.custom.data.bean.ai.FakeAIArrayBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * AI快捷标签
 * Created by HONGBO on 2017/12/5 14:10.
 */

public class AiTagView extends FrameLayout {

    @BindView(R.id.ai_tag_layout_title)
    TextView ai_tag_layout_title;
    private final int AIGETDATA_DURATION = 1;//天数
    private final int AIGETDATA_ACCOMPANY = 2;//伴随
    private int type = 0;
    Object object;
    FakeAIArrayBean bean;
    OnClickListener onClickListener1;

    public AiTagView(@NonNull Context context) {
        this(context, null);
    }


    public AiTagView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.ai_tag_layout, this);
        view.setOnClickListener(onClickListener);
        ButterKnife.bind(this, view);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onClickListener1 != null) {
                onClickListener1.click(AiTagView.this, bean, type);
            }
        }
    };

    public void init(Object object) {
        this.object = object;
        if (object != null) {
            if (object instanceof FakeAIArrayBean){
                bean = (FakeAIArrayBean) object;
            }
            else if (object instanceof DurationReq) {
                bean = new FakeAIArrayBean();
                bean.destinationId = ((DurationReq) object).durationId;
                bean.destinationName = ((DurationReq) object).durationValue;
                type = AIGETDATA_DURATION;
            } else if (object instanceof AccompanyReq) {
                bean = new FakeAIArrayBean();
                bean.destinationId = ((AccompanyReq) object).accompanyId;
                bean.destinationName = ((AccompanyReq) object).accompanyValue;
                type = AIGETDATA_ACCOMPANY;
            }
        }
        ai_tag_layout_title.setText(bean.destinationName);
    }

    public FakeAIArrayBean getBean() {
        return bean;
    }

    public void setOnClickListener(OnClickListener onClickListener1) {
        this.onClickListener1 = onClickListener1;
    }

    public interface OnClickListener {
        void click(AiTagView view, FakeAIArrayBean bean, int type);
    }
}
