package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.GoodsFilterBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.widget.SkuThemeTagGroup;
import com.hugboga.custom.widget.TagGroup;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@ContentView(R.layout.fragment_sku_scope_filter)
public class SkuScopeFilterFragment extends BaseFragment implements TagGroup.OnTagItemClickListener {

    @Bind(R.id.sku_filter_day_one_tv)
    TextView dayOneTV;
    @Bind(R.id.sku_filter_day_two_tv)
    TextView dayTwoTV;
    @Bind(R.id.sku_filter_day_multi_tv)
    TextView dayMultiTV;

    @Bind(R.id.sku_filter_theme_title_tv)
    TextView themeTitleTV;
    @Bind(R.id.sku_filter_theme_taggroup)
    SkuThemeTagGroup themeTagGroup;
    @Bind(R.id.sku_filter_theme_line_view)
    View lineView;

    private SkuFilterBean skuFilterBean;
    private SkuFilterBean skuFilterBeanCache;
    private ArrayList<GoodsFilterBean.FilterTheme> themeList;
    private String dayTypes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected void initView() {
        themeTagGroup.setOnTagItemClickListener(this);
        skuFilterBean = new SkuFilterBean();
        skuFilterBeanCache = new SkuFilterBean();
        updateThemeViews(themeList);
        if (skuFilterBean != null) {
            skuFilterBean.themeList = themeList;
            skuFilterBeanCache.themeList = themeList;
        }
        setDayTypes(dayTypes);
    }

    @OnClick({R.id.sku_filter_scope_outside_layout})
    public void onOutsideClick() {
        EventBus.getDefault().post(new EventAction(EventType.FILTER_CLOSE));
    }

    @OnClick({R.id.sku_filter_day_one_tv, R.id.sku_filter_day_two_tv, R.id.sku_filter_day_multi_tv})
    public void onSelectDay(View view) {
        switch (view.getId()) {
            case R.id.sku_filter_day_one_tv:
                skuFilterBean.dayOne =!dayOneTV.isSelected();
                setDatViewSelected(dayOneTV, skuFilterBean.dayOne);
                break;
            case R.id.sku_filter_day_two_tv:
                skuFilterBean.dayTwo =!dayTwoTV.isSelected();
                setDatViewSelected(dayTwoTV, skuFilterBean.dayTwo);
                break;
            case R.id.sku_filter_day_multi_tv:
                skuFilterBean.dayMulti =!dayMultiTV.isSelected();
                setDatViewSelected(dayMultiTV, skuFilterBean.dayMulti);
                break;
        }
        skuFilterBean.isInitial = false;
        skuFilterBean.isSave = false;
    }

    @Override
    public void onTagClick(View view, int position) {
        themeTagGroup.setViewSelected((TextView) view, !view.isSelected());
        skuFilterBean.isInitial = false;
        skuFilterBean.isSave = false;
    }

    @OnClick({R.id.sku_filter_reset_tv, R.id.sku_filter_confirm_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sku_filter_reset_tv:
                skuFilterBean.reset();
                updateUI(skuFilterBean);
                skuFilterBean.isSave = false;
                break;
            case R.id.sku_filter_confirm_tv:
                skuFilterBean.themeList = themeTagGroup.getThemeList();
                skuFilterBeanCache = (SkuFilterBean) skuFilterBean.clone();
                skuFilterBean.isSave = true;
                EventBus.getDefault().post(new EventAction(EventType.SKU_FILTER_SCOPE, skuFilterBeanCache));
                break;
        }
    }

    public void setDatViewSelected(TextView view, boolean isSelected) {
        view.setSelected(isSelected);
        view.setTextColor(isSelected ? 0xFFFFC620 : 0xFF8A8A8A);
    }

    public void resetAllFilterBean() {
        skuFilterBean.reset();
        skuFilterBeanCache.reset();
        updateUI(skuFilterBean);
    }

    public void resetCacheFilter() {
        if (!skuFilterBean.isSave) {
            skuFilterBean = (SkuFilterBean) skuFilterBeanCache.clone();
            updateUI(skuFilterBean);
        }
    }

