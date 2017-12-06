package com.hugboga.custom.widget.ai;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
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
                onClickListener1.click(AiTagView.this, bean);
            }
        }
    };

    public void init(FakeAIArrayBean bean) {
        this.bean = bean;
        if (bean != null) {
            ai_tag_layout_title.setText(bean.destinationName);
        }
    }

    public FakeAIArrayBean getBean() {
        return bean;
    }

    public void setOnClickListener(OnClickListener onClickListener1) {
        this.onClickListener1 = onClickListener1;
    }

    public interface OnClickListener {
        void click(AiTagView view, FakeAIArrayBean bean);
    }
}
