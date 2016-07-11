package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.widget.HackyViewPager;
import com.viewpagerindicator.CirclePageIndicator;


import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;

import io.rong.photoview.PhotoView;


/**
 * Created by qingcha on 16/6/18.
 */
@ContentView(R.layout.fg_larger_image)
public class FgLargerImage extends BaseFragment {

    @ViewInject(R.id.larger_img_pager)
    HackyViewPager mViewPager;
    @ViewInject(R.id.larger_img_indicator)
    CirclePageIndicator mIndicator;

    private Params params;
    private LargerImageAdapter mAdapter;

    public static class Params implements Serializable {
        public ArrayList<String> imageUrlList;
        public int position;
    }

    public static FgLargerImage newInstance(Params params) {
        FgLargerImage fragment = new FgLargerImage();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params)savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                params = (Params)bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    protected void initHeader() {
        if (params != null) {
            mAdapter = new LargerImageAdapter();
            mViewPager.setAdapter(mAdapter);
            mIndicator.setViewPager(mViewPager);
            mIndicator.setCurrentItem(params.position);
            if (params.imageUrlList.size() == 1) {
                mIndicator.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    private class LargerImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return params.imageUrlList == null ? 0 : params.imageUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(getContext())
                    .load(params.imageUrlList.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
