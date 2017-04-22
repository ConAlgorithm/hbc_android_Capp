package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CapacityBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.widget.SliderLayout;
import com.hugboga.custom.widget.SliderView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@ContentView(R.layout.fragment_guide_filiter)
public class GuideFilterFragment extends BaseFragment implements SliderView.OnValueChangedListener{

    @Bind(R.id.guide_filter_genders_male_layout)
    LinearLayout gendersMaleLayout;
    @Bind(R.id.guide_filter_genders_male_iv)
    ImageView gendersMaleIV;
    @Bind(R.id.guide_filter_genders_male_tv)
    TextView gendersMaleTV;

    @Bind(R.id.guide_filter_genders_female_layout)
    LinearLayout gendersFemaleLayout;
    @Bind(R.id.guide_filter_genders_female_iv)
    ImageView gendersFemaleIV;
    @Bind(R.id.guide_filter_genders_female_tv)
    TextView gendersFemaleTV;

    @Bind(R.id.guide_filter_pickorsend_tv)
    TextView pickorsendTV;
    @Bind(R.id.guide_filter_single_tv)
    TextView singleTV;
    @Bind(R.id.guide_filter_daily_tv)
    TextView dailyTV;

    @Bind(R.id.guide_filter_slider_layout)
    SliderLayout sliderLayout;

