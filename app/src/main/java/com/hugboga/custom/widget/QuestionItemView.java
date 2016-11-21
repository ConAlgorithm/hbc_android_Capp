package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/11/14.
 */
public class QuestionItemView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.question_item_avatar_iv)
    ImageView avatarIV;
    @Bind(R.id.question_item_container_layout)
    LinearLayout containerLayout;

    private ServiceQuestionBean serviceQuestionBean;
    private int lastCustomRole;

    public QuestionItemView(Context context) {
        this(context, null);
    }

    public QuestionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_question_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof ServiceQuestionBean)) {
            return;
        }
        serviceQuestionBean = (ServiceQuestionBean) _data;

        ArrayList<ServiceQuestionBean.QuestionItem> questionList = serviceQuestionBean.questionList;
        final int listSize = questionList.size();
        if (questionList == null || questionList.size() <= 0) {
            containerLayout.removeAllViews();
            return;
        }

        final int containerChildCount = containerLayout.getChildCount();
        int isWelcome = 0;

        if (!TextUtils.isEmpty(serviceQuestionBean.welcome)) {//设置欢迎语
            ++isWelcome;
            View itemView = null;
            if (containerChildCount > 0) {
                itemView = containerLayout.getChildAt(0);
                itemView.setVisibility(VISIBLE);
            } else {
                itemView = addNewItemView(0);
            }

            itemView.findViewById(R.id.question_member_line_view).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.question_member_arrow_iv).setVisibility(View.GONE);
            TextView titleTV = (TextView) itemView.findViewById(R.id.question_member_title_tv);
            titleTV.setText(serviceQuestionBean.welcome);
            resetViewHight(titleTV, serviceQuestionBean.welcome, false, itemView);
        }

        for (int i = 0; i < listSize; i++) {
            ServiceQuestionBean.QuestionItem questionItem = questionList.get(i);
            int childIndex = i + isWelcome;
            View itemView = null;
            if (childIndex < containerChildCount) {//复用view
                itemView = containerLayout.getChildAt(childIndex);
                itemView.setVisibility(VISIBLE);
            } else {
                itemView = addNewItemView(childIndex);
            }

            ImageView arrowIV = (ImageView) itemView.findViewById(R.id.question_member_arrow_iv);
            TextView titleTV = (TextView) itemView.findViewById(R.id.question_member_title_tv);
            if (questionItem.isAnswer) {//答案
                titleTV.setText(questionItem.answer);
                arrowIV.setVisibility(View.GONE);
            } else {
                titleTV.setText(questionItem.adviceName);
                arrowIV.setVisibility(View.VISIBLE);
            }
            if (questionItem.type == 3) {//记录最近一条客服ID
                lastCustomRole = questionItem.customRole;
            }

            resetViewHight(titleTV, titleTV.getText() != null ? titleTV.getText().toString() : "", !questionItem.isAnswer, itemView);

            //分割线的隐藏
            View lineView= itemView.findViewById(R.id.question_member_line_view);
            if (i < (listSize - 1)) {
                lineView.setVisibility(LinearLayout.VISIBLE);
                RelativeLayout.LayoutParams lineViewParams = (RelativeLayout.LayoutParams) lineView.getLayoutParams();
                if (lineViewParams == null) {
                    lineViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
                    lineViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }
                if (questionItem.type == 3 || questionList.get(i + 1).type == 3 || questionItem.isAnswer) {
                    lineViewParams.leftMargin = 0;
                    lineViewParams.rightMargin = 0;
                } else {
                    lineViewParams.leftMargin = UIUtils.dip2px(15);
                    lineViewParams.rightMargin = UIUtils.dip2px(15);
                }
                lineView.setLayoutParams(lineViewParams);
            } else {
                lineView.setVisibility(LinearLayout.GONE);
            }
        }
        for (int j = listSize + isWelcome; j < containerChildCount; j++) {
            containerLayout.getChildAt(j).setVisibility(View.GONE);
        }
    }

    private View addNewItemView(int _index) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_question_member, null);
        view.setTag(_index);
        containerLayout.addView(view, LayoutParams.MATCH_PARENT, UIUtils.dip2px(60));
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceQuestionBean == null || serviceQuestionBean.questionList == null) {
                    return;
                }
                int index = (int)v.getTag();
                if (!TextUtils.isEmpty(serviceQuestionBean.welcome)) {
                    if (index == 0) {
                        return;
                    } else {
                        --index;
                    }
                }
                ArrayList<ServiceQuestionBean.QuestionItem> questionList = serviceQuestionBean.questionList;
                if (questionList == null || index >= questionList.size()) {
                    return;
                }
                ServiceQuestionBean.QuestionItem questionItem = questionList.get(index);
                if (questionItem == null || questionItem.isAnswer || (questionItem.type == 1 && questionItem.questionItemList == null)) {
                    return;
                }
                if (questionItem.type == 2) {
                    questionItem.lastCustomRole = lastCustomRole;
                }
                EventBus.getDefault().post(new EventAction(EventType.QUESTION_ITEM, questionItem));
            }
        });
        return view;
    }

    private void resetViewHight(TextView textView, String text, boolean isShowArrowIV, View parentView) {
        int textWidth = UIUtils.getStringWidth(textView, text);
        int textHight = 0;
        if (textWidth > 0) {
            // text实际显示区域,PreDrawListener略费内存
            // 8 + 45(avatar) + 6 + {15 + textView + 15 + (9 + 15)(arrow)} + 8;
            int textScope = 0;
            if (isShowArrowIV) {
                textScope = UIUtils.getScreenWidth() - UIUtils.dip2px(121);
            } else {
                textScope = UIUtils.getScreenWidth() - UIUtils.dip2px(97);
            }
            int lines = textWidth / textScope;
            if (textWidth % textScope > 0) {
                ++lines;
            }
            textHight = lines * textView.getLineHeight() + UIUtils.dip2px(12) * 2 + UIUtils.dip2px(3);
        }
        if (textHight <= UIUtils.dip2px(60)) {
            textHight = UIUtils.dip2px(60);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) parentView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, textHight);
        }else {
            layoutParams.width = LayoutParams.MATCH_PARENT;
            layoutParams.height = textHight;
        }
        parentView.setLayoutParams(layoutParams);
    }
}
