package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.FilterGuideOptionsBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.FilterTagUtils;
import com.hugboga.custom.widget.FilterTagGroupBase;
import com.hugboga.custom.widget.GuideSkillFilterTagGroup;
import com.hugboga.custom.widget.SliderLayout;
import com.hugboga.custom.widget.SliderView;
import com.hugboga.custom.widget.TagGroup;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideFilterFragment extends BaseFragment implements SliderView.OnValueChangedListener{

    @BindView(R.id.guide_filter_genders_male_layout)
    LinearLayout gendersMaleLayout;
    @BindView(R.id.guide_filter_genders_male_iv)
    ImageView gendersMaleIV;
    @BindView(R.id.guide_filter_genders_male_tv)
    TextView gendersMaleTV;

    @BindView(R.id.guide_filter_genders_female_layout)
    LinearLayout gendersFemaleLayout;
    @BindView(R.id.guide_filter_genders_female_iv)
    ImageView gendersFemaleIV;
    @BindView(R.id.guide_filter_genders_female_tv)
    TextView gendersFemaleTV;

    @BindView(R.id.guide_filter_pickorsend_tv)
    TextView pickorsendTV;
    @BindView(R.id.guide_filter_single_tv)
    TextView singleTV;
    @BindView(R.id.guide_filter_daily_tv)
    TextView dailyTV;

    @BindView(R.id.guide_filter_slider_layout)
    SliderLayout sliderLayout;

    @BindView(R.id.guide_filter_language_foreign_layout)
    LinearLayout foreignLanguageLayout;
    @BindView(R.id.guide_filter_language_foreign_taggroup)
    FilterTagGroupBase foreignLanguageTagGroup;

    @BindView(R.id.guide_filter_language_local_layout)
    LinearLayout localLanguageLayout;
    @BindView(R.id.guide_filter_language_local_taggroup)
    FilterTagGroupBase localLanguageTagGroup;

    @BindView(R.id.guide_filter_skill_layout)
    LinearLayout skillLayout;
    @BindView(R.id.guide_filter_skill_taggroup)
    GuideSkillFilterTagGroup skillLanguageTagGroup;

    private GuideFilterBean guideFilterBean;
    private GuideFilterBean guideFilterBeanCache;
    private FilterGuideOptionsBean filterGuideOptionsBean;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_guide_filter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    protected void initView() {
        guideFilterBean = new GuideFilterBean();
        guideFilterBeanCache = new GuideFilterBean();

        if (filterGuideOptionsBean != null) {
            setFilterGuideOptionsBean(filterGuideOptionsBean);
        } else {
            sliderLayout.setMax(11);
        }
        sliderLayout.setMin(1);
        sliderLayout.setValue(2);
        sliderLayout.setOnValueChangedListener(this);
        sliderLayout.setShowNumberIndicator(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                guideFilterBean.guideForeignLangs = foreignLanguageTagGroup.getList();
                guideFilterBean.guideLocalLangs = localLanguageTagGroup.getList();
                guideFilterBean.guideSkillLabels = skillLanguageTagGroup.getList();
                guideFilterBeanCache = (GuideFilterBean) guideFilterBean.clone();
                guideFilterBean.isSave = true;
                EventBus.getDefault().post(new EventAction(EventType.GUIDE_FILTER_SCOPE, guideFilterBeanCache));
                break;
        }
    }

    //FIXME 数据传递有空改成eventbus sticky实现，待优化
    public void setFilterGuideOptionsBean(FilterGuideOptionsBean _bean) {
        if (_bean == null) {
            return;
        }
        if (sliderLayout != null && foreignLanguageLayout != null && localLanguageLayout != null && skillLayout != null) {
            sliderLayout.setMax(_bean.numOfPerson);
            updateForeignLangsViews(_bean.guideForeignLangs);
            updateLocalLangsViews(_bean.guideLocalLangs);
            updateSkillViews(_bean.guideSkillLabels);
            if (guideFilterBean != null) {
                guideFilterBean.guideForeignLangs = _bean.guideForeignLangs;
                guideFilterBean.guideLocalLangs = _bean.guideLocalLangs;
                guideFilterBean.guideSkillLabels = _bean.guideSkillLabels;
            }
            if (guideFilterBeanCache != null) {
                guideFilterBeanCache.guideForeignLangs = _bean.guideForeignLangs;
                guideFilterBeanCache.guideLocalLangs = _bean.guideLocalLangs;
                guideFilterBeanCache.guideSkillLabels = _bean.guideSkillLabels;
            }
        } else {
            this.filterGuideOptionsBean = _bean;
        }
    }

    public void setGendersMaleLayoutSelected(boolean isSelected) {
        gendersMaleLayout.setSelected(isSelected);
        gendersMaleIV.setBackgroundResource(isSelected ? R.mipmap.guide_man_check : R.mipmap.guide_man_uncheck);
        gendersMaleTV.setTextColor(isSelected ? getContext().getResources().getColor(R.color.default_yellow) : 0xFF8A8A8A);
    }

    public void setGendersFemaleLayoutSelected(boolean isSelected) {
        gendersFemaleLayout.setSelected(isSelected);
        gendersFemaleIV.setBackgroundResource(isSelected ? R.mipmap.guide_woman_check : R.mipmap.guide_woman_uncheck);
        gendersFemaleTV.setTextColor(isSelected ? getContext().getResources().getColor(R.color.default_yellow) : 0xFF8A8A8A);
    }

    public void setCharterViewSelected(TextView view, boolean isSelected) {
        view.setSelected(isSelected);
        view.setTextColor(isSelected ? getContext().getResources().getColor(R.color.default_yellow) : 0xFF8A8A8A);
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

        updateForeignLangsViews(guideFilterBean.guideForeignLangs);
        updateLocalLangsViews(guideFilterBean.guideLocalLangs);
        updateSkillViews(guideFilterBean.guideSkillLabels);
    }

    public void updateForeignLangsViews(ArrayList<FilterGuideOptionsBean.GuideLanguage> guideForeignLangs) {
        if (guideForeignLangs != null && guideForeignLangs.size() > 0) {
            foreignLanguageLayout.setVisibility(View.VISIBLE);
            foreignLanguageTagGroup.setData(guideForeignLangs);
            foreignLanguageTagGroup.setOnTagItemClickListener(new TagGroup.OnTagItemClickListener() {
                @Override
                public void onTagClick(View view, int position) {
                    foreignLanguageTagGroup.setViewSelected((TextView) view, !view.isSelected());
                    guideFilterBean.isInitial = false;
                    guideFilterBean.isSave = false;
                }
            });
        } else {
            foreignLanguageLayout.setVisibility(View.GONE);
        }
    }

    public void updateLocalLangsViews(ArrayList<FilterGuideOptionsBean.GuideLanguage> guideLocalLangs) {
        if (guideLocalLangs != null && guideLocalLangs.size() > 0) {
            localLanguageLayout.setVisibility(View.VISIBLE);
            localLanguageTagGroup.setData(guideLocalLangs);
            localLanguageTagGroup.setOnTagItemClickListener(new TagGroup.OnTagItemClickListener() {
                @Override
                public void onTagClick(View view, int position) {
                    if (position == 0) {
                        return;
                    }
                    localLanguageTagGroup.setViewSelected((TextView) view, !view.isSelected());
                    guideFilterBean.isInitial = false;
                    guideFilterBean.isSave = false;
                }
            });
        } else {
            localLanguageLayout.setVisibility(View.GONE);
        }
    }

    public void updateSkillViews(ArrayList<FilterGuideOptionsBean.GuideSkillLabel> guideSkillLabels) {
        if (guideSkillLabels != null && guideSkillLabels.size() > 0) {
            skillLayout.setVisibility(View.VISIBLE);
            skillLanguageTagGroup.setData(guideSkillLabels);
            skillLanguageTagGroup.setOnTagItemClickListener(new TagGroup.OnTagItemClickListener() {
                @Override
                public void onTagClick(View view, int position) {
                    skillLanguageTagGroup.setViewSelected((TextView) view, !view.isSelected());
                    guideFilterBean.isInitial = false;
                    guideFilterBean.isSave = false;
                }
            });
        } else {
            skillLayout.setVisibility(View.GONE);
        }
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

        public ArrayList<FilterGuideOptionsBean.GuideLanguage> guideForeignLangs;
        public ArrayList<FilterGuideOptionsBean.GuideLanguage> guideLocalLangs;
        public ArrayList<FilterGuideOptionsBean.GuideSkillLabel> guideSkillLabels;

        public boolean isInitial = true;
        public boolean isSave = false;

        public GuideFilterBean() {
            guideForeignLangs = new ArrayList<>();
            guideLocalLangs = new ArrayList<>();
            guideSkillLabels = new ArrayList<>();
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

            FilterTagUtils.reset(guideForeignLangs);
            FilterTagUtils.resetLocalLangsList(guideLocalLangs);
            FilterTagUtils.reset(guideSkillLabels);
        }

        public int getOperateCount() {
            int operateCount = 0;

            if (male) operateCount++;
            if (female) operateCount++;

            if (pickorsend) operateCount++;
            if (single) operateCount++;
            if (daily) operateCount++;

            if (travelerCount != 2) operateCount++;

            operateCount += FilterTagUtils.getOperateCount(guideForeignLangs);
            operateCount += (FilterTagUtils.getOperateCount(guideLocalLangs) - 1);
            operateCount += FilterTagUtils.getOperateCount(guideSkillLabels);
            return operateCount;
        }

        @Override
        public Object clone() {
            GuideFilterBean guideFilterBean = null;
            try {
                guideFilterBean = (GuideFilterBean) super.clone();

                if (guideForeignLangs != null) {
                    guideFilterBean.guideForeignLangs = new ArrayList<>();
                    for (int i = 0; i < guideForeignLangs.size(); i++) {
                        guideFilterBean.guideForeignLangs.add((FilterGuideOptionsBean.GuideLanguage)guideForeignLangs.get(i).clone());
                    }
                }
                if (guideLocalLangs != null) {
                    guideFilterBean.guideLocalLangs = new ArrayList<>();
                    for (int i = 0; i < guideLocalLangs.size(); i++) {
                        guideFilterBean.guideLocalLangs.add((FilterGuideOptionsBean.GuideLanguage)guideLocalLangs.get(i).clone());
                    }
                }

                if (guideSkillLabels != null) {
                    guideFilterBean.guideSkillLabels = new ArrayList<>();
                    int size = guideSkillLabels.size();
                    for (int i = 0; i < size; i++) {
                        guideFilterBean.guideSkillLabels.add((FilterGuideOptionsBean.GuideSkillLabel)guideSkillLabels.get(i).clone());
                    }
                }

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

        public String getForeignLanguageRequestParams() {
            return FilterTagUtils.getIds(guideForeignLangs);
        }

        public String getLocalLanguageRequestParams() {
            return FilterTagUtils.getLocalLangsIds(guideLocalLangs);
        }

        public String getSkillRequestParams() {
            return FilterTagUtils.getIds(guideSkillLabels);
        }
    }
}
