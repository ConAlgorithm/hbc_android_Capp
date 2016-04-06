package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestEvaluate;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 评价页面
 * Created by ZHZEPHI on 2015/7/28.
 */
@ContentView(R.layout.fg_assessment)
public class FgAssessment extends BaseFragment {

    @ViewInject(R.id.assessment_label1)
    TextView label1;
    @ViewInject(R.id.assessment_ratingbar1)
    RatingBar ratingBar1;
    @ViewInject(R.id.assessment_ratingbar2)
    RatingBar ratingBar2;
    @ViewInject(R.id.assessment_ratingbar3)
    RatingBar ratingBar3;
    @ViewInject(R.id.assessment_comment)
    EditText commentEditText;

    /*
    以下为需要传递过来的参数
     */
    public static final String GUIDE_ID = "guideID";
    public static final String ORDER_ID = "orderID";
    public static final String IMAGE_URL = "imageUrl";
    public static final String GUIDE_NAME = "guide_name";
    public static final String GUIDE_CAR = "guide_car";
    public static final String ORDER_TYPE = "order_type";

    private int orderType;
    private String guideId;
    private String guideName;
    private String orderId;

    @Override
    protected void initHeader() {
        fgTitle.setText("评价司导");
    }


    protected void initView() {

        Bundle bundle = getArguments();
        if (bundle == null) return;
        guideId = bundle.getString(GUIDE_ID, "");
        guideName = bundle.getString(GUIDE_NAME, "");
        orderId = bundle.getString(ORDER_ID, "");
        //选项显示差异化
        orderType = bundle.getInt(ORDER_TYPE);
        if (orderType == 1 || orderType == 2 || orderType == 4) {
            //接机|送机
            label1.setText("准时程度");
        }
//        getView().findViewById(R.id.assessment_submit).setBackgroundResource(Constants.BtnBg.get(orderType));
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.assessment_submit})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.assessment_submit:
                //提交评价
                int numStarts1 = (int) ratingBar1.getRating();
                int numStarts2 = (int) ratingBar2.getRating();
                int numStarts3 = (int) ratingBar3.getRating();
                String comment = commentEditText.getText().toString();
                String userId = UserEntity.getUser().getUserId();
                String userName = UserEntity.getUser().getNickname(getActivity());
                if (!guideId.isEmpty() && !orderId.isEmpty()) {
                    RequestEvaluate request = new RequestEvaluate(getActivity(), userId, userName, guideId, guideName, orderId, orderType, numStarts1, numStarts2, numStarts3, comment);
                    requestData(request);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onDataRequestSucceed(BaseRequest parser) {
        new AlertDialog.Builder(getActivity()).setTitle("评价已提交，感谢您对皇包车的支持").setNegativeButton("返回我的行程", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyOrderList(FgTravel.TYPE_ORDER_FINISH, false, true, false);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FRAGMENT_NAME, FgAssessment.class.getSimpleName());
                bringToFront(FgTravel.class, bundle);
            }
        }).setPositiveButton("还需用车", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bringToFront(FgHome.class, new Bundle());
//                finish();
            }
        }).setCancelable(false)
                .show();
//        notifyOrderList();
    }

    private void notifyOrderList() {
        Intent intent = new Intent();
        intent.setAction(FgTravel.FILTER_FLUSH);
        getActivity().sendBroadcast(intent);
    }

    private void notifyOrderList(int jumpType, boolean refreshRunning, boolean refreshFinish, boolean refreshCancel) {
        Intent intent = new Intent();
        intent.setAction(FgTravel.FILTER_FLUSH);
        intent.putExtra(FgTravel.JUMP_TYPE, jumpType);
        intent.putExtra(FgTravel.REFRESH_RUNNING, refreshRunning);
        intent.putExtra(FgTravel.REFRESH_FINISH, refreshFinish);
        intent.putExtra(FgTravel.REFRESH_CANCEL, refreshCancel);
        getActivity().sendBroadcast(intent);
    }
}
