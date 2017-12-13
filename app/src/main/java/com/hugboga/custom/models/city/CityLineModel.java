package com.hugboga.custom.models.city;

import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

/**
 * Created by HONGBO on 2017/12/13 20:00.
 */

public class CityLineModel extends EpoxyModelWithHolder<CityLineModel.LineHolder> {

    LineHolder lineHolder;

    @Override
    protected LineHolder createNewHolder() {
        return new LineHolder();
    }

    @Override
    public void bind(LineHolder holder) {
        super.bind(holder);
        this.lineHolder = holder;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_line_model_layout;
    }

    public class LineHolder extends EpoxyHolder {

        LinearLayout itemView;

        @Override
        protected void bindView(View itemView) {
            this.itemView = (LinearLayout) itemView;
        }

        public LinearLayout getItemView() {
            return itemView;
        }
    }

    public LineHolder getLineHolder() {
        return lineHolder;
    }
}
