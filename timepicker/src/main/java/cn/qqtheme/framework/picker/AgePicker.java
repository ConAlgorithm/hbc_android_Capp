package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by zhangqiang on 17/5/21.
 */

public class AgePicker extends OptionPicker {
    String[] items;
    private List<String> itemStrings = new ArrayList<String>();
    //public String[] items={"00后","90后","80后","70后","60后"};
    public AgePicker(Activity activity, String[] items) {
        super(activity, items);
        this.items= items;
    }


    @Override
    public void setOnWheelListener(OnWheelListener onWheelListener) {
        super.setOnWheelListener(onWheelListener);
    }

    @Override
    public void setOnItemPickListener(OnItemPickListener<String> listener) {
        super.setOnItemPickListener(listener);
    }

    @Override
    protected boolean fromAgeOrSexPicker(){
        return true;
    }
}
