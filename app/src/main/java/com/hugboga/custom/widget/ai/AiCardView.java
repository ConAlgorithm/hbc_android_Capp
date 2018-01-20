package com.hugboga.custom.widget.ai;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.FakeAIActivity;
import com.hugboga.custom.data.bean.ai.ServiceType;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.IntentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqi on 2018/1/16.
 */

public class AiCardView extends FrameLayout implements View.OnClickListener {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    private ServiceType data;
    private boolean b = true;

    public AiCardView(Context context) {
        this(context, null);
    }

    public AiCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.fake_item_card_item, this);

        ButterKnife.bind(view);
    }

    public void bindData(ServiceType data) {
        this.data = data;
        setOnClickListener(this);
        initView();
    }

    /**
     * 包车 "3" 、接送机 "1"、单次 "4";
     **/
    private void initView() {
        if (data == null || data.serviceId == null || data.serviceTextMain == null || data.serviceTextSub == null) {
            return;
        }
        int imageId = 0;
        switch (data.serviceId) {
            case "3":
                imageId = R.mipmap.search_carcase_icon;
                break;
            case "1":
                imageId = R.mipmap.search_aeroplane_icon;
                break;
            case "4":
                imageId = R.mipmap.search_transport_icon;
                break;
        }
        imageView.setImageDrawable(getResources().getDrawable(imageId));
        textView1.setText(data.serviceTextMain);
        textView2.setText(data.serviceTextSub);
    }

    @Override
    public void onClick(View view) {
        if (!b) {
            return;
        }
        switch (data.serviceId) {
            case "3":
                ((FakeAIActivity) getContext()).clickCharteredBus();
                break;
            case "1":
                IntentUtils.intentPickupActivity(getContext(), getEventSource());
                MobClickUtils.onEvent(StatisticConstant.LAUNCH_J);
                break;
            case "4":
                IntentUtils.intentSingleActivity(getContext(), getEventSource());
                MobClickUtils.onEvent(StatisticConstant.LAUNCH_C);
                break;
        }

    }

    public void clickState(boolean b) {
        this.b = b;
    }

    public String getEventSource() {
        return "AI对话";
    }

}
