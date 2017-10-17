package com.hugboga.custom.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.HackyViewPager;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by qingcha on 16/8/4.
 */
public class LargerImageActivity extends BaseActivity{

    @Bind(R.id.larger_img_pager)
    HackyViewPager mViewPager;

    protected Params params;
    protected LargerImageAdapter mAdapter;

    public static class Params implements Serializable {
        public List<String> imageUrlList;
        public int position;
        public boolean isLocalPic;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_larger_image;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params)savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (Params)bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    protected void initView() {
        if (params == null || params.imageUrlList == null) {
            finish();
        }
        initDefaultTitleBar();

        mAdapter = new LargerImageAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(params.position);

        final int imgListSize = params.imageUrlList.size();
        setIndicatorText(params.position, imgListSize);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicatorText(position, imgListSize);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setIndicatorText(int position, int size) {
        String indicatorText = String.format("%1$s/%2$s", "" + (position + 1), "" + size);
        fgTitle.setText(indicatorText);
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
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
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (params.isLocalPic) {
                File dir = new File(params.imageUrlList.get(position));
                Uri dirUri = Uri.fromFile(dir);
                photoView.setImageURI(dirUri);
            } else {
                Tools.showImageFitCenter(photoView,  params.imageUrlList.get(position), R.mipmap.guide_car_default);
            }
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    LargerImageActivity.this.finish();
                }

                @Override
                public void onOutsidePhotoTap() {
                    LargerImageActivity.this.finish();
                }
            });
            container.addView(photoView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
