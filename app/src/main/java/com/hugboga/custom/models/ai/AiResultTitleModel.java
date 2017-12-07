package com.hugboga.custom.models.ai;

import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HONGBO on 2017/12/7 12:15.
 */

public class AiResultTitleModel extends EpoxyModelWithHolder<AiResultTitleModel.AiResultTitleHolder> {

    private String title;

    public AiResultTitleModel(String title) {
        this.title = title;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.ai_result_title_model_layout;
    }

    @Override
    protected AiResultTitleHolder createNewHolder() {
        return new AiResultTitleHolder();
    }

    @Override
    public void bind(AiResultTitleHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        holder.setText(title);
    }

    class AiResultTitleHolder extends EpoxyHolder {

        @BindView(R.id.ai_result_title)
        TextView ai_result_title;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        public void setText(String title) {
            ai_result_title.setText(title);
        }
    }
}
