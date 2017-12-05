package com.hugboga.custom.models;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.data.bean.CountryGroupBean;
import com.hugboga.custom.data.bean.FakeAIBean;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CityListCustomView;

/**
 * Created by Administrator on 2017/12/2.
 */

public class FakeAIHeaderModel extends EpoxyModel<LinearLayout> {
    private FakeAIBean fakeAIBean;
    private ImageView imageView;
    private TextView textView;
    private TextView textView2;
    public ObjectAnimator animator ;
    @Override
    protected int getDefaultLayout() {
        return R.layout.fake_header_item;
    }

    @Override
    public void bind(LinearLayout view) {
        float viewHeight = (35 / 100.0f) * UIUtils.getScreenHeight();
        view.getLayoutParams().height = (int)viewHeight;
        imageView = (ImageView) view.findViewById(R.id.fake_image);
        textView = (TextView) view.findViewById(R.id.fake_text_create1);
        textView2 = (TextView) view.findViewById(R.id.fake_text_create2);

        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 7200f);
        animator.setDuration(20000);
        imageAnimatorStart();
        if(fakeAIBean!=null&&textView!=null)
            bindData();

    }
    public void bindData(){
        textView.setText(fakeAIBean.hiList.get(0));
        textView2.setText(fakeAIBean.hiList.get(1)+fakeAIBean.hiList.get(2));
    }

    public void imageAnimatorStart(){
        animator.start();
    }
    public void imageAnimatorStop(){
        animator.cancel();
    }

    public void setData(FakeAIBean fakeAIBean) {
        this.fakeAIBean = fakeAIBean;
        if(fakeAIBean!=null&&textView!=null)
            bindData();

    }


}
