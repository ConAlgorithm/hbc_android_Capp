package com.hugboga.custom.models.home;

import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by qingcha on 17/12/5.
 */

public class HomeEmptyModel extends EpoxyModel<LinearLayout> {

    public LinearLayout itemView;

    @Override
    protected int getDefaultLayout() {
        return R.layout.no_network_layout;
    }

    @Override
    public void bind(LinearLayout view) {
        super.bind(view);
        this.itemView = view;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventAction(EventType.REQUEST_HOME_DATA));
            }
        });
    }
}