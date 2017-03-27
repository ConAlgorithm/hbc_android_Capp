package com.hugboga.custom.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.adapter.CityRouteAdapter;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.fragment.FgCharterList;
import com.hugboga.custom.models.CharterModelBehavior;
import com.hugboga.custom.widget.charter.CharterEmptyView;
import com.hugboga.custom.widget.charter.CharterSubtitleView;

/**
 * Created by qingcha on 17/3/27.
 */

public class CharterFragmentAgent {

    private static final String FG_TAG_1 = "fg_tag_1";
    private static final String FG_TAG_2 = "fg_tag_2";

    private BaseActivity mActivity;
    private int containerViewId;

    private String currentTag = FG_TAG_1;
    private FgCharterList currentFragment;

    private CityRouteAdapter.OnCharterItemClickListener onCharterItemClickListener;
    private CharterSubtitleView.OnPickUpOrSendSelectedListener onPickUpOrSendSelectedListener;
    private CharterEmptyView.OnRefreshDataListener onRefreshDataListener;

    public CharterFragmentAgent(BaseActivity activity, int containerViewId) {
        this.mActivity = activity;
        this.containerViewId = containerViewId;
        setCurrentFg(true, currentTag, true, null);
    }

    public void setCurrentFg(boolean isNext, OnInitializedListener onInitializedListener) {
        setCurrentFg(false, getCurrentTag(), isNext, onInitializedListener);
    }

    public void setCurrentFg(boolean isFirst, String tag, boolean isNext, final OnInitializedListener onInitializedListener) {
        boolean isInit = false;
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        Fragment fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
        if (!isFirst) {//第一次不加载动画
            if (isNext) {
                ft.setCustomAnimations(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out);
            } else {
                ft.setCustomAnimations(R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
            }
        }
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        if (fragment == null) {
            isInit = true;
            fragment = new FgCharterList();
            ft.add(containerViewId, fragment, String.valueOf(tag));
        } else {
            ft.show(fragment);
        }
        currentFragment = (FgCharterList)fragment;
        currentFragment.setOnInitializedListener(new FgCharterList.OnInitializedListener() {
            @Override
            public void onInitialized() {
                currentFragment.getCityRouteAdapter().setOnCharterItemClickListener(onCharterItemClickListener);
                currentFragment.getCityRouteAdapter().setOnPickUpOrSendSelectedListener(onPickUpOrSendSelectedListener);
                currentFragment.getCityRouteAdapter().setOnRefreshDataListener(onRefreshDataListener);
                if (onInitializedListener != null) {
                    onInitializedListener.onInitialized();
                }
            }
        });
        ft.commitAllowingStateLoss();
        if (!isInit && onInitializedListener != null) {
            onInitializedListener.onInitialized();
        }
    }

    public String getCurrentTag() {
        return currentTag = getNextTag();
    }

    public String getNextTag() {
        return currentTag.equals(FG_TAG_1) ? FG_TAG_2 : FG_TAG_1;
    }

    public interface OnInitializedListener {
        public void onInitialized();
    }

    public void setOnCharterItemClickListener(CityRouteAdapter.OnCharterItemClickListener listener) {
        this.onCharterItemClickListener = listener;
    }

    public void setOnPickUpOrSendSelectedListener(CharterSubtitleView.OnPickUpOrSendSelectedListener listener) {
        this.onPickUpOrSendSelectedListener = listener;
    }

    public void setOnRefreshDataListener(CharterEmptyView.OnRefreshDataListener listener) {
        this.onRefreshDataListener = listener;
    }

    public void updateSelectedModel() {
        currentFragment.getCityRouteAdapter().updateSelectedModel();
    }

    public void showEmpty(int type, boolean isShow) {
        currentFragment.getCityRouteAdapter().showEmpty(type, isShow);
    }

    public void notifyAllModelsChanged(CityRouteBean cityRouteBean, int selectedRouteType) {
        currentFragment.getCityRouteAdapter().notifyAllModelsChanged(cityRouteBean, selectedRouteType);
    }

    public void updatePickupModel() {
        currentFragment.getCityRouteAdapter().updatePickupModel();
    }

    public void updateSendModel() {
        currentFragment.getCityRouteAdapter().updateSendModel();
    }

    public void showPickupModel() {
        currentFragment.getCityRouteAdapter().showPickupModel();
    }

    public void updateSubtitleModel() {
        currentFragment.getCityRouteAdapter().updateSubtitleModel();
    }

    public void showSendModel() {
        currentFragment.getCityRouteAdapter().showSendModel();
    }

    public void hideSendModel() {
        currentFragment.getCityRouteAdapter().hideSendModel();
    }

    public CharterModelBehavior getSelectedModel() {
        return currentFragment.getCityRouteAdapter().getSelectedModel();
    }

    public void smoothScrollToPosition(int position) {
        currentFragment.getRecyclerView().smoothScrollToPosition(position);
    }

}
