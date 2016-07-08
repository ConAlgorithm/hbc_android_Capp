package com.hugboga.custom;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.utils.AnimationUtils;
import com.viewpagerindicator.LinePageIndicator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    //引导图片资源
    private static final int[] pics = {R.mipmap.splash_1, R.mipmap.splash_2, R.mipmap.splash_3};

    @ViewInject(R.id.splash_viewpage)
    ViewPager viewPager;
    LinePageIndicator mIndicator;
    TextView enter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gotoSetp(); //执行主体任务
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 执行引导页显示任务
     */
    private void gotoSetp() {
        /*
         * 构建过第一次引导页之后，改变首次运行值
         * 解决在finish之前修改的问题
         */
//        UserEntity.getUser().setVersion(SplashActivity.this, PhoneInfo.getSoftwareVersion(SplashActivity.this));

        //构建滑动页
        List<View> views = new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setBackgroundResource(pics[i]);
            views.add(iv);
        }

        //增加最后一页滑动
//        RelativeLayout relativeLayout = new RelativeLayout(this);
//        relativeLayout.setLayoutParams(mParams);
//        relativeLayout.setBackgroundColor(getResources().getColor(R.color.basic_white));
//        relativeLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.exit_alpha));
//        views.add(relativeLayout);

        AdPageAdapter aAdapter = new AdPageAdapter(views);
        viewPager.setAdapter(aAdapter);

        mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);

        enter = (TextView)findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.showAnimationBig(viewPager, 500, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finishHandler.sendEmptyMessageDelayed(0,0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 2) {
                    enter.setVisibility(View.VISIBLE);
                    AnimationUtils.showAnimationAlpha(enter,1000,null);
                }else{
                    enter.setVisibility(GONE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 进入登录界面
     */
    Handler finishHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //记录首次运行标记
            super.handleMessage(msg);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_anim, R.anim.enter_anim);

        }

    };

    /**
     * 引导页
     * com.hugboga.custom.AdPageAdapter
     *
     * @author ZHZEPHI
     *         Create at 2015年1月30日 下午7:06:36
     */
    public class AdPageAdapter extends PagerAdapter {

        private List<View> views;

        public AdPageAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
//            if (position == 3) {
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finishHandler.sendEmptyMessage(0);
                    }
                });
//            }
            ((ViewPager) container).addView(view, 0);
            return views.get(position);
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
         */
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

    }
}
