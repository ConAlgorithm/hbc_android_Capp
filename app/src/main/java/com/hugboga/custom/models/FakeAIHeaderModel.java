package com.hugboga.custom.models;

import android.animation.ObjectAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 */

public class FakeAIHeaderModel extends EpoxyModel<LinearLayout> {

    private ImageView imageView;
    private TextView textView;
    private TextView textView2;
    public ObjectAnimator animator;

    @Override
    protected int getDefaultLayout() {
        return R.layout.fake_header_item;
    }

    @Override
    public void bind(LinearLayout view) {

        float viewHeight = (35 / 100.0f) * UIUtils.getScreenHeight();
        view.getLayoutParams().height = (int) viewHeight;
        imageView = (ImageView) view.findViewById(R.id.fake_image);
        textView = (TextView) view.findViewById(R.id.fake_text_create1);
        textView2 = (TextView) view.findViewById(R.id.fake_text_create2);

        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 7200f);
        animator.setDuration(20000);
        imageAnimatorStart();
    }

    public void bindData(List<String> hiList) {
        if (hiList != null && hiList.size() > 0) {
            textView.setText(hiList.get(0));
            textView2.setText(getMessage2(hiList));
        }
        imageAnimatorStop(); //初始值设置后停止动画
    }

    private String getMessage2(List<String> hiList) {
        StringBuilder sb = new StringBuilder();
        if (hiList.size() > 1) {
            sb.append(hiList.get(1));
        }
        if (hiList.size() > 2) {
            sb.append(hiList.get(2));
        }
        return sb.toString();
    }

    public void imageAnimatorStart() {
        animator.start();
    }

    public void imageAnimatorStop() {
        animator.cancel();
    }

}
