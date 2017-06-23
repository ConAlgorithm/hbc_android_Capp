package com.hugboga.custom.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huangbaoche.imageselector.bean.Image;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HackyViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by qingcha on 16/8/4.
 */
public class LargerImageActivity extends BaseActivity{

    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;

    @Bind(R.id.larger_img_pager)
    HackyViewPager mViewPager;
    //@Bind(R.id.larger_img_indicator)
    //CirclePageIndicator mIndicator;

    private Params params;
    private LargerImageAdapter mAdapter;
    public ArrayList<String> deleteList = new ArrayList<String>(1);
    public static class Params implements Serializable {
        public ArrayList<String> imageUrlList;
        public int position;
        public boolean isLocalPic;
        public boolean isEvaluated;
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
    int deletePosition;
    private void initView() {
        if (params != null) {
            mAdapter = new LargerImageAdapter();
            mViewPager.setAdapter(mAdapter);
            deletePosition = params.position;
            //mIndicator.setViewPager(mViewPager);
            //mIndicator.setCurrentItem(params.position);
            mViewPager.setCurrentItem(params.position);
            int page = params.position +1;
            headerTitle.setText(page + "/" + params.imageUrlList.size());
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mViewPager.setCurrentItem(position);
                    deletePosition = position;
                    int page = position +1;
                    headerTitle.setText(page + "/" + params.imageUrlList.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            headerLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new EventAction(EventType.EVALUTE_PIC_DELETE,deleteList));
                    finish();
                }
            });
            RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(20), UIUtils.dip2px(20));
            headerRightImageParams.rightMargin = UIUtils.dip2px(18);
            headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
            if(params.isEvaluated){
                headerRightBtn.setVisibility(View.GONE);
            }
            headerRightBtn.setLayoutParams(headerRightImageParams);
            headerRightBtn.setPadding(0,0,0,0);
            headerRightBtn.setVisibility(View.VISIBLE);
            //headerRightBtn.setImageResource(R.mipmap.evaluate_photo_bin);
            headerRightBtn.setImageResource(R.mipmap.evaluate_photo_bin);
            headerRightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete pic.
                    //剩最后一张,删除后直接返回上一页
                    int item = mViewPager.getCurrentItem();
                    if(params.imageUrlList.size() == 1){
                        deleteList.add(params.imageUrlList.get(mViewPager.getCurrentItem()));
                        EventBus.getDefault().post(new EventAction(EventType.EVALUTE_PIC_DELETE,deleteList));
                        finish();
                        return;
                    }
                    if(item < params.imageUrlList.size()-1){
                        params.position = item;
                    }else{
                        params.position = item-1;
                    }
                    deleteList.add(params.imageUrlList.get(mViewPager.getCurrentItem()));
                    params.imageUrlList.remove(mViewPager.getCurrentItem());
                    mViewPager.setAdapter(mAdapter);
                    mViewPager.setCurrentItem(params.position);
                    mAdapter.notifyDataSetChanged();

                    int page = params.position +1;
                    headerTitle.setText(page + "/" + params.imageUrlList.size());
                }
            });
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
            if(params.isLocalPic){
                File dir = new File(params.imageUrlList.get(position));
                Uri dirUri = Uri.fromFile(dir);
                photoView.setImageURI(dirUri);
            }else{
                Glide.with(LargerImageActivity.this)
                        .load(params.imageUrlList.get(position))
                        .placeholder(R.mipmap.guide_car_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(photoView);
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
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