    public void updateUI(SkuFilterBean skuFilterBean) {
        setDatViewSelected(dayOneTV, skuFilterBean.dayOne);
        setDatViewSelected(dayTwoTV, skuFilterBean.dayTwo);
        setDatViewSelected(dayMultiTV, skuFilterBean.dayMulti);
        updateThemeViews(skuFilterBean.themeList);
    }

    public void setThemeList(ArrayList<GoodsFilterBean.FilterTheme> themeList) {
        if (themeTitleTV != null) {
            updateThemeViews(themeList);
            if (skuFilterBean != null) {
                skuFilterBean.themeList = themeList;
                skuFilterBeanCache.themeList = themeList;
            }
        } else {
            this.themeList = themeList;
        }
    }

    public void setDayTypes(String dayTypes) {
        if (themeTitleTV != null) {
            updateDayTypeViews(dayTypes);
        } else {
            this.dayTypes = dayTypes;
        }
    }

    public void updateDayTypeViews(String dayTypes) {
        if (TextUtils.isEmpty(dayTypes)) {
            return;
        }
        String[] dayType = dayTypes.split(",");
        for (int i = 0; i < dayType.length; i++) {
            if ("1".equals(dayType[i])) {
                skuFilterBean.dayOne = true;
                skuFilterBeanCache.dayOne = true;
            } else if ("2".equals(dayType[i])) {
                skuFilterBean.dayTwo = true;
                skuFilterBeanCache.dayTwo = true;
            } else if ("-1".equals(dayType[i])) {
                skuFilterBean.dayMulti = true;
                skuFilterBeanCache.dayTwo = true;
            }
        }
        updateUI(skuFilterBean);
    }

    public void updateThemeViews(ArrayList<GoodsFilterBean.FilterTheme> _themeList) {
        if (_themeList != null && _themeList.size() > 0) {
            themeTitleTV.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            themeTagGroup.setVisibility(View.VISIBLE);
        } else {
            themeTitleTV.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
            themeTagGroup.setVisibility(View.GONE);
        }
        themeTagGroup.setThemeData(_themeList);
    }

    public static class SkuFilterBean implements Serializable, Cloneable {

        public boolean dayOne;
        public boolean dayTwo;
        public boolean dayMulti;
        public ArrayList<GoodsFilterBean.FilterTheme> themeList;

        public boolean isInitial = true;
        public boolean isSave = false;

        public SkuFilterBean() {
            themeList = new ArrayList<>();
            reset();
        }

        public void reset() {
            isInitial = true;

            dayOne = false;
            dayTwo = false;
            dayMulti = false;

            if (themeList != null) {
                int labelsSize = themeList.size();
                for (int i = 0; i < labelsSize; i++) {
                    themeList.get(i).isSelected = false;
                }
            }
        }

        public int getOperateCount() {
            int operateCount = 0;

            if (dayOne) operateCount++;
            if (dayTwo) operateCount++;
            if (dayMulti) operateCount++;

            if (themeList != null) {
                int labelsSize = themeList.size();
                for (int i = 0; i < labelsSize; i++) {
                    if (themeList.get(i).isSelected) {
                        operateCount++;
                    }
                }
            }
            return operateCount;
        }

        @Override
        public Object clone() {
            SkuFilterBean skuFilterBean = null;
            try {
                skuFilterBean = (SkuFilterBean) super.clone();
                skuFilterBean.themeList = new ArrayList<>();
                int size = themeList.size();
                for (int i = 0; i < size; i++) {
                    skuFilterBean.themeList.add((GoodsFilterBean.FilterTheme)themeList.get(i).clone());
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return skuFilterBean;
        }

        public String getDayTypeParams() {
            StringBuilder stringBuilder = new StringBuilder();
            if (dayOne) {
                stringBuilder.append("1");
            }
            if (dayTwo) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append("2");
            }
            if (dayMulti) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append("-1");
            }
            return stringBuilder.toString();
        }

        public String getThemeIds() {
            if (themeList == null) {
                return null;
            }
            String result = "";
            final int size = themeList.size();
            for (int i = 0; i < size; i++) {
                if (!themeList.get(i).isSelected) {
                    continue;
                }
                if (!TextUtils.isEmpty(result)) {
                    result += ",";
                }
                result += themeList.get(i).themeId;
            }
            return result;
        }
    }

}
