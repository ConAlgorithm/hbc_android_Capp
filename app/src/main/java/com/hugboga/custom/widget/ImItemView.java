package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/5.
 */
public class ImItemView extends FrameLayout implements HbcViewBehavior  {

    @BindView(R.id.letter_item_img)
    public CircleImageView mImage; //头像
    @BindView(R.id.footer_order_btn_point)
    public TextView mUnReadCount;
    @BindView(R.id.letter_item_username)
    public TextView mUsername;
    @BindView(R.id.letter_item_time)
    public TextView mTime;
    @BindView(R.id.letter_item_message)
    public TextView mMessage;
    @BindView(R.id.footer_service_unread)
    public View serviceUnread;
    @BindView(R.id.letter_item_service_icon_tv)
    public TextView serviceIconTV;
    @BindView(R.id.letter_item_country_name_iv)
    public ImageView countryNameIV;
    @BindView(R.id.letter_item_country_name_tv)
    public TextView countryNameTV;

    public ImItemView(Context context) {
        this(context,null);
    }

    public ImItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.item_chat,this);
        ButterKnife.bind(this, view);
    }

    @Override
    public void update(Object _data) {
        final ChatBean chatBean = (ChatBean)_data;
        if (chatBean != null) {
            mUsername.setText(chatBean.targetName);
            if (chatBean.getTargetType() == 3) {
                mMessage.setText("您有任何问题，欢迎咨询客服小包子");
                serviceIconTV.setVisibility(View.VISIBLE);
                countryNameIV.setVisibility(View.GONE);
                countryNameTV.setVisibility(View.GONE);
            } else {
                if (!TextUtils.isEmpty(chatBean.getLastMsg())) {
                    mMessage.setText(chatBean.getLastMsg().trim());
                    serviceIconTV.setVisibility(View.GONE);
                } else {
                    mMessage.setText("");
                    serviceIconTV.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(chatBean.country_name)) {
                    countryNameIV.setVisibility(View.VISIBLE);
                    countryNameTV.setVisibility(View.VISIBLE);
                    countryNameTV.setText(chatBean.country_name);
                } else {
                    countryNameIV.setVisibility(View.GONE);
                    countryNameTV.setVisibility(View.GONE);
                }
            }
            if(chatBean.getTimeStamp()!=0){
                try {
                    mTime.setText(getDate(chatBean.getTimeStamp()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                mTime.setText("");
            }

            if (!TextUtils.isEmpty(chatBean.targetAvatar)) {
                Tools.showImage(mImage, chatBean.targetAvatar, R.mipmap.icon_avatar_guide);
            } else {
                mImage.setImageResource(R.mipmap.icon_avatar_guide);
            }
            flushPoint( chatBean);
        }
    }

    /**
     * 刷新未读消息数
     */
    private void flushPoint(ChatBean chatBean) {
        if(chatBean.getTargetType()==3){
            mUnReadCount.setVisibility(View.GONE);
            int unreadCount = SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT,0);
            if(unreadCount>0){
                serviceUnread.setVisibility(View.VISIBLE);
            }else{
                serviceUnread.setVisibility(View.GONE);
            }
        }else{
            serviceUnread.setVisibility(View.GONE);
            Integer ints = chatBean.getImCount();
            if (ints > 0) {
                mUnReadCount.setVisibility(View.VISIBLE);
                if (ints > 99) {
                   mUnReadCount.setText("99+");
                }else{
                    mUnReadCount.setText(""+ints);
                }
            } else {
                mUnReadCount.setVisibility(View.GONE);
            }
        }
    }

    private String getDate(long time) {
        Date date = new Date(time);
        if (DateUtils.isToday(date)) {
            try {
                SimpleDateFormat dateformatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return dateformatter.format(date);
            }catch (Exception e){
                return "";
            }
        } else {
            try {
                SimpleDateFormat dateformatter = new SimpleDateFormat("MM-dd", Locale.getDefault());
                return dateformatter.format(date);
            }catch (Exception e){
                return "";
            }
        }
    }
}
