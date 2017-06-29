package com.hugboga.custom.activity;

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
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HackyViewPager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class EvaluatedLargerImageActivity extends BaseActivity {

    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.larger_img_pager)
    HackyViewPager mViewPager;

    protected Params params;
    protected LargerImageAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.fg_larger_image;
    }

    public static class Params implements Serializable {
        public List<String> imageUrlList;
        public int position;
        public boolean isLocalPic;
        public boolean isEvaluated;
    }
    private ArrayList<String> deleteList = new ArrayList<String>(1);

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
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventAction(EventType.EVALUTE_PIC_DELETE, deleteList));
                finish();
            }
        });
        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(20), UIUtils.dip2px(20));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);

        headerRightBtn.setLayoutParams(headerRightImageParams);
        headerRightBtn.setPadding(0,0,0,0);
        if(params.isEvaluated){
            headerRightBtn.setVisibility(View.GONE);
        }else{
            headerRightBtn.setVisibility(View.VISIBLE);
        }
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
                notifyDataSetChanged();

                int page = params.position +1;
                headerTitle.setText(page + "/" + params.imageUrlList.size());
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
            if (params.isLocalPic) {
                File dir = new File(params.imageUrlList.get(position));
                Uri dirUri = Uri.fromFile(dir);
                photoView.setImageURI(dirUri);
            } else {
                Glide.with(EvaluatedLargerImageActivity.this)
                        .load(params.imageUrlList.get(position))
                        .placeholder(R.mipmap.guide_car_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(photoView);
            }
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    EvaluatedLargerImageActivity.this.finish();
                }

                @Override
                public void onOutsidePhotoTap() {
                    EvaluatedLargerImageActivity.this.finish();
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
