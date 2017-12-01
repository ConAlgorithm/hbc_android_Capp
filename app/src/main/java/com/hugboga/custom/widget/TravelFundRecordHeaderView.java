package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.TravelFundData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by on 16/8/9.
 */
public class TravelFundRecordHeaderView extends FrameLayout implements HbcViewBehavior{

    @BindView(R.id.record_invited_tv)
    TextView invitedTV;
    @BindView(R.id.record_obtain_tv)
    TextView obtainTV;

    public TravelFundRecordHeaderView(Context context) {
        this(context, null);
    }

    public TravelFundRecordHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.header_travel_fund_record, this);
        ButterKnife.bind(this);

        invitedTV.setText(getSpannableString("0", R.string.people));
        obtainTV.setText(getSpannableString("0", R.string.yuan));
    }

    @Override
    public void update(Object _data) {
        TravelFundData travelFundData = (TravelFundData) _data;
        if (travelFundData == null) {
            return;
        }
        invitedTV.setText(getSpannableString(travelFundData.getInvitedUserCount(), R.string.people));
        obtainTV.setText(getSpannableString(travelFundData.getInvitationAmount(), R.string.yuan));
    }

    private SpannableString getSpannableString(String _text, int unitResId) {
        String text = _text + "  " + getContext().getResources().getString(unitResId);
        int start = _text.length();
        int end = text.length();
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new RelativeSizeSpan(0.5f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(Typeface.NORMAL), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }
}
