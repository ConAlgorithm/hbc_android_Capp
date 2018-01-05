package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.NIMChatActivity;
import com.hugboga.custom.data.bean.GuideExtinfoBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ChooseGuideUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.IMUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideWebDetailBottomView extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.guide_detail_bottom_top_line_view)
    View topLineView;
    @BindView(R.id.guide_detail_bottom_hint_tv)
    TextView hintTv;

    @BindView(R.id.guide_detail_bottom_time_tv)
    TextView timeTv;

    @BindView(R.id.guide_detail_bottom_contact_iv)
    ImageView contactIv;
    @BindView(R.id.guide_detail_bottom_contact_online)
    TextView contactOnline; //联系司导状态
    @BindView(R.id.guide_detail_bottom_contact_tv)
    TextView contactTv;
    @BindView(R.id.guide_detail_bottom_contact_layout)
    LinearLayout contactLayout;

    @BindView(R.id.guide_detail_bottom_book_tv)
    TextView bottomBookTv;

    @BindView(R.id.guide_detail_bottom_choose_guide_tv)
    TextView chooseGuideTv;

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private SimpleDateFormat dateTimeFormat;
    private volatile long delayedMillis;
    private volatile boolean isStop = false;

    private GuideExtinfoBean guideExtinfoBean;
    private GuideWebDetailActivity.Params params;
    private ChooseGuideUtils chooseGuideUtils;

    public GuideWebDetailBottomView(Context context) {
        this(context, null);
    }

    public GuideWebDetailBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_guide_detail_bottom, this);
        ButterKnife.bind(view);
        setOrientation(LinearLayout.VERTICAL);
        dateTimeFormat = new SimpleDateFormat("HH:mm");
    }

    public void showChooseGuideView(GuideWebDetailActivity.Params params) {
        this.params = params;
        setVisibility(View.VISIBLE);
        hintTv.setVisibility(View.GONE);
        topLineView.setVisibility(View.GONE);
        chooseGuideTv.setVisibility(View.VISIBLE);
        chooseGuideTv.setEnabled(true);
    }

    @OnClick(R.id.guide_detail_bottom_choose_guide_tv)
    public void chooseGuide() {
        if (params == null || params.chooseGuide == null || params.orderNo == null || !UserEntity.getUser().isLogin(getContext())) {
            return;
        }
        if (chooseGuideUtils == null) {
            chooseGuideUtils = new ChooseGuideUtils((Activity) getContext(), params.orderNo, "司导详情");
        }
        chooseGuideUtils.chooseGuide(params.chooseGuide);
    }

    @Override
    public void update(Object _data) {
        if (!UserEntity.getUser().isLogin(getContext())) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        guideExtinfoBean = (GuideExtinfoBean) _data;

        chooseGuideTv.setVisibility(View.GONE);
        chooseGuideTv.setEnabled(false);

        if (guideExtinfoBean.accessible == 0) {//是否可联系司导
            hintTv.setVisibility(View.VISIBLE);
            topLineView.setVisibility(View.VISIBLE);
            contactIv.setImageResource(R.mipmap.navbar_chat_white);
            contactTv.setTextColor(0xFFFFFFFF);
            contactLayout.setBackgroundResource(R.drawable.shape_rounded_disabled);
            contactLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogUtils.showAlertDialog(v.getContext(), false, CommonUtils.getString(R.string.guide_detail_contact_dialog_title)
                            , CommonUtils.getString(R.string.guide_detail_contact_dialog_content)
                            , CommonUtils.getString(R.string.dialog_btn_know), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }
            });

        } else {
            hintTv.setVisibility(View.GONE);
            topLineView.setVisibility(View.GONE);
            contactTv.setTextColor(getContext().getResources().getColor(R.color.default_black));
            contactLayout.setBackgroundResource(R.drawable.shape_rounded_yellow);
            if(guideExtinfoBean.accessible!=0){
                contactIv.setImageResource(getTvImage(guideExtinfoBean.accessible)); //设置司导在线状态描述
            }
            contactLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (guideExtinfoBean == null || TextUtils.isEmpty(guideExtinfoBean.neUserId) || !IMUtil.getInstance().isLogined() || !UserEntity.getUser().isLogin(getContext())) {
                        return;
                    }
                    String source = "司导个人页";
                    if (getContext() instanceof GuideWebDetailActivity) {
                        source = ((GuideWebDetailActivity) getContext()).getEventSource();
                    }
                    NIMChatActivity.start(getContext(), guideExtinfoBean.neUserId, source);
                    StatisticClickEvent.click(StatisticConstant.CLICK_CHATG);
                }
            });
        }

        if (TextUtils.isEmpty(guideExtinfoBean.localTime) || guideExtinfoBean.localTimezone == null) {
            timeTv.setText("");
        } else {
            isStop = false;
            String timeZoneString = "GMT";
            if (guideExtinfoBean.localTimezone >= 0) {
                timeZoneString += "+";
            } else {
                timeZoneString += "-";
            }
            timeZoneString += Math.abs(guideExtinfoBean.localTimezone);
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
            dateTimeFormat.setTimeZone(timeZone);

            Calendar mCalendar = Calendar.getInstance(timeZone);
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            delayedMillis = 60 * 1000 - mCalendar.get(Calendar.SECOND) * 1000 - mCalendar.get(Calendar.MILLISECOND);
            singleThreadExecutor.execute(timeRunnable);
        }
    }

    /**
     * 获取底部联系我图标、根据司导在线状态设置图标
     *
     * @return
     */
    private int getTvImage(int i) {
        String txt = "";
        int drawableId;
        switch (i) {
            case 2:
                drawableId = R.drawable.ic_im_online;
                txt = getResources().getString(R.string.guide_state_on_line);
                break;
            case 3:
                drawableId = R.drawable.ic_im_reset;
                txt = getResources().getString(R.string.guide_state_rest);
                break;
            case 4:
                drawableId = R.drawable.ic_im_busy;
                txt = getResources().getString(R.string.guide_state_busy);
                break;
            default:
                drawableId = R.mipmap.navbar_chat;
                break;
        }
        if (!TextUtils.isEmpty(txt)) {
            contactOnline.setVisibility(View.VISIBLE);
            contactOnline.setText(txt);
        }
        return drawableId;
    }


    private Runnable timeRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                if (delayedMillis > 0 && mHandler != null) {
                    mHandler.sendEmptyMessage(1);
                    Thread.sleep(delayedMillis);
                    delayedMillis = 0;
                }
                do {
                    mHandler.sendEmptyMessage(1);
                    Thread.sleep(60 * 1000);
                } while (!isStop && mHandler != null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isStop) {
                        return;
                    }
                    timeTv.setText(CommonUtils.getString(R.string.guide_detail_time, dateTimeFormat.format(System.currentTimeMillis())));
                    break;
                default:
                    break;
            }
        }
    };

    public TextView getBookTextView() {
        return bottomBookTv;
    }

    ;

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
}
