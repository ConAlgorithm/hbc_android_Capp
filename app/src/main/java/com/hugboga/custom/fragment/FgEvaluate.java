package com.hugboga.custom.fragment;

import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.TagGroup;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 16/5/29.
 */
@ContentView(R.layout.fg_evaluate)
public class FgEvaluate extends BaseFragment implements TagGroup.OnTagItemClickListener {

    @ViewInject(R.id.evaluate_taggroup)
    TagGroup tagGroup;

    private final static int DEFAULT_TAG_COUNTS = 6;
    ArrayList<String> listData;

    @Override
    protected void initHeader() {
        listData = new ArrayList<String>(7);
        listData.add("车很干净");
        listData.add("漂移 你懂吗？");
        listData.add("司机很帅的啊 哈哈");
        listData.add("这SB不认识路");
        listData.add("车很干净");
        listData.add("好快");
        listData.add("车很干净7");
        listData.add("车很干净8");
        listData.add("好快9");
        listData.add("车很干净0");

        tagGroup.setOnTagItemClickListener(this);
        addTag(listData, AddTagState.MORE_BTN);
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

    @Override
    public void onTagClick(View view, int position) {
        if (getString(R.string.more).equals(view.getTag())) {
            tagGroup.removeView(view);
            addTag(listData, AddTagState.SURPLUS);
        } else {
            view.setSelected(!view.isSelected());
        }
    }

    /**
     * ADDALL 添加全部
     * SURPLUS 添加剩余的tag
     * MOREBTN 添加更多btn
     * */
    public enum AddTagState {
        ADDALL, SURPLUS, MORE_BTN
    }

    private void addTag(ArrayList<String> _listData, AddTagState state) {
        if (_listData == null) {
            return;
        }

        final int size = _listData.size();
        List<String> listData = _listData;

        if (state == AddTagState.SURPLUS && DEFAULT_TAG_COUNTS < size) {
            listData = _listData.subList(DEFAULT_TAG_COUNTS, size);
            if (listData == null) {
                return;
            }
        }

        ArrayList<View> viewList = new ArrayList<View>(size);
        for (int i = 0; i < size; i++) {
            TextView tagTV = new TextView(getActivity());
            tagTV.setPadding(UIUtils.dip2px(24), UIUtils.dip2px(5), UIUtils.dip2px(24), UIUtils.dip2px(6));
            if (state == AddTagState.MORE_BTN && i == DEFAULT_TAG_COUNTS) {
                tagTV.setText(getString(R.string.more));
                tagTV.setBackgroundResource(R.drawable.shape_evaluate_more);
                tagTV.setTag(getString(R.string.more));
                viewList.add(tagTV);
                tagGroup.setTags(viewList);
                return;
            }
            tagTV.setText(_listData.get(i));
            tagTV.setBackgroundResource(R.drawable.shape_evaluate_tag);
            viewList.add(tagTV);
        }
        tagGroup.setTags(viewList);
        return;
    }

}