    private GuideFilterBean guideFilterBean;
    private GuideFilterBean guideFilterBeanCache;
    private CapacityBean capacityBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView() {
        guideFilterBean = new GuideFilterBean();
        guideFilterBeanCache = new GuideFilterBean();

        if (capacityBean != null) {
            sliderLayout.setMax(capacityBean.numOfPerson);
        } else {
            sliderLayout.setMax(11);
        }
        sliderLayout.setMin(1);
        sliderLayout.setValue(2);
        sliderLayout.setOnValueChangedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.guide_filter_genders_male_layout, R.id.guide_filter_genders_female_layout})
    public void onSelectGenders(View view) {
        switch (view.getId()) {
            case R.id.guide_filter_genders_male_layout:
                guideFilterBean.male =!gendersMaleLayout.isSelected();
                setGendersMaleLayoutSelected(guideFilterBean.male);
                break;
            case R.id.guide_filter_genders_female_layout:
                guideFilterBean.female =!gendersFemaleLayout.isSelected();
                setGendersFemaleLayoutSelected(guideFilterBean.female);
                break;
        }
        guideFilterBean.isInitial = false;
        guideFilterBean.isSave = false;
    }

    @OnClick({R.id.guide_filter_pickorsend_tv, R.id.guide_filter_single_tv, R.id.guide_filter_daily_tv})
    public void onSelectCharter(View view) {
        switch (view.getId()) {
            case R.id.guide_filter_pickorsend_tv:
                guideFilterBean.pickorsend =!pickorsendTV.isSelected();
                setCharterViewSelected(pickorsendTV, guideFilterBean.pickorsend);
                break;
            case R.id.guide_filter_single_tv:
                guideFilterBean.single =!singleTV.isSelected();
                setCharterViewSelected(singleTV, guideFilterBean.single);
                break;
            case R.id.guide_filter_daily_tv:
                guideFilterBean.daily =!dailyTV.isSelected();
                setCharterViewSelected(dailyTV, guideFilterBean.daily);
                break;
        }
        guideFilterBean.isInitial = false;
        guideFilterBean.isSave = false;
    }

    @OnClick({R.id.guide_filter_reset_tv, R.id.guide_filter_confirm_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guide_filter_reset_tv:
                guideFilterBean.reset();
                updateUI(guideFilterBean);
                guideFilterBean.isSave = false;
                break;
            case R.id.guide_filter_confirm_tv:
                guideFilterBeanCache = (GuideFilterBean) guideFilterBean.clone();
                guideFilterBean.isSave = true;
                EventBus.getDefault().post(new EventAction(EventType.GUIDE_FILTER_SCOPE, guideFilterBeanCache));
                break;
        }
    }

    @OnClick({R.id.guide_filter_scope_outside_layout})
    public void onOutsideClick() {
        EventBus.getDefault().post(new EventAction(EventType.GUIDE_FILTER_CLOSE));
    }

    public void setCapacityBean(CapacityBean capacityBean) {
        if (capacityBean == null) {
            return;
        }
        if (sliderLayout != null) {
            sliderLayout.setMax(capacityBean.numOfPerson);
        } else {
            this.capacityBean = capacityBean;
        }
    }

    public void setGendersMaleLayoutSelected(boolean isSelected) {
        gendersMaleLayout.setSelected(isSelected);
        gendersMaleIV.setBackgroundResource(isSelected ? R.mipmap.guide_man_check : R.mipmap.guide_man_uncheck);
        gendersMaleTV.setTextColor(isSelected ? 0xFFFFC620 : 0xFF8A8A8A);
    }

    public void setGendersFemaleLayoutSelected(boolean isSelected) {
        gendersFemaleLayout.setSelected(isSelected);
        gendersFemaleIV.setBackgroundResource(isSelected ? R.mipmap.guide_woman_check : R.mipmap.guide_woman_uncheck);
        gendersFemaleTV.setTextColor(isSelected ? 0xFFFFC620 : 0xFF8A8A8A);
    }

    public void setCharterViewSelected(TextView view, boolean isSelected) {
        view.setSelected(isSelected);
        view.setTextColor(isSelected ? 0xFFFFC620 : 0xFF8A8A8A);
    }

    public void resetALLFilterBean() {
        guideFilterBean.reset();
        guideFilterBeanCache.reset();
        updateUI(guideFilterBean);
    }

    public boolean resetCacheFilter() {
        if (!guideFilterBean.isSave) {
            guideFilterBean = (GuideFilterBean) guideFilterBeanCache.clone();
            updateUI(guideFilterBeanCache);
            return true;
        } else {
            return false;
        }
    }

    public void updateUI(GuideFilterBean guideFilterBean) {
        setGendersMaleLayoutSelected(guideFilterBean.male);
        setGendersFemaleLayoutSelected(guideFilterBean.female);

        setCharterViewSelected(pickorsendTV, guideFilterBean.pickorsend);
        setCharterViewSelected(singleTV, guideFilterBean.single);
        setCharterViewSelected(dailyTV, guideFilterBean.daily);

        sliderLayout.setValue(guideFilterBean.travelerCount);
    }

    @Override
    public void onSliderScrolled(int value, int type) {
        guideFilterBean.isInitial = false;
        guideFilterBean.isSave = false;
        guideFilterBean.travelerCount = value;
    }

    @Override
    public void onValueChanged(int value, int type) {

    }

    public static class GuideFilterBean implements Serializable, Cloneable {
        public boolean male;
        public boolean female;

        public boolean pickorsend;
        public boolean single;
        public boolean daily;

        public int travelerCount;

        public boolean isInitial = true;
        public boolean isSave = false;

        public GuideFilterBean() {
            reset();
        }

        public void reset() {
            male = false;
            female = false;

            pickorsend = false;
            single = false;
            daily = false;

            travelerCount = 2;

            isInitial = true;
        }

        public int getOperateCount() {
            int operateCount = 0;

            if (male) operateCount++;
            if (female) operateCount++;

            if (pickorsend) operateCount++;
            if (single) operateCount++;
            if (daily) operateCount++;

            if (travelerCount != 2) operateCount++;

            return operateCount;
        }

        @Override
        public Object clone() {
            GuideFilterBean guideFilterBean = null;
            try {
                guideFilterBean = (GuideFilterBean) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return guideFilterBean;
        }

        public String getGendersRequestParams() {
            String result = "";
            if (male && female) {
                result = "1,2";
            } else if (male) {
                result = "1";
            } else if (female){
                result = "2";
            }
            return result;
        }

        public String getCharterRequestParams() {
            StringBuilder stringBuilder = new StringBuilder();
            if (pickorsend) {
                stringBuilder.append("1");
            }
            if (daily) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append("3");
            }
            if (single) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append("4");
            }
            return stringBuilder.toString();
        }
    }
}
