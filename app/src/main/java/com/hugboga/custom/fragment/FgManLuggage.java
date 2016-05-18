package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created  on 16/5/18.
 */
@ContentView(R.layout.fg_man_luggage)
public class FgManLuggage extends BaseFragment {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.m_left)
    TextView mLeft;
    @Bind(R.id.m_sub)
    TextView mSub;
    @Bind(R.id.m_num)
    TextView mNum;
    @Bind(R.id.m_plus)
    TextView mPlus;
    @Bind(R.id.c_left)
    TextView cLeft;
    @Bind(R.id.c_right)
    TextView cRight;
    @Bind(R.id.c_sub)
    TextView cSub;
    @Bind(R.id.c_num)
    TextView cNum;
    @Bind(R.id.c_plus)
    TextView cPlus;
    @Bind(R.id.l_left)
    TextView lLeft;
    @Bind(R.id.l_right)
    TextView lRight;
    @Bind(R.id.l_sub)
    TextView lSub;
    @Bind(R.id.l_num)
    TextView lNum;
    @Bind(R.id.l_plus)
    TextView lPlus;
    @Bind(R.id.c_seat_left)
    TextView cSeatLeft;
    @Bind(R.id.c_seat_sub)
    TextView cSeatSub;
    @Bind(R.id.c_seat_num)
    TextView cSeatNum;
    @Bind(R.id.c_seat_plus)
    TextView cSeatPlus;
    @Bind(R.id.free_c_seat_left)
    TextView freeCSeatLeft;
    @Bind(R.id.free_c_seat_right)
    TextView freeCSeatRight;
    @Bind(R.id.free_c_seat_num)
    TextView freeCSeatNum;
    @Bind(R.id.charge_seat_left)
    TextView chargeSeatLeft;
    @Bind(R.id.charge_seat_right)
    TextView chargeSeatRight;
    @Bind(R.id.charge_seat_num)
    TextView chargeSeatNum;
    @Bind(R.id.show_child_seat)
    LinearLayout showChildSeat;
    @Bind(R.id.top_tips)
    TextView topTips;

    ManLuggageBean bean = new ManLuggageBean();

    int mNums = 0;
    int cNums = 0;
    int LNUms = 0;
    int seatNums = 0;
    @Override
    protected void initHeader() {
        fgTitle.setText(R.string.man_luggage_title);
        fgRightBtn.setText(R.string.dialog_btn_sure);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bean.mans = mNum.getText().toString();
                EventBus.getDefault().post(new EventAction(EventType.MAN_CHILD_LUUAGE,bean));
                finish();
            }
        });
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.m_sub, R.id.m_num, R.id.m_plus, R.id.c_sub, R.id.c_num, R.id.c_plus, R.id.l_sub, R.id.l_num, R.id.l_plus, R.id.c_seat_sub, R.id.c_seat_num, R.id.c_seat_plus, R.id.free_c_seat_num, R.id.charge_seat_num})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_sub:
                break;
            case R.id.m_num:
                break;
            case R.id.m_plus:
                break;
            case R.id.c_sub:
                break;
            case R.id.c_num:
                break;
            case R.id.c_plus:
                break;
            case R.id.l_sub:
                break;
            case R.id.l_num:
                break;
            case R.id.l_plus:
                break;
            case R.id.c_seat_sub:
                break;
            case R.id.c_seat_num:
                break;
            case R.id.c_seat_plus:
                break;
            case R.id.free_c_seat_num:
                break;
            case R.id.charge_seat_num:
                break;
        }
    }

    @OnClick(R.id.top_tips)
    public void onClick() {
    }
}
