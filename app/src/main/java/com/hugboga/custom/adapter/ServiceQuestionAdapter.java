package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;

import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.widget.QuestionItemView;
import com.hugboga.custom.widget.QuestionUserItemView;

/**
 * Created by qingcha on 16/11/15.
 */
public class ServiceQuestionAdapter extends HbcRecyclerTypeBaseAdpater<ServiceQuestionBean>{

    public ServiceQuestionAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getItemView(int position) {
        View result = null;
        switch (datas.get(position).viewType){
            case ServiceQuestionBean.TYPE_QUESTION_ITEM:
                result = new QuestionItemView(getContext());
                break;
            case ServiceQuestionBean.TYPE_QUESTION_USER_ITEM:
                result = new QuestionUserItemView(getContext());
                break;
        }
        return result;
    }

    @Override
    protected int getChildItemViewType(int position) {
        return datas.get(position).viewType;
    }
}
