package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.utils.Tools;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/11/14.
 */
public class QuestionUserItemView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.question_user_item_avatar_iv)
    ImageView avatarIV;
    @Bind(R.id.question_user_item_title_tv)
    TextView titleTV;

    public QuestionUserItemView(Context context) {
        this(context, null);
    }

    public QuestionUserItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_question_user_item, this);
        ButterKnife.bind(view);

        final String avatar = UserEntity.getUser().getAvatar(getContext());
        if (!TextUtils.isEmpty(avatar)) {
            Tools.showImage(avatarIV, avatar, R.mipmap.icon_avatar_user);
        } else {
            avatarIV.setImageResource(R.mipmap.icon_avatar_user);
        }
    }

    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof ServiceQuestionBean)) {
            return;
        }
        ServiceQuestionBean serviceQuestionBean = (ServiceQuestionBean) _data;

        ArrayList<ServiceQuestionBean.QuestionItem> questionList = serviceQuestionBean.questionList;
        if (questionList == null || questionList.size() <= 0) {
            return;
        }
        ServiceQuestionBean.QuestionItem questionItem = questionList.get(0);
        titleTV.setText(questionItem.adviceName);
    }
}
